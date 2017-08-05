package de.uni_erlangen.wi1.footballdashboard.ui_components.main_list;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;

/**
 * Created by knukro on 18.07.17.
 *
 */

public class MainListView extends LinearLayout
{

    private RecyclerView recyclerView;

    private LiveTeamListAdapter overViewListAdapter;
    private LivePlayerListAdapter detailFragListAdapter;


    public MainListView(Context context)
    {
        super(context);
        init();
    }

    public MainListView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public MainListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init()
    {
        inflate(getContext(), R.layout.main_list_helper, this);
        recyclerView = (RecyclerView) findViewById(R.id.main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(50);

        overViewListAdapter = new LiveTeamListAdapter(getContext(), recyclerView);
    }

    public void setActive(int position)
    {
        if (position == 0) {
            recyclerView.setAdapter(overViewListAdapter);
            StatusBar.getInstance().setLiveTeamListAdapter(overViewListAdapter);
        } else if (detailFragListAdapter != null) {
            recyclerView.setAdapter(detailFragListAdapter);
        }
    }

    public void setOverviewListTeam(int teamId, ILiveFilter filter)
    {
        StatusBar sBar = StatusBar.getInstance();
        overViewListAdapter.teamChanged(filter, teamId, sBar.getMinRange(), sBar.getMaxRange());
        setAdapter(overViewListAdapter);
    }

    public void setLivePlayerListAdapter(FragmentManager fm, OPTA_Team team)
    {
        if (detailFragListAdapter == null)
            detailFragListAdapter = new LivePlayerListAdapter(fm, getContext(), team);
        setAdapter(detailFragListAdapter);
    }

    public boolean playerAdapterChangedTeam(OPTA_Team team)
    {
        boolean teamChanged = detailFragListAdapter.changedTeam(team);
        setAdapter(detailFragListAdapter);
        return teamChanged;
    }

    public void setAdapter(RecyclerView.Adapter adapter)
    {
        //TODO: Nice animation
        recyclerView.setAdapter(adapter);
    }

}
