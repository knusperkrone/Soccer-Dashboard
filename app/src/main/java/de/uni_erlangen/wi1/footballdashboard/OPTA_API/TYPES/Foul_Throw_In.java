package de.uni_erlangen.wi1.footballdashboard.OPTA_API.TYPES;

import de.uni_erlangen.wi1.footballdashboard.OPTA_API.API_TYPE_IDS;
import de.uni_erlangen.wi1.footballdashboard.OPTA_API.OPTA_Type;

/**
 * Created by knukro on 5/22/17.
 */

public class Foul_Throw_In extends OPTA_Type
{

    public Foul_Throw_In(boolean outcome, int period_id, int min, int sec, int playerId, int teamId, int x, int y)
    {
        super(outcome, period_id, min, sec, playerId, teamId, x, y);
    }

    @Override
    public int GetID()
    {
        return API_TYPE_IDS.FOUL_THROW_IN;
    }

}
