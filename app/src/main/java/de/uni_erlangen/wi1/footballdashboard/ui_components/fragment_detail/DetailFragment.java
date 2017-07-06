package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.PlayerInfoFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.TeamInfoFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.live_list.LivePlayerListAdapter;

public class DetailFragment extends Fragment
{

    private LivePlayerListAdapter rankedPlayerListAdapter;
    private final GameGovernor gov = GameGovernor.getInstance();

    public static DetailFragment newInstance()
    {
        return new DetailFragment();
    }

    public void setActive()
    {
        // Prepares View (Fragments) when user scrolls
        OPTA_Team team = GameGovernor.getInstance().getDisplayedTeam();
        // Get active fragment
        Fragment unknownFrag = getFragmentManager().findFragmentById(R.id.detail_content);

        if (rankedPlayerListAdapter.changedTeam(team)) {
            // We changed the team!
            if (unknownFrag instanceof PlayerInfoFragment) {
                // We have a player Fragment, but want a team Fragment -> replace it
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detail_content,
                                TeamInfoFragment.newInstance(gov.getDisplayedTeam()))
                        .commit();
            } else {
                // We got already a team Fragment there
                ((TeamInfoFragment) unknownFrag).changeTeam(team);
            }

        } else if (unknownFrag instanceof PlayerInfoFragment) {
            // We didn't change the team and have Player statistic open
            ((PlayerInfoFragment) unknownFrag).refreshStatistics();
        } else {
            // We didn't change the team and have Team statistic open
            ((TeamInfoFragment) unknownFrag).refreshStatistics();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.fragment_detail, container, false);

        RecyclerView mainList = (RecyclerView) root.findViewById(R.id.main_list);
        mainList.setHasFixedSize(true);
        mainList.setLayoutManager(new LinearLayoutManager(getContext()));
        rankedPlayerListAdapter = new LivePlayerListAdapter(getFragmentManager(), getContext(),
                GameGovernor.getInstance().getDisplayedTeam());
        mainList.setAdapter(rankedPlayerListAdapter);

        // Create and show a TeamFragment
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_content, TeamInfoFragment.newInstance(gov.getDisplayedTeam()))
                .commit();

        return root;
    }

}
