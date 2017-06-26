package de.uni_erlangen.wi1.footballdashboard.database_adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.opta_api.API_QUALIFIER_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Qualifier;

/**
 * Created by knukro on 6/16/17.
 */

class DbGameHelper extends SQLiteOpenHelper
{

    private final String DB_PATH;
    private SQLiteDatabase mDatabase;

    private final List<TimeList> cachedEventsHomePeriods = new ArrayList<>(4);
    private final List<TimeList> cachedEventsAwayPeriods = new ArrayList<>(4);


    DbGameHelper(Context context, String name)
    {
        super(context, name, null, 1);

        DB_PATH = context.getDatabasePath(name).getPath();
        // Copy Database if necessary
        if (!new File(DB_PATH).exists()) {
            getReadableDatabase(); // Somehow necessary
            Log.d("[DB_META]", "Database does not exist yet");
            if (DatabaseAdapter.copyDatabase(context, DB_PATH, name))
                Toast.makeText(context, "[GAME] Database inited!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, "[GAME] Database initing failed!", Toast.LENGTH_SHORT).show();
        }
        // Cache all data!
        cacheData();
    }

    private void openDatabase()
    {
        if (mDatabase == null || !mDatabase.isOpen())
            mDatabase = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
    }

    private void closeDatabase()
    {
        if (mDatabase != null && mDatabase.isOpen())
            mDatabase.close();
    }

    private void cacheData()
    {
        openDatabase();
        int period = 1;
        do {
            List<OPTA_Event>[] periodData = getPeriod(period);
            if (periodData == null)
                break;

            cachedEventsHomePeriods.add(new TimeList(periodData[0]));
            cachedEventsAwayPeriods.add(new TimeList(periodData[1]));
            period++;
        } while (period < 5); // Cache data until penalty shootout
        closeDatabase();
    }

    private List<OPTA_Event>[] getPeriod(int period)
    {
        final String query = "SELECT id_event, typ_Id , outcome, min, sec,id_player,id_team, x_coord, y_cood, value, qualifier_id " +
                " FROM EventData " +
                " JOIN Qualifier " +
                " ON EventData.ID_Qualifier == Qualifier.ID " +
                " WHERE period == ? " +
                " ORDER BY min, sec";

        Cursor cursor = mDatabase.rawQuery(query, new String[]{"" + period});
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            cursor.close(); // Nothing to do here
            return null;
        }

        List<OPTA_Event> homeList = new ArrayList<>(cursor.getCount() / 2);
        List<OPTA_Event> awayList = new ArrayList<>(cursor.getCount() / 2);

        GameGovernor governor = GameGovernor.getInstance();

        while (!cursor.isAfterLast()) {
            // Parse SQL-Cursor
            int eventId = cursor.getInt(0);
            int teamId = cursor.getInt(6);
            OPTA_Event currEvent = OPTA_Event.newInstance(
                    cursor.getInt(1),
                    cursor.getInt(2) == 1,
                    period,
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getInt(6),
                    cursor.getDouble(7),
                    cursor.getDouble(8));
            do {
                // Get all qualifiers
                currEvent.qualifiers.add(OPTA_Qualifier.newInstance(cursor.getInt(10), cursor.getString(9)));
                cursor.moveToNext();
            } while (!cursor.isAfterLast() && cursor.getInt(0) == eventId);

            // Add to teamList
            if (governor.isHomeTeamId(teamId))
                homeList.add(currEvent);
            else
                awayList.add(currEvent);
        }
        cursor.close();
        return new List[]{homeList, awayList}; // Return tuple
    }

    void getPlayerData(GameGovernor.GameGovernorData data, boolean home)
    {
        final String query = "SELECT qualifier_id, value, ID_team " +
                " FROM EventData " +
                " JOIN Qualifier " +
                " ON EventData.ID_Qualifier = Qualifier.ID " +
                " WHERE ID_team == ? " +
                " AND Period == 16 " +
                " ORDER BY Qualifier_ID";

        // Choose the references form @data
        SparseArray<OPTA_Player> players = (home) ? data.homeTeam.getPlayers() : data.awayTeam.getPlayers();
        int teamId = (home) ? data.homeTeam.getId() : data.awayTeam.getId();

        openDatabase();
        Cursor cursor = mDatabase.rawQuery(query, new String[]{"" + teamId});
        cursor.moveToFirst();
        // PlayerIDs are luckily the first row!
        String[] playerIds = cursor.getString(1).split(", ");
        for (String s : playerIds) {
            int id = Integer.valueOf(s);
            players.put(id, new OPTA_Player(id));
        }

        // Parse Pre-Game Query
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            switch (cursor.getInt(0)) {
                case API_QUALIFIER_IDS.PLAYER_POSITION: // Goalkeeper, Defender, etc.
                    String[] positions = cursor.getString(1).split(", ");
                    for (int i = 0; i < positions.length; i++)
                        players.get(Integer.valueOf(playerIds[i])).setPosition(positions[i]);
                    break;
                case API_QUALIFIER_IDS.TEAM_FORMATION:
                    if (home)
                        data.homeLayout = Integer.valueOf(cursor.getString(1));
                    else
                        data.awayLayout = Integer.valueOf(cursor.getString(1));
                    break;
                case API_QUALIFIER_IDS.JERSEY_NUMBER: // ShirtNumber
                    String[] shirtNumbers = cursor.getString(1).split(", ");
                    for (int i = 0; i < shirtNumbers.length; i++)
                        players.get(Integer.valueOf(playerIds[i])).setShirtNumber(shirtNumbers[i]);
                    break;
                case API_QUALIFIER_IDS.TEAM_PLAYER_FORMATION: // player position in formation
                    String[] layoutPos = cursor.getString(1).split(", ");
                    for (int i = 0; i < layoutPos.length; i++)
                        players.get(Integer.valueOf(playerIds[i])).setLayoutPosition(layoutPos[i]);
                    break;
                case API_QUALIFIER_IDS.CAPTAIN:
                    players.get(Integer.valueOf(cursor.getString(1))).setCaptain(true);
                    break;

                case API_QUALIFIER_IDS.INVOLVED: // Already done!
                case API_QUALIFIER_IDS.TEAM_KIT: // Not sure what that even is
                case API_QUALIFIER_IDS.RESUME:
            }
        }
        cursor.close();
        closeDatabase();
    }


    void getPlayerEvents(SparseArray<OPTA_Player> players, boolean homeTeam)
    {
        List<TimeList> events = (homeTeam) ? cachedEventsHomePeriods : cachedEventsAwayPeriods;
        // Adds every event to a player - not as time consuming btw
        for (TimeList list : events) {
            for (OPTA_Event info : list.dataList) {
                OPTA_Player OPTAPlayer = players.get(info.playerId);
                if (OPTAPlayer != null)
                    OPTAPlayer.actions.add(info);
            }
        }
    }


    List<OPTA_Event> getNewHomeValues(int period, int time)
    {
        return cachedEventsHomePeriods.get(period).updateValues(time);
    }

    List<OPTA_Event> getNewAwayValues(int period, int time)
    {
        return cachedEventsAwayPeriods.get(period).updateValues(time);
    }


    List<OPTA_Event> getHomeValuesUntil(int period, int time)
    {
        return cachedEventsHomePeriods.get(period).getValuesUntil(time);
    }

    List<OPTA_Event> getAwayValuesUntil(int period, int time)
    {
        return cachedEventsAwayPeriods.get(period).getValuesUntil(time);
    }


    // Unecessary Stuff
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}
