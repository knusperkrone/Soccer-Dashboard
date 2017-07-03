package de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO;

import de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Qualifier;
import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Red_Card;
import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Second_Yellow;
import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Yellow_Card;

/**
 * Created by knukro on 5/22/17.
 */

public class Card extends OPTA_Event
{

    public Card(boolean outcome, int period_id, int min, int sec, int playerId, int teamId, double x, double y)
    {
        super(outcome, period_id, min, sec, playerId, teamId, x, y);
    }


    @Override
    public int getID()
    {
        return API_TYPE_IDS.CARD;
    }

    //outcome is always set to 1
    @Override
    public String getDescription()
    {
        for(OPTA_Qualifier q : qualifiers){
            if(q instanceof Red_Card){
                return gov.getPlayerName(playerId) + " received a red card";
            }
            if(q instanceof Second_Yellow){
                return gov.getPlayerName(playerId) + " received a second yellow card";
            }
            if(q instanceof Yellow_Card){
                return gov.getPlayerName(playerId) + " received a yellow card";
            }
        }
        return gov.getPlayerName(playerId) + " received a card";
    }

}
