package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.ActiveView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.custom_views.TabIconView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.viewpager_adapter.TeamStatsViewPagerAdapter;

/**
 * Created by knukro on 6/18/17.
 *
 */

public class TeamInfoFragment extends Fragment implements ActiveView
{

    private OPTA_Team team;
    private TeamStatsViewPagerAdapter teamAdapter;

    public static TeamInfoFragment newInstance(@NonNull OPTA_Team team)
    {
        TeamInfoFragment frag = new TeamInfoFragment();
        frag.team = team;
        return frag;
    }

    public void changeTeam(@NonNull OPTA_Team team)
    {
        this.team = team;
        if (teamAdapter != null)
            teamAdapter.changeTeam(team);
    }

    @Override
    public void setActive()
    {
        if (teamAdapter != null)
            teamAdapter.setShownFragActive();
    }

    @Override
    public void setInactive()
    {
        // Nothing to un-register here!
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.fragment_detail_teaminfo, container, false);

        // Setup statistics ViewPager
        TabLayout mTabLayout = (TabLayout) root.findViewById(R.id.details_team_tab);
        ViewPager statisticPager = (ViewPager) root.findViewById(R.id.details_team_viewpager);
        teamAdapter = new TeamStatsViewPagerAdapter(getFragmentManager(), team);
        statisticPager.setAdapter(teamAdapter);
        mTabLayout.setupWithViewPager(statisticPager);
        // Fill tabLayout with nice round Icons TODO: Create nice icons
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setCustomView(new TabIconView(getContext(), null));
        }

        return root;
    }

}
