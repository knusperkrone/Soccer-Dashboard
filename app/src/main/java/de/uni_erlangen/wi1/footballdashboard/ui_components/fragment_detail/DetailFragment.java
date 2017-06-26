package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.uni_erlangen.wi1.footballdashboard.PlayerTeam;
import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.PlayerInfoFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.TeamInfoFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.list_ranked_player.RankedPlayerListAdapter;

public class DetailFragment extends Fragment
{

    private RankedPlayerListAdapter rankedPlayerListAdapter;
    private TeamInfoFragment teamInfoFragment;

    public static DetailFragment newInstance()
    {
        return new DetailFragment();
    }

    public void prepare()
    {
        // Prepares View (Fragments) when user scroll
        PlayerTeam team = GameGovernor.getInstance().getDisplayedTeam();

        if (rankedPlayerListAdapter.changeTeam(team)) {
            // Team changed but playerView is shown
            Fragment playerFrag = getFragmentManager().findFragmentById(R.id.detail_content);
            if (playerFrag instanceof PlayerInfoFragment) {
                // Need to create a new Fragment (?)
                teamInfoFragment = teamInfoFragment.newInstance(team);
                getFragmentManager().beginTransaction()
                        .replace(R.id.detail_content, teamInfoFragment)
                        .commit();
                return; // Team is already set
            }
        }

        teamInfoFragment.changeTeam(team); // Set new team and redraw old fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.fragment_detail, container, false);

        RecyclerView mainList = (RecyclerView) root.findViewById(R.id.main_list);
        mainList.setHasFixedSize(true);
        mainList.setLayoutManager(new LinearLayoutManager(getContext()));
        rankedPlayerListAdapter = new RankedPlayerListAdapter(getFragmentManager(), getContext(),
                GameGovernor.getInstance().getDisplayedTeam());
        mainList.setAdapter(rankedPlayerListAdapter);

        // Create initial TeamFragment
        teamInfoFragment = TeamInfoFragment.newInstance(GameGovernor.getInstance().getDisplayedTeam());
        getFragmentManager().beginTransaction()
                .replace(R.id.detail_content, teamInfoFragment).commit();

        return root;
    }

}
