package com.andyhuang.bluff.Constant;

public class ConstantForWebRTC {
    public static final int REMOTE_X = 0;
    public static final int REMOTE_Y = 0;
    public static final int REMOTE_WIDTH = 100;
    public static final int REMOTE_HEIGHT = 100;
    // Local preview screen position before call is connected.
    public static final int LOCAL_X_CONNECTING = 0;
    public static final int LOCAL_Y_CONNECTING = 0;
    public static final int LOCAL_WIDTH_CONNECTING = 100;
    public static final int LOCAL_HEIGHT_CONNECTING = 100;
    // Local preview screen position after call is connected.
    public static final int LOCAL_X_CONNECTED = 73;
    public static final int LOCAL_Y_CONNECTED = 3;
    public static final int LOCAL_WIDTH_CONNECTED = 25;
    public static final int LOCAL_HEIGHT_CONNECTED = 25;
    //Constant use for firebase
    public static final String CHANNEL_VIDEO = "channel";
    //Constant for TURN server URL
    public static final String TURN_SERVER_URL =
            "https://networktraversal.googleapis.com/v1alpha/iceconfig?key=AIzaSyAJdh2HkajseEIltlZ3SIXO02Tze9sO3NY";
}
