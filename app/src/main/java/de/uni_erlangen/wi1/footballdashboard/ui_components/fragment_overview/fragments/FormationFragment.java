package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import ca.hss.heatmaplib.HeatMap;
import de.uni_erlangen.wi1.footballdashboard.ui_components.HeatMapHelper;
import de.uni_erlangen.wi1.footballdashboard.PlayerTeam;
import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.custom_views.PlayerView;


public class FormationFragment extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int layoutId;
    private boolean homeTeam;
    private final PlayerView[] playerViews;
    private PlayerTeam team;


    public FormationFragment()
    {
        playerViews = new PlayerView[11];
    }

    public static Fragment newInstance(int layoutId, boolean homeTeam)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, layoutId);
        args.putBoolean(ARG_PARAM2, homeTeam);

        FormationFragment tmp = new FormationFragment();
        tmp.setArguments(args);

        return tmp;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            layoutId = args.getInt(ARG_PARAM1, R.layout.formation2_left_to_right);
            homeTeam = args.getBoolean(ARG_PARAM2, false);
            if (homeTeam)
                team = GameGovernor.getInstance().getHomeTeam();
            else
                team = GameGovernor.getInstance().getAwayTeam();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View root = inflater.inflate(layoutId, container, false);

        // Init playerViews
        playerViews[0] = (PlayerView) root.findViewById(R.id.p1);
        playerViews[1] = (PlayerView) root.findViewById(R.id.p2);
        playerViews[2] = (PlayerView) root.findViewById(R.id.p3);
        playerViews[3] = (PlayerView) root.findViewById(R.id.p4);
        playerViews[4] = (PlayerView) root.findViewById(R.id.p5);
        playerViews[5] = (PlayerView) root.findViewById(R.id.p6);
        playerViews[6] = (PlayerView) root.findViewById(R.id.p7);
        playerViews[7] = (PlayerView) root.findViewById(R.id.p8);
        playerViews[8] = (PlayerView) root.findViewById(R.id.p9);
        playerViews[9] = (PlayerView) root.findViewById(R.id.p10);
        playerViews[10] = (PlayerView) root.findViewById(R.id.p11);

        // Setup heatMap
        final HeatMap heatMap = (HeatMap) root.findViewById(R.id.heatmap);
        heatMap.setMinimum(10.0);
        heatMap.setMaximum(100.0);
        Map<Float, Integer> colors = new ArrayMap<>();
        colors.put(0.0f, 0xffeef442);
        colors.put(1.0f, 0xff0000);
        heatMap.setColorStops(colors);

        // Setup teamHolder
        team.setViews(playerViews);

        for (final PlayerView pView : playerViews) {
            pView.playerImage.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    // Draw heatMap on UI-Thread
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            heatMap.clearData();
                            if (team.prepHeatMap(pView)) {
                                HeatMapHelper.addDataPointsToHeatmap(pView.getMappedPlayer(), heatMap, homeTeam);
                            }
                            heatMap.forceRefresh();
                        }
                    });
                }
            });
        }

        return root;
    }

}
