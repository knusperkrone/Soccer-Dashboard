package DatabaseTables;

/**
 * Created by knukro on 6/14/17.
 */
public class Table_Team implements SQLInsertable {

    // [PRIMARY KEYS]
    public final int uID_ref;
    // [DATA]
    public String name;

    public Table_Team(String uID_ref, String name)
    {
        this.uID_ref = Integer.valueOf(uID_ref.substring(1));
        this.name = name;
    }

    @Override
    public String tableName()
    {
        return "Team";
    }

    @Override
    public String insertValues()
    {
        return String.format("(%d, '%s')",
                uID_ref, name);
    }

}
