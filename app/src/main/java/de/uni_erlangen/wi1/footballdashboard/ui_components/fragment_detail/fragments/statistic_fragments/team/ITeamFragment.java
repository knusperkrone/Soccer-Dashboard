package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.statistic_fragments.team;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;

/**
 * Created by knukro on 6/20/17.
 */

public interface ITeamFragment
{

    void setNewTeam(OPTA_Team team);

    void drawStatistics();
}
