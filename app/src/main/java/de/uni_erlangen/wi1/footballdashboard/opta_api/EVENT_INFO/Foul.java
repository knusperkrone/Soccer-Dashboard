package de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO;

import de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Qualifier;
import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Hand;

/**
 * Created by knukro on 5/22/17.
 */

public class Foul extends OPTA_Event
{

    public Foul(boolean outcome, int period_id, int min, int sec, int playerId, int teamId, double x, double y)
    {
        super(outcome, period_id, min, sec, playerId, teamId, x, y);
    }

    @Override
    public int getID()
    {
        return API_TYPE_IDS.FOUL;
    }

    @Override
    public String getDescription()
    {
        for (OPTA_Qualifier q : qualifiers) {
            if (q instanceof Hand) {
                return "Handball by " + gov.getPlayerName(playerId, teamId);
            }
        }
        return outcome ? gov.getPlayerName(playerId, teamId) + " was fouled" : "Foul by " + gov.getPlayerName(playerId, teamId);
    }

}
