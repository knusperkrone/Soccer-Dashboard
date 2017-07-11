package de.uni_erlangen.wi1.footballdashboard.database_adapter;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.live_list.LiveTeamListAdapter;

/**
 * Created by knukro on 6/16/17.
 */

public class DatabaseAdapter
{

    private static DatabaseAdapter instance;

    private final HashMap<String, DbGameHelper> cachedGames = new HashMap<>(); // gameID
    private final HashMap<String, OPTA_Team> cachedGameTeams = new HashMap<>(); // gameID + isHomeTeam
    private final HashMap<String, List<GameInfo>> cachedTeamGames = new HashMap<>(); // teamName

    private final DbMetaHelper dbMeta;
    private DbGameHelper currGame;


    private DatabaseAdapter(Context context)
    {
        this.dbMeta = new DbMetaHelper(context); // DatabaseAdapter for the META database
    }

    public static void initInstance(Context context)
    {
        if (instance == null)
            instance = new DatabaseAdapter(context);
    }

    public static DatabaseAdapter getInstance()
    {
        return instance;
    }

    public void updateLiveList(LiveTeamListAdapter listAdapter, int endTime)
    {
        // Just update new data
        List<OPTA_Event> newValues;
        if (StatusBar.getInstance().isShowingHome())
            newValues = currGame.getNewHomeValues(endTime);
        else
            newValues = currGame.getNewAwayValues(endTime);

        // Append in list, the other way around
        for (int i = newValues.size() - 1; i >= 0; i--)
            listAdapter.prePendEventInfo(newValues.get(i));
    }

    public List<OPTA_Event> getLiveListData(int teamId, int startTime, int endTime)
    {
        // Get a whole bunch of data
        List<OPTA_Event> wrongOrder;
        if (teamId == GameGovernor.getInstance().getHomeTeamId())
            wrongOrder = currGame.getHomeValuesUntil(startTime, endTime);
        else
            wrongOrder = currGame.getAwayValuesUntil(startTime, endTime);

        return reverseList(wrongOrder);
    }

    public List<GameInfo> getGamesForTeam(OPTA_Team team)
    {
        if (!cachedTeamGames.containsKey(team.getTeamName())) {
            List<GameInfo> tmpList = dbMeta.getAllGamesForTeam(team.getTeamName(), currGame.gameID);
            cachedTeamGames.put(team.getTeamName(), tmpList);
        }
        return cachedTeamGames.get(team.getTeamName());
    }

    void setGame(Context context, GameGovernor.GameGovernorData data, String gameID)
    {
        getTeamsForGame(data, gameID);
        currGame = getCachedGames(context, gameID, data.homeTeam.getId());

        getTeamData(data.homeTeam, currGame);
        getTeamData(data.awayTeam, currGame);
    }

    private void getTeamData(OPTA_Team team, DbGameHelper dbGame)
    {
        if (team.dataFetched)
            return;

        dbGame.getPlayerData(team); // Get player/lineup
        dbGame.mapPlayerEvents(team); // Get Events
        team.setEvents(dbGame.getTeamEvents(team.isHomeTeam())); // Game Data for teams
        dbMeta.getPlayerNames(team); // Get Names
        team.dataFetched = true;
    }

    public OPTA_Team getGameDataFor(Context context, String gameID, OPTA_Team origTeam)
    {
        if (!cachedGameTeams.containsKey(gameID + true) // Is game already cached?
                || !cachedGameTeams.containsKey(gameID + false)) {

            // Get/Cache team meta-data for the game
            OPTA_Team[] oldTeams = dbMeta.getTeam(gameID);
            cachedGameTeams.put(gameID + true, oldTeams[0]);
            cachedGameTeams.put(gameID + false, oldTeams[1]);
            // Get/Cache team game-data for the game
            DbGameHelper gameData = getCachedGames(context, gameID, oldTeams[0].getId());
            getTeamData(oldTeams[0], gameData);
            getTeamData(oldTeams[1], gameData);
        }

        // Return the right team
        OPTA_Team homeTeam = cachedGameTeams.get(gameID + true);
        if (homeTeam.getTeamName().equals(origTeam.getTeamName()))
            return homeTeam;
        return cachedGameTeams.get(gameID + false);
    }

    private DbGameHelper getCachedGames(Context context, String gameID, int homeTeamId)
    {
        if (!cachedGames.containsKey(gameID)) // Lazy load gameData
            cachedGames.put(gameID, new DbGameHelper(context, gameID, homeTeamId));
        return cachedGames.get(gameID);
    }

    private void getTeamsForGame(GameGovernor.GameGovernorData data, String gameID)
    {
        if (!cachedGameTeams.containsKey(gameID + true)
                || !cachedGameTeams.containsKey(gameID + false)) {

            dbMeta.getTeam(data, gameID);
            cachedGameTeams.put(gameID + true, data.homeTeam);
            cachedGameTeams.put(gameID + false, data.awayTeam);
        }
    }

    // Helper
    private static <E> List reverseList(List<E> list)
    {
        List outList = new ArrayList(list.size());
        for (int i = list.size() - 1; i >= 0; i--)
            outList.add(list.get(i));
        return outList;
    }

    // Android API-Helper method
    static boolean copyDatabase(Context context, String DB_PATH, String DB_NAME)
    {
        try {
            InputStream inputStream = context.getAssets().open(DB_NAME);
            OutputStream outputStream = new FileOutputStream(DB_PATH);
            byte[] buff = new byte[1024];
            int length;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
