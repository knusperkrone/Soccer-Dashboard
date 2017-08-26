package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.statistic_fragments.player;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.ui_components.ActiveView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.seekbar.OnSeekBarChangeAble;

/**
 * Created by knukro on 6/20/17.
 */

public interface IPlayerFragment extends ActiveView, OnSeekBarChangeAble
{

    void setNewPlayer(OPTA_Player player);

}
