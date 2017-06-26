package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.formation_adapter.FormatViewPagerAdapter;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.fragments.LiveEventListFragment;

public class OverviewFragment extends Fragment
{

    private LiveEventListFragment listFrag;

    public static OverviewFragment newInstance()
    {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void prepare()
    {
        StatusBar.getInstance().setLiveEventListAdapter(listFrag.getLiveAdapter());
    }

    private void showList()
    {
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

        ViewPager formationViewPager = (ViewPager) root.findViewById(R.id.overview_viewpager);
        FormatViewPagerAdapter pagerAdapter = new FormatViewPagerAdapter(getFragmentManager());
        formationViewPager.setAdapter(pagerAdapter);

        //Pass data to singletons
        final StatusBar statusBar = StatusBar.getInstance();
        formationViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                statusBar.teamSwitched(position);
                listFrag = LiveEventListFragment.newInstance(statusBar.shownTeamId);
                showList();
            }
        });

        // Starts with homeTeam
        listFrag = LiveEventListFragment.newInstance(GameGovernor.getInstance().getHomeTeamId());
        showList();

        return root;
    }

}