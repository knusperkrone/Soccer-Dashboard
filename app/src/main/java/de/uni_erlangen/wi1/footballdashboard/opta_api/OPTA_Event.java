package de.uni_erlangen.wi1.footballdashboard.opta_api;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Aerial;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Attempt_Saved;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Ball_Recovery;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Ball_Touch;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Card;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Challenge;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Change_Missed;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Claim;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Clearance;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Collection_End;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Condition_Change;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Cross_Not_Claimed;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Deleted_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Dispossessed;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.End;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Error;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Foul;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Foul_Throw_In;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Goal;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Goalkeeper_Becomes_Player;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Good_Skill;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Interception;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Keeper_Pick_Up;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Keeper_Sweeper;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Miss;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Official_Change;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Offside_Pass;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Offside_Provoked;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Out;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Pass;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Penalty_faced;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Player_Becomes_Goalkeeper;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Player_Changed_Jersey_Number;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Player_Changed_Position;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Player_Off;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Player_On;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Player_Retired;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Player_Returns;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Post;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Punch;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Rescinded_Card;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Resume;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Save;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Smother;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Start;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Start_Delay;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Tackle;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Take_On;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Team_Set_up;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Temp_Goal;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Temp_Save;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Turnover;

import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.AERIAL;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.ATTEMPT_SAVED;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.BALL_RECOVERY;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.BALL_TOUCH;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.CARD;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.CHALLENGE;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.CHANGE_MISSED;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.CLAIM;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.CLEARANCE;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.COLLECTION_END;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.CONDITION_CHANGE;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.CONTENTIOUS_REFEREE_DECISION;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.CORNER_AWARDED;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.CROSS_NOT_CLAIMED;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.DELETED_EVENT;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.DISPOSSESSED;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.FORMATION_CHANGE;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.FOUL;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.FOUL_THROW_IN;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.GOAL;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.GOALKEEPER_BECOMES_PLAYER;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.GOOD_SKILL;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.INTERCEPTION;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.KEEPER_PICK_UP;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.KEEPER_SWEEPER;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.MISS;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.OFFICIAL_CHANGE;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.OFFSIDE_PASS;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.OFFSIDE_PROVOKED;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.PENALTY_FACED;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.PLAYER_BECOMES_GOALKEEPER;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.PLAYER_CHANGED_JERSEY_NUMBER;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.PLAYER_CHANGED_POSITION;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.PLAYER_OFF;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.PLAYER_ON;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.PLAYER_RETIRED;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.PLAYER_RETURNS;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.POST;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.SAVE;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.SHIELD_BALL_OPP;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.SMOTHER;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.START_DELAY;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.STOP_DELAY;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.TACKLE;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.TAKE_ON;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.TEAM_SET_UP;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.TEMP_ATTEMPT;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.TEMP_GOAL;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.TEMP_SAVE;
import static de.uni_erlangen.wi1.footballdashboard.opta_api.API_TYPE_IDS.TURNOVER;


public abstract class OPTA_Event implements Parent<OPTA_Qualifier>
{

    protected final GameGovernor gov = GameGovernor.getInstance();

    public final List<OPTA_Qualifier> qualifiers = new ArrayList<>(11);

    public final boolean outcome;
    public final int period_id;
    public final int min;
    public final int sec;
    public final int playerId;
    public final int teamId;
    public final double x;
    public final double y;

    public OPTA_Event(boolean outcome, int period_id, int min, int sec, int playerId,
                      int teamId, double x, double y)
    {
        this.outcome = outcome;
        this.period_id = period_id;
        this.min = min;
        this.sec = sec;
        this.playerId = playerId;
        this.teamId = teamId;
        this.x = x;
        this.y = y;
    }

    @Override
    public List<OPTA_Qualifier> getChildList()
    {
        return qualifiers;
    }

    @Override
    public boolean isInitiallyExpanded()
    {
        return false;
    }

    public abstract int getID();

    public String getDescription()
    {
        return "ID=" + getID();
    }

    public int getCRTime()
    {
        return min * 60 + sec;
    }

    public String getHRTime()
    {
        return ((min < 10) ? "0" : "") + min + ":" + ((sec < 10) ? "0" : "") + sec;
    }

    public static OPTA_Event newInstance(int ID, boolean outcome, int period_id,
                                         int min, int sec, int playerId, int teamId,
                                         double x, double y)
    {
        OPTA_Event info = null;
        switch (ID) {
            case AERIAL:
                info = new Aerial(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case ATTEMPT_SAVED:
                info = new Attempt_Saved(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case BALL_RECOVERY:
                info = new Ball_Recovery(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case BALL_TOUCH:
                info = new Ball_Touch(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case CARD:
                info = new Card(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case CHALLENGE:
                info = new Challenge(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case CHANGE_MISSED:
                info = new Change_Missed(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case CLAIM:
                info = new Claim(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case CLEARANCE:
                info = new Clearance(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case COLLECTION_END:
                info = new Collection_End(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case CONDITION_CHANGE:
                info = new Condition_Change(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case CONTENTIOUS_REFEREE_DECISION:
                //info = new CONTENTIOUS_REFEREE_DECISION(outcome, period_id, min, sec, playerId, teamId, x, y, value);
                break;
            case CORNER_AWARDED:
                //info = new CORNER_AWARDED(outcome, period_id, min, sec, playerId, teamId, x, y, value);
                break;
            case CROSS_NOT_CLAIMED:
                info = new Cross_Not_Claimed(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case DELETED_EVENT:
                info = new Deleted_Event(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case DISPOSSESSED:
                info = new Dispossessed(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case API_TYPE_IDS.END:
                info = new End(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case API_TYPE_IDS.ERROR:
                info = new Error(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case FORMATION_CHANGE:
                //info = new FORMATION_CHANGE(outcome, period_id, min, sec, playerId, teamId, x, y, value);
                break;
            case FOUL:
                info = new Foul(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case FOUL_THROW_IN:
                info = new Foul_Throw_In(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case GOAL:
                info = new Goal(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case GOALKEEPER_BECOMES_PLAYER:
                info = new Goalkeeper_Becomes_Player(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case GOOD_SKILL:
                info = new Good_Skill(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case INTERCEPTION:
                info = new Interception(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case KEEPER_PICK_UP:
                info = new Keeper_Pick_Up(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case KEEPER_SWEEPER:
                info = new Keeper_Sweeper(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case MISS:
                info = new Miss(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case OFFICIAL_CHANGE:
                info = new Official_Change(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case OFFSIDE_PASS:
                info = new Offside_Pass(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case OFFSIDE_PROVOKED:
                info = new Offside_Provoked(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case API_TYPE_IDS.OUT:
                info = new Out(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case API_TYPE_IDS.PASS:
                info = new Pass(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case PENALTY_FACED:
                info = new Penalty_faced(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case PLAYER_BECOMES_GOALKEEPER:
                info = new Player_Becomes_Goalkeeper(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case PLAYER_CHANGED_JERSEY_NUMBER:
                info = new Player_Changed_Jersey_Number(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case PLAYER_CHANGED_POSITION:
                info = new Player_Changed_Position(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case PLAYER_OFF:
                info = new Player_Off(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case PLAYER_ON:
                info = new Player_On(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case PLAYER_RETIRED:
                info = new Player_Retired(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case PLAYER_RETURNS:
                info = new Player_Returns(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case POST:
                info = new Post(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case API_TYPE_IDS.PUNCH:
                info = new Punch(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case API_TYPE_IDS.RESCINDED_CARD:
                info = new Rescinded_Card(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case API_TYPE_IDS.RESUME:
                info = new Resume(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case SAVE:
                info = new Save(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case SHIELD_BALL_OPP:
                //info = new SHIELD_BALL_OPP(outcome, period_id, min, sec, playerId, teamId, x, y, value);
                break;
            case SMOTHER:
                info = new Smother(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case API_TYPE_IDS.START:
                info = new Start(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case START_DELAY:
                info = new Start_Delay(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case STOP_DELAY:
                //info = new STOP_DELAY(outcome, period_id, min, sec, playerId, teamId, x, y, value);
                break;
            case TACKLE:
                info = new Tackle(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case TAKE_ON:
                info = new Take_On(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case TEAM_SET_UP:
                info = new Team_Set_up(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case TEMP_ATTEMPT:
                //info = new TEMP_ATTEMPT(outcome, period_id, min, sec, playerId, teamId, x, y, value);
                break;
            case TEMP_GOAL:
                info = new Temp_Goal(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case TEMP_SAVE:
                info = new Temp_Save(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
            case TURNOVER:
                info = new Turnover(outcome, period_id, min, sec, playerId, teamId, x, y);
                break;
        }
        if (info == null) {
            //FIXME: Log.d("[OPTA_EVENTINFO]", "New Instace got null for :" + ID);
            info = new Turnover(outcome, period_id, min, sec, playerId, teamId, x, y);
        }
        return info;
    }

}

