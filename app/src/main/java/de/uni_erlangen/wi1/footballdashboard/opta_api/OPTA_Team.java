package de.uni_erlangen.wi1.footballdashboard.helper;

import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Pass;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.custom_views.PlayerView;


public class PlayerTeam
{

    private final int teamId;
    private final boolean homeTeam;
    private final String teamName;

    private final SparseArray<OPTA_Player> playerSparseArray;
    private final List<OPTA_Player[]> playerRankings = new ArrayList<>(6 * 90); // Every 10 seconds

    private List<OPTA_Event> events;
    private PlayerView[] playerViews;
    private OPTA_Player[] rankedPlayers;


    public PlayerTeam(int teamId, String teamName, boolean homeTeam)
    {
        this.teamId = teamId;
        this.homeTeam = homeTeam;
        this.teamName = teamName;
        this.playerSparseArray = new SparseArray<>(22);
    }

    public void setViews(PlayerView[] playerViews)
    {
        this.playerViews = playerViews;
        mapToPlayerData();
        evaluateRanking();
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

    public OPTA_Player[] getRankedPlayers()
    {
        int index = StatusBar.getInstance().currTime / 10;
        if (playerRankings.size() >= index)
            return playerRankings.get(playerRankings.size() - 1);
        return playerRankings.get(index);
    }

    public void updateLightColors()
    {
        int i = 0; // Rank
        for (OPTA_Player player : getRankedPlayers()) {

            if (player.mappedView != null) {
                // Update playerImage overlay color
                player.mappedView.updateCircleLight();
                // Update playerImage border color
                if (i < 3) // Best 3
                    player.mappedView.setTop();
                else if (i > 7) // Worst 3
                    player.mappedView.setBad();
                else
                    player.mappedView.setAverage();

            }
            i++;
        }
    }

    public SparseArray getPassesFrom(OPTA_Player giver)
    {
        SparseArray ret = new SparseArray(rankedPlayers.length);
        int currTime = StatusBar.getInstance().currTime;

        boolean lastWasPass = false;
        for (OPTA_Event currEvent : events) {
            if (lastWasPass) {
                //TODO: Filter out data noise
                try {
                    int recevierID = playerSparseArray.get(currEvent.getPlayerId()).getId();
                    ret.put(recevierID, (int) ret.get(recevierID, 0) + 1);
                    lastWasPass = false;
                } catch (Exception e) {
                    Log.d("PLAYER_TEAM", "Coulnd't find anything for: " + currEvent.getPlayerId());
                }
            }

            if (currEvent.getCRTime() > currTime)
                break;
            if (currEvent instanceof Pass && currEvent.getPlayerId() == giver.getId())
                lastWasPass = true;
        }
        return ret;
    }

    public SparseArray getPassesFor(OPTA_Player receiver)
    {
        SparseArray ret = new SparseArray(rankedPlayers.length);
        int currTime = StatusBar.getInstance().currTime;

        OPTA_Event lastPass = null;
        for (OPTA_Event currEvent : events) {
            if (lastPass != null && currEvent.getPlayerId() == receiver.getId()) {
                int giverId = playerSparseArray.get(lastPass.getPlayerId()).getId();
                ret.put(giverId, (int) ret.get(giverId, 0) + 1);
                lastPass = null;
            }

            if (currEvent.getCRTime() > currTime)
                break;
            if (currEvent instanceof Pass)
                lastPass = currEvent;
        }

        return ret;
    }

    private void mapToPlayerData()
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

        // Map view to Player
        int i = 0;
        for (PlayerView playerView : playerViews) {
            playerView.setMappedPlayer(rankedPlayers[i]);
            rankedPlayers[i].mapView(playerView);
            i++;
        }
    }

    private void evaluateRanking()
    {
        // Fill a reference and index array for easy referencing and mapping
        OPTA_Player[] players = new OPTA_Player[playerSparseArray.size()];
        int[] lastEventIndex = new int[playerSparseArray.size()];
        // Second arrays to sort and pass new sorted position
        OPTA_Player[] sortPlayers = new OPTA_Player[playerSparseArray.size()];
        // Map values
        for (int i = 0; i < playerSparseArray.size(); i++) {
            players[i] = playerSparseArray.valueAt(i);
            sortPlayers[i] = playerSparseArray.valueAt(i);
        }

        // Is every player at the end
        boolean[] finished = new boolean[playerSparseArray.size()];
        Arrays.fill(finished, false);

        // Will be calculated every 10 Seconds
        int currTime = 10;

        do {
            /*
            Iterate over all players until all are on the same time level, then sort them
            within their ranking and save the ranking Position for every 10 Seconds
            */

            int i = 0;
            for (OPTA_Player currPlayer : players) {
                List<OPTA_Event> actions = currPlayer.getActions();
                // While we haven't finished all event's that in out time frame
                while (lastEventIndex[i] < actions.size()) {
                    OPTA_Event currEvent = currPlayer.getActions().get(lastEventIndex[i]);
                    if (currEvent.getCRTime() > currTime)
                        break;

                    currEvent.calcRankingPoint(currPlayer); // Calc player Ranking
                    lastEventIndex[i]++; // next Element
                }

                // Check if already finished all events
                if (lastEventIndex[i] >= actions.size())
                    finished[i] = true;

                i++; // Go to next player
            }

            // Sort the array and save the state
            Arrays.sort(sortPlayers);
            for (short sortedPos = 0; sortedPos < players.length; sortedPos++) {
                sortPlayers[sortedPos].playerRankings.add(sortedPos);
            }
            // Save sortedPlayers state in rankings
            playerRankings.add(sortPlayers.clone());

            currTime += 10;

        } while (!allFinished(finished));
    }

    private static boolean allFinished(boolean[] arr)
    { // Helper for evaluateRanking
        for (boolean b : arr)
            if (!b)
                return false;
        return true;
    }

    public String getPlayerName(int playerId)
    {
        OPTA_Player tmp = playerSparseArray.get(playerId);
        return (tmp != null) ? tmp.getName() : null;
    }

    public boolean isHomeTeam()
    {
        return homeTeam;
    }

    public SparseArray<OPTA_Player> getPlayers()
    {
        return playerSparseArray;
    }

    public void setEvents(List<OPTA_Event> events)
    {
        this.events = events;
    }

    public List<OPTA_Event> getEvents()
    {
        return events;
    }

    public String getTeamName()
    {
        return teamName;
    }

    public int getId()
    {
        return teamId;
    }

}
