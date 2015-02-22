package com.skynet.basketassistant.Otros;

/**
 * Created by yamil.marques on 19/09/2014.
 */
public class Constants {

    public static boolean TESTING_MODE = true;

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String TEAM_NAME = "TEAM_NAME";
    public static final String TEAM_ID = "TEAM_ID";
    public static final String GAME_ID = "GAME_ID";
    public static final String PLAYER_ID = "PLAYER_ID";
    public static final String CONSTANT_SHOOT = "CONSTANT_SHOOT";

    //Others
    public static final String GAME = "GAME";
    public static final String MYTEAM = "MYTEAM";
    public static final String OPPONENTTEAM = "OPPONENTTEAM";

    //SHOOTS
    public static final String SHOOT_TYPE_SIMPLE = "SIMPLE";
    public static final String SHOOT_TYPE_DOUBLE = "DOUBLE";
    public static final String SHOOT_TYPE_TRIPLE = "TRIPLE";

    public static final int MAXIMUN_OF_PLAYERS_ON_TEAM = 13;
    public static final int SIMPLE_SHOOT = 1;
    public static final int DOUBLE_SHOOT = 2;
    public static final int TRIPLE_SHOOT = 3;

    public static final int SIMPLE_SHOOT_VALUE = 1;
    public static final int DOUBLE_SHOOT_VALUE = 2;
    public static final int TRIPLE_SHOOT_VALUE = 3;

    // SHOOT STATE -- FRAGDIALOG_SCORE_OR_NOT --
    public static final int SHOOT_SCORED = 1;
    public static final int SHOOT_FAILED = 0;

    //Shoot Dialog Fragment
    public static final String ADD_OR_REMOVE = "ADD_OR_REMOVE";

    //Yes-No Dialog Fragment
    public static final int YES = 1;
    public static final int NO = 0;
    //called by:
    public static final int YES_NO_ASSISTANCE = 0;
    public static final int YES_NO_FINAL_GAME = 1;

    //Mode for Dialog Fragments
    public static final int MODE_ADD = 0;
    public static final int MODE_REMOVE = 1;


    //OFENSIVE DEFENSIVE DIALOG
    public static final String FRAGMENT_DIALOG_OFENSIVE_DEFENSIVE = "FRAGMENT_DIALOG_OFENSIVE_DEFENSIVE";
    public static final String OFENSIVE = "OFENSIVE";
    public static final String DEFENSIVE = "DEFENSIVE";
    public static final String FRAGDIALOG_TITLE = "FRAGDIALOG_TITLE";
    public static final String WHO_CALL = "WHO_CALL";

    //REBOUNDS CONSTANT
    public static final String REBOUND_CALL = "REBOUND";  //Used in WHO_CALL BUNDLE
    public static final String FOUL_CALL = "FOUL";   //Used in WHO_CALL BUNDLE

    //Quarters or Times
    public static final int MAX_NUMBER_OF_QUARTERS = 5;

    //Fouls Constants
    public static final int MAX_NUMBER_OF_FOULS_PER_QUARTER = 5;

    //Fragments Dialogs
    public static final String FRAGMENT_DIALOG_SCORE_OR_NOT = "FRAGMENT_DIALOG_SCORE_OR_NOT";
    public static final String FRAGMENT_DIALOG_PLAYERS_LIST = "FRAGMENT_DIALOG_PLAYERS_LIST";
    public static final String FRAGMENT_DIALOG_YES_NO = "FRAGMENT_DIALOG_YES_NO";
    public static final String FRAGMENT_DIALOG_ACTION = "FRAGMENT_DIALOG_ACTION";
    public static final String FRAGMENT_DIALOG_GAME_INFORMATION = "FRAGMENT_DIALOG_GAME_INFORMATION";
    public static final String FRAGMENT_DIALOG_FINAL_STATISTICS = "FRAGMENT_DIALOG_FINAL_STATISTICS";
        //ACTIONS
            public static final String FRAGMENT_DIALOG_ACTION_ADD_ASSISTANCE = "ADD_ASSISTANCE";


}
