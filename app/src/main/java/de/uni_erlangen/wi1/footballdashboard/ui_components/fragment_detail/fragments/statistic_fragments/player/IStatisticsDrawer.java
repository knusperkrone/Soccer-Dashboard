package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.statistic_fragments.player;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;

/**
 * Created by knukro on 7/6/17.
 */

interface IStatisticsDrawer
{

    void additionalStatisticData(OPTA_Player player, String name);

}
