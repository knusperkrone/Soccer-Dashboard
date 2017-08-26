package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.viewpager_adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.statistic_fragments.player.IPlayerFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.statistic_fragments.player.LineChartFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.statistic_fragments.player.PlayerHeatmapFragment;

/**
 * Created by knukro on 6/20/17.
 */

public class PlayerStatsViewPagerAdapter extends FragmentStatePagerAdapter
{

    private IPlayerFragment currFragment;
    private OPTA_Player player;
    private final OPTA_Team team;

    public PlayerStatsViewPagerAdapter(FragmentManager fm, OPTA_Team team, OPTA_Player player)
    {
        super(fm);
        this.team = team;
        this.player = player;
    }

    public void setNewPlayer(OPTA_Player player)
    {
        this.player = player;
        currFragment.setNewPlayer(player);
        currFragment.setActive();
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
                return PlayerHeatmapFragment.newInstance(player);
            case 1:
                return LineChartFragment.newInstance(team, player);
        }
        throw new IllegalArgumentException("Size " + getCount() + " - request: " + position);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object)
    {
        if (currFragment != object) {
            if (currFragment != null) {
                currFragment.setInactive();
            }

            currFragment = ((IPlayerFragment) object);
            currFragment.setActive();
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getCount()
    {
        return 2;
    }
}
