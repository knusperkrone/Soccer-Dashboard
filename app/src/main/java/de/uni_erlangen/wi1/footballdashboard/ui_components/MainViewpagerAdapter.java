package de.uni_erlangen.wi1.footballdashboard.ui_components;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.DetailFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.OverviewFragment;

/**
 * Created by knukro on 5/23/17.
 */

public class MainViewpagerAdapter extends FragmentStatePagerAdapter
{

    private OverviewFragment overviewFragment;
    private DetailFragment detailFragment;

    public MainViewpagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    public Fragment getFragmentAt(int position)
    {
        if (position == 0)
            return overviewFragment;
        return detailFragment;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
                overviewFragment = OverviewFragment.newInstance();
                return overviewFragment;
            case 1:
                detailFragment = DetailFragment.newInstance();
                return detailFragment;
        }
        throw new IllegalArgumentException("Size " + getCount() + " - request" + position);
    }

    @Override
    public int getCount()
    {
        return 2;
    }

}
