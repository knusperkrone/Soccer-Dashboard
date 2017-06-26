package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.graph_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.statistic_fragments.player.IPlayerFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.statistic_fragments.player.PlayerHeatmapFragment;

/**
 * Created by knukro on 6/20/17.
 */

public class PlayerStatsViewPagerAdapter extends FragmentStatePagerAdapter
{

    private FragmentManager fm;
    private IPlayerFragment currFragment;
    private OPTA_Player player;

    public PlayerStatsViewPagerAdapter(FragmentManager fm, OPTA_Player player)
    {
        super(fm);
        this.fm = fm;
        this.player = player;
    }

    public void setNewPlayer(OPTA_Player player)
    {
        this.player = player;
        currFragment.setNewPlayer(player);
        // Redraw Fragment
        fm.beginTransaction()
                .detach((Fragment) currFragment)
                .attach((Fragment) currFragment)
                .commit();
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
                return PlayerHeatmapFragment.newInstance(player);
            case 1:
            case 2:
                return FragmentPieExample.newInstance();
        }
        throw new IllegalArgumentException("DEAD_CODE");
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object)
    {
        if (currFragment != object) {
            currFragment = ((IPlayerFragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getCount()
    {
        return 3;
    }
}
