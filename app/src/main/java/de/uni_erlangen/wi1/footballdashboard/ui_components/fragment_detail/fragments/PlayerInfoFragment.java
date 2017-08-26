package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.ActiveView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.custom_views.TabIconView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.viewpager_adapter.PlayerStatsViewPagerAdapter;

/**
 * Created by knukro on 6/18/17
 * .
 */

public class PlayerInfoFragment extends Fragment implements ActiveView
{

    private OPTA_Player player;
    private OPTA_Team team;

    private PlayerStatsViewPagerAdapter playerStatsAdapter;
    private TextView name, position, number;
    private CircleImageView image;
    private boolean init = false;

    public static Fragment newInstance(OPTA_Team team, OPTA_Player player)
    {
        PlayerInfoFragment frag = new PlayerInfoFragment();
        frag.setValues(team, player);
        return frag;
    }

    public void setValues(OPTA_Team team, OPTA_Player player)
    {
        this.player = player;
        this.team = team;
        if (init) // onCreateView() already setted up everything
            initPlayerData();
    }

    private void initPlayerData()
    {
        // Setup Player Info
        name.setText(player.getName());
        position.setText(String.valueOf(player.getPosition()));
        number.setText(String.valueOf(player.getShirtNumber()));
        image.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.lloris)); //TODO: Dynamically!
        // Set Border Color according to Card
        if (player.hasYellowCard())
            image.setBorderColor(ContextCompat.getColor(getContext(), R.color.playerinfo_yellow));
        else if (player.hasRedCard())
            image.setBorderColor(ContextCompat.getColor(getContext(), R.color.playerinfo_red));
        else
            image.setBorderColor(ContextCompat.getColor(getContext(), R.color.playerinfo_normal));

        // Also change player for statisticsViewPager
        if (playerStatsAdapter != null)
            playerStatsAdapter.setNewPlayer(player);
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.fragment_detail_playerinfo, container, false);

        // Bind view references
        name = (TextView) root.findViewById(R.id.details_player_info_name);
        position = (TextView) root.findViewById(R.id.details_player_info_pos);
        number = (TextView) root.findViewById(R.id.details_player_info_number);
        image = (CircleImageView) root.findViewById(R.id.details_player_info_image);
        init = true;

        // Fill references
        initPlayerData();

        // Setup static viewPager
        TabLayout mTabLayout = (TabLayout) root.findViewById(R.id.details_player_tab);
        ViewPager statisticPager = (ViewPager) root.findViewById(R.id.details_player_viewpager);

        playerStatsAdapter = new PlayerStatsViewPagerAdapter(getFragmentManager(), team, player);
        statisticPager.setAdapter(playerStatsAdapter);
        mTabLayout.setupWithViewPager(statisticPager);

        // Fill tabLayout with nice round Icons TODO: Create nice icons
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setCustomView(new TabIconView(getContext(), null));
        }

        return root;
    }

    @Override
    public void setActive()
    {
    }

    @Override
    public void setInactive()
    {

    }
}
