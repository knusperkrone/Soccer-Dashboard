package de.uni_erlangen.wi1.footballdashboard;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Timer;

import de.uni_erlangen.wi1.footballdashboard.database_adapter.DatabaseAdapter;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.ui_components.MainViewpagerAdapter;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.DetailFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.OverviewFragment;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    /*
    TODO:
    res/layout-sw600dp/main_activity.xml   # For 7” tablets (600dp wide and bigger)
    res/layout-sw720dp/main_activity.xml   # For 10” tablets (720dp wide and bigger)
     */

    private static boolean singleTon = false;

    private final FragmentManager fm = getSupportFragmentManager();
    private final GameGovernor governor = GameGovernor.getInstance();
    private DrawerLayout drawer;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Set layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Init singletons!
        initDatabase();
        setupStatusBar();
        singleTon = true;
        // NavigationDrawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ViewPager (Main-Content)
        final VerticalViewPager mainViewPager = (VerticalViewPager) findViewById(R.id.main_viewpager);
        final MainViewpagerAdapter adapter = new MainViewpagerAdapter(fm);
        mainViewPager.setAdapter(adapter);
        mainViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(final int position)
            {
                // Test if ui-thread is a good idea.
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Fragment frag = adapter.getFragmentAt(position);
                        if (frag == null)
                            return;
                        if (position == 0)
                            ((OverviewFragment) frag).prepare();
                        else
                            ((DetailFragment) frag).prepare();
                    }
                });
            }
        });
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_camera:
            case R.id.nav_gallery:
            case R.id.nav_slideshow:
            case R.id.nav_manage:
            case R.id.nav_share:
            case R.id.nav_send:
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initDatabase()
    {
        if (singleTon)
            return;
        DatabaseAdapter.initInstance(this);
        GameGovernor.getInstance().setGame(this, "838507");
    }

    private void setupStatusBar()
    {
        if (singleTon)
            return;

        if (timer == null)
            timer = new Timer();
        else
            timer.cancel();

        StatusBar.initInstance(new Handler(), governor.getHalfTimeLength(),
                (TextView) findViewById(R.id.statusbar_time),
                (TextView) findViewById(R.id.statusbar_goal),
                (TextView) findViewById(R.id.statusbar_team));
        StatusBar.getInstance().startClock();
    }

}
