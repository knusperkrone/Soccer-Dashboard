package DatabaseTables;

import com.sun.istack.internal.NotNull;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by knukro on 6/14/17.
 */
public class DataPool {

    public final HashSet<Table_Event> events = new HashSet<>();
    public final HashSet<Table_EventData> eventData = new HashSet<>(); // COMMON ERROR: A PRIMARY KEY constraint failed (UNIQUE constraint failed: EventData.ID_Event, EventData.ID_Game)
    public final HashSet<Table_Player> players = new HashSet<>(); // COMMON ERROR: [SQLITE_ERROR] SQL error or missing database (near "Golo": syntax error)
    public final HashSet<Table_Qualifier> qualifiers = new HashSet<>();
    private final HashMap<Integer, Table_Team> teams = new HashMap<>();
    public final HashSet<Table_Game> games = new HashSet<>();
    public final HashSet<Table_GamePlayer> gamePlayers = new HashSet<>();

    public void add(Table_Team team)
    {
        if (!teams.containsKey(team.uID_ref))
            teams.put(team.uID_ref, team);
    }

    public void insertInDataBase(@NotNull final String path)
    {
        int duplicates = 0;
        Connection conn;
        try {
            File tmp = new File(path);
            boolean exists;
            if (!(exists = tmp.exists()))
                tmp.createNewFile();

            conn = DriverManager.getConnection("jdbc:sqlite:" + path);
            if (!exists)
                makeDataBaseSchema(conn);

            insert(events, conn);
            insert(eventData, conn);
            insert(qualifiers, conn);
            insert(gamePlayers, conn);

            conn.close();
        } catch (SQLException e) {
            System.out.println("\n[ERROR] Couldn't connect to the database\n");
        } catch (IOException e) {
            System.out.println("\n[ERROR] IO exception " + e);
        }
    }

    public void insertMetaInDataBase(@NotNull final String path)
    {
        Connection conn;
        try {
            File tmp = new File(path);
            boolean exists;
            if (!(exists = tmp.exists()))
                tmp.createNewFile();

            conn = DriverManager.getConnection("jdbc:sqlite:" + path);
            if (!exists)
                makeMetaDataBaseSchema(conn);

            insert(games, conn);
            insert(players, conn);
            insert(teams.values(), conn);

            conn.close();
        } catch (SQLException e) {
            System.out.println("\n[ERROR] Couldn't connect to the database\n");
        } catch (IOException e) {
            System.out.println("\n[ERROR] IO exception " + e);
        }
    }

    private static <T extends SQLInsertable> void insert(@NotNull Iterable<T> elements, @NotNull Connection conn)
    {
        if (!elements.iterator().hasNext())
            return;

        String tableName = elements.iterator().next().tableName();
        String sqlInsert = "INSERT INTO " + tableName + " VALUES ";
        StringBuilder builder = new StringBuilder(4096);
        builder.append(sqlInsert);
        for (T elem : elements) {
            builder.append(elem.insertValues());
            builder.append(", ");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));

        try (PreparedStatement pstmt = conn.prepareStatement(builder.toString())) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("\n[EXCEPTION AT '" + tableName + "'] Switching back to fallback insert\n" + e);
            fallBackInsert(elements, conn, sqlInsert);
        }
    }

    private static <T extends SQLInsertable> void fallBackInsert(@NotNull Iterable<T> elements, @NotNull Connection conn,
                                                                 @NotNull String sqlInsert)
    {
        for (T elem : elements) {
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert + elem.insertValues())) {
                pstmt.executeUpdate();
            } catch (SQLException e) {
                if (!e.getMessage().contains("[SQLITE_CONSTRAINT_PRIMARYKEY]"))
                    System.out.println("[FallbackInsert] Error at: " + elem.insertValues() + "\n" + e);
            }
        }
    }

    private static void makeDataBaseSchema(Connection conn)
    {
        final String[] create = {
                "CREATE TABLE `Event` ( `ID` INTEGER, `ID_Game` INTEGER, PRIMARY KEY(`ID`,`ID_Game`), FOREIGN KEY(`ID_Game`) REFERENCES Game(ID) ) ",
                "CREATE TABLE `EventData` ( `ID_Event` INTEGER, `ID_Game` INTEGER, `Typ_ID` INTEGER, `Period` INTEGER NOT NULL, `Min` INTEGER NOT NULL, `Sec` INTEGER NOT NULL, `ID_Qualifier` INTEGER NOT NULL, `ID_Player` INTEGER NOT NULL, `ID_Team` INTEGER NOT NULL, `Outcome` INTEGER, `X_Coord` REAL NOT NULL, `Y_Cood` REAL NOT NULL, PRIMARY KEY(`ID_Event`,`ID_Game`,`Typ_ID`,`Period`,`Min`,`Sec`,`ID_Qualifier`), FOREIGN KEY(`ID_Event`) REFERENCES `Event`(`ID`), FOREIGN KEY(`ID_Game`) REFERENCES `Event`(`ID_Game`), FOREIGN KEY(`ID_Qualifier`) REFERENCES `Qualifier`(`ID`), FOREIGN KEY(`ID_Player`) REFERENCES `Player`(`ID`), FOREIGN KEY(`ID_Team`) REFERENCES `Team`(`ID`) )",
                "CREATE TABLE `GamePlayer` ( `ID_Game` INTEGER, `ID_Player` INTEGER, `ID_Team` INTEGER, `Position` TEXT NOT NULL, `Status` TEXT NOT NULL, `ShirtNumber` INTEGER NOT NULL, `Captain` INTEGER NOT NULL, PRIMARY KEY(`ID_Game`,`ID_Player`,`ID_Team`), FOREIGN KEY(`ID_Game`) REFERENCES `Game`(`ID`), FOREIGN KEY(`ID_Player`) REFERENCES `Player`(`ID`), FOREIGN KEY(`ID_Team`) REFERENCES `Team`(`ID`) )",
                "CREATE TABLE `Qualifier` ( `ID` INTEGER, `Qualifier_ID` INTEGER NOT NULL, `Value` TEXT, PRIMARY KEY(`ID`) )",
        };

        for (String creat : create) {
            try (PreparedStatement pstm = conn.prepareStatement(creat)) {
                pstm.executeUpdate();
            } catch (SQLException e) {
                System.out.println("[CreateDB] Error at:\n" + e);
            }
        }
    }

    private static void makeMetaDataBaseSchema(Connection conn)
    {
        final String[] create = {
                "CREATE TABLE `Game` ( `ID` INTEGER, `ID_Team_home` INTEGER NOT NULL, `ID_Team_away` INTEGER NOT NULL, `First_half_length` INTEGER NOT NULL, `Second_half_length` INTEGER NOT NULL, PRIMARY KEY(`ID`), FOREIGN KEY(`ID_Team_home`) REFERENCES `Team`(`ID`), FOREIGN KEY(`ID_Team_away`) REFERENCES `Team`(`ID`) )",
                "CREATE TABLE `Player` ( `ID` INTEGER, `FirstName` TEXT NOT NULL, `KnownName` TEXT, `LastName` TEXT NOT NULL, PRIMARY KEY(`ID`) )",
                "CREATE TABLE `Team` ( `ID` INTEGER, `Name` TEXT NOT NULL, PRIMARY KEY(`ID`) )"
        };

        for (String creat : create) {
            try (PreparedStatement pstm = conn.prepareStatement(creat)) {
                pstm.executeUpdate();
            } catch (SQLException e) {
                System.out.println("[CreateDB] Error at:\n" + e);
            }
        }
    }

}
