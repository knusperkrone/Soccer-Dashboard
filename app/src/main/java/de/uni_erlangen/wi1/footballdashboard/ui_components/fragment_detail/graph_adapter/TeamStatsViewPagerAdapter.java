package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.graph_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import de.uni_erlangen.wi1.footballdashboard.PlayerTeam;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.statistic_fragments.team.ITeamFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.statistic_fragments.team.TeamHeatMapFragment;

/**
 * Created by knukro on 5/24/17.
 */

public class TeamStatsViewPagerAdapter extends FragmentStatePagerAdapter
{

    private PlayerTeam team;
    private ITeamFragment currFragment;

    public TeamStatsViewPagerAdapter(FragmentManager fm, PlayerTeam team)
    {
        super(fm);
        this.team = team;
    }

    public void changeTeam(PlayerTeam team)
    {
        Log.d("[TEAM_STAT_ADAPTER]", "changeTeam() called");
        this.team = team;
        currFragment.changeTeam(team);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
                return TeamHeatMapFragment.newInstance(team);
            case 1:
            case 2:
                return FragmentPieExample.newInstance();
        }
        throw new IllegalArgumentException("Size " + getCount() + " - request" + position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object)
    {
        if (object != currFragment) {
            currFragment = (ITeamFragment) object;
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getCount()
    {
        return 3;
    }

}
