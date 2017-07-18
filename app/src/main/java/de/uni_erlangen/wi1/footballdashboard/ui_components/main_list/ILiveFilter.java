package de.uni_erlangen.wi1.footballdashboard.ui_components.main_list;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;

/**
 * Created by knukro on 6/19/17.
 */

public interface ILiveFilter
{
    boolean isValid(OPTA_Event event);
}
