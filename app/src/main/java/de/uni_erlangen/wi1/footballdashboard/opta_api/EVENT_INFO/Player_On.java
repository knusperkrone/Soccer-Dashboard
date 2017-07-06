package de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO;

import de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Qualifier;
import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Injury;

/**
 * Created by knukro on 5/22/17.
 */

public class Player_On extends OPTA_Event
{

    public Player_On(boolean outcome, int period_id, int min, int sec, int playerId, int teamId, double x, double y)
    {
        super(outcome, period_id, min, sec, playerId, teamId, x, y);
    }

    @Override
    public int getID()
    {
        return API_TYPE_IDS.PLAYER_ON;
    }

    //outcome is always set to 1
    @Override
    public String getDescription()
    {
        for (OPTA_Qualifier q : qualifiers) {
            if (q instanceof Injury) {
                return gov.getPlayerName(playerId, teamId) + " was substituted on for an injured player";
            }
        }
        return gov.getPlayerName(playerId, teamId) + " came on as a substitute";
    }

}
