package DatabaseTables;

/**
 * Created by knukro on 6/14/17.
 */
public class Table_Game implements SQLInsertable {

    // [Primary Key]
    public final int game_uID;
    // [Data]
    public int team_home, team_away; // [FK] team_uID_Ref
    public int first_half, second_half;

    public Table_Game(String game_uID, String team_home, String team_away, String first_half, String second_half)
    {
        this.game_uID = Integer.valueOf(game_uID.substring(1));
        this.team_home = Integer.valueOf(team_home.substring(1));
        this.team_away = Integer.valueOf(team_away.substring(1));
        this.first_half = Integer.valueOf(first_half);
        this.second_half = Integer.valueOf(second_half);
    }


    @Override
    public String tableName()
    {
        return "Game";
    }

    @Override
    public String insertValues()
    {
        return String.format("(%d, %d, %d, %d, %d)",
                game_uID, team_home, team_away, first_half, second_half);
    }

}
