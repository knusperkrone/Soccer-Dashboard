package DatabaseTables;

import com.sun.istack.internal.Nullable;

/**
 * Created by knukro on 6/14/17.
 */
public class Table_EventData implements SQLInsertable {

    // [PRIMARY KEYS]
    public final int event_ID; // [FK] Event
    public final int game_ID; // [FK] Game
    public final int type_ID;
    public final int period;
    public final int min;
    public final int sec;
    // [DATA]
    public int qualifier_ID; // [FK] Qualifier - TODO: REDESIGN DATABASE HERE?
    public int player_ID;
    public int team_ID;
    public int outcome;
    public double x, y;


    public Table_EventData(String event_ID, String game_ID, String typ_ID, String period, String min, String sec, String qualifier_ID, @Nullable String player_ID, String team_ID, String outcome, String x, String y)
    {
        this.event_ID = Integer.valueOf(event_ID);
        this.game_ID = Integer.valueOf(game_ID);
        this.type_ID = Integer.valueOf(typ_ID);
        this.period = Integer.valueOf(period);
        this.min = Integer.valueOf(min);
        this.sec = Integer.valueOf(sec);
        this.qualifier_ID = Integer.valueOf(qualifier_ID);
        this.player_ID = (player_ID == null) ? -1 : Integer.valueOf(player_ID); // Some actions don't have a player
        this.team_ID = Integer.valueOf(team_ID);
        this.outcome = Integer.valueOf(outcome);
        this.x = Double.valueOf(x);
        this.y = Double.valueOf(y);
    }

    @Override
    public String tableName()
    {
        return "EventData";
    }

    @Override
    public String insertValues()
    {
        return String.format("(%d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %f, %f)"
                , event_ID, game_ID, type_ID, period, min, sec, qualifier_ID, player_ID, team_ID, outcome, x, y);
    }
}
