package de.uni_erlangen.wi1.footballdashboard.database_adapter;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.list_live_event.LiveEventListAdapter;

/**
 * Created by knukro on 6/16/17.
 */

public class DatabaseAdapter
{

    private static DatabaseAdapter instance;

    private final GameGovernor governor;
    private final DbMetaHelper dbMeta;
    private DbGameHelper dbGame;

    private DatabaseAdapter(Context context)
    {
        this.dbMeta = new DbMetaHelper(context);
        governor = GameGovernor.getInstance();
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
        // Get all meta data
        dbMeta.getMetaData(data, gameID);
        dbGame = new DbGameHelper(context, gameID);
        // Fill data for home team
        dbGame.getPlayerData(data, true);
        dbGame.getPlayerEvents(data.homeTeam.getPlayers(), true);
        dbMeta.getPlayerNames(data.homeTeam.getPlayers());
        // Fill data for away team
        dbGame.getPlayerData(data, false);
        dbGame.getPlayerEvents(data.awayTeam.getPlayers(), false);
        dbMeta.getPlayerNames(data.awayTeam.getPlayers());
    }

    public void updateLiveList(LiveEventListAdapter listAdapter, int time)
    {
        StatusBar statusBar = StatusBar.getInstance();
        List<OPTA_Event> newValues;
        int period = governor.data.period - 1;
        if (statusBar.isShowingHome())
            newValues = dbGame.getNewHomeValues(period, time);
        else
            newValues = dbGame.getNewAwayValues(period, time);

        // Append in list
        for (int i = newValues.size() - 1; i >= 0; i--)
            listAdapter.prePendEventInfo(newValues.get(i));
    }

    public List<OPTA_Event> fillLiveList(int time, int teamId)
    {
        List<OPTA_Event> wrongOrder;
        if (governor.isHomeTeamId(teamId))
            wrongOrder = dbGame.getHomeValuesUntil(governor.data.period - 1, time);
        else
            wrongOrder = dbGame.getAwayValuesUntil(governor.data.period - 1, time);

        return reverseList(wrongOrder);
    }

    // helper
    private static <E> List reverseList(List<E> list)
    {
        List outList = new ArrayList(list.size());
        for (int i = list.size() - 1; i >= 0; i--)
            outList.add(list.get(i));
        return outList;
    }

    // HeatMapHelper method for SQLiteOpenHelper
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
