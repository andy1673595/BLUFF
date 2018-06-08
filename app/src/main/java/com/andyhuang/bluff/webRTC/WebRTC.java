package com.andyhuang.bluff.webRTC;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.andyhuang.bluff.Callback.DisconnectDialogCallback;
import com.andyhuang.bluff.GamPage.GamePagePresenter;
import com.andyhuang.bluff.activities.GamePage;
import com.andyhuang.bluff.webRTC.AppRTCClient.SignalingParameters;
import com.andyhuang.bluff.webRTC.PeerConnectionClient.PeerConnectionParameters;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class WebRTC {
    //Url of google WebRTC server
    private String roomUrl = "https://appr.tc";
    private String roomID;
    private long callStartedTimeMs = 0;
    private boolean iceConnected;
    private Uri roomUri;
    //game page elements
    private GamePagePresenter mPresenter;
    private GamePage mGamePageView;
    //WebRTC elements
    private SignalingParameters signalingParameters;
    private PeerConnectionParameters peerConnectionParameters;
    private PeerConnectionClient peerConnectionClient;
    private PeerConnectionEvent mPeerConnectionEvent;
    private AppRTCClient mFirebaseRTCClient;
    private AppRTCSingalEvent mAppRTCSingalEvent;
    private AppRTCClient.RoomConnectionParameters roomConnectionParameters;
    private AppRTCAudioManager audioManager = null;

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
        //user Firebase as communication channel
        mAppRTCSingalEvent = new AppRTCSingalEvent(mGamePageView,this);
        mFirebaseRTCClient = new FirebaseRTCClient(mAppRTCSingalEvent,roomID,this);
        roomConnectionParameters = new AppRTCClient.RoomConnectionParameters(roomUri.toString(), roomID, false);
        peerConnectionClient = PeerConnectionClient.getInstance();
        mPeerConnectionEvent = new PeerConnectionEvent(mGamePageView,peerConnectionParameters,mFirebaseRTCClient,this);
        peerConnectionClient.createPeerConnectionFactory(
                mGamePageView.getApplicationContext(), peerConnectionParameters,mPeerConnectionEvent);
        //bind peerConnectionClient to events, then they can call method in peerConnectionClient
        mAppRTCSingalEvent.setPeerConnectionClient(peerConnectionClient);
    }

    public void startCall() {
        callStartedTimeMs = System.currentTimeMillis();
        mAppRTCSingalEvent.setCallStartedTimeMs(callStartedTimeMs);
        mFirebaseRTCClient.connectToRoom(roomConnectionParameters);
        audioManager = AppRTCAudioManager.create(mGamePageView, new Runnable() {
           //Audio Manager的Method
            @Override
            public void run() {
                //onAudioManagerChangedState();
            }
        });
        audioManager.init();
    }

    public void disconnectReset() {
        if (mFirebaseRTCClient != null) {
            mFirebaseRTCClient.disconnectFromRoom();
            mFirebaseRTCClient = null;
        }
        Log.d("errorTEST","endWebRTC close Firebase end");
        if (peerConnectionClient != null) {
            peerConnectionClient.close();
            peerConnectionClient = null;
            Log.d("errorTEST","endWebRTC peerConnection=null end ");
        }
        if (audioManager != null) {
            audioManager.close();
            audioManager = null;
        }
        Log.d("errorTEST","endWebRTC audio = null end");
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

    public void setSignalingParameters(SignalingParameters signalingParametersInput) {
        signalingParameters = signalingParametersInput;
    }

    public SignalingParameters getSignalingParameters() {
        return signalingParameters;
    }

    public void showDisconnectMessage() {
        //show Disconnect confirm dialog
        new DisconnectDialog(mGamePageView,new DisconnectDialogCallback() {
            @Override
            public void confirm() {
                //disconnect all element and reset some variables
                mGamePageView.freshSwitchUI(false);
                mPresenter.disconnectVideo();
            }
        }).show();
    }

    public void disconnect() {
        mPresenter.disconnectVideo();
    }
    public void setIceConnected(boolean iceConnected) {
        this.iceConnected = iceConnected;
    }
    public boolean getIceConnected(){return iceConnected;}
}
