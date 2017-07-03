package de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO;

import de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;

/**
 * Created by knukro on 5/22/17.
 */

public class Rescinded_Card extends OPTA_Event
{

    public Rescinded_Card(boolean outcome, int period_id, int min, int sec, int playerId, int teamId, double x, double y)
    {
        super(outcome, period_id, min, sec, playerId, teamId, x, y);
    }

    @Override
    public int getID()
    {
        return API_TYPE_IDS.RESCINDED_CARD;
    }

    //outcome is always set to 1
    @Override
    public String getDescription()
    {
        //Todo Unterscheidung Gelb/Rot
        return gov.getPlayerName(playerId) + "; Referee rescinded a card";
    }
}

