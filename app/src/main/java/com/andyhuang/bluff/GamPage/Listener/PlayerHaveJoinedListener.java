package com.andyhuang.bluff.GamPage.Listener;

import com.andyhuang.bluff.Callback.PlayerJoinedListenerCallback;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

public class PlayerHaveJoinedListener implements ChildEventListener {
    private PlayerJoinedListenerCallback mCallback;
    private ArrayList<Gamer> playerJoinedList = new ArrayList<>();
    public PlayerHaveJoinedListener(PlayerJoinedListenerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if(dataSnapshot.exists()) {
            Gamer gamerJoined = new Gamer(null,null,null);

            for (DataSnapshot dataChild: dataSnapshot.getChildren()) {
                //read every gamer have joined
                switch (dataChild.getKey()) {
                    case "userEmail":
                        gamerJoined.setUserEmail(dataChild.getValue(String.class));
                        break;
                    case "userName":
                        gamerJoined.setUserName(dataChild.getValue(String.class));
                        break;
                    case "userPhotoURL" :
                        gamerJoined.setUserPhotoURL(dataChild.getValue(String.class));
                        break;
                    case "userUID":
                        gamerJoined.setUserUID(dataChild.getValue(String.class));
                        break;
                }
            }
            //add him/her to list , then fresh the UI
            playerJoinedList.add(gamerJoined);
            mCallback.freshShowPlayerCountUI(playerJoinedList,gamerJoined);
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
