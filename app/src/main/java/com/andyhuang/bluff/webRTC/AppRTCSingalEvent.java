package com.andyhuang.bluff.webRTC;


import android.widget.Toast;
import com.andyhuang.bluff.activities.GamePage;
import com.andyhuang.bluff.webRTC.AppRTCClient.SignalingParameters;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;
import org.webrtc.VideoCapturer;

public class AppRTCSingalEvent implements AppRTCClient.SignalingEvents  {
    private long callStartedTimeMs = 0;
    private GamePage mGamePageView;
    private PeerConnectionClient peerConnectionClient;
    private WebRTC mWebRTC;
    private MyVideoCapture mMyVideoCapture;

    AppRTCSingalEvent(GamePage gamePage,WebRTC webRTC) {
        mWebRTC = webRTC;
        mGamePageView = gamePage;
        mMyVideoCapture = new MyVideoCapture();
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
                //兩人收到sdp都需要設定RemoteDescription
                peerConnectionClient.setRemoteDescription(sdp);
                AppRTCClient.SignalingParameters peer = mWebRTC.getSignalingParameters();
                if (!mWebRTC.getSignalingParameters().initiator) {
                    //第二個進入房間的人回傳answer
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

    public void setCallStartedTimeMs (long time) {
        callStartedTimeMs = time;
    }
    public void setPeerConnectionClient(PeerConnectionClient peerConnectionClientInput) {
        peerConnectionClient = peerConnectionClientInput;
    }


    private void onConnectedToRoomInternal(final SignalingParameters params) {
        mWebRTC.setSignalingParameters(params);
        VideoCapturer videoCapturer = mMyVideoCapture.createVideoCapturer();
        //將視訊流導入peerConnectionClient
        peerConnectionClient.createPeerConnection(
                mGamePageView.getRootEglBase().getEglBaseContext(),mGamePageView.getLocalRender()
                ,mGamePageView.getRemoteRenderers(), videoCapturer, mWebRTC.getSignalingParameters());

        if (params.initiator) {
            //I'm Initiator, I should Create SDP offer
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

}
