package de.uni_erlangen.wi1.footballdashboard.helper;

import de.uni_erlangen.wi1.footballdashboard.opta_api.API_QUALIFIER_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Qualifier;

/**
 * Created by knukro on 17.07.17.
 */

public class EventParser
{

    public static TeamFormationChange parseFormationChange(OPTA_Event event, int size)
    {
        TeamFormationChange changeData = new TeamFormationChange(size);
        // Parse qualifiers
        for (OPTA_Qualifier qualifier : event.qualifiers) {
            int i;
            switch (qualifier.getId()) {

                case API_QUALIFIER_IDS.CAPTAIN:
                    changeData.setCaptainId(Integer.valueOf(qualifier.getValue()));
                    break;

                case API_QUALIFIER_IDS.INVOLVED:
                    i = 0;
                    for (String playerIds : qualifier.getValue().split(", ")) {
                        changeData.setPlayerId(i, Integer.valueOf(playerIds));
                        i++;
                    }
                    break;

                case API_QUALIFIER_IDS.JERSEY_NUMBER:
                    i = 0;
                    for (String jerseyNumber : qualifier.getValue().split(", ")) {
                        changeData.setJerseyNumber(i, Integer.valueOf(jerseyNumber));
                        i++;
                    }
                    break;

                case API_QUALIFIER_IDS.PLAYER_POSITION:
                    i = 0;
                    for (String playerPosition : qualifier.getValue().split(", ")) {
                        changeData.setPlayerLayoutPosition(i, Integer.valueOf(playerPosition));
                        i++;
                    }
                    break;

                case API_QUALIFIER_IDS.TEAM_FORMATION:
                    changeData.setLayoutId(Integer.valueOf(qualifier.getValue()));
                    break;

                case API_QUALIFIER_IDS.TEAM_PLAYER_FORMATION:
                    i = 0;
                    for (String playerFormation : qualifier.getValue().split(", ")) {
                        changeData.setPlayerPosition(i, Integer.valueOf(playerFormation));
                        i++;
                    }
                    break;
            }
        }

        return changeData;
    }

}
