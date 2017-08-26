package de.uni_erlangen.wi1.footballdashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import de.uni_erlangen.wi1.footballdashboard.database_adapter.DatabaseAdapter;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.ui_components.GamePicker;
import de.uni_erlangen.wi1.footballdashboard.ui_components.MainViewpagerAdapter;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.main_list.MainListView;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    private static boolean singleTon = false;

    private final FragmentManager fm = getSupportFragmentManager();
    private MainListView mainListView;
    private DrawerLayout drawer;
    private StatusBar statusBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Set layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Init singletons!
        initDatabase();
        initStatusBar();
        singleTon = true;
        initMainList();
        initSeekBar();

        // NavigationDrawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ViewPager (Main-Content)
        VerticalViewPager mainViewPager = (VerticalViewPager) findViewById(R.id.main_viewpager);
        MainViewpagerAdapter adapter = new MainViewpagerAdapter(fm, mainListView);
        mainViewPager.setAdapter(adapter);
    }

    private void initDatabase()
    {
        if (singleTon)
            return;

        DatabaseAdapter.initInstance(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String game = sp.getString("gameID", "838532");
        GameGovernor.getInstance().setGame(this, game);
    }

    private void initStatusBar()
    {
        if (singleTon)
            return;

        StatusBar.initInstance(
                this,
                new Handler(),
                (TextView) findViewById(R.id.statusbar_time),
                (TextView) findViewById(R.id.statusbar_goal),
                (TextView) findViewById(R.id.statusbar_team));
        statusBar = StatusBar.getInstance();
        statusBar.startClock();
    }

    private void initMainList()
    {
        mainListView = (MainListView) findViewById(R.id.list_placeholder);
    }

    @SuppressWarnings("unchecked")
    private void initSeekBar()
    {
        final ImageButton seekerButton = (ImageButton) findViewById(R.id.start_button);
        seekerButton.setOnClickListener(new View.OnClickListener()
        {
            private boolean clicked = false;

            @Override
            public void onClick(View view)
            {
                if (clicked) {
                    StatusBar.getInstance().startClock();
                    seekerButton.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.button_play));
                } else {
                    StatusBar.getInstance().stopClock();
                    seekerButton.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.button_pause));
                }
                clicked = !clicked;
            }
        });
        RangeSeekBar<Integer> rangeSeekBar = (RangeSeekBar) findViewById(R.id.rangeBar);
        StatusBar.getInstance().setRangeBar(rangeSeekBar);
    }


    @Override
    public void onBackPressed()
    {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            super.onBackPressed(); // Close Application
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_manage:
                drawer.closeDrawer(GravityCompat.START);
                new GamePicker().show(getSupportFragmentManager(), "this");
                break;

            case R.id.nav_slow:
                statusBar.decreaseGameTime();
                break;

            case R.id.nav_fast:
                statusBar.increaseGameTime();
                break;
        }

        return true;
    }

}
