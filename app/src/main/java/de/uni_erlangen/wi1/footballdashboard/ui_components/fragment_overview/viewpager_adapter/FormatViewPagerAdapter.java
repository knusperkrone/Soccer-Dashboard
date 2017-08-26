package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.viewpager_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.ui_components.ActiveView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.fragments.FormationFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.main_list.MainListView;

/**
 * Created by knukro on 5/24/17.
 * This Class ist a viewPagerAdapter, that just chooses the right Layout for 2 FormationFragments
 */

public class FormatViewPagerAdapter extends FragmentStatePagerAdapter
{

    private final MainListView mainList;
    private final StatusBar statusBar;
    private final int layoutHome, layoutAway;
    private ActiveView currFragment;

    public FormatViewPagerAdapter(FragmentManager fm, MainListView mainList)
    {
        super(fm);
        this.mainList = mainList;
        this.statusBar = StatusBar.getInstance();
        GameGovernor governor = GameGovernor.getInstance();
        this.layoutHome = getHomeLayoutId(governor.getHomeLayout());
        this.layoutAway = getAwayLayoutId(governor.getAwayLayout());
    }

    @Override
    public Fragment getItem(int position)
    {
        FormationFragment fragment;
        switch (position) {
            case 0:
                fragment = FormationFragment.newInstance(layoutHome, mainList, true);
                break;
            case 1:
                fragment = FormationFragment.newInstance(layoutAway, mainList, false);
                break;
            default:
                throw new IllegalArgumentException("You can't have more than 2 teams playing");
        }
        return fragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object)
    {
        if (object != currFragment) {
            statusBar.teamSwitched(position);
            if (currFragment != null) {
                currFragment.setInactive();
            }
            currFragment = (ActiveView) object;
            currFragment.setActive();
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    public static int getHomeLayoutId(int optaID)
    {
        switch (optaID) {
            case 1:
                break;
            case 2:
                return R.layout.formation2_left_to_right;
            case 3:
                return R.layout.formation3_left_to_right;
            case 4:
                return R.layout.formation4_left_to_right;
            case 5:
                return R.layout.formation5_left_to_right;
            case 6:
                return R.layout.formation6_left_to_right;
            case 7:
                return R.layout.formation7_left_to_right;
            case 8:
                return R.layout.formation8_left_to_right;
            case 9:
                return R.layout.formation9_left_to_right;
            case 10:
                return R.layout.formation10_left_to_right;
            case 11:
                return R.layout.formation11_left_to_right;
            case 12:
                return R.layout.formation12_left_to_right;
            case 13:
                return R.layout.formation13_left_to_right;
            case 14:
                return R.layout.formation14_left_to_right;
            case 15:
                return R.layout.formation15_left_to_right;
            case 16:
                return R.layout.formation16_left_to_right;
            case 17:
                return R.layout.formation17_left_to_right;
            case 18:
                return R.layout.formation18_left_to_right;
        }
        return R.layout.formation2_left_to_right;
    }


    public static int getAwayLayoutId(int optaID)
    {
        switch (optaID) {
            case 1:
                break;
            case 2:
                return R.layout.formation2_right_to_left;
            case 3:
                return R.layout.formation3_right_to_left;
            case 4:
                return R.layout.formation4_right_to_left;
            case 5:
                return R.layout.formation5_right_to_left;
            case 6:
                return R.layout.formation6_right_to_left;
            case 7:
                return R.layout.formation7_right_to_left;
            case 8:
                return R.layout.formation8_right_to_left;
            case 9:
                return R.layout.formation9_right_to_left;
            case 10:
                return R.layout.formation10_right_to_left;
            case 11:
                return R.layout.formation11_right_to_left;
            case 12:
                return R.layout.formation12_right_to_left;
            case 13:
                return R.layout.formation13_right_to_left;
            case 14:
                return R.layout.formation14_right_to_left;
            case 15:
                return R.layout.formation15_right_to_left;
            case 16:
                return R.layout.formation16_right_to_left;
            case 17:
                return R.layout.formation17_right_to_left;
            case 18:
                return R.layout.formation18_right_to_left;
        }
        return R.layout.formation2_right_to_left;
    }
}
