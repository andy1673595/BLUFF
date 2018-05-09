package com.andyhuang.bluff.GamPage.Listener;

import com.andyhuang.bluff.GamPage.GameHelper.CurrentStateHelper;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

public class PlayerCurrentStateListener implements ChildEventListener {
    CurrentStateHelper mCurrentStateHelper;
    public PlayerCurrentStateListener(CurrentStateHelper helper) {
        mCurrentStateHelper = helper;
    }
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String state = (String)dataSnapshot.getValue();
        mCurrentStateHelper.dealCurrentState(state);
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
