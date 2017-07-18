package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview;

import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.helper.TeamFormationChange;
import de.uni_erlangen.wi1.footballdashboard.opta_api.API_QUALIFIER_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Qualifier;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.viewpager_adapter.FormatViewPagerAdapter;

/**
 * Created by knukro on 17.07.17.
 */

public class EventParser
{

    private final OPTA_Team homeTeam;
    private final OPTA_Team awayTeam;
    private FormatViewPagerAdapter formationViewPager;

    public EventParser(FormatViewPagerAdapter formationViewPager,
                       OPTA_Team homeTeam, OPTA_Team awayTeam)
    {
        this.formationViewPager = formationViewPager;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        // TODO: register on liveAdapter
    }

    public void setActiveEvents(List<OPTA_Event> events)
    {
        boolean homeTeam = StatusBar.getInstance().isShowingHome();
        resetValues();
        for (OPTA_Event event : events)
            addActiveEvent(event, homeTeam);
    }


    public void addActiveEvent(OPTA_Event event)
    {
        addActiveEvent(event, event.getTeamId() == homeTeam.getId());
    }

    private void addActiveEvent(OPTA_Event event, boolean isHomeTeam)
    {
        final OPTA_Team team = (isHomeTeam) ? homeTeam : awayTeam;

        switch (event.getID()) {
            case API_TYPE_IDS.FOUL:
                for (OPTA_Qualifier qualifier : event.qualifiers) {
                    switch (qualifier.getId()) {
                        case API_QUALIFIER_IDS.SECOND_YELLOW:
                            team.setRedCard(event.getPlayerId());
                            break;
                        case API_QUALIFIER_IDS.YELLOW_CARD:
                            team.setYellowCard(event.getPlayerId());
                            break;
                    }
                }
                break;
            case API_TYPE_IDS.FORMATION_CHANGE:
                int newFormation = -1;
                int newCaptain = -1;
                TeamFormationChange[] newPlayerData =
                        new TeamFormationChange[team.getPlayers().size()];
                for (OPTA_Qualifier qualifier : event.qualifiers) {
                    int i = 0;
                    switch (qualifier.getId()) {

                        case API_QUALIFIER_IDS.JERSEY_NUMBER:
                            for (String jerseyNumber : qualifier.getValue().split(", ")) {
                                newPlayerData[i].jerseyNumber = Integer.valueOf(jerseyNumber);
                                i++;
                            }
                            break; // Ignore

                        case API_QUALIFIER_IDS.TEAM_FORMATION:
                            newFormation = Integer.valueOf(qualifier.getValue());
                            break;

                        case API_QUALIFIER_IDS.PLAYER_POSITION:
                            for (String playerPosition : qualifier.getValue().split(", ")) {
                                newPlayerData[i].position = Integer.valueOf(playerPosition);
                                i++;
                            }
                            break;

                        case API_QUALIFIER_IDS.TEAM_PLAYER_FORMATION:
                            for (String playerFormation : qualifier.getValue().split(", ")) {
                                newPlayerData[i].layoutPosition = Integer.valueOf(playerFormation);
                                i++;
                            }
                            break;

                        case API_QUALIFIER_IDS.CAPTAIN:
                            newCaptain = Integer.valueOf(qualifier.getValue());
                            break;

                        case API_QUALIFIER_IDS.INVOLVED:
                            for (String playerIds : qualifier.getValue().split(", ")) {
                                newPlayerData[i].playerId = Integer.valueOf(playerIds);
                                i++;
                            }
                            break;
                    }
                }

                team.setNewTeamData(newPlayerData, newCaptain);
                team.setLayout(newFormation);
                formationViewPager.teamChangeFormation(isHomeTeam, newFormation);
                break;

            case API_TYPE_IDS.GOAL:
                StatusBar.getInstance().addGoal(isHomeTeam);
                break;

            // Not in our data samples!
            case API_TYPE_IDS.GOALKEEPER_BECOMES_PLAYER:
            case API_TYPE_IDS.PLAYER_BECOMES_GOALKEEPER:
            case API_TYPE_IDS.PLAYER_CHANGED_POSITION:
            case API_TYPE_IDS.PLAYER_CHANGED_JERSEY_NUMBER:
        }
    }

    private void resetValues()
    {
        homeTeam.resetCards();
        awayTeam.resetCards();
    }


}
