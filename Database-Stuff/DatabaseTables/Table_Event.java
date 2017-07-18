package DatabaseTables;

import java.sql.PreparedStatement;

/**
 * Created by knukro on 6/14/17.
 */
public class Table_Event implements SQLInsertable {

    // [PRIMARY KEYS]
    public final int iD;
    public final int id_Game;


    public Table_Event(String ID, String id_Game)
    {
        this.iD = Integer.valueOf(ID);
        this.id_Game = Integer.valueOf(id_Game);
    }

    @Override
    public String tableName()
    {
        return "Event";
    }

    @Override
    public String insertValues()
    {
        return String.format("(%d, %d)", iD, id_Game);
    }

}
