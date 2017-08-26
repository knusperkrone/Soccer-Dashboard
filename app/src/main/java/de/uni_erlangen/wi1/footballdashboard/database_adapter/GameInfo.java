package de.uni_erlangen.wi1.footballdashboard.database_adapter;

/**
 * Created by knukro on 7/11/17.
 */

public class GameInfo
{

    private final String homeTeam;
    private final String awayTeam;
    private final int gameId;

    GameInfo(int gameId, String homeTeam, String awayTeam)
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
