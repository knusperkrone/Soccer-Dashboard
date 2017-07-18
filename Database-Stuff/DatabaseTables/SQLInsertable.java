package DatabaseTables;

import java.sql.PreparedStatement;

/**
 * Created by knukro on 6/14/17.
 */
interface SQLInsertable {

    String tableName();

    String insertValues();

}
