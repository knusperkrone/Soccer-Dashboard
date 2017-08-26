package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.viewpager_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.statistic_fragments.team.ITeamFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.statistic_fragments.team.TeamHeatMapFragment;

/**
 * Created by knukro on 5/24/17.
 */

public class TeamStatsViewPagerAdapter extends FragmentStatePagerAdapter
{

    private OPTA_Team team;
    private ITeamFragment currFragment;

    public TeamStatsViewPagerAdapter(FragmentManager fm, OPTA_Team team)
    {
        super(fm);
        this.team = team;
    }

    public void changeTeam(OPTA_Team team)
    {
        this.team = team;
        currFragment.setNewTeam(team);
        currFragment.setActive();
    }

    public void setShownFragActive()
    {
        if (currFragment != null)
            currFragment.setActive();
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
                return TeamHeatMapFragment.newInstance(team);
        }
        throw new IllegalArgumentException("Size " + getCount() + " - request: " + position);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object)
    {
        if (object != currFragment) {
            if (currFragment != null)
                currFragment.setInactive();

            currFragment = (ITeamFragment) object;
            currFragment.setActive();
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getCount()
    {
        return 1;
    }

}
