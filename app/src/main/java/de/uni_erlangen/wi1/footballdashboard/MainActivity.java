package de.uni_erlangen.wi1.footballdashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import de.uni_erlangen.wi1.footballdashboard.UI_Components.ViewPagerAdapter.MainViewpagerAdapter;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    private final FragmentManager fm = getSupportFragmentManager();
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // NavigationDrawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // ViewPager (Main-Content)
        VerticalViewPager mainViewPager = (VerticalViewPager) findViewById(R.id.main_viewpager);
        mainViewPager.setAdapter(new MainViewpagerAdapter(fm));
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

}
