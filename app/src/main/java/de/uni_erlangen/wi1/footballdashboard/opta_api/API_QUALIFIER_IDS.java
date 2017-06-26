package de.uni_erlangen.wi1.footballdashboard.opta_api;


public class API_QUALIFIER_IDS
{

    // PASS EVENTS
    // EVENTS: 1(PASS)
    public static final int LONG_BALL = 1;
    public static final int CROSS = 2;
    public static final int HEAD_PASS = 3;
    public static final int THROUGH_BALL = 4;
    public static final int FREE_KICK_TAKEN = 5;
    public static final int CORNER_TAKEN = 6;
    public static final int PLAYERS_CAUGHT_OFFSIDE = 7;
    public static final int GOAL_DISALLOWED = 8;
    public static final int ATTACKING_PASS = 106;
    public static final int THROW_IN = 107;
    public static final int PASS_END_X = 140;
    public static final int PASS_END_Y = 141;
    public static final int CHIPPED = 155;
    public static final int LAY_OFF = 156;
    public static final int LAUNCH = 157;
    public static final int FLICK_ON = 168;
    public static final int PULL_BACK = 195;
    public static final int SWITCH_OF_PLAY = 196;
    public static final int ASSIST = 210;
    public static final int LENGTH = 212;
    public static final int ANGLE = 213;
    public static final int SECOND_ASSIST = 218;
    public static final int PLAYERS_ON_BOTH_POSTS = 219;
    public static final int PLAYERS_ON_NEAR_POSTS = 220;
    public static final int PLAYERS_ON_FAR_POSTS = 221;
    public static final int NO_PLAYERS_ON_POSTS = 222;
    public static final int IN_SWINGER = 223;
    public static final int OUT_SWINGER = 224;
    public static final int STRAIGHT = 255;

    // BODY PART
    public static final int HEAD = 15;
    public static final int LEFT_FOOTED = 72;
    public static final int RIGHT_FOOTED = 20;
    public static final int OTHER_BODY_PART = 21;

    // PATTERN OF PLAY
    // EVENTS: 13(MISS), 14(POST), 15(ATTEMPT_SAVED), 16(GOAL)
    public static final int REGULAR_PLAY = 22;
    public static final int FAST_BREAK = 23;
    public static final int SET_PIECE = 24;
    public static final int FROM_CORNER = 25;
    public static final int FREE_KICK = 26;
    public static final int CORNER_SITATION = 96;
    public static final int DIRECT_FREE = 97;
    public static final int SCRAMLE = 112;
    public static final int THROW_IN_SET_PIECE = 160;
    public static final int ASSISTED = 29;
    public static final int INTENTIONAL_ASSIST = 154;
    public static final int RELATED_EVENT_ID = 55;
    public static final int SECOND_RELATED_EVENT_ID = 216;


    // SHOT DESCRIPTORS:
    // EVENTS: 13(MISS), 14(POST), 15(ATTEMPT_SAVED), 16(GOAL)
    public static final int PENALTY = 9; // EVENT 4(FOUL) when a penalty was awarded
    public static final int OWN_GOAL = 28; // Inverse coordinates for the goal location
    public static final int STRONG = 113;
    public static final int WEAK = 114;
    public static final int RISING = 115;
    public static final int DIPPING = 116;
    public static final int LOB = 117;
    public static final int SWERVE_LEFT = 120;
    public static final int SWERVE_RIGHT = 121;
    public static final int SWERVE_MOVING = 122;
    public static final int DEFLECTION = 133;
    public static final int KEEPER_TOUCHED = 136;
    public static final int KEEPER_SAVED = 137;
    public static final int HIT_WOODWORK = 138;
    public static final int NOT_PAST_GOAL_LINE = 153;
    public static final int BIG_CHANGE = 214;
    public static final int INDIVIUAL_PLAY = 215;
    public static final int SECOND_ASSITED = 217;
    public static final int OWN_SHOT_BLOCKED = 228;


    // SHOT LOCATION DESCRIPTORS
    public static final int SMALL_BOX_CENTRE = 16;
    public static final int BOX_CENTRE = 17;
    public static final int OUT_OF_BOX_CENTRE = 18;
    public static final int THIRTYFIVE_CENTRE = 19;
    public static final int SMALL_BOX_RIGHT = 60;
    public static final int SMALL_BOX_LEFT = 61;
    public static final int BOX_DEEP_RIGHT = 62;
    public static final int BOX_RIGHT = 63;
    public static final int BOX_LEFT = 64;
    public static final int BOX_DEEP_LEFT = 65;
    public static final int OUTOF_BOX_DEEP_RIGHT = 66;
    public static final int OUTOF_BOX_RIGHT = 67;
    public static final int OUTOF_BOX_LEFT = 68;
    public static final int OUTOF_BOX_DEEP_LEFT = 69;
    public static final int THIRTYFIVE_RIGHT = 70;
    public static final int THIRTYFIVE_LEIFT = 71;
    public static final int LEFT = 73;
    public static final int HIGH = 74;
    public static final int RIGHT = 75;
    public static final int LOW_LEFT = 76;
    public static final int HIGH_LEFT = 77;
    public static final int LOW_CENTRE = 78;
    public static final int HIGH_CENTRE = 79;
    public static final int LOW_RIGHT = 80;
    public static final int HIGH_RIGHT = 81;
    public static final int BLOCKED = 82;
    public static final int CLOSE_LEFT = 83;
    public static final int CLOSE_RIGHT = 84;
    public static final int CLOSE_HIGH = 85;
    public static final int CLOSE_LEFT_HIGH = 86;
    public static final int CLOSE_RIGHT_HIGH = 87;
    public static final int SIX_YARD_BLOCKED = 100;
    public static final int SAVED_OFF_LINE = 101;
    public static final int GOAL_MOUTH_Y_COORDINATE = 102;
    public static final int GOAL_MOUTH_Z_COORDINATE = 103;
    public static final int BLOCKED_X_COORDINATE = 146;
    public static final int BLOCKED_Y_COORDINATE = 147;


    // FOUL AND CARD EVENTS
    // EVENTS: 4(FOUL) - except for cards
    public static final int RESCINDED_CARD = 171;
    public static final int NO_IMPACT_ON_TIMING = 172;
    public static final int DISSIENT = 184;
    public static final int OFF_THE_BALL_FOUL = 191;
    public static final int BLOCKED_BY_HAND = 192;

    // GOALKEEPER EVENTS
    // EVENTS: 10(SAVE), 11(CLAIM), 12(CLEARANCE)
    public static final int FROM_SHOT_OFF_TARGET = 190;
    public static final int HIGH_CLAIM = 88;
    public static final int ONE_ON_ONE = 89;
    public static final int DEFLECTED_SAVE = 90;
    public static final int DIVE_AND_DEFLECT = 91;
    public static final int CATCH = 92;
    public static final int DIVE_AND_CATCH = 92;
    public static final int KEEPER_THROW = 123;
    public static final int GOAL_KICK = 124;
    public static final int PUNCH = 128;
    public static final int OWN_PLAYER = 139;
    public static final int PARRIED_SAFE = 173;
    public static final int PARRIED_DANGER = 174;
    public static final int FINGERTIP = 175;
    public static final int CAUGHT = 176;
    public static final int COLLECTED = 177;
    public static final int STANDING = 178;
    public static final int DIVING = 179;
    public static final int STOOPING = 180;
    public static final int REACHING = 181;
    public static final int HANDS = 182;
    public static final int FEET = 183;
    public static final int SCORED = 186;
    public static final int SAVED = 187;
    public static final int MISSED = 188;
    public static final int GK_HOOF = 198;
    public static final int GK_KICK_FROM_HAND = 199;

    // DEFENSIVE EVENTS
    public static final int LAST_LINE = 14;
    public static final int DEF_BLOCK = 94;
    public static final int OUT_OF_PLAY = 167;
    public static final int LEADING_TO_ATTEMPT = 169;
    public static final int LEADING_TO_GOAL = 170;
    public static final int BLOCKED_CROSS = 185;

    // LINE UP/SUBS/FORMATION
    // EVENTS: 32(), 34(), 35(), 36(), 40()
    public static final int INVOLVED = 30;
    public static final int INJURY = 41;
    public static final int TACTICAL = 42;
    public static final int PLAYER_POSITION = 44;
    public static final int JERSEY_NUMBER = 59;
    public static final int TEAM_FORMATION = 130;
    public static final int TEAM_PLAYER_FORMATION = 131;
    public static final int FORMATION_SLOT = 145;
    public static final int CAPTAIN = 194;
    public static final int TEAM_KIT = 197;

    // REFEREE
    public static final int OFFICIAL_POSITION = 50;
    public static final int OFFICIAL_ID = 51;
    public static final int REFEREE_STOP = 200;
    public static final int REFEREE_DELAY = 201;
    public static final int REFEREE_INJURY = 208;

    // ATTENDANCE FIGURE
    public static final int ATTENDANCE_FIGURE = 49;

    // STOPPAGES
    // EVENTS: 27
    public static final int INJURED_PLAYER_ID = 53;
    public static final int WEATHER_PROBLEM = 202;
    public static final int CROWD_TROUBLE = 203;
    public static final int FIRE = 204;
    public static final int OBJECT_THROWN_ON_PITCH = 205;
    public static final int SPECTATOR_ON_PITCH = 206;
    public static final int AWAITING_OFFICIALS_DECISION = 207;
    //public static final int REFEREE_INJURY = 208; // Already defined
    public static final int SUSPENDED = 226;
    public static final int RESUME = 227;

    // GENERAL
    public static final int END_CAUSE = 54;
    public static final int ZONE = 56;
    public static final int END_TYPE = 57;
    public static final int DIRECTION_OF_PLAY = 127;
    public static final int DELETED_EVENT_TYPE = 144;
    public static final int PLAYER_NOT_VISIBLE = 189;
    public static final int GAME_END = 209;
    public static final int OVERRUN = 211;
    public static final int POST_MATCH_COMPLETE = 229;
}
