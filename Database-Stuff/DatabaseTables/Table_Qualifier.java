package DatabaseTables;

import com.sun.istack.internal.Nullable;

/**
 * Created by knukro on 6/14/17.
 */
public class Table_Qualifier implements SQLInsertable {

    // [PRIMARY KEY]
    public final int ID;
    // [DATA]
    public int qualifier_ID;
    public String value;


    public Table_Qualifier(String ID, String qualifier_ID, @Nullable String value)
    {
        this.ID = Integer.valueOf(ID);
        this.qualifier_ID = Integer.valueOf(qualifier_ID);
        this.value = value;
    }

    @Override
    public String tableName()
    {
        return "Qualifier";
    }

    @Override
    public String insertValues()
    {
        return String.format("(%d, %d, '%s')",
                ID, qualifier_ID, value);
    }
}
