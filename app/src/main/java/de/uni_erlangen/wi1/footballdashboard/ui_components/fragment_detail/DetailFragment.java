package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.ActiveView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.PlayerInfoFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.TeamInfoFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.main_list.MainListView;

public class DetailFragment extends Fragment implements ActiveView
{

    private final GameGovernor gov = GameGovernor.getInstance();
    private MainListView mainListView;

    public static DetailFragment newInstance(MainListView mainListView)
    {
        DetailFragment fragment = new DetailFragment();
        fragment.mainListView = mainListView;
        return fragment;
    }

    @Override
    public void setActive()
    {

        // Prepares View (Fragments) when user scrolls
        OPTA_Team team = GameGovernor.getInstance().getDisplayedTeam();
        // Get active fragment
        Fragment unknownFrag = getFragmentManager().findFragmentById(R.id.detail_content);

        if (mainListView.playerAdapterChangedTeam(team)) {
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
            ((PlayerInfoFragment) unknownFrag).setActive();
        } else {
            // We didn't change the team and have Team statistic open
            ((TeamInfoFragment) unknownFrag).setActive();
        }
    }


    @Override
    public void setInactive()
    {
        ActiveView view = (ActiveView) getFragmentManager().findFragmentById(R.id.detail_content);
        if (view != null)
            view.setInactive();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.fragment_detail, container, false);

        mainListView.setLivePlayerListAdapter(getFragmentManager(), GameGovernor.getInstance().getDisplayedTeam());

        // Create and show a TeamFragment
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.detail_content, TeamInfoFragment.newInstance(gov.getDisplayedTeam()))
                .commit();

        return root;
    }

}
