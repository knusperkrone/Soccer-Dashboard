package de.uni_erlangen.wi1.footballdashboard.UI_Components.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.UI_Components.ViewPagerAdapter.FormatViewPagerAdapter;

public class OverviewFragment extends Fragment
{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int layout_team1;
    private int layout_team2;


    public static OverviewFragment newInstance(int layout_team1, int layout_team2)
    {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, layout_team1);
        args.putInt(ARG_PARAM2, layout_team2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            layout_team1 = args.getInt(ARG_PARAM1);
            layout_team2 = args.getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.fragment_overview, container, false);

        ViewPager formationViewPager = (ViewPager) root.findViewById(R.id.overview_viewpager);
        formationViewPager.setAdapter(new FormatViewPagerAdapter(getFragmentManager(),
                layout_team1, layout_team2));
        return root;
    }

}