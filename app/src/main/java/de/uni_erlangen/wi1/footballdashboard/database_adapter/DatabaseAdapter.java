package de.uni_erlangen.wi1.footballdashboard.database_adapter;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.live_list.LiveTeamListAdapter;

/**
 * Created by knukro on 6/16/17.
 */

public class DatabaseAdapter
{

    private static DatabaseAdapter instance;

    private final DbMetaHelper dbMeta;
    private DbGameHelper dbGame;

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


    void setGame(Context context, GameGovernor.GameGovernorData data, String gameID)
    {

        dbMeta.getMetaData(data, gameID); // Get all meta data
        dbGame = new DbGameHelper(context, gameID);  // Create DataBaseAdapter for the Game
        // Get player/lineup
        dbGame.getPlayerData(data, true);
        dbGame.getPlayerData(data, false);
        // Get Events
        dbGame.mapPlayerEvents(data.homeTeam.getPlayers(), true);
        dbGame.mapPlayerEvents(data.awayTeam.getPlayers(), false);
        // Game Data for teams
        data.homeTeam.setEvents(dbGame.getTeamEvents(true));
        data.awayTeam.setEvents(dbGame.getTeamEvents(false));
        // Get Names
        dbMeta.getPlayerNames(data.homeTeam.getPlayers());
        dbMeta.getPlayerNames(data.awayTeam.getPlayers());
    }

    public void updateLiveList(LiveTeamListAdapter listAdapter, int endTime)
    {
        // Just update new data
        List<OPTA_Event> newValues;
        if (StatusBar.getInstance().isShowingHome())
            newValues = dbGame.getNewHomeValues(endTime);
        else
            newValues = dbGame.getNewAwayValues(endTime);

        // Append in list, the other way around
        for (int i = newValues.size() - 1; i >= 0; i--)
            listAdapter.prePendEventInfo(newValues.get(i));
    }

    public List<OPTA_Event> getLiveListData(int teamId, int startTime, int endTime)
    {
        // Get a whole bunch of data
        List<OPTA_Event> wrongOrder;
        if (teamId == GameGovernor.getInstance().getHomeTeamId())
            wrongOrder = dbGame.getHomeValuesUntil(startTime, endTime);
        else
            wrongOrder = dbGame.getAwayValuesUntil(startTime, endTime);

        return reverseList(wrongOrder);
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
