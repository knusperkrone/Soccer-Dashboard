package de.uni_erlangen.wi1.footballdashboard.UI_Components.ViewPagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import de.uni_erlangen.wi1.footballdashboard.UI_Components.Fragments.FormationFragment;

/**
 * Created by knukro on 5/24/17.
 */

public class FormatViewPagerAdapter extends FragmentStatePagerAdapter
{

    private final int layout1, layout2;

    public FormatViewPagerAdapter(FragmentManager fm, int layout1, int layout2)
    {
        super(fm);
        this.layout1 = layout1;
        this.layout2 = layout2;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0: return FormationFragment.newInstance(layout1);
            case 1: return FormationFragment.newInstance(layout2);
        }
        throw new IllegalArgumentException("Size "+ getCount() + " - request"+ position );
    }

    @Override
    public int getCount()
    {
        return 2;
    }
}
