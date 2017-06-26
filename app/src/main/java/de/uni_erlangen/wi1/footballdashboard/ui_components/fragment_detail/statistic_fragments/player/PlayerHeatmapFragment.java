package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.statistic_fragments.player;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import ca.hss.heatmaplib.HeatMap;
import de.uni_erlangen.wi1.footballdashboard.ui_components.HeatMapHelper;
import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;

/**
 * Created by knukro on 6/20/17.
 */

public class PlayerHeatmapFragment extends Fragment implements IPlayerFragment
{

    private OPTA_Player player;
    private boolean homeTeam;

    public static Fragment newInstance(OPTA_Player player)
    {
        PlayerHeatmapFragment fragment = new PlayerHeatmapFragment();
        fragment.homeTeam = StatusBar.getInstance().isShowingHome();
        fragment.player = player;
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.stat_playerinfo_heatmap, container, false);

        HeatMap heatMap = (HeatMap) root.findViewById(R.id.stats_player_heatmap);
        heatMap.setMinimum(10.0);
        heatMap.setMaximum(100.0);
        Map<Float, Integer> colors = new ArrayMap<>();
        colors.put(0.0f, 0xffeef442);
        colors.put(1.0f, 0xff0000);
        heatMap.setColorStops(colors);

        HeatMapHelper.addDataPointsToHeatmap(player, heatMap, homeTeam);
        heatMap.forceRefresh();

        return root;
    }

    @Override
    public void setNewPlayer(OPTA_Player player)
    {
        this.player = player;
    }
}
