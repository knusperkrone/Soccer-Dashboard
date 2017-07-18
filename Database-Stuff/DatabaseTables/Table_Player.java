package DatabaseTables;

import com.sun.istack.internal.Nullable;

/**
 * Created by knukro on 6/14/17.
 */
public class Table_Player implements SQLInsertable {

    // [PRIMARY KEYS]
    public final int ID; // Text
    // [DATA]
    public String firstName, knownName, lastName; //Text

    public Table_Player(String refId, String firstName, @Nullable String knownName, String lastName)
    {
        this.ID = Integer.valueOf(refId.substring(1));
        this.firstName = firstName.replaceAll("'", "''");
        if (knownName != null)
            this.knownName = knownName.replaceAll("'", "''");
        this.lastName = lastName.replaceAll("'", "''");
    }

    @Override
    public String tableName()
    {
        return "Player";
    }

    @Override
    public String insertValues()
    {
        return String.format("(%d, '%s', '%s', '%s')",
                ID, firstName, knownName, lastName);
    }
}
