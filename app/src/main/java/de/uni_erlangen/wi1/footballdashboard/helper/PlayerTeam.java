package de.uni_erlangen.wi1.footballdashboard;

import android.util.SparseArray;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.custom_views.PlayerView;


public class PlayerTeam
{

    private final int teamId;
    private final boolean homeTeam;
    private final String name;

    private final SparseArray<OPTA_Player> playerSparseArray;

    private List<OPTA_Event> events;

    private PlayerView[] playerViews;
    private OPTA_Player[] rankedPlayers;


    public PlayerTeam(int teamId, String name, boolean homeTeam)
    {
        this.teamId = teamId;
        this.homeTeam = homeTeam;
        this.name = name;
        this.playerSparseArray = new SparseArray<>(11);
    }

    public boolean setClicked(PlayerView selectedView)
    {
        /* Only selected view is clicked and returns
           true if player is already selected  */
        for (PlayerView currView : playerViews) {
            if (currView == selectedView) {
                if (selectedView.isClickedMode())
                    return true;
                currView.setClickedMode();
            } else {
                currView.setDefaultMode();
            }
        }
        return false;
    }

    public void setUnclicked(PlayerView selectedView)
    {
        selectedView.setDefaultMode();
    }

    public void setViews(PlayerView[] playerViews)
    {
        this.playerViews = playerViews;
        mapViewToPlayerData();
    }

    public String getPlayerName(int playerId)
    {
        OPTA_Player tmp = playerSparseArray.get(playerId);
        return (tmp != null) ? tmp.getName() : null;
    }

    public void updateLightColors()
    {
        Arrays.sort(rankedPlayers);
        /*
        , new Comparator<OPTA_Player>()
        {
            @Override
            public int compare(OPTA_Player opta_player, OPTA_Player t1)
            {
                if (opta_player.isActive() && !t1.isActive())
                    return -1;
                else if(!opta_player.isActive() && t1.isActive())
                    return 1;
                else if (!opta_player.isActive() && !t1.isActive())
                    return 0;
                return opta_player.getRankingPoints() - t1.getRankingPoints();
            }
        });
        */
        int i = 0;
        for (OPTA_Player player : rankedPlayers) {
            //Log.d("[PlayerTeam]", "Rank " + i+ " " + player.getName());
            if (player.mappedView != null) {
                player.mappedView.checkLightStatus();
                if (i < 3) // Best 3
                    player.mappedView.isTop();
                else if (i > 7) // Worst 3
                    player.mappedView.isBad();
                else
                    player.mappedView.isAverage();
            }
            i++;
        }
    }

    public List<OPTA_Event> getEvents()
    {
        return events;
    }

    public void setEvents(List<OPTA_Event> events)
    {
        this.events = events;
    }


    public SparseArray<OPTA_Player> getPlayers()
    {
        return playerSparseArray;
    }

    public void sortRankedPlayers()
    {
        Arrays.sort(rankedPlayers);
    }

    public OPTA_Player[] getRankedPlayers()
    {
        return rankedPlayers;
    }

    private void mapViewToPlayerData()
    {
        // Fill players Array
        rankedPlayers = new OPTA_Player[playerSparseArray.size()];
        for (int i = 0; i < playerSparseArray.size(); i++)
            rankedPlayers[i] = playerSparseArray.valueAt(i);
        // Sort players layoutPosition
        Arrays.sort(rankedPlayers, new Comparator<OPTA_Player>()
        {
            @Override
            public int compare(OPTA_Player p, OPTA_Player t1)
            {
                if (!p.isActive() && !t1.isActive())
                    return 0;
                if (!t1.isActive())
                    return -1;
                if (!p.isActive())
                    return 1;
                return p.getLayoutPosition() - t1.getLayoutPosition();
            }
        });
        // Actuall mapping
        int i = 0;
        for (PlayerView playerView : playerViews) {
            playerView.setMappedPlayer(rankedPlayers[i]);
            rankedPlayers[i].mapView(playerView);
            i++;
        }
    }


    public String getTeamName()
    {
        return name;
    }

    public int getId()
    {
        return teamId;
    }

    public boolean isHomeTeam()
    {
        return homeTeam;
    }

}
