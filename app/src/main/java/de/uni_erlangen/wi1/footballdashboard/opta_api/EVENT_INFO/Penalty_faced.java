package de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO;

import de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;

/**
 * Created by knukro on 5/22/17.
 */

public class Penalty_faced extends OPTA_Event
{

    public Penalty_faced(boolean outcome, int period_id, int min, int sec, int playerId, int teamId, double x, double y)
    {
        super(outcome, period_id, min, sec, playerId, teamId, x, y);
    }

    @Override
    public int getID()
    {
        return API_TYPE_IDS.PENALTY_FACED;
    }

    //outcome is always set to 1
    @Override
    public String getDescription()
    {
        //Todo: Unsicher was man hiermit machen soll
        return gov.getPlayerName(playerId) + "Opposing team was awarded a penalty";
    }

}
