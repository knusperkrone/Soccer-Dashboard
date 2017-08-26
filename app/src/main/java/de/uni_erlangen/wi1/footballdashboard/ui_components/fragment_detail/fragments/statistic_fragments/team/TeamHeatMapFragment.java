package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.statistic_fragments.team;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import ca.hss.heatmaplib.HeatMap;
import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.helper.HeatMapHelper;
import de.uni_erlangen.wi1.footballdashboard.helper.StatisticHelper;
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
    private CheckBox activeBox;
    private CheckBox defenseBox;
    private CheckBox midfieldBox;
    private CheckBox offenseBox;

    private TextView passCounterView;
    private TextView shootCounterView;
    private TextView tackleCounterView;
    private TextView foulCounterView;


    public static Fragment newInstance(OPTA_Team team)
    {
        TeamHeatMapFragment frag = new TeamHeatMapFragment();
        frag.homeTeam = StatusBar.getInstance().isShowingHome();
        frag.team = team;
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.stat_teaminfo_heatmap, container, false);

        // Heatmap
        heatMap = (HeatMap) root.findViewById(R.id.stats_team_heatmap);
        HeatMapHelper.setupHeatMap(heatMap);
        // Checkboxes
        CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                setActive();
            }
        };

        activeBox = (CheckBox) root.findViewById(R.id.act_box);
        activeBox.setOnCheckedChangeListener(checkListener);
        defenseBox = (CheckBox) root.findViewById(R.id.def_box);
        defenseBox.setOnCheckedChangeListener(checkListener);
        midfieldBox = (CheckBox) root.findViewById(R.id.mid_box);
        midfieldBox.setOnCheckedChangeListener(checkListener);
        offenseBox = (CheckBox) root.findViewById(R.id.off_box);
        offenseBox.setOnCheckedChangeListener(checkListener);
        // TextViews
        passCounterView = (TextView) root.findViewById(R.id.team_paesse);
        shootCounterView = (TextView) root.findViewById(R.id.team_shoots);
        tackleCounterView = (TextView) root.findViewById(R.id.team_tackles);
        foulCounterView = (TextView) root.findViewById(R.id.team_fouls);

        setActive();

        return root;
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

        int[][] countedEvents = new int[5][2];

        for (OPTA_Player player : sortedPlayers) {
            // Filter
            CheckBox filter = null;
            switch (player.getPosition()) {
                case GOALKEEPER:
                case DEFENDER:
                    filter = defenseBox;
                    break;
                case MIDFIELDER:
                    filter = midfieldBox;
                    break;
                case STRIKER:
                    filter = offenseBox;
                    break;
                case SUBSTITUTE:
                    filter = activeBox;
                    break;
            }

            if (filter != null && filter.isChecked()) {
                StatisticHelper.countPlayerEvents(player, countedEvents, minVal, maxVal);
                HeatMapHelper.addDataPointsToHeatmap(player, heatMap, homeTeam, minVal, maxVal, pointValue);
            }
        }

        StatisticHelper.setText(passCounterView, countedEvents[0]);
        StatisticHelper.setText(shootCounterView, countedEvents[1]);
        StatisticHelper.setText(tackleCounterView, countedEvents[2]);
        StatisticHelper.setText(foulCounterView, countedEvents[3]);
        heatMap.forceRefresh();
    }

}
