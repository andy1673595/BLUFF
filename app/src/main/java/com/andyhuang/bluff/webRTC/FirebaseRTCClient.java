package com.andyhuang.bluff.webRTC;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.andyhuang.bluff.Constant.ConstantForWebRTC;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.PeerConnection;
import org.webrtc.SessionDescription;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static com.andyhuang.bluff.Constant.ConstantForWebRTC.CHANNEL_VIDEO;

public class FirebaseRTCClient implements AppRTCClient, ValueEventListener {
    private static final int TURN_HTTP_TIMEOUT_MS = 5000;
    private static final String TAG = "FirebaseRTCClient";
    private String roomID;
    private String gameRoomID;
    private boolean isConnected = false;
    private boolean isInitiator = false;
    private final Handler handler;
    private DatabaseReference database;
    private AppRTCSingalEvent events;
    private Hashtable<String, Boolean> sdpAdded = new Hashtable<String, Boolean>();
    private WebRTC mWebRTC;


    public FirebaseRTCClient( AppRTCSingalEvent events,String gameRoomIDInput,WebRTC webRTC) {
        database = FirebaseDatabase.getInstance().getReference();
        this.events = events;
        mWebRTC = webRTC;
        //bind firebaseClient to events
        gameRoomID = gameRoomIDInput;
        final HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d(TAG, "onDataChanged");
        if(!dataSnapshot.exists() && !isInitiator) {
            //I'm the communication Initiator
            isInitiator = true;
        }

        if(!dataSnapshot.hasChild(roomID)) {
            if(isConnected) {
                isConnected =false;
                disconnectFromRoom();
                //無資料但我還在連線->對方斷線, 因此離開房間
                mWebRTC.showDisconnectMessage();
                return;
            }
            else {
                //tell server I'm connected
                database.child(CHANNEL_VIDEO).child(gameRoomID).child(roomID).child("connected").setValue(true);
                isConnected = true;
                //Connected
                connectedToRoomOnAppRTC();
            }
        }

        if(dataSnapshot.hasChildren()) {
            Iterator<DataSnapshot> children = dataSnapshot.getChildren().iterator();
            while(children.hasNext()) {
                DataSnapshot child = children.next();
                if(child.getKey() != roomID) {
                    if(child.hasChild("sdp") ) {
                        //接收先進房間的人傳送的sdp
                        //setRemoteDescription on AppRTC
                        setRemoteDescriptionOnAppRTC(getSdp(child.child("sdp")));
                    }
                    Iterator<DataSnapshot>  iceList = child.child("icecandidate").getChildren().iterator();
                    while (iceList.hasNext()) {
                        //添加候補ice伺服器
                        addIceCandiateOnAppRTC(getIceCandidate(iceList.next()));
                    }
                }
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
    @Override
    public void connectToRoom(RoomConnectionParameters connectionParameters) {
        Log.d(TAG, "connect to room : " + connectionParameters.roomId);
        roomID = Build.SERIAL;
        database.child(CHANNEL_VIDEO).child(gameRoomID).addValueEventListener(this);
    }


    @Override
    public void sendOfferSdp(SessionDescription sdp) {
        Log.d(TAG, "send offer sdp");
        database.child(CHANNEL_VIDEO).child(gameRoomID).child(roomID).child("sdp").setValue(sdp);
    }

    @Override
    public void sendAnswerSdp(SessionDescription sdp) {
        Log.d(TAG, "send answer sdp");
        database.child(CHANNEL_VIDEO).child(gameRoomID).child(roomID).child("sdp").setValue(sdp);
    }

    @Override
    public void sendLocalIceCandidate(IceCandidate candidate) {
        Log.d(TAG, "send local ice candidate: " + candidate);
        database.child(CHANNEL_VIDEO).child(gameRoomID).child(roomID).child("icecandidate").child("" + candidate.hashCode()).setValue(candidate);
    }

    @Override
    public void sendLocalIceCandidateRemovals(IceCandidate[] candidates) {
        Log.d(TAG, "send local ice candidate removal");

        for(IceCandidate candidate:candidates) {
            database.child(CHANNEL_VIDEO).child(gameRoomID).child(roomID).child("icecandidate").child("" + candidate.hashCode()).removeValue();
        }
    }

    @Override
    public void disconnectFromRoom() {
        isConnected = false;
        database.child(CHANNEL_VIDEO).child(gameRoomID).removeValue();
        database.child(CHANNEL_VIDEO).child(gameRoomID).removeEventListener(this);
        sdpAdded.clear();
    }

    //request for TURN servers
    private LinkedList<PeerConnection.IceServer> requestTurnServers(String url)
            throws IOException, JSONException {
        LinkedList<PeerConnection.IceServer> turnServers = new LinkedList<PeerConnection.IceServer>();
        Log.d(TAG, "Request TURN from: " + url);
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("REFERER", "https://appr.tc");
        connection.setConnectTimeout(TURN_HTTP_TIMEOUT_MS);
        connection.setReadTimeout(TURN_HTTP_TIMEOUT_MS);
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Non-200 response when requesting TURN server from " + url + " : "
                    + connection.getHeaderField(null));
        }
        InputStream responseStream = connection.getInputStream();
        String response = drainStream(responseStream);
        connection.disconnect();
        Log.d(TAG, "TURN response: " + response);
        JSONObject responseJSON = new JSONObject(response);
        JSONArray iceServers = responseJSON.getJSONArray("iceServers");
        for (int i = 0; i < iceServers.length(); ++i) {
            JSONObject server = iceServers.getJSONObject(i);
            JSONArray turnUrls = server.getJSONArray("urls");
            String username = server.has("username") ? server.getString("username") : "";
            String credential = server.has("credential") ? server.getString("credential") : "";
            for (int j = 0; j < turnUrls.length(); j++) {
                String turnUrl = turnUrls.getString(j);
                turnServers.add(new PeerConnection.IceServer(turnUrl, username, credential));
            }
        }
        return turnServers;
    }

    // Return the contents of an InputStream as a String.
    private static String drainStream(InputStream in) {
        Scanner s = new Scanner(in).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


    /* AppRtc methods */
    //return the sdp
    private SessionDescription getSdp(DataSnapshot db) {
        String type = db.child("type").getValue().toString();
        String desc = db.child("description").getValue().toString();
        return new SessionDescription(SessionDescription.Type.fromCanonicalForm(type), desc);
    }
    //get Ice Candidate data
    private IceCandidate getIceCandidate(DataSnapshot db) {
        String sdp = db.child("sdp").getValue(String.class);
        int sdpMLineIndex = db.child("sdpMLineIndex").getValue(Integer.class);
        String sdpMid = db.child("sdpMid").getValue(String.class);
        return new IceCandidate(sdpMid, sdpMLineIndex, sdp);
    }

    //Connect to room on AppRTC
    private void connectedToRoomOnAppRTC() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<PeerConnection.IceServer> iceServerList = null;
                try {
                    iceServerList = requestTurnServers(ConstantForWebRTC.TURN_SERVER_URL);
                    SignalingParameters parameters = new SignalingParameters(
                            // Ice TURN servers are not needed for direct connections
                            iceServerList,
                            isInitiator, // Server side acts as the initiator on direct connections.
                            null,
                            null,
                            null,
                            null,
                            null
                    );
                    //connect to room
                    events.onConnectedToRoom(parameters);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setRemoteDescriptionOnAppRTC(final SessionDescription sdp) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(sdpAdded.get(sdp.type + sdp.description) == null) {
                    events.onRemoteDescription(sdp);
                    sdpAdded.put(sdp.type + sdp.description, true);
                }
            }
        });
    }

    private void addIceCandiateOnAppRTC(final IceCandidate candidate) {
        Log.d(TAG, "onRemoteIceCandidate");
        handler.post(new Runnable() {
            @Override
            public void run() {
                events.onRemoteIceCandidate(candidate);
            }
        });
    }



}
