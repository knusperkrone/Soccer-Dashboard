package de.uni_erlangen.wi1.footballdashboard.UI_Components.ViewPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import de.uni_erlangen.wi1.footballdashboard.UI_Components.Fragments.DetailFragment;
import de.uni_erlangen.wi1.footballdashboard.UI_Components.Fragments.OverviewFragment;
import de.uni_erlangen.wi1.footballdashboard.R;

/**
 * Created by knukro on 5/23/17.
 */

public class MainViewpagerAdapter extends FragmentStatePagerAdapter
{


    public MainViewpagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0: return OverviewFragment.newInstance(R.layout.formation2, R.layout.formation2reversed);
            case 1: return DetailFragment.newInstance();
        }
        throw new IllegalArgumentException("Size "+ getCount() + " - request"+ position );
    }

    @Override
    public int getCount()
    {
        return 2;
    }

}
