package de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO;

import de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Qualifier;
import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Overrun;

/**
 * Created by knukro on 5/22/17.
 */

public class Take_On extends OPTA_Event
{

    public Take_On(boolean outcome, int period_id, int min, int sec, int playerId, int teamId, double x, double y)
    {
        super(outcome, period_id, min, sec, playerId, teamId, x, y);
    }

    @Override
    public int getID()
    {
        return API_TYPE_IDS.TAKE_ON;
    }

    @Override
    public void calcRankingPoint(OPTA_Player player)
    {
        int value = (outcome) ? 1 : -1;

        player.changeRankingPoints(value);
    }

    @Override
    public String getDescription()
    {
        for (OPTA_Qualifier q : qualifiers) {
            if (q instanceof Overrun) {
                return "Successful take on by " + gov.getPlayerName(playerId, teamId) + " ,but player lost the ball";
            }
        }
        return (outcome ? "Successful" : "Unsuccessful") + " Take on by " + gov.getPlayerName(playerId, teamId);
    }
}
