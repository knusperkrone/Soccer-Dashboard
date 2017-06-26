package de.uni_erlangen.wi1.footballdashboard.database_adapter;

import android.content.Context;
import android.util.SparseArray;

import de.uni_erlangen.wi1.footballdashboard.PlayerTeam;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
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

    }

    static class GameGovernorData
    {
        int homeLayout, awayLayout;
        int firstHalfLength, secondHalfLength;
        int period = 1;
        int gameId;

        PlayerTeam homeTeam;
        PlayerTeam awayTeam;

        //final SparseArray<OPTA_Player> homePlayers = new SparseArray<>();
        //final SparseArray<OPTA_Player> awayPlayers = new SparseArray<>();
    }

    public static GameGovernor getInstance()
    {
        if (instance == null)
            instance = new GameGovernor();
        return instance;
    }

    public void setGame(Context context, String gameId)
    {
        //data.homePlayers.clear();
        //data.awayPlayers.clear();
        data.gameId = Integer.valueOf(gameId);

        DatabaseAdapter.getInstance().setGame(context, data, gameId);

    }

    boolean isHomeTeamId(int teamId)
    {
        return teamId == data.homeTeam.getId();
    }

    public SparseArray<OPTA_Player> getHomePlayers()
    {
        return data.homeTeam.getPlayers();
    }

    public SparseArray<OPTA_Player> getAwayPlayers()
    {
        return data.awayTeam.getPlayers();
    }

    public String getPlayerName(int playerID)
    {
        String tmp = data.homeTeam.getPlayerName(playerID);
        if (tmp == null)
            tmp = data.awayTeam.getPlayerName(playerID);
        return (tmp == null) ? "" : tmp;
    }

    public int getHalfTimeLength()
    {
        return 45;
        //TODO: return (period == 1) ? firstHalfLength : secondHalfLength;
    }

    public PlayerTeam getDisplayedTeam()
    {
        return (StatusBar.getInstance().isShowingHome()) ? data.homeTeam : data.awayTeam;
    }

    public PlayerTeam getHomeTeam()
    {
        return data.homeTeam;
    }

    public PlayerTeam getAwayTeam()
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
