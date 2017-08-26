package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.fragments;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;

import ca.hss.heatmaplib.HeatMap;
import de.uni_erlangen.wi1.footballdashboard.helper.HeatMapHelper;
import de.uni_erlangen.wi1.footballdashboard.helper.ReferenceHolder;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.custom_views.ArrowView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.custom_views.PlayerView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.seekbar.OnSeekBarChangeAble;


class FormationClickListener implements View.OnClickListener, OnSeekBarChangeAble
{

    // TeamShared Graphic/Layout references
    private final HeatMap heatMap;
    private final OPTA_Team team;
    private final PlayerView pView;
    private final PlayerView[] playerViews;
    private final Activity activity;
    private final FrameLayout parentLayout;
    // TeamShared ViewState state reference
    private final ReferenceHolder<View> teamArrowView;
    private final ReferenceHolder<Integer> teamClickState;
    private final ReferenceHolder<FormationClickListener> clickedListener;

    // Flags
    private final boolean homeTeam;
    private final StatusBar statusBar = StatusBar.getInstance();


    FormationClickListener(OPTA_Team team, PlayerView pView, PlayerView[] playerViews,
                           Activity activity, HeatMap heatMap, @NonNull FrameLayout parentLayout,
                           ReferenceHolder<View> teamArrowView,
                           ReferenceHolder<Integer> teamClickState,
                           ReferenceHolder<FormationClickListener> clickedListener)
    {
        this.team = team;
        this.pView = pView;
        this.playerViews = playerViews;
        this.activity = activity;
        this.heatMap = heatMap;
        this.parentLayout = parentLayout;
        this.teamArrowView = teamArrowView;
        this.teamClickState = teamClickState;
        this.clickedListener = clickedListener;
        this.homeTeam = team.isHomeTeam();
    }

    @Override
    public void onClick(View view)
    {
        // Check if we already clicked that view, if yes, we reset the state
        if (!team.OnClicked(pView))
            teamClickState.val = 0;

        // Register this as only active item
        statusBar.registerOnClicked(this);
        clickedListener.val = this;

        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                switch (teamClickState.val) {
                    case 0:
                        clearViews();
                        state0(); // HeatMap
                        teamClickState.val = 1;
                        break;
                    case 1:
                        clearViews();
                        state1(); // Passes by player
                        teamClickState.val = 2;
                        break;
                    case 2:
                        clearViews();
                        state2(); // Passes to player
                        teamClickState.val = 3;
                        break;
                    case 3:
                        reset();
                        break;
                }
            }
        });
    }

    @Override
    public void seekBarChanged(int minVal, int maxVal)
    {
        clearViews();
        switch (teamClickState.val) {
            case 1:
                state0();
                break;
            case 2:
                state1();
                break;
            case 3:
                state2();
                break;
        }
    }

    void reset()
    {
        // Reset internal state
        team.setUnclicked(pView); // Notify team
        statusBar.unRegisterOnClicked(this); // Unregister
        teamClickState.val = 0;
        clickedListener.val = null;
        clearViews();
    }

    private void clearViews()
    {
        heatMap.clearData();
        heatMap.forceRefresh();
        if (teamArrowView.val != null) {
            parentLayout.removeView(teamArrowView.val);
            teamArrowView.val = null;
        }
    }

    private void state0()
    {
        // Show heatMap
        heatMap.clearData();
        HeatMapHelper // Add data Points
                .drawCurrentHeatmap(pView.getMappedPlayer(), heatMap, homeTeam);
        heatMap.forceRefresh(); // Redraw
    }

    private void state1()
    {
        // Draw arrowView - passes FROM this player
        SparseArray passCount = team
                .getPassesBy(pView.getMappedPlayer(),
                        statusBar.getMinRange(), statusBar.getMaxRange());
        teamArrowView.val =
                new ArrowView(activity, pView, playerViews, passCount, true);
        parentLayout.addView(teamArrowView.val);
    }

    private void state2()
    {
        // Draw new arrowView - passes FOR this player
        SparseArray passCount = team.getPassesFor(pView.getMappedPlayer(),
                statusBar.getMinRange(), statusBar.getMaxRange());
        teamArrowView.val =
                new ArrowView(activity, pView, playerViews, passCount, false);
        parentLayout.addView(teamArrowView.val);
    }

}
