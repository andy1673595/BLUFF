package com.andyhuang.bluff.GamPage.Listener;

import com.andyhuang.bluff.GamPage.GameFirebaseHelper;
import com.andyhuang.bluff.GamPage.GamePageContract;
import com.andyhuang.bluff.GamPage.GamePagePresenter;
import com.andyhuang.bluff.helper.CurrentInformation;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class CurrentPlayerInformationListener implements ValueEventListener {
    CurrentInformation mCurrentInformation;
    GameFirebaseHelper mGameFirebaseHelper;
    GamePageContract.View gamepageView;
    public CurrentPlayerInformationListener(GameFirebaseHelper helper,GamePageContract.View viewInput) {
        mGameFirebaseHelper = helper;
        gamepageView = viewInput;
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mCurrentInformation = dataSnapshot.getValue(CurrentInformation.class);
        if( mCurrentInformation!= null ) {
            mGameFirebaseHelper.setCurrentInformation(mCurrentInformation);
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
