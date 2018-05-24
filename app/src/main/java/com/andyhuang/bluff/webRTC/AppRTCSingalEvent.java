package com.andyhuang.bluff.webRTC;


import android.widget.Toast;

import com.andyhuang.bluff.activities.GamePage;

import org.webrtc.Camera1Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;
import org.webrtc.VideoCapturer;
import com.andyhuang.bluff.webRTC.AppRTCClient.SignalingParameters;

public class AppRTCSingalEvent implements AppRTCClient.SignalingEvents  {
    GamePage mGamePageView;
    private long callStartedTimeMs = 0;
    private PeerConnectionClient peerConnectionClient;
    private WebRTC mWebRTC;

    AppRTCSingalEvent(GamePage gamePage,WebRTC webRTC) {
        mWebRTC = webRTC;
        mGamePageView = gamePage;
    }
    public void setCallStartedTimeMs (long time) {
        callStartedTimeMs = time;
    }
    public void setPeerConnectionClient(PeerConnectionClient peerConnectionClientInput) {
        peerConnectionClient = peerConnectionClientInput;
    }

    @Override
    public void onConnectedToRoom(final AppRTCClient.SignalingParameters params) {
        mGamePageView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onConnectedToRoomInternal(params);
            }
        });
    }

    @Override
    public void onRemoteDescription(final SessionDescription sdp) {
        mGamePageView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (peerConnectionClient == null) {
                    return;
                }
                peerConnectionClient.setRemoteDescription(sdp);
                AppRTCClient.SignalingParameters peer = mWebRTC.getSignalingParameters();
                if (!mWebRTC.getSignalingParameters().initiator) {
                    // Create answer. Answer SDP will be sent to offering client in
                    // PeerConnectionEvents.onLocalDescription event.
                    peerConnectionClient.createAnswer();
                }
            }
        });
    }

    @Override
    public void onRemoteIceCandidate(final IceCandidate candidate) {
        mGamePageView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (peerConnectionClient == null) {
                    Toast.makeText(mGamePageView,"NO peerConnectionClient", Toast.LENGTH_LONG).show();
                    return;
                }
                peerConnectionClient.addRemoteIceCandidate(candidate);
            }
        });
    }

    @Override
    public void onRemoteIceCandidatesRemoved(final IceCandidate[] candidates) {
        mGamePageView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (peerConnectionClient == null) {
                    Toast.makeText(mGamePageView,"NO peerConnectionClient", Toast.LENGTH_LONG).show();
                    return;
                }
                peerConnectionClient.removeRemoteIceCandidates(candidates);
            }
        });
    }

    @Override
    public void onChannelClose() {
        mWebRTC.disconnect();
    }

    @Override
    public void onChannelError(final String description) {
        //show Error description
        mGamePageView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mGamePageView,"發生錯誤: "+description, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void onConnectedToRoomInternal(final SignalingParameters params) {
        final long delta = System.currentTimeMillis() - callStartedTimeMs;
        mWebRTC.setSignalingParameters(params);
        VideoCapturer videoCapturer = createVideoCapturer();
        peerConnectionClient.createPeerConnection(
                mGamePageView.getRootEglBase().getEglBaseContext(),mGamePageView.getLocalRender()
                ,mGamePageView.getRemoteRenderers(), videoCapturer, mWebRTC.getSignalingParameters());

        if (params.initiator) {
            // Create offer. Offer SDP will be sent to answering client in
            // PeerConnectionEvents.onLocalDescription event.
            peerConnectionClient.createOffer();
        } else {
            if (params.offerSdp != null) {
                peerConnectionClient.setRemoteDescription(params.offerSdp);
                // Create answer. Answer SDP will be sent to offering client in
                // PeerConnectionEvents.onLocalDescription event.
                peerConnectionClient.createAnswer();
            }
            if (params.iceCandidates != null) {
                // Add remote ICE candidates from room.
                for (IceCandidate iceCandidate : params.iceCandidates) {
                    peerConnectionClient.addRemoteIceCandidate(iceCandidate);
                }
            }
        }
    }

    private VideoCapturer createVideoCapturer() {
        VideoCapturer videoCapturer = null;
        //get camera
        videoCapturer = createCameraCapturer(new Camera1Enumerator(true));
        if (videoCapturer == null) {
            return null;
        }
        return videoCapturer;
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();
        // First, try to find front facing camera
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        return null;
    }
}
