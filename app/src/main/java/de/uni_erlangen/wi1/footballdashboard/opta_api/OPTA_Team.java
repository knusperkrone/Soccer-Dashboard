package de.uni_erlangen.wi1.footballdashboard.opta_api;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.helper.EventParser;
import de.uni_erlangen.wi1.footballdashboard.helper.StatisticHelper;
import de.uni_erlangen.wi1.footballdashboard.helper.TeamFormationChange;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.custom_views.PlayerView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.fragments.FormationFragment;


public class OPTA_Team
{

    private final int teamId;
    private final boolean homeTeam;
    private final String teamName;

    private final List<OPTA_Player[]> playerRankings = new ArrayList<>(6 * 90); // Every 10 seconds
    private final List<OPTA_Event> uiEvents = new ArrayList<>();

    private SparseArray<OPTA_Player> playerSparseArray;
    private List<OPTA_Event> events;

    private OPTA_Player[] mappedPlayers;
    private PlayerView[] playerViews;
    private FormationFragment parentFragment;

    private boolean reset = false;

    private TeamFormationChange origFormation;
    private TeamFormationChange currFormation;
    private int teamGoals;
    private int layoutId;

    private int lastIndex = 0;


    public OPTA_Team(int teamId, String teamName, boolean homeTeam)
    {
        this.teamId = teamId;
        this.teamName = teamName;
        this.homeTeam = homeTeam;
    }

    public void setViews(@NonNull PlayerView[] playerViews, FormationFragment parentFragment)
    {
        this.playerViews = playerViews;
        this.parentFragment = parentFragment;
        mapViewToPlayers();
    }

    public void setEvents(@NonNull List<OPTA_Event> events)
    {
        this.events = events;
        mapPlayerEvents();
        filterUIEvents();
        evaluateRanking();
    }

    public boolean OnClicked(PlayerView selectedView)
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

    public OPTA_Player[] getRankedPlayers(int timeInSeconds)
    {
        // Returns the ordered array for the time @timeInSeconds
        int index = timeInSeconds / 10;
        if (playerRankings.size() <= index)
            return playerRankings.get(playerRankings.size() - 1);
        return playerRankings.get(index);
    }

    public SparseArray getPassesBy(OPTA_Player giver, int minTime, int maxTime)
    {
        return StatisticHelper.getPassesBy(new SparseArray(mappedPlayers.length), events, playerSparseArray, giver, minTime, maxTime);
    }

    public SparseArray getPassesFor(OPTA_Player receiver, int minTime, int maxTime)
    {
        return StatisticHelper.getPassesFor(new SparseArray(mappedPlayers.length), events, playerSparseArray, receiver, minTime, maxTime);
    }

    public void resetAndRefreshUI(int maxTime)
    {
        // Only reset when going backwards
        int newIndex = 0;
        for (OPTA_Event event : uiEvents) {
            if (event.getCRTime() > maxTime)
                break;
            newIndex++;
        }
        if (lastIndex > newIndex) {
            resetState();
        }

        refreshUI(maxTime);
    }

    public void refreshUI(int maxTime)
    {
        int goalCounter = -1;
        TeamFormationChange newFormation = null;

        int i = lastIndex;
        for (; i < uiEvents.size(); i++) {
            OPTA_Event event = uiEvents.get(i);

            if (event.getCRTime() > maxTime)
                break;

            switch (event.getID()) {

                case API_TYPE_IDS.CARD:
                    for (OPTA_Qualifier qualifier : event.qualifiers) {
                        if (qualifier.getId() == API_QUALIFIER_IDS.YELLOW_CARD) {
                            setYellowCard(event.getPlayerId());
                        } else if (qualifier.getId() == API_QUALIFIER_IDS.YELLOW_CARD) {
                            setRedCard(event.getPlayerId());
                        }
                    }

                case API_TYPE_IDS.GOAL:
                    if (goalCounter == -1)
                        goalCounter = 0;
                    goalCounter++;
                    break;

                case API_TYPE_IDS.FORMATION_CHANGE:
                    newFormation = EventParser.parseFormationChange(event, playerSparseArray.size());
                    break;
            }
        }
        lastIndex = i;

        if (reset) {
            reset = false;

            if (newFormation == null && !origFormation.equals(currFormation))
                newFormation = origFormation;

            if (goalCounter == -1)
                goalCounter = 0;
        }

        if (goalCounter != -1) {
            teamGoals += goalCounter;
            StatusBar.getInstance().setGoals(teamGoals, isHomeTeam());
        }

        if (newFormation != null) {
            currFormation = newFormation;
            teamFormationChanged(newFormation, playerSparseArray);
        }

        // Set circle (border) colors
        updateLightColors(maxTime);
    }


    private void resetState()
    {
        reset = true;
        lastIndex = teamGoals = 0;

        for (OPTA_Player player : mappedPlayers)
            player.removeAllCards();
    }

    private void updateLightColors(int timeInSeconds)
    {
        // Updates the views to their cached ranking @timeInSeconds
        OPTA_Player[] sortedPlayers = getRankedPlayers(timeInSeconds);
        int i = 0; // Rank counter
        for (OPTA_Player player : sortedPlayers) {

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

    private void teamFormationChanged(TeamFormationChange changeData, SparseArray<OPTA_Player> playerSparseArray)
    {
        // Iterate over the dataSet and change available values
        for (int i = 0; i < changeData.size(); i++) {
            OPTA_Player currPlayer = playerSparseArray.get(changeData.getPlayerId(i));
            // Set new values
            if (currPlayer != null) {
                if (changeData.hasPosition())
                    currPlayer.setPosition(changeData.getPosition(i));

                if (changeData.hasJerseyNumbers())
                    currPlayer.setShirtNumber(changeData.getJerseyNumber(i));

                if (changeData.hasLayoutPosition())
                    currPlayer.setLayoutPosition(changeData.getLayoutPosition(i));

                if (changeData.hasCaptain())
                    currPlayer.setCaptain(currPlayer.getId() == changeData.getCaptainId());
            }
        }
        if (changeData.hasLayoutId())
            this.layoutId = changeData.getLayoutId();

        // Only if this is an active game
        if (parentFragment != null) {
            parentFragment.changeFormation(layoutId);
            mapViewToPlayers();
        }
    }


    /**
     * Sorts the mappedPlayers array and save the original formation
     */
    private void mapViewToPlayers()
    {
        // Fill players Array
        mappedPlayers = new OPTA_Player[playerSparseArray.size()];
        for (int i = 0; i < playerSparseArray.size(); i++)
            mappedPlayers[i] = playerSparseArray.valueAt(i);

        // Sort new array with layoutPosition
        Arrays.sort(mappedPlayers, new Comparator<OPTA_Player>()
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

        // Save a copy of the original formation
        if (origFormation == null) {
            saveOrigFormation(mappedPlayers);
        }

        // Maps the player data to the view references
        int i = 0;
        // Iterate over the playerViews
        for (PlayerView playerView : playerViews) {

            // Set the playerView to Data and the data to playerView
            playerView.setMappedPlayer(mappedPlayers[i]);
            mappedPlayers[i].mapView(playerView);

            i++;
        }
    }

    /**
     * Maps the team events to the players
     */
    private void mapPlayerEvents()
    {
        for (OPTA_Event event : events) {
            OPTA_Player player = this.playerSparseArray.get(event.getPlayerId());
            if (player != null) {
                player.actions.add(event);
            }
        }
    }


    /***
     * Filters out the relevant elements, for UI actions
     */
    private void filterUIEvents()
    {
        for (OPTA_Event event : events) {

            switch (event.getID()) {
                case API_TYPE_IDS.GOAL:
                case API_TYPE_IDS.CARD:
                case API_TYPE_IDS.FORMATION_CHANGE:
                    uiEvents.add(event);
            }

        }
    }

    /**
     * Iterate over all players until all are on the same time level, then sort them
     * within their ranking and save the ranking Position for every 10 Seconds
     */
    private void evaluateRanking()
    {
        SparseArray<OPTA_Player> evaluationArray = new SparseArray<>(playerSparseArray.size());
        // Array for easy iterating over players
        OPTA_Player[] players = new OPTA_Player[playerSparseArray.size()];
        // Array for sorting position only
        OPTA_Player[] sortedPlayers = new OPTA_Player[playerSparseArray.size()];

        // Clone all players, to don't change their state
        for (int i = 0; i < playerSparseArray.size(); i++) {
            OPTA_Player clonedPlayer = playerSparseArray.valueAt(i).pseudoClone();
            sortedPlayers[i] = players[i] = clonedPlayer;
            evaluationArray.append(clonedPlayer.getId(), clonedPlayer);
        }

        // Save the last event we visited for each player
        int[] lastEventIndex = new int[playerSparseArray.size()];
        Arrays.fill(lastEventIndex, 0); // Unnecessary in Java
        // Is every player finished
        boolean[] finished = new boolean[playerSparseArray.size()];
        Arrays.fill(finished, false); // Unnecessary in Java
        int parsedEventIndex = 0;

        // Time variables
        final int TIME_FRAME = 10;
        int currTime = TIME_FRAME;

        do {

            // Check for layout changes
            for (; parsedEventIndex < uiEvents.size(); parsedEventIndex++) {
                OPTA_Event event = uiEvents.get(parsedEventIndex);
                if (event.getCRTime() > currTime) {
                    break;
                }

                if (event.getID() == API_TYPE_IDS.FORMATION_CHANGE) {
                    // Change the states of all players
                    teamFormationChanged(EventParser.parseFormationChange(event, playerSparseArray.size()),
                            evaluationArray);
                }
            }

            // Start iteration over players
            int playerIndex = 0;
            for (OPTA_Player currPlayer : players) {
                // Player already reached end of his events
                if (finished[playerIndex]) {
                    playerIndex++;
                    continue;
                }

                List<OPTA_Event> actions = currPlayer.getActions();
                // Evaluate event's that are inside the time frame
                while (lastEventIndex[playerIndex] < actions.size()) {
                    OPTA_Event currEvent = currPlayer.getActions().get(lastEventIndex[playerIndex]);
                    if (currEvent.getCRTime() > currTime)
                        break;

                    currEvent.calcRankingPoint(currPlayer); // Calc player Ranking
                    lastEventIndex[playerIndex]++; // next Element
                }

                // Check if already finished all events
                if (lastEventIndex[playerIndex] >= actions.size())
                    finished[playerIndex] = true;

                playerIndex++; // Go to next player
            }

            // Sort the array and save the state
            Arrays.sort(sortedPlayers);
            for (short sortedPos = 0; sortedPos < players.length; sortedPos++) {
                sortedPlayers[sortedPos].playerRankings.add(sortedPos); // Save position on player
            }
            // Save sortedPlayers state in rankings
            playerRankings.add(sortedPlayers.clone());

            currTime += TIME_FRAME;

        } while (!allFinished(finished));
    }

    private void saveOrigFormation(OPTA_Player[] startFormation)
    {
        origFormation = new TeamFormationChange(mappedPlayers.length);
        origFormation.setLayoutId(this.layoutId);

        int i = 0;
        for (OPTA_Player player : startFormation) {
            origFormation.setPlayerId(i, player.getId());
            origFormation.setJerseyNumber(i, player.getShirtNumber());
            origFormation.setPlayerLayoutPosition(i, player.getLayoutPosition());
            origFormation.setPlayerPosition(i, player.getPosition().ordinal());
            if (player.isCaptain()) {
                origFormation.setCaptainId(player.getId());
            }

            i++;
        }
    }

    // Helper for evaluateRanking
    private static boolean allFinished(boolean[] arr)
    {
        for (boolean b : arr)
            if (!b)
                return false;
        return true;
    }

    private void setYellowCard(int playerId)
    {
        playerSparseArray.get(playerId).setCardYellow();
    }

    private void setRedCard(int playerId)
    {
        playerSparseArray.get(playerId).setCardRed();
    }


    public String getPlayerName(int playerId)
    {
        OPTA_Player tmp = playerSparseArray.get(playerId);
        return (tmp != null) ? tmp.getName() : null;
    }

    public void setPlayers(SparseArray<OPTA_Player> playerSparseArray)
    {
        this.playerSparseArray = playerSparseArray;
    }

    public boolean isHomeTeam()
    {
        return homeTeam;
    }

    public void setUnclicked(PlayerView selectedView)
    {
        selectedView.setDefaultMode();
    }

    public List<OPTA_Event> getEvents()
    {
        return events;
    }

    public String getTeamName()
    {
        return teamName;
    }

    public SparseArray<OPTA_Player> getPlayers()
    {
        return this.playerSparseArray;
    }

    public int getId()
    {
        return teamId;
    }

    public void setLayoutId(int layoutId)
    {
        this.layoutId = layoutId;
    }

    public int getLayoutId()
    {
        return this.layoutId;
    }

}
