package com.andyhuang.bluff.GamPage.Listener;

import com.andyhuang.bluff.Callback.EndGameListenerCallback;
import com.andyhuang.bluff.GamPage.GameHelper.GameEndInformation;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class EndGameListener implements ValueEventListener {
    private GameEndInformation mGameEndInformation;
    private EndGameListenerCallback callback;
    public EndGameListener(EndGameListenerCallback callbackInput) {
        callback = callbackInput;
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mGameEndInformation = dataSnapshot.getValue(GameEndInformation.class);
        callback.completedLoadInfo(mGameEndInformation);
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
