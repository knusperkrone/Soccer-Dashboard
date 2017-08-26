package de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO;

import de.uni_erlangen.wi1.footballdashboard.opta_api.API_QUALIFIER_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Qualifier;

/**
 * Created by knukro on 5/22/17.
 */
public class Pass extends OPTA_Event
{

    public Pass(boolean outcome, int period_id, int min, int sec, int playerId, int teamId, double x, double y)
    {
        super(outcome, period_id, min, sec, playerId, teamId, x, y);
    }

    @Override
    public int getID()
    {
        return API_TYPE_IDS.PASS;
    }

    @Override
    public void calcRankingPoint(OPTA_Player player)
    {
        int value = (outcome) ? 1 : -1;

        if (player.getPosition().equals(OPTA_Player.Position.DEFENDER) ||
                player.getPosition().equals(OPTA_Player.Position.GOALKEEPER)) {
            value = (outcome) ? 1 : -2;
        }

        for (OPTA_Qualifier qualifier : this.qualifiers) {

            switch (qualifier.getId()) {
                case API_QUALIFIER_IDS.LONG_BALL:
                    value = (outcome) ? 2 : -1;
                    break;
                case API_QUALIFIER_IDS.CROSS:
                    value = (outcome) ? 2 : -1;
                    break;
                case API_QUALIFIER_IDS.ATTACKING_PASS:
                    value = (outcome) ? 2 : -1;
                    break;
                case API_QUALIFIER_IDS.ASSIST:
                    value = (outcome) ? 3 : -2;
                    break;
            }


        }

        player.changeRankingPoints(value);
    }

    @Override
    public String getDescription()
    {
        return (outcome ? "Successful" : "Unsuccessful") + " Pass by " + gov.getPlayerName(playerId, teamId);
    }

}
