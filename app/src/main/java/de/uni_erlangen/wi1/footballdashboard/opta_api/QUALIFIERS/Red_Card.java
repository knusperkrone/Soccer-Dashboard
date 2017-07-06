package de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS;

import de.uni_erlangen.wi1.footballdashboard.opta_api.API_QUALIFIER_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Qualifier;

/**
 * Created by Jan on 02.07.2017.
 */

public class Red_Card extends OPTA_Qualifier
{

    public Red_Card(String value)
    {
        super(value);
    }

    @Override
    public int getId()
    {
        return API_QUALIFIER_IDS.RED_CARD;
    }

    @Override
    public String describeContent()
    {
        return null;
    }
}
