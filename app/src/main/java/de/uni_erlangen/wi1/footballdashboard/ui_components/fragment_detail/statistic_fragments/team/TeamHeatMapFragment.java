package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.statistic_fragments.team;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import ca.hss.heatmaplib.HeatMap;
import de.uni_erlangen.wi1.footballdashboard.PlayerTeam;
import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.ui_components.HeatMapHelper;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;

/**
 * Created by knukro on 6/20/17.
 */

public class TeamHeatMapFragment extends Fragment implements ITeamFragment
{

    private PlayerTeam team;
    private boolean homeTeam;

    private HeatMap heatMap;

    public static Fragment newInstance(PlayerTeam team)
    {
        TeamHeatMapFragment frag = new TeamHeatMapFragment();
        frag.homeTeam = StatusBar.getInstance().isShowingHome();
        frag.team = team;
        return frag;
    }

    @Override
    public void changeTeam(PlayerTeam team)
    {
        Log.d("[TEAM_HEATMAP_FRAG]", "changeTeam called()");
        this.team = team;
        draw();
    }

    private void draw()
    {
        heatMap.clearData();
        for (OPTA_Player player : team.getRankedPlayers()) {
            HeatMapHelper.addDataPointsToHeatmap(player, heatMap, homeTeam);
        }
        heatMap.forceRefresh();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.stat_teaminfo_heatmap, container, false);

        heatMap = (HeatMap) root.findViewById(R.id.stats_team_heatmap);
        heatMap.setMinimum(10.0);
        heatMap.setMaximum(100.0);
        Map<Float, Integer> colors = new ArrayMap<>();
        colors.put(0.0f, 0xffeef442);
        colors.put(1.0f, 0xff0000);
        heatMap.setColorStops(colors);

        draw();

        return root;
    }
}
