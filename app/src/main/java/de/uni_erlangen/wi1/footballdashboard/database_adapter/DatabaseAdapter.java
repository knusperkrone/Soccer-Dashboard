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
import de.uni_erlangen.wi1.footballdashboard.ui_components.main_list.ILiveFilter;
import de.uni_erlangen.wi1.footballdashboard.ui_components.main_list.LiveActionListAdapter;

/**
 * Created by knukro on 6/16/17
 * .
 */

@SuppressWarnings("unchecked")
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

    public void updateLiveList(LiveActionListAdapter listAdapter, int endTime)
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

    public List<OPTA_Event> getLiveListData(int teamId, ILiveFilter filter, int startTime, int endTime)
    {
        // Return the whole list
        List<OPTA_Event> wrongOrder;
        if (teamId == GameGovernor.getInstance().getHomeTeamId())
            wrongOrder = currGame.getHomeValuesUntil(filter, startTime, endTime);
        else
            wrongOrder = currGame.getAwayValuesUntil(filter, startTime, endTime);

        // List is in the wrong order
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
        // Set teams, with the game Id and save it in gameGovernor
        getTeamsForGame(data, gameID);
        // Get init new DbGameHelper if necessary
        currGame = getCachedGames(context, gameID, data.homeTeam.getId());

        // Get data from dbMeta and currGame
        getTeamData(data.homeTeam, currGame);
        getTeamData(data.awayTeam, currGame);
    }

    private void getTeamData(OPTA_Team team, DbGameHelper dbGame)
    {
        // Lazy check if the data got already fetched
        if (team.getEvents() != null)
            return;

        dbGame.getPlayerData(team); // Get players and lineup
        dbMeta.getPlayerNames(team); // Get player names
        dbGame.getTeamEvents(team); // Game Events for teams - has to be last
    }

    public OPTA_Team getGameDataForGameTeam(Context context, String gameID, OPTA_Team origTeam)
    {
        // Is if  game already cached
        if (!cachedGameTeams.containsKey(gameID + true)
                || !cachedGameTeams.containsKey(gameID + false)) {

            // Get/Cache team meta-data for the game
            OPTA_Team[] oldTeams = dbMeta.getTeamsForGame(gameID);
            cachedGameTeams.put(gameID + true, oldTeams[0]); // Save as homeTeam
            cachedGameTeams.put(gameID + false, oldTeams[1]); // Save as awayTeam

            // Get/Cache team game-data for the game
            DbGameHelper gameData = getCachedGames(context, gameID, oldTeams[0].getId());
            getTeamData(oldTeams[0], gameData);
            getTeamData(oldTeams[1], gameData);
        }

        // Return the right team
        OPTA_Team homeTeam = cachedGameTeams.get(gameID + true);
        return (homeTeam.getId() == origTeam.getId()) ? homeTeam : cachedGameTeams.get(gameID + false);
    }

    public void getAllGames(List<String> gameNames, List<Integer> gameIds)
    {
        dbMeta.getAllGames(gameNames, gameIds);
    }

    private void getTeamsForGame(GameGovernor.GameGovernorData data, String gameID)
    {
        // Check if game is already cached
        if (!cachedGameTeams.containsKey(gameID + true)
                || !cachedGameTeams.containsKey(gameID + false)) {

            dbMeta.getTeamsForGame(data, gameID);
            cachedGameTeams.put(gameID + true, data.homeTeam);
            cachedGameTeams.put(gameID + false, data.awayTeam);
        }
    }

    private DbGameHelper getCachedGames(Context context, String gameID, int homeTeamId)
    {
        if (!cachedGames.containsKey(gameID)) // Lazy load gameData
            cachedGames.put(gameID, new DbGameHelper(context, gameID, homeTeamId));
        return cachedGames.get(gameID);
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
