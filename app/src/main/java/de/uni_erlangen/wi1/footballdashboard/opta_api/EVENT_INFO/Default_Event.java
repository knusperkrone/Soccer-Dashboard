package de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;

/**
 * Created by knukro on 07.08.17.
 */

public class Default_Event extends OPTA_Event
{

    private final int id;

    public Default_Event(boolean outcome, int period_id, int min, int sec, int playerId, int teamId, double x, double y, int id)
    {
        super(outcome, period_id, min, sec, playerId, teamId, x, y);
        this.id = id;
    }

    @Override
    public int getID()
    {
        return id;
    }
}
