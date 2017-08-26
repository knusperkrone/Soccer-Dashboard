package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.statistic_fragments.player;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.hss.heatmaplib.HeatMap;
import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.helper.HeatMapHelper;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.ui_components.ActiveView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.main_list.ILiveFilter;
import de.uni_erlangen.wi1.footballdashboard.ui_components.main_list.LiveActionListAdapter;

/**
 * Created by knukro on 6/20/17.
 */

public class PlayerHeatmapFragment extends Fragment implements IPlayerFragment, ActiveView
{

    private OPTA_Player player;
    private boolean homeTeam;
    private HeatMap heatMap;
    private LiveActionListAdapter playerAdapter;

    public static Fragment newInstance(OPTA_Player player)
    {
        PlayerHeatmapFragment fragment = new PlayerHeatmapFragment();
        fragment.homeTeam = StatusBar.getInstance().isShowingHome();
        fragment.player = player;
        return fragment;
    }

    @Override
    public void setNewPlayer(final OPTA_Player player)
    {
        this.player = player;

        GameGovernor gov = GameGovernor.getInstance();
        StatusBar bar = StatusBar.getInstance();
        int teamId = (homeTeam) ? gov.getHomeTeamId() : gov.getAwayTeamId();
        this.playerAdapter.valuesChanged(makeFilter(), teamId, bar.getMinRange(), bar.getMaxRange());
    }

    @Override
    public void setActive()
    {
        if (heatMap != null)
            HeatMapHelper.drawCurrentHeatmap(player, heatMap, homeTeam);

        if (playerAdapter != null) // Register LiveActionList
            StatusBar.getInstance().setLiveActionListAdapter(playerAdapter);

        StatusBar.getInstance().registerOnClicked(this);
    }

    @Override
    public void setInactive()
    {
        StatusBar.getInstance().unRegisterOnClicked(this);
        StatusBar.getInstance().unregisterLiveActionListAdapter(playerAdapter);
    }

    @Override
    public void seekBarChanged(int minVal, int maxVal)
    {
        if (heatMap != null)
            HeatMapHelper.drawCurrentHeatmap(player, heatMap, homeTeam);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.stat_playerinfo_heatmap, container, false);

        heatMap = (HeatMap) root.findViewById(R.id.stats_player_heatmap);
        HeatMapHelper.setupHeatMap(heatMap);

        RecyclerView playerLiveList = (RecyclerView) root.findViewById(R.id.details_player_playerlivelist);
        playerLiveList.setLayoutManager(new LinearLayoutManager(getContext()));
        playerLiveList.setHasFixedSize(true);
        playerLiveList.setItemViewCacheSize(50);

        playerAdapter = new LiveActionListAdapter(getContext(), playerLiveList);
        playerAdapter.setFilter(makeFilter());
        playerLiveList.setAdapter(playerAdapter);
        setNewPlayer(player);

        return root;
    }

    private ILiveFilter makeFilter()
    {
        final OPTA_Player filterPlayer = player;
        return new ILiveFilter()
        {
            @Override
            public boolean isValid(OPTA_Event event)
            {
                return event.getPlayerId() == filterPlayer.getId();
            }
        };
    }

}
