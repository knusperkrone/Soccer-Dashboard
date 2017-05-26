package de.uni_erlangen.wi1.footballdashboard.OPTA_API.QUALIFIERS;

import de.uni_erlangen.wi1.footballdashboard.OPTA_API.OPTA_Qualifier;

/**
 * Created by knukro on 5/22/17.
 */

public class Corner_Taken extends OPTA_Qualifier
{

    public Corner_Taken(String value)
    {
        super(value);
    }

    @Override
    public int GetId()
    {
        return 0;
    }
}
