package com.andyhuang.bluff.webRTC;

import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

public class AppRTCSingalEvent implements AppRTCClient.SignalingEvents  {
    @Override
    public void onConnectedToRoom(AppRTCClient.SignalingParameters params) {

    }

    @Override
    public void onRemoteDescription(SessionDescription sdp) {

    }

    @Override
    public void onRemoteIceCandidate(IceCandidate candidate) {

    }

    @Override
    public void onRemoteIceCandidatesRemoved(IceCandidate[] candidates) {

    }

    @Override
    public void onChannelClose() {

    }

    @Override
    public void onChannelError(String description) {

    }
}
