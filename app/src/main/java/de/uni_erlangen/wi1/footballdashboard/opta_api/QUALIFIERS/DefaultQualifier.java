package de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Qualifier;

/**
 * Created by knukro on 06.08.17.
 */

public class DefaultQualifier extends OPTA_Qualifier
{

    private final int id;

    public DefaultQualifier(String value, int id)
    {
        super(value);
        this.id = id;
    }

    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public String describeContent()
    {
        return value;
    }
}
