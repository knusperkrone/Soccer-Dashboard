package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ca.hss.heatmaplib.HeatMap;
import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.helper.HeatMapHelper;
import de.uni_erlangen.wi1.footballdashboard.helper.ReferenceHolder;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.ActiveView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.custom_views.PlayerView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.viewpager_adapter.FormatViewPagerAdapter;
import de.uni_erlangen.wi1.footballdashboard.ui_components.main_list.ILiveFilter;
import de.uni_erlangen.wi1.footballdashboard.ui_components.main_list.MainListView;


public class FormationFragment extends Fragment implements ActiveView
{

    private final PlayerView[] playerViews;
    private MainListView mainListView;
    private OPTA_Team team;
    private boolean homeTeam;
    private int layoutId;

    private final ReferenceHolder<FormationClickListener> clickedListener = new ReferenceHolder<>();

    public FormationFragment()
    {
        playerViews = new PlayerView[11];
    }

    public static FormationFragment newInstance(int layoutId, MainListView mainListView, boolean homeTeam)
    {
        FormationFragment frag = new FormationFragment();
        // Set necessary data
        GameGovernor gov = GameGovernor.getInstance();
        frag.mainListView = mainListView;
        frag.team = (homeTeam) ? gov.getHomeTeam() : gov.getAwayTeam();
        frag.layoutId = layoutId;
        frag.homeTeam = homeTeam;

        return frag;
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
        team.setViews(playerViews, this);

        // Setup heatMap
        HeatMap heatMap = (HeatMap) root.findViewById(R.id.heatmap);
        HeatMapHelper.setupHeatMap(heatMap);


        // References to shared values in the team, necessary to a consistent view state
        ReferenceHolder<View> viewHolder = new ReferenceHolder<>();
        ReferenceHolder<Integer> clickStateHolder = new ReferenceHolder<>(1);
        // Setup onClick listener
        FrameLayout overLayout = (FrameLayout) root.findViewById(R.id.playfieldlayout);
        for (final PlayerView pView : playerViews) {
            // Set all the same references for each player in the team
            pView.playerImage.setOnClickListener(
                    new FormationClickListener(team, pView, playerViews, getActivity(), heatMap,
                            overLayout, viewHolder, clickStateHolder, clickedListener));
        }

        return root;
    }

    public void changeFormation(int optaId)
    {
        // Get new layoutId and call onCreateView()
        int newLayoutId;
        if (homeTeam) {
            newLayoutId = FormatViewPagerAdapter.getHomeLayoutId(optaId);
        } else {
            newLayoutId = FormatViewPagerAdapter.getAwayLayoutId(optaId);
        }

        this.layoutId = newLayoutId;
        getFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();
    }


    @Override
    public void setActive()
    {
        if (mainListView != null) {
            mainListView.setOverviewListTeam(team.getId(), new ILiveFilter()
            {
                @Override
                public boolean isValid(OPTA_Event event)
                {
                    return event.getTeamId() == team.getId();
                }
            });
        }
    }

    @Override
    public void setInactive()
    {
        if (clickedListener.val != null)
            clickedListener.val.reset();
    }
}
