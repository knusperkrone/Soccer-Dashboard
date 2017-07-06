package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

import de.hdodenhof.circleimageview.CircleImageView;
import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.custom_views.TabIconView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.viewpager_adapter.PlayerStatsViewPagerAdapter;
import de.uni_erlangen.wi1.footballdashboard.ui_components.live_list.ILiveFilter;
import de.uni_erlangen.wi1.footballdashboard.ui_components.live_list.LiveTeamListAdapter;

/**
 * Created by knukro on 6/18/17.
 */

public class PlayerInfoFragment extends Fragment
{

    private OPTA_Player player;
    private int teamId;

    private PlayerStatsViewPagerAdapter playerStatsAdapter;
    private RecyclerView playerLiveList;
    private TextView name, position, number;
    private CircleImageView image;
    boolean init = false;

    public static Fragment newInstance(OPTA_Player player, int teamId)
    {
        PlayerInfoFragment frag = new PlayerInfoFragment();
        frag.setValues(player, teamId);
        return frag;
    }

    public void refreshStatistics()
    {
        playerStatsAdapter.refreshActiveItem();
    }

    public void setValues(OPTA_Player player, int teamId)
    {
        this.player = player;
        this.teamId = teamId;
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

        // Create a list with all passed event's for this Player
        StatusBar bar = StatusBar.getInstance();
        int maxTime = bar.getMaxRange();
        int minTime = bar.getMinRange();
        LinkedList<OPTA_Event> passedPlayerActions = new LinkedList<>();

        for (OPTA_Event event : player.getActions()) {
            if (event.getCRTime() < minTime)
                continue; // Not quite yet
            if (event.getCRTime() > maxTime)
                break; // That's enough now
            passedPlayerActions.add(0, event);
        }
        // Create a liveListAdapter, with parsed data as base
        LiveTeamListAdapter adapter = new LiveTeamListAdapter(passedPlayerActions, getContext(),
                teamId, playerLiveList, new ILiveFilter()
        {
            @Override
            public boolean isValid(OPTA_Event event)
            {
                return event.playerId == player.getId();

            }
        });
        // Set and register LiveListAdapter
        playerLiveList.setAdapter(adapter);
        bar.setLiveTeamListAdapter(adapter);

        // Also change player for statisticsViewPager
        if (playerStatsAdapter != null)
            playerStatsAdapter.setNewPlayer(player);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.fragment_detail_playerinfo, container, false);

        // Setup LiveList
        playerLiveList = (RecyclerView) root.findViewById(R.id.details_player_playerlist);
        playerLiveList.setLayoutManager(new LinearLayoutManager(getContext()));
        playerLiveList.setHasFixedSize(true);
        // BInd view references
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
        playerStatsAdapter = new PlayerStatsViewPagerAdapter(getFragmentManager(), player);
        statisticPager.setAdapter(playerStatsAdapter);
        mTabLayout.setupWithViewPager(statisticPager);

        // Fill tabLayout with nice round Icons TODO: Create nice icons
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setCustomView(new TabIconView(getContext(), null));
        }

        return root;
    }
}
