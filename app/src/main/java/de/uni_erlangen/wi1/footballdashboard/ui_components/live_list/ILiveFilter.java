package de.uni_erlangen.wi1.footballdashboard.ui_components.list_live_event;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;

/**
 * Created by knukro on 6/19/17.
 */

public interface LiveFilter
{
    boolean isValid(OPTA_Event event);
}
