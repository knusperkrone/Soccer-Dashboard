package de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO;

import de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;

/**
 * Created by knukro on 5/22/17.
 */

public class Tackle extends OPTA_Event
{

    public Tackle(boolean outcome, int period_id, int min, int sec, int playerId, int teamId, double x, double y)
    {
        super(outcome, period_id, min, sec, playerId, teamId, x, y);
    }

    @Override
    public int getID()
    {
        return API_TYPE_IDS.TACKLE;
    }

    @Override
    public void calcRankingPoint(OPTA_Player player)
    {
        int value = (outcome) ? 1 : 0;


        if (player.getPosition().equals(OPTA_Player.Position.DEFENDER)) {
            value = (outcome) ? 2 : 1;
        }

        player.changeRankingPoints(value);
    }

    @Override
    public String getDescription()
    {
        return (outcome ? "Unsuccessful" : "Successful") + "tackle by " + gov.getPlayerName(playerId, teamId);
    }
}
