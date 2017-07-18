package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.statistic_fragments.team;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.ActiveView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.seekbar.OnSeekBarChangeAble;

/**
 * Created by knukro on 6/20/17.
 */

public interface ITeamFragment extends ActiveView, OnSeekBarChangeAble
{

    void setNewTeam(OPTA_Team team);

}
