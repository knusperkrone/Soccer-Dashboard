package de.uni_erlangen.wi1.footballdashboard.OPTA_API;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by knukro on 5/22/17.
 */

public class OPTA_Event
{

    public final List<OPTA_Type> types;
    public final int id;


    public OPTA_Event(int id)
    {
        this.id = id;

        types = new ArrayList<>(4);
    }

}
