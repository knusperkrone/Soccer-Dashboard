package de.uni_erlangen.wi1.footballdashboard.UI_Components.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.UI_Components.ViewPagerAdapter.DetailStatisticViewPagerAdapter;
import de.uni_erlangen.wi1.footballdashboard.UI_Components.Views.TabIconView;

public class DetailFragment extends Fragment
{

    public static DetailFragment newInstance()
    {
        return new DetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View root =  inflater.inflate(R.layout.fragment_detail, container, false);

        ViewPager mViewPager = (ViewPager) root.findViewById(R.id.playerdetail_viewpager);
        TabLayout mTabLayout = (TabLayout) root.findViewById(R.id.details_tablayout);

        mViewPager.setAdapter(new DetailStatisticViewPagerAdapter(getFragmentManager(),getContext()));
        mTabLayout.setupWithViewPager(mViewPager);

        // Fill with Icons!
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setCustomView(new TabIconView(getContext(), null));
        }

        return root;
    }

}
