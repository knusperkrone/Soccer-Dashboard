package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.DatabaseAdapter;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.live_list.ILiveFilter;
import de.uni_erlangen.wi1.footballdashboard.ui_components.live_list.LiveTeamListAdapter;

/**
 * Created by knukro on 6/17/17.
 */

public class LiveEventListFragment extends Fragment
{

    private int teamId;
    private LiveTeamListAdapter listAdapter;


    public static LiveEventListFragment newInstance(int teamId)
    {
        LiveEventListFragment frag = new LiveEventListFragment();
        frag.teamId = teamId;
        return frag;
    }

    public LiveTeamListAdapter getLiveAdapter()
    {
        return listAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.part_detaillist, container, false);

        // Init expandableList
        RecyclerView expList = (RecyclerView) root.findViewById(R.id.main_list);
        expList.setHasFixedSize(true);
        expList.setItemViewCacheSize(50);
        expList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get data Range
        StatusBar statusBar = StatusBar.getInstance();
        List<OPTA_Event> passedEvents = DatabaseAdapter.getInstance()
                .getLiveListData(teamId, statusBar.getMinRange(), statusBar.getMaxRange());
        // init listAdapter
        listAdapter = new LiveTeamListAdapter(passedEvents, getContext(), teamId, expList,
                new ILiveFilter()
                {
                    @Override
                    public boolean isValid(OPTA_Event event)
                    {
                        return event.teamId == teamId;
                    }
                });
        // Set and register adapter
        expList.setAdapter(listAdapter);
        StatusBar.getInstance().setLiveTeamListAdapter(listAdapter);

        return root;
    }


}
