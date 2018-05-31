package com.andyhuang.bluff.Util;

public class Constants {
    public static final String FACEBOOK_HINT = "ThisFacebookDontShowAcocount";
    public static String FIREBASE_URL = "https://myproject-556f6.firebaseio.com/";
    public static String TAG = "Bluff Debug";
    public static String NODATA = "No data";
    //String tag for SharedPreferences
    public static String TAG_FOR_SHAREDPREFREENCE = "userData";
    public static String USER_EMAIL_SHAREDPREFREENCE = "userEmail";
    public static String USER_PASSWORD_SHAREDPREFREENCE = "userPassword";
    public static String USER_NAME_SHAREDPREFREENCE = "userName";
    public static String USER_UID_SHAREDPREFREENCE = "userUID";
    public static String FACEBOOKID_SHAREDPREFREENCE = "facebookID";
    public static String FACEBOOK_PHOTO_URL_SHAREDPREFREENCE = "facebookURL";


    //Tag for firebase
    public static String USER_DATA_FIREBASE = "userData";
    public static String USER_EMAIL_FIREBASE = "userEmail";
    public static String USER_PASSWORD_FIREBASE = "userPassword";
    public static String USER_NAME_FIREBASE = "userName";
    public static String USER_PHOTO_FIREBASE = "userPhoto";
    public static String USER_COMMENT_FIREBASE = "userComment";
    public static String FRIEND_LIST_FIREBASE = "friendlist";
    public static String FRIEND_INVITE_FIREBASE = "isInvite";
    public static String USER_UID_FIREBASE = "userUID";
    public static String GAME_INVITE = "gameInvite";
    public static String GAME ="gameInformaiton";
    public static String GAME_ROOM = "gameRoom";
    public static String PLAYED_TOTAL_INVITED = "player total invited";

    //Tag for Game firebase
    public static String GAME_ROOM_ID_REF = "https://myproject-556f6.firebaseio.com/GameRoomID/";
    public static String GAMER_FIREBASE = "gamer";
    public static String URL_GAME_ROOM_DATA= "https://myproject-556f6.firebaseio.com/GameData/";
    public static String GAME_STATE = "gameState";
    public static String GAMER_LIST = "gamer list";
    public static String CURRENT_STATE_LIST = "current state list";
    public static String DICE_LIST = "dice list";
    public static String DICE_TOTAL_LIST = "dice total list";
    public static String NEXT_PLAYER_INFORMATION = "next player";
    public static String END_INFORMATION = "end information";
    public static String ONLINE_STATE = "online state";
    public static String IS_GAMING = "is gaming";
    //Game State Button
    public static String BUTTON_START ="start";
    public static String BUTTON_WAIT = "wait host start";
    public static String BUTTON_READY = "ready";
    public static String BUTTON_GET_READY = "get ready";
    public static String BUTTON_PLAYING = "playing";
    //GAME STATE
    public static String WAIT_HOST = "wait host";
    public static String READ_INIT_DATA = "read init data";
    public static String WAIT_READY = "wait ready";
    public static String NEW_DICE = "get new dice";
    public static String GET_INITIAL_GAME_DATA ="get initial game data";
    public static String PLAYING = "playing";
    public static String LOAD_END_INFO = "load end information";
    public static String EVERYONE_EXIT_GAME = "exit game";
    //Gamer current state
    public static String COMPLETED_READ_INIT = "completed read init";
    public static String GET_READY = "get ready";
    public static String CANCEL_READY = "cancel ready";
    public static String COMPLETED_NEW_DICE = "completed new dice";
    public static String READY_FOR_PLAYING = "ready for playing";
    public static String EXIT_GAME = "exit game";

    //Game result TAG
    public static String GAME_RESULT = "GameResult";
    public static String LOSE_TIMES = "loseTimes";
    public static String WIN_TIMES = "winTimes";
    public static String TOTAL_TIMES = "totalTimes";

    //Activity request code
    //Constant for Login activity callback request code
    public static int CLOSE_ACTIVITY = 2;
    //Constant for change user photo
    public static final int GET_PHOTO_FROM_GALLERY =3;
    public static final int GET_PHOTO_CROP =4;


    //Constant for random games
    public static String RANDOM_GAME = "randomGame";

    //float radius of dialog
    public static float DIALOG_RADIUS = 70f;



}
