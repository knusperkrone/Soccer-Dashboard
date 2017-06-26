package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.uni_erlangen.wi1.footballdashboard.PlayerTeam;
import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.custom_views.TabIconView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.graph_adapter.TeamStatsViewPagerAdapter;

/**
 * Created by knukro on 6/18/17.
 */

public class TeamInfoFragment extends Fragment
{

    private PlayerTeam team;
    private TeamStatsViewPagerAdapter teamAdapter;

    public static TeamInfoFragment newInstance(PlayerTeam team)
    {
        TeamInfoFragment frag = new TeamInfoFragment();
        frag.team = team;
        return frag;
    }

    public void changeTeam(@NonNull PlayerTeam team)
    {
        Log.d("[TEAM_INFO_FRAG", "changeTeam() called");
        this.team = team;
        if (teamAdapter != null)
            teamAdapter.changeTeam(team);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.fragment_detail_teaminfo, container, false);

        TabLayout mTabLayout = (TabLayout) root.findViewById(R.id.details_team_tab);
        ViewPager statisticPager = (ViewPager) root.findViewById(R.id.details_team_viewpager);
        teamAdapter = new TeamStatsViewPagerAdapter(getFragmentManager(), team);
        statisticPager.setAdapter(teamAdapter);
        mTabLayout.setupWithViewPager(statisticPager);
        // Fill with Icons!
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setCustomView(new TabIconView(getContext(), null));
        }

        return root;
    }

}
