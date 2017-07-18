package DatabaseTables;

public class Table_GamePlayer implements SQLInsertable {

    // [PRIMARY KEY]
    public final int game_uID; // FK Game
    public final int pref_Id; // FK Player
    public final int team_Id; // FK Player
    // [DATA]
    public String position, status;
    public int shirtNumber;
    public boolean captain;


    public Table_GamePlayer(String game_uID, String pref_Id,String team_id, String position, String shirtNumber, String status, boolean captain)
    {
        this.game_uID = Integer.valueOf(game_uID.substring(1));
        this.pref_Id = Integer.valueOf(pref_Id.substring(1));
        this.team_Id = Integer.valueOf(team_id.substring(1));
        this.shirtNumber = Integer.valueOf(shirtNumber);
        this.position = position;
        this.status = status;
        this.captain = captain;
    }

    @Override
    public String tableName()
    {
        return "GamePlayer";
    }

    @Override
    public String insertValues()
    {
        return String.format("(%d, %d, %d,'%s', '%s', %d, %d)",
                game_uID, pref_Id, team_Id,  position, status, shirtNumber, (captain) ? 1 : 0);
    }

}
