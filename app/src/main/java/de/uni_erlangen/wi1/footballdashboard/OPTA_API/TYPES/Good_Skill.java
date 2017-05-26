package de.uni_erlangen.wi1.footballdashboard.OPTA_API.TYPES;

import de.uni_erlangen.wi1.footballdashboard.OPTA_API.API_TYPE_IDS;
import de.uni_erlangen.wi1.footballdashboard.OPTA_API.OPTA_Type;

/**
 * Created by knukro on 5/22/17.
 */

public class Good_Skill extends OPTA_Type
{

    public Good_Skill(boolean outcome, int period_id, int min, int sec, int playerId, int teamId, int x, int y)
    {
        super(outcome, period_id, min, sec, playerId, teamId, x, y);
    }

    @Override
    public int GetID()
    {
        return API_TYPE_IDS.GOOD_SKILL;
    }

}
