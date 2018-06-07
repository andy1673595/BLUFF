package com.andyhuang.bluff.GamPage.Listener;

import com.andyhuang.bluff.GamPage.GamePagePresenter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class LoadPlayerTotalInvitedListener implements ValueEventListener {
    GamePagePresenter mPresenter;
    public LoadPlayerTotalInvitedListener(GamePagePresenter presenter) {
        mPresenter = presenter;
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()) {
            mPresenter.updatePlayInvitedCountToUI((int)((long)dataSnapshot.getValue()));
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
