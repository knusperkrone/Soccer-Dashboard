package de.uni_erlangen.wi1.footballdashboard.ui_components;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.DetailFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.OverviewFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.main_list.MainListView;

/**
 * Created by knukro on 5/23/17.
 */

public class MainViewpagerAdapter extends FragmentStatePagerAdapter
{

    private ActiveView activeFragment;
    private final MainListView mainList;

    public MainViewpagerAdapter(FragmentManager fm, MainListView mainListView)
    {
        super(fm);
        this.mainList = mainListView;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
                return OverviewFragment.newInstance(mainList);

            case 1:
                return DetailFragment.newInstance(mainList);
        }
        throw new IllegalArgumentException("Size " + getCount() + " - request" + position);
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object)
    {
        if (activeFragment != object) {
            if (activeFragment != null) {
                activeFragment.setInactive();
            }

            activeFragment = (ActiveView) object;
            activeFragment.setActive();
            mainList.setActive(position);
        }

        super.setPrimaryItem(container, position, object);
    }

}
