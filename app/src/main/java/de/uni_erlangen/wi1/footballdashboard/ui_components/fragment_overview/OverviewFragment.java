package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.fragments.LiveEventListFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.viewpager_adapter.FormatViewPagerAdapter;

public class OverviewFragment extends Fragment
{

    private LiveEventListFragment listFrag; // LiveList

    public static OverviewFragment newInstance()
    {
        return new OverviewFragment();
    }

    public void setActive()
    {
        // (Re-)Registers the current liveList Adapter
        StatusBar.getInstance().setLiveTeamListAdapter(listFrag.getLiveAdapter());
    }

    private void refreshList()
    {
        // Draws the current listFrag
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.overview_list_placeholder, listFrag)
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.fragment_overview, container, false);

        // Set's up the formationViewPager so you can switch between both teams
        ViewPager formationViewPager = (ViewPager) root.findViewById(R.id.overview_viewpager);
        final FormatViewPagerAdapter pagerAdapter = new FormatViewPagerAdapter(getFragmentManager());
        formationViewPager.setAdapter(pagerAdapter);

        // FormationViewPager adjusts listFrag to the new team
        formationViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {

            final StatusBar statusBar = StatusBar.getInstance();

            @Override
            public void onPageSelected(int position)
            {
                // Redraw teamLiveList
                statusBar.teamSwitched(position);
                listFrag = LiveEventListFragment.newInstance(statusBar.shownTeamId);
                refreshList();

                // Delete inactive fragment Overlay
                pagerAdapter.getInactiveFragment().clearViewOverlays();
            }
        });

        // Shows homeTeam on first Start
        listFrag = LiveEventListFragment.newInstance(GameGovernor.getInstance().getHomeTeamId());
        refreshList();

        // Init and register RangeSeekbar
        RangeSeekBar rangeBar = (RangeSeekBar) root.findViewById(R.id.rangeBar);
        StatusBar.getInstance().setRangeBar(rangeBar);

        return root;
    }

}