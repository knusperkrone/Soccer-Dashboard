package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.formation_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.fragments.FormationFragment;

/**
 * Created by knukro on 5/24/17.
 */

public class FormatViewPagerAdapter extends FragmentStatePagerAdapter
{

    private final int layoutHome, layoutAway;

    public FormatViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
        GameGovernor governor = GameGovernor.getInstance();
        this.layoutHome = getHomeLayoutId(governor.getHomeLayout());
        this.layoutAway = getAwayLayoutId(governor.getAwayLayout());
    }

    /*public void changeLayout(int layoutHome, int layoutAway)
    {
        this.layoutHome = getHomeLayoutId(layoutHome);
        this.layoutAway = getAwayLayoutId(layoutAway);
        this.notifyDataSetChanged(); // Redraw?
    }*/

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
                return FormationFragment.newInstance(layoutHome, true);
            case 1:
                return FormationFragment.newInstance(layoutAway, false);
        }
        throw new IllegalArgumentException("Size " + getCount() + " - request" + position);
    }

    @Override
    public int getCount()
    {
        return 2;
    }


    private static int getHomeLayoutId(int optaID)
    {
        switch (optaID) {
            case 1:
            case 2:
                return R.layout.formation2_left_to_right;
            case 3:
            case 4:
                return R.layout.formation4_left_to_right;
            case 5:
            case 6:
            case 7:
            case 8:
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
        switch (optaID) {
            case 1:
            case 2:
                return R.layout.formation2_right_to_left;
            case 3:
            case 4:
                return R.layout.formation4_right_to_left;
            case 5:
            case 6:
            case 7:
            case 8:
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
