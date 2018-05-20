package com.andyhuang.bluff.Profile.Listener;

import com.andyhuang.bluff.Callback.GameResultCallback;
import com.andyhuang.bluff.Object.GameResult;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class GameResultListener implements ValueEventListener {
    GameResultCallback mCallback;
    public GameResultListener(GameResultCallback callback) {
        mCallback = callback;
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()) {
            GameResult gameResult = dataSnapshot.getValue(GameResult.class);
            mCallback.getGameResultData(gameResult);
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
