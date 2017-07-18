package de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO;

import de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Qualifier;
import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Leading_To_Attempt;
import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Leading_To_Goal;

/**
 * Created by knukro on 5/22/17.
 */

public class Error extends OPTA_Event
{

    public Error(boolean outcome, int period_id, int min, int sec, int playerId, int teamId, double x, double y)
    {
        super(outcome, period_id, min, sec, playerId, teamId, x, y);
    }

    @Override
    public int getID()
    {
        return API_TYPE_IDS.ERROR;
    }

    //outcome is always set to 1
    @Override
    public String getDescription()
    {
        for (OPTA_Qualifier q : qualifiers) {
            if (q instanceof Leading_To_Attempt) {
                return gov.getPlayerName(playerId, teamId) + " lost the ball which led to an attempt";
            }
            if (q instanceof Leading_To_Goal) {
                return gov.getPlayerName(playerId, teamId) + " lost the ball which led to a goal";
            }
        }
        return gov.getPlayerName(playerId, teamId) + " lost the ball";
    }

}
