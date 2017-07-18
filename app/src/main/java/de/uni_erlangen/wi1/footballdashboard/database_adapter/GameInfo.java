package de.uni_erlangen.wi1.footballdashboard.database_adapter;

/**
 * Created by knukro on 7/11/17.
 */

public class GameInfo
{

    final String homeTeam;
    final String awayTeam;
    final int gameId;

    public GameInfo(int gameId, String homeTeam, String awayTeam)
    {
        this.gameId = gameId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public String getID()
    {
        return gameId + "";
    }

    @Override
    public String toString()
    {
        return homeTeam + " vs " + awayTeam;
    }

}
