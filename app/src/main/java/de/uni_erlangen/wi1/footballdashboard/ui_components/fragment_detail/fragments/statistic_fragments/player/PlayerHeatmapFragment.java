package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.statistic_fragments.player;


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
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;

/**
 * Created by knukro on 6/20/17.
 */

public class PlayerHeatmapFragment extends Fragment implements IPlayerFragment
{

    private OPTA_Player player;
    private boolean homeTeam;
    private HeatMap heatMap;

    public static Fragment newInstance(OPTA_Player player)
    {
        PlayerHeatmapFragment fragment = new PlayerHeatmapFragment();
        fragment.homeTeam = StatusBar.getInstance().isShowingHome();
        fragment.player = player;
        return fragment;
    }

    @Override
    public void setNewPlayer(OPTA_Player player)
    {
        this.player = player;
    }

    public void drawStatistics()
    {
        HeatMapHelper.drawCurrentHeatmap(player, heatMap, homeTeam);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.stat_playerinfo_heatmap, container, false);

        heatMap = (HeatMap) root.findViewById(R.id.stats_player_heatmap);
        HeatMapHelper.setupHeatMap(heatMap);
        drawStatistics();

        return root;
    }
}
