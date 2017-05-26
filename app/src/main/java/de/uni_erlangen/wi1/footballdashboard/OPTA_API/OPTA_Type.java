package de.uni_erlangen.wi1.footballdashboard.OPTA_API;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by knukro on 5/22/17.
 */

public abstract class OPTA_Type
{

    public final List<OPTA_Qualifier> qualifiers;

    public final boolean outcome;
    public final int period_id;
    public final int min;
    public final int sec;
    public final int playerId;
    public final int teamId;
    public final int x, y;


    public OPTA_Type(boolean outcome, int period_id, int min, int sec, int playerId, int teamId, int x, int y)
    {
        this.outcome = outcome;
        this.period_id = period_id;
        this.min = min;
        this.sec = sec;
        this.playerId = playerId;
        this.teamId = teamId;
        this.x = x;
        this.y = y;

        qualifiers = new ArrayList<>(8);
    }

    public abstract int GetID();

}

