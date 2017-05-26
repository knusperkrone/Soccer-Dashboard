package de.uni_erlangen.wi1.footballdashboard.OPTA_API.QUALIFIERS;

import de.uni_erlangen.wi1.footballdashboard.OPTA_API.OPTA_Qualifier;

/**
 * Created by knukro on 5/22/17.
 */

public class Cross extends OPTA_Qualifier
{
    public Cross(String value)
    {
        super(value);
    }

    @Override
    public int GetId()
    {
        return 0;
    }
}
