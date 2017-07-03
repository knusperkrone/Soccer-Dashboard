package de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO;

import de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;

/**
 * Created by knukro on 5/22/17.
 */

public class Player_Changed_Position extends OPTA_Event
{

    public Player_Changed_Position(boolean outcome, int period_id, int min, int sec, int playerId, int teamId, double x, double y)
    {
        super(outcome, period_id, min, sec, playerId, teamId, x, y);
    }

    @Override
    public int getID()
    {
        return API_TYPE_IDS.PLAYER_CHANGED_POSITION;
    }

    //outcome is always set to 1
    @Override
    public String getDescription()
    {
        //Todo: wohin ist der Spieler gewechselt?
        return gov.getPlayerName(playerId) + " changed position";
    }

}
