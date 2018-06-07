package com.andyhuang.bluff.GamPage.Listener;

import com.andyhuang.bluff.GamPage.GameHelper.GameFirebaseHelper;
import com.andyhuang.bluff.GamPage.GamePageContract;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.GamPage.GameObject.CurrentInformation;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class CurrentPlayerInformationListener implements ValueEventListener {
    String myUID = UserManager.getInstance().getUserUID();
    boolean firstTurn = true;
    CurrentInformation mCurrentInformation;
    GameFirebaseHelper mGameFirebaseHelper;
    GamePageContract.View mGamepageView;

    public CurrentPlayerInformationListener(GameFirebaseHelper helper,GamePageContract.View viewInput) {
        mGameFirebaseHelper = helper;
        mGamepageView = viewInput;
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mCurrentInformation = dataSnapshot.getValue(CurrentInformation.class);
        if( mCurrentInformation!= null ) {
            mGameFirebaseHelper.setCurrentInformation(mCurrentInformation);
            //fresh who increase what dice and how many it is
            if(mCurrentInformation.getRecentDiceType()!= 0) {
                mGamepageView.freshRecentDiceUI(mCurrentInformation);
                firstTurn =false;
            } else {
                firstTurn =true;
            }

            if(myUID.equals(mCurrentInformation.currentPlayer)) {
                //I'm currentPlayer ,I can increase the dice
                if(firstTurn) mGamepageView.freshCatchAndIncreaseUI(true,false);
                else mGamepageView.freshCatchAndIncreaseUI(true,true);
            } else if(myUID.equals(mCurrentInformation.recentPlayer)){
                //I'm recent player ,I can't catch myself
                mGamepageView.freshCatchAndIncreaseUI(false,false);
            } else {
                //I'm not the currentPlayer, I only can catch recent person
                if(firstTurn) mGamepageView.freshCatchAndIncreaseUI(false,false);
                else mGamepageView.freshCatchAndIncreaseUI(false,true);
            }
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
