package de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO;

import de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;

/**
 * Created by knukro on 5/22/17.
 */

public class End extends OPTA_Event
{

    public End(boolean outcome, int period_id, int min, int sec, int playerId, int teamId, double x, double y)
    {
        super(outcome, period_id, min, sec, playerId, teamId, x, y);
    }

    @Override
    public int getID()
    {
        return API_TYPE_IDS.END;
    }

    //outcome is always set to 1
    @Override
    public String getDescription()
    {
        switch(period_id){
            case 1:
                return "First half ended";
            case 2:
                return "Second half ended";
            case 3:
                return "First period of extra time ended";
            case 4:
                return "Second period of extra time ended";
            case 5:
                return "Penalty shoot out ended";
            default:
                return "Match period ended";
        }
    }

}
