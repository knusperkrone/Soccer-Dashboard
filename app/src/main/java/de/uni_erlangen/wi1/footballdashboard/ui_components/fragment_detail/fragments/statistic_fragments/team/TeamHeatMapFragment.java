package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.statistic_fragments.team;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.hss.heatmaplib.HeatMap;
import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.helper.HeatMapHelper;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.seekbar.OnSeekBarChangeAble;

/**
 * Created by knukro on 6/20/17.
 * Draws a heatMap for the whole Team, in the seekBar range and has some filter options
 */

public class TeamHeatMapFragment extends Fragment implements ITeamFragment, OnSeekBarChangeAble
{

    private OPTA_Team team;
    private boolean homeTeam;

    private HeatMap heatMap;

    public static Fragment newInstance(OPTA_Team team)
    {
        TeamHeatMapFragment frag = new TeamHeatMapFragment();
        frag.homeTeam = StatusBar.getInstance().isShowingHome();
        frag.team = team;
        return frag;
    }

    @Override
    public void setNewTeam(OPTA_Team team)
    {
        this.team = team;
    }

    @Override
    public void setActive()
    {
        if (heatMap == null)
            return;

        StatusBar statusBar = StatusBar.getInstance();
        int minTime = statusBar.getMinRange();
        int maxTime = statusBar.getMaxRange();
        seekBarChanged(minTime, maxTime);

        StatusBar.getInstance().registerOnClicked(this);
    }

    @Override
    public void setInactive()
    {
        StatusBar.getInstance().unRegisterOnClicked(this);
    }

    @Override
    public void seekBarChanged(int minVal, int maxVal)
    {
        // Redraw heatmap and evaluate point transparency
        double pointValue = HeatMapHelper.evaluatePointValue(minVal, maxVal);
        OPTA_Player[] sortedPlayers = team.getRankedPlayers(maxVal);
        heatMap.clearData();

        for (OPTA_Player player : sortedPlayers) {
            // TODO: POSITION filter
            HeatMapHelper.addDataPointsToHeatmap(player, heatMap, homeTeam, minVal, maxVal, pointValue);
        }

        heatMap.forceRefresh();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.stat_teaminfo_heatmap, container, false);

        heatMap = (HeatMap) root.findViewById(R.id.stats_team_heatmap);
        HeatMapHelper.setupHeatMap(heatMap);

        setActive();

        return root;
    }


}
