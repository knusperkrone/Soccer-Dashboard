package de.uni_erlangen.wi1.footballdashboard.UI_Components.ViewPagerAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import de.uni_erlangen.wi1.footballdashboard.UI_Components.GraphExamples.FragmentPieExample;

/**
 * Created by knukro on 5/24/17.
 */

public class DetailStatisticViewPagerAdapter extends FragmentStatePagerAdapter
{

    private final Context context;

    public DetailStatisticViewPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
            case 1:
            case 2:
                return FragmentPieExample.newInstance();
        }
        throw new IllegalArgumentException("Size " + getCount() + " - request" + position);
    }

    @Override
    public int getCount()
    {
        return 3;
    }

}
