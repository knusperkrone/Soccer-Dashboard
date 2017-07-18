package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.viewpager_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
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
    private int layoutHome, layoutAway;
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

    public void teamChangeFormation(boolean homeTeam, int newFormation)
    {
        if (homeTeam)
            layoutHome = newFormation;
        else
            layoutAway = newFormation;
        // TODO: Check if that works
        notifyDataSetChanged();
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

    private static int getHomeLayoutId(int optaID)
    {
        // TODO:!!!!
        switch (optaID) {
            case 1:
            case 2:
                Log.d("FormationViewAdapter", "2lr");
                return R.layout.formation2_left_to_right;
            case 3:
            case 4:
                Log.d("FormationViewAdapter", "4lr");
                return R.layout.formation4_left_to_right;
            case 5:
            case 6:
            case 7:
            case 8:
                Log.d("FormationViewAdapter", "8lr");
                return R.layout.formation8_left_to_right;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
        }
        return R.layout.formation2_left_to_right;
    }


    private static int getAwayLayoutId(int optaID)
    {
        // TODO:!!!!
        switch (optaID) {
            case 1:
            case 2:
                Log.d("FormationViewAdapter", "2rl");
                return R.layout.formation2_right_to_left;
            case 3:
            case 4:
                Log.d("FormationViewAdapter", "4rl");
                return R.layout.formation4_right_to_left;
            case 5:
            case 6:
            case 7:
            case 8:
                Log.d("FormationViewAdapter", "8rl");
                return R.layout.formation8_right_to_left;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
        }
        return R.layout.formation2_right_to_left;
    }
}
