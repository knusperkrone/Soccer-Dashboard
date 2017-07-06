package de.uni_erlangen.wi1.footballdashboard.database_adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.SparseArray;
import android.widget.Toast;

import java.io.File;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;

/**
 * Created by knukro on 6/16/17.
 */

class DbMetaHelper extends SQLiteOpenHelper
{

    private static final String DB_NAME = "META";
    private final String DB_PATH;
    private SQLiteDatabase mDatabase;

    DbMetaHelper(Context context)
    {
        super(context, DB_NAME, null, 1);

        DB_PATH = context.getDatabasePath(DB_NAME).getPath();

        // Is the database in main-Memory?
        if (!new File(DB_PATH).exists()) {
            getReadableDatabase(); // Somehow necessary
            if (DatabaseAdapter.copyDatabase(context, DB_PATH, DB_NAME))
                Toast.makeText(context, "[META] Database inited!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, "[META] Database initing failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void openDatabase()
    {
        if (mDatabase == null || !mDatabase.isOpen())
            mDatabase = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

    private void closeDatabase()
    {
        if (mDatabase != null && mDatabase.isOpen())
            mDatabase.close();
    }


    void getMetaData(GameGovernor.GameGovernorData data, String gameId)
    {
        final String query = "SELECT thome.ID, thome.Name, taway.ID, taway.Name, first_half_length, second_half_length " +
                " FROM Game " +
                " JOIN Team as thome " +
                " ON  Game.ID_Team_home = thome.ID " +
                " JOIN Team as taway\n" +
                " ON  Game.ID_Team_away = taway.ID " +
                " WHERE Game.ID == ?";

        openDatabase();
        Cursor cursor = mDatabase.rawQuery(query, new String[]{gameId});

        // Get data from the first row
        cursor.moveToFirst();

        data.homeTeam = new OPTA_Team(
                cursor.getInt(0), // TeamID
                cursor.getString(1), // Team Name
                true); // HomeTeam
        data.awayTeam = new OPTA_Team(
                cursor.getInt(2), // TeamID
                cursor.getString(3),  // Team Name
                false); // Away Team

        // Depreciated?
        data.firstHalfLength = cursor.getInt(3);
        data.secondHalfLength = cursor.getInt(5);

        cursor.close();
        closeDatabase();
    }

    void getPlayerNames(SparseArray<OPTA_Player> players)
    {
        // Create dynamic query for every playerID
        StringBuilder getPlayers = new StringBuilder("SELECT * FROM Player WHERE ");
        for (int i = 0; i < players.size() - 1; i++) {
            getPlayers.append("ID == ");
            getPlayers.append(players.keyAt(i));
            getPlayers.append(" OR ");
        }
        getPlayers.append(" ID == ");
        getPlayers.append(players.keyAt(players.size() - 1));

        // Evaluate query
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(getPlayers.toString(), new String[]{ });
        // Iterate over cursor
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            players.get(
                    cursor.getInt(0)) // PlayerID
                    .setName(cursor.getString(1), cursor.getString(2), cursor.getString(3)); // Name
        }

        cursor.close();
        closeDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}
