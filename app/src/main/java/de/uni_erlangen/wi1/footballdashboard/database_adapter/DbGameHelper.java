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
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Pass;
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

    private TimeList cachedHomeEvents;
    private TimeList cachedAwayEvents;

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

        // make a query and save return tuple
        List<OPTA_Event>[] periodData = cacheQuery();
        cachedHomeEvents = new TimeList(periodData[0]);
        cachedAwayEvents = new TimeList(periodData[1]);

        closeDatabase();
    }

    private List<OPTA_Event>[] cacheQuery()
    {
        final String query = "SELECT id_event, typ_Id, outcome, min, sec, id_player, id_team, x_coord, y_cood, value, qualifier_id, period " +
                " FROM EventData " +
                " JOIN Qualifier " +
                " ON EventData.ID_Qualifier == Qualifier.ID " +
                " WHERE period < 6 AND period > 0 " +
                " ORDER BY min, sec";

        Cursor cursor = mDatabase.rawQuery(query, new String[]{ });
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            cursor.close(); // Nothing to do here
            return null;
        }

        // Allocate 2 Lists, with approximately enough memory
        List<OPTA_Event> homeList = new ArrayList<>(cursor.getCount() / 2);
        List<OPTA_Event> awayList = new ArrayList<>(cursor.getCount() / 2);

        // Values for easy filtering
        int homeId = GameGovernor.getInstance().getHomeTeamId();
        OPTA_Event oldHomeEvent = null;
        OPTA_Event oldAwayEvent = null;

        // Parse SQL-Cursor
        while (!cursor.isAfterLast()) {

            // Get attributes, to classify event
            int eventId = cursor.getInt(0);
            int teamId = cursor.getInt(6);

            // Create Event
            OPTA_Event currEvent = OPTA_Event.newInstance(
                    cursor.getInt(1), // typeID
                    cursor.getInt(2) == 1, // Outcome
                    cursor.getInt(11), // Period
                    cursor.getInt(3), // Min
                    cursor.getInt(4), // Sec
                    cursor.getInt(5), // PlayerId
                    teamId, // TeamId
                    cursor.getDouble(7), // X-Coord
                    cursor.getDouble(8)); // Y-Coord

            // Parse all Qualifiers - there is at least 1
            do {
                currEvent.qualifiers.add(OPTA_Qualifier.newInstance(
                        cursor.getInt(10),
                        cursor.getString(9)));

                // Automatically moves to next event here, when condition fails
                cursor.moveToNext();
            } while (!cursor.isAfterLast() && cursor.getInt(0) == eventId);


            if (homeId == teamId) { // HomeTeam

                // Check if the oldEvent, equals the currEvent to filter duplicates
                if (oldHomeEvent == null || currEvent instanceof Pass
                        || (oldHomeEvent.getID() != currEvent.getID()
                        && oldHomeEvent.getPlayerId() != currEvent.getPlayerId())) {

                    // Add to list and set new oldEvent
                    homeList.add(currEvent);
                    oldHomeEvent = currEvent;
                }
            } else { // AwayTeam

                // Check if the oldEvent, equals the currEvent to filter duplicates
                if (oldAwayEvent == null || currEvent instanceof Pass
                        || (oldAwayEvent.getID() != currEvent.getID()
                        && oldAwayEvent.getPlayerId() != currEvent.getPlayerId())) {

                    // Add to list and set new oldEvent
                    awayList.add(currEvent);
                    oldAwayEvent = currEvent;
                }
            }
        }

        cursor.close();
        return new List[]{homeList, awayList}; // Return list tuple
    }


    void getPlayerData(GameGovernor.GameGovernorData data, boolean home)
    {
        // Only Parse data for period 16 -> Pregame
        final String query = "SELECT qualifier_id, value, ID_team " +
                " FROM EventData " +
                " JOIN Qualifier " +
                " ON EventData.ID_Qualifier = Qualifier.ID " +
                " WHERE ID_team == ? " +
                " AND Period == 16 " +
                " ORDER BY Qualifier_ID";

        // Choose the right team references form @data
        SparseArray<OPTA_Player> players = (home) ? data.homeTeam.getPlayers() : data.awayTeam.getPlayers();
        int teamId = (home) ? data.homeTeam.getId() : data.awayTeam.getId();

        // Make the query
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(query, new String[]{"" + teamId});
        cursor.moveToFirst();

        // PlayerIDs are in the first entry!
        String[] playerIds = cursor.getString(1).split(", ");
        for (String s : playerIds) {
            // Parse the string and create new players for every parsed ID
            int id = Integer.valueOf(s);
            players.put(id, new OPTA_Player(id));
        }

        // Parse Pre-Game Query
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            switch (cursor.getInt(0)) {

                case API_QUALIFIER_IDS.PLAYER_POSITION:
                    // Parse for Goalkeeper, Defender, Striker for every Player
                    String[] positions = cursor.getString(1).split(", ");
                    for (int i = 0; i < positions.length; i++)
                        players.get(Integer.valueOf(playerIds[i])).setPosition(positions[i]);
                    break;

                case API_QUALIFIER_IDS.TEAM_FORMATION:
                    // Get the team formation/layout value
                    if (home)
                        data.homeLayout = Integer.valueOf(cursor.getString(1));
                    else
                        data.awayLayout = Integer.valueOf(cursor.getString(1));
                    break;

                case API_QUALIFIER_IDS.JERSEY_NUMBER:
                    // Parse shirtNumber for every Player
                    String[] shirtNumbers = cursor.getString(1).split(", ");
                    for (int i = 0; i < shirtNumbers.length; i++)
                        players.get(Integer.valueOf(playerIds[i])).setShirtNumber(shirtNumbers[i]);
                    break;

                case API_QUALIFIER_IDS.TEAM_PLAYER_FORMATION:
                    // Parse player position in formation for every Player
                    String[] layoutPos = cursor.getString(1).split(", ");
                    for (int i = 0; i < layoutPos.length; i++)
                        players.get(Integer.valueOf(playerIds[i])).setLayoutPosition(layoutPos[i]);
                    break;

                case API_QUALIFIER_IDS.CAPTAIN:
                    // Set Captain
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

    List<OPTA_Event> getTeamEvents(boolean homeTeam)
    {
        // Return the list for the right team and period
        return (homeTeam) ? cachedHomeEvents.dataList : cachedAwayEvents.dataList;
    }

    void mapPlayerEvents(SparseArray<OPTA_Player> players, boolean homeTeam)
    {
        // Maps every event to the right player
        // Choose right list
        TimeList list = (homeTeam) ? cachedHomeEvents : cachedAwayEvents;
        for (OPTA_Event info : list.dataList) {
            OPTA_Player OPTAPlayer = players.get(info.playerId);
            if (OPTAPlayer != null)
                OPTAPlayer.actions.add(info);
        }
    }

    List<OPTA_Event> getNewHomeValues(int endTime)
    {
        return cachedHomeEvents.updateValues(endTime);
    }

    List<OPTA_Event> getNewAwayValues(int endTime)
    {
        return cachedAwayEvents.updateValues(endTime);
    }

    List<OPTA_Event> getHomeValuesUntil(int startTime, int endTime)
    {
        return cachedHomeEvents.getValuesRange(startTime, endTime);
    }

    List<OPTA_Event> getAwayValuesUntil(int startTime, int endTime)
    {
        return cachedAwayEvents.getValuesRange(startTime, endTime);
    }

    // Unnecessary Stuff
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}
