package com.andyhuang.bluff.webRTC;

import android.util.Log;

import com.andyhuang.bluff.activities.GamePage;
import com.andyhuang.bluff.webRTC.AppRTCClient.SignalingParameters;
import com.andyhuang.bluff.webRTC.PeerConnectionClient.PeerConnectionParameters;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;
import org.webrtc.StatsReport;

public class PeerConnectionEvent implements  PeerConnectionClient.PeerConnectionEvents {
    private PeerConnectionClient mPeerConnectionClient;
    private FirebaseRTCClient mFirebaseRTCClient;
    private GamePage mGamePageView;
    private long callStartedTimeMs =0;
    private AppRTCClient.SignalingParameters mSignalingParameters;
    private PeerConnectionParameters mPeerConnectionParameters;
    private boolean iceConnected;
    private WebRTC mWebRTC;
    public PeerConnectionEvent(GamePage gamePage, PeerConnectionParameters peerParameters
            , FirebaseRTCClient firebaseRTCClientInput,WebRTC webRTC) {
        mGamePageView = gamePage;
        mPeerConnectionParameters = peerParameters;
        mFirebaseRTCClient =firebaseRTCClientInput;
        mWebRTC = webRTC;
    }
    public void setPeerConnectionClient(PeerConnectionClient client) {
        mPeerConnectionClient = client;
    }

    public void setCallStartedTimeMs(long time) {
        callStartedTimeMs = time;
    }
    @Override
    public void onLocalDescription(final SessionDescription sdp) {
        final long delta = System.currentTimeMillis() - callStartedTimeMs;
        mGamePageView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mFirebaseRTCClient != null) {
                    if (mSignalingParameters.initiator) {
                        mFirebaseRTCClient.sendOfferSdp(sdp);
                    } else {
                        mFirebaseRTCClient.sendAnswerSdp(sdp);
                    }
                }
                if (mPeerConnectionParameters.videoMaxBitrate > 0) {
                    mPeerConnectionClient.setVideoMaxBitrate(mPeerConnectionParameters.videoMaxBitrate);
                }
            }
        });
    }

    @Override
    public void onIceCandidate(final IceCandidate candidate) {
        mGamePageView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mFirebaseRTCClient != null) {
                    mFirebaseRTCClient.sendLocalIceCandidate(candidate);
                }
            }
        });
    }

    @Override
    public void onIceCandidatesRemoved(final IceCandidate[] candidates) {
        mGamePageView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mFirebaseRTCClient != null) {
                    mFirebaseRTCClient.sendLocalIceCandidateRemovals(candidates);
                }
            }
        });
    }

    @Override
    public void onIceConnected() {
        final long delta = System.currentTimeMillis() - callStartedTimeMs;
        iceConnected = true;
        mWebRTC.setIceConnected(iceConnected);
        mGamePageView.setIceConnected(iceConnected);
        callConnected();
    }

    private void callConnected() {
        final long delta = System.currentTimeMillis() - callStartedTimeMs;
        if (mPeerConnectionClient == null ) {
            return;
        }
        // Update video view.
        mGamePageView.updateVideoView();
        // Enable statistics callback.
        mPeerConnectionClient.enableStatsEvents(true, 1000);
    }

    @Override
    public void onIceDisconnected() {
        iceConnected = false;
        mWebRTC.setIceConnected(iceConnected);
        mGamePageView.setIceConnected(iceConnected);
        disconnect();
    }

    private void disconnect() {
        mWebRTC.disconnectReset();
        mGamePageView.releaseSurfaceView();
        mGamePageView.closeVideo();
    }

    @Override
    public void onPeerConnectionClosed() {

    }

    @Override
    public void onPeerConnectionStatsReady(StatsReport[] reports) {
        //ready
    }

    @Override
    public void onPeerConnectionError(String description) {

    }

    public void setSignalingParameters(SignalingParameters signalingParameters) {
        mSignalingParameters = signalingParameters;
    }
}