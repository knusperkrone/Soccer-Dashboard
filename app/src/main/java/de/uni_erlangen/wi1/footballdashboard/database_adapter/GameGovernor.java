package de.uni_erlangen.wi1.footballdashboard.database_adapter;

import android.content.Context;

import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;

/**
 * Created by knukro on 6/15/17.
 */

public class GameGovernor
{

    private static GameGovernor instance;
    public final GameGovernorData data = new GameGovernorData();

    private GameGovernor()
    {
        // Empty constructor
    }

    static class GameGovernorData
    {
        // Just a struct for referencing
        int homeLayout, awayLayout;
        int firstHalfLength, secondHalfLength;
        int period = 1;
        int gameId;

        OPTA_Team homeTeam;
        OPTA_Team awayTeam;
    }

    public static GameGovernor getInstance()
    {
        if (instance == null)
            instance = new GameGovernor();
        return instance;
    }

    public void setGame(Context context, String gameId)
    {
        data.gameId = Integer.valueOf(gameId);
        DatabaseAdapter.getInstance().setGame(context, data, gameId);
    }

    public String getPlayerName(int playerID, int teamID)
    {
        // Check the right team and return value if it's there
        String tmp = (teamID == data.homeTeam.getId()) ?
                data.homeTeam.getPlayerName(playerID) :
                data.awayTeam.getPlayerName(playerID);
        return (tmp == null) ? "" : tmp;
    }

    public OPTA_Team getDisplayedTeam()
    {
        return (StatusBar.getInstance().isShowingHome()) ? data.homeTeam : data.awayTeam;
    }

    public int getLatestEventTime()
    {
        List<OPTA_Event> home = getHomeTeam().getEvents();
        return home.get(home.size() - 1).getCRTime();
    }

    boolean isHomeTeamId(int teamId)
    {
        return teamId == data.homeTeam.getId();
    }

    public OPTA_Team getHomeTeam()
    {
        return data.homeTeam;
    }

    public OPTA_Team getAwayTeam()
    {
        return data.awayTeam;
    }

    public String getHomeTeamName()
    {
        return data.homeTeam.getTeamName();
    }

    public String getAwayTeamName()
    {
        return data.awayTeam.getTeamName();
    }

    public int getHomeTeamId()
    {
        return data.homeTeam.getId();
    }

    public int getAwayTeamId()
    {
        return data.awayTeam.getId();
    }

    public int getHomeLayout()
    {
        return data.homeLayout;
    }

    public int getAwayLayout()
    {
        return data.awayLayout;
    }


}
