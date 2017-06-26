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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.custom_views.TabIconView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.graph_adapter.PlayerStatsViewPagerAdapter;
import de.uni_erlangen.wi1.footballdashboard.ui_components.list_live_event.LiveEventListAdapter;
import de.uni_erlangen.wi1.footballdashboard.ui_components.list_live_event.LiveFilter;

/**
 * Created by knukro on 6/18/17.
 */

public class PlayerInfoFragment extends Fragment
{

    private final List<OPTA_Event> passedActions = new LinkedList<>();
    private OPTA_Player player;

    private PlayerStatsViewPagerAdapter playerAdapter;
    private RecyclerView playerActions;
    private TextView name, position, number;
    private CircleImageView image;

    public static Fragment newInstance(OPTA_Player player)
    {
        // TODO: Check for already inited instance
        PlayerInfoFragment frag = new PlayerInfoFragment();
        frag.setValues(player);
        return frag;
    }

    public void setValues(OPTA_Player player)
    {
        this.player = player;
        if (name != null) // Already inited?
            initPlayerData();
    }

    private void initPlayerData()
    {
        // Player Info
        name.setText(player.getName());
        position.setText(String.valueOf(player.getPosition()));
        number.setText(String.valueOf(player.getShirtNumber()));
        image.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.lloris));
        // Set Border Color
        if (player.hasYellowCard())
            image.setBorderColor(ContextCompat.getColor(getContext(), R.color.playerinfo_yellow));
        else if (player.hasRedCard())
            image.setBorderColor(ContextCompat.getColor(getContext(), R.color.playerinfo_red));
        else
            image.setBorderColor(ContextCompat.getColor(getContext(), R.color.playerinfo_normal));

        // Player actionList
        passedActions.clear();
        StatusBar bar = StatusBar.getInstance();
        int currTime = bar.currTime;
        for (OPTA_Event event : player.getActions()) {
            if (event.getCRTime() > currTime)
                break;
            passedActions.add(0, event);
        }
        LiveEventListAdapter adapter = new LiveEventListAdapter(passedActions, getContext(),
                playerActions, new LiveFilter()
        {
            @Override
            public boolean isValid(OPTA_Event event)
            {
                return event.playerId == player.getId();

            }
        });
        playerActions.setAdapter(adapter);
        bar.setLiveEventListAdapter(adapter);

        // Statistic View
        if (playerAdapter != null)
            playerAdapter.setNewPlayer(player);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.fragment_detail_playerinfo, container, false);

        // Player liveList
        playerActions = (RecyclerView) root.findViewById(R.id.details_player_playerlist);
        playerActions.setLayoutManager(new LinearLayoutManager(getContext()));
        playerActions.setHasFixedSize(true);
        // PlayerInfo
        name = (TextView) root.findViewById(R.id.details_player_info_name);
        position = (TextView) root.findViewById(R.id.details_player_info_pos);
        number = (TextView) root.findViewById(R.id.details_player_info_number);
        image = (CircleImageView) root.findViewById(R.id.details_player_info_image);
        initPlayerData(); // Set values

        //Statistics
        TabLayout mTabLayout = (TabLayout) root.findViewById(R.id.details_player_tab);
        ViewPager statisticPager = (ViewPager) root.findViewById(R.id.details_player_viewpager);
        playerAdapter = new PlayerStatsViewPagerAdapter(getFragmentManager(), player);
        statisticPager.setAdapter(playerAdapter);
        mTabLayout.setupWithViewPager(statisticPager);
        // Fill with Icons!
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setCustomView(new TabIconView(getContext(), null));
        }


        return root;
    }
}
