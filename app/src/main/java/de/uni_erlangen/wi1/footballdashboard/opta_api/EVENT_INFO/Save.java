package de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO;

import de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;

/**
 * Created by knukro on 5/22/17.
 */

public class Save extends OPTA_Event
{

    public Save(boolean outcome, int period_id, int min, int sec, int playerId, int teamId, double x, double y)
    {
        super(outcome, period_id, min, sec, playerId, teamId, x, y);
    }

    @Override
    public int getID()
    {
        return API_TYPE_IDS.SAVE;
    }

    @Override
    public void calcRankingPoint(OPTA_Player player)
    {
        int value = 2;

        player.changeRankingPoints(value);
    }

    // outcome is always set to 1
    @Override
    public String getDescription()
    {
        return "Save by " + gov.getPlayerName(playerId, teamId);
    }

}
