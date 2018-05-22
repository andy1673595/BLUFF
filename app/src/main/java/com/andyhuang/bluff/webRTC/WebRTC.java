package com.andyhuang.bluff.webRTC;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.andyhuang.bluff.GamPage.GamePagePresenter;
import com.andyhuang.bluff.activities.GamePage;
import com.andyhuang.bluff.webRTC.AppRTCClient.SignalingParameters;
import com.andyhuang.bluff.webRTC.PeerConnectionClient.PeerConnectionParameters;
import static android.app.Activity.RESULT_CANCELED;


public class WebRTC {
    //Url of google WebRTC server
    private String roomUrl = "https://appr.tc";
    private Uri roomUri;
    private boolean iceConnected;
    private boolean activityRunning;
    private boolean isError;
    private boolean commandLineRun =false;
    private GamePagePresenter mPresenter;
    private GamePage mGamePageView;
    private SignalingParameters signalingParameters;
    private PeerConnectionParameters peerConnectionParameters;
    private String roomID;
    FirebaseRTCClient mFirebaseRTCClient;
    AppRTCSingalEvent mAppRTCSingalEvent;

    // List of permissions
    private static final String[] MANDATORY_PERMISSIONS = {"android.permission.MODIFY_AUDIO_SETTINGS",
            "android.permission.RECORD_AUDIO", "android.permission.INTERNET"};
    public WebRTC(GamePagePresenter presenter, GamePage gamePageView,String roomIDInput) {
        mPresenter = presenter;
        mGamePageView = gamePageView;
        roomID = roomIDInput;
        init();
    }

    public void init() {
        iceConnected = false;
        signalingParameters = null;
        checkPermissions();
        // Create connection parameters.
        roomUri = Uri.parse(roomUrl);
        //create a all default setting PeerConnectionParameters to Create PeerConnection
        peerConnectionParameters =
                new PeerConnectionClient.PeerConnectionParameters(true, false,
                        false, 0, 0, 0,
                        0, "VP8",
                        true,
                        0, "OPUS",
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false);
        commandLineRun = false;
        //user Firebase as communication channel
        mAppRTCSingalEvent = new AppRTCSingalEvent(mGamePageView);
        mFirebaseRTCClient = new FirebaseRTCClient(mAppRTCSingalEvent,roomID);
    }

    void checkPermissions() {
        // Check for mandatory permissions.
        for (String permission : MANDATORY_PERMISSIONS) {
            if (mGamePageView.checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mGamePageView,"沒拿到權限,可能無法使用視訊功能", Toast.LENGTH_LONG).show();
                mGamePageView.setResult(RESULT_CANCELED);
              //  mGamePageView.finish();
                return;
            }
        }
    }
}
