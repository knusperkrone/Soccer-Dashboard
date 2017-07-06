package de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Qualifier;

/**
 * Created by knukro on 5/22/17.
 */

public class Head_Pass extends OPTA_Qualifier
{
    public Head_Pass(String value)
    {
        super(value);
    }

    @Override
    public int getId()
    {
        return 0;
    }

    @Override
    public String describeContent()
    {
        return null;
    }
}
