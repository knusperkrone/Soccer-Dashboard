package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ca.hss.heatmaplib.HeatMap;
import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.helper.HeatMapHelper;
import de.uni_erlangen.wi1.footballdashboard.helper.ReferenceHolder;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.custom_views.PlayerView;


public class FormationFragment extends Fragment
{

    private final PlayerView[] playerViews;
    private OPTA_Team team;
    private int layoutId;

    private final ReferenceHolder<FormationClickListener> clickedListener = new ReferenceHolder<>();

    public FormationFragment()
    {
        playerViews = new PlayerView[11];
    }

    public static FormationFragment newInstance(int layoutId, boolean homeTeam)
    {
        FormationFragment frag = new FormationFragment();
        // Set necessary data
        GameGovernor gov = GameGovernor.getInstance();
        frag.team = (homeTeam) ? gov.getHomeTeam() : gov.getAwayTeam();
        frag.layoutId = layoutId;

        return frag;
    }

    public void clearViewOverlays()
    {
        if (clickedListener.val != null)
            clickedListener.val.reset();
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

        // Map views to team
        team.setViews(playerViews);

        // Setup heatMap
        HeatMap heatMap = (HeatMap) root.findViewById(R.id.heatmap);
        HeatMapHelper.setupHeatMap(heatMap);


        // References to shared values in the team, necessary to a consistent view state
        ReferenceHolder<View> viewHolder = new ReferenceHolder<>();
        ReferenceHolder<Integer> clickStateHolder = new ReferenceHolder<>(1);
        // Setup onClick listener
        FrameLayout overLayout = (FrameLayout) root.findViewById(R.id.frame);
        Log.d("FORMATION_FRAG", "OVerviewlayout:" + overLayout);

        for (final PlayerView pView : playerViews) {
            // Set all the same references for each player in the team
            pView.playerImage.setOnClickListener(
                    new FormationClickListener(team, pView, playerViews, getActivity(), heatMap,
                            overLayout, viewHolder, clickStateHolder, clickedListener));
        }

        return root;
    }
}
