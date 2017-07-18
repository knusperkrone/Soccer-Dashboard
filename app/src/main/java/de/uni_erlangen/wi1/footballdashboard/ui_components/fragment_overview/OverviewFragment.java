package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.ui_components.ActiveView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.viewpager_adapter.FormatViewPagerAdapter;
import de.uni_erlangen.wi1.footballdashboard.ui_components.main_list.MainListView;

public class OverviewFragment extends Fragment implements ActiveView
{

    private MainListView mainListView;

    public static OverviewFragment newInstance(MainListView mainListView)
    {
        OverviewFragment frag = new OverviewFragment();
        frag.mainListView = mainListView;
        return frag;
    }

    @Override
    public void setActive()
    {
    }

    @Override
    public void setInactive()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.fragment_overview, container, false);

        // Set's up the formationViewPager so you can switch between both teams
        ViewPager formationViewPager = (ViewPager) root.findViewById(R.id.overview_viewpager);
        FormatViewPagerAdapter pagerAdapter =
                new FormatViewPagerAdapter(getFragmentManager(), mainListView);
        formationViewPager.setAdapter(pagerAdapter);

        return root;
    }

}