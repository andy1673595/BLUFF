package com.andyhuang.bluff.GamPage.Listener;

import com.andyhuang.bluff.GamPage.GameHelper.GameFirebaseHelper;
import com.andyhuang.bluff.GamPage.GamePageContract;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.helper.CurrentInformation;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class CurrentPlayerInformationListener implements ValueEventListener {
    CurrentInformation mCurrentInformation;
    GameFirebaseHelper mGameFirebaseHelper;
    GamePageContract.View gamepageView;
    GamePageContract.Presenter gamepagePresenter;
    boolean firstTurn = true;
    String myUID = UserManager.getInstance().getUserUID();
    public CurrentPlayerInformationListener(GameFirebaseHelper helper,GamePageContract.View viewInput) {
        mGameFirebaseHelper = helper;
        gamepageView = viewInput;
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        mCurrentInformation = dataSnapshot.getValue(CurrentInformation.class);
        if( mCurrentInformation!= null ) {
            mGameFirebaseHelper.setCurrentInformation(mCurrentInformation);
            //fresh who increase what dice and how many it is
            if(mCurrentInformation.getRecentDiceType()!= 0) {
                gamepageView.freshRecentDiceUI(mCurrentInformation);
                firstTurn =false;
            } else {
                firstTurn =true;
            }

            if(myUID.equals(mCurrentInformation.currentPlayer)) {
                //I'm currentPlayer ,I can increase the dice
                if(firstTurn) gamepageView.refreshCatchAndIncreaseUI(true,false);
                else gamepageView.refreshCatchAndIncreaseUI(true,true);
            } else if(myUID.equals(mCurrentInformation.recentPlayer)){
                //I'm recent player ,I can't catch myself
                gamepageView.refreshCatchAndIncreaseUI(false,false);
            } else {
                //I'm not the currentPlayer, I only can catch recent person
                if(firstTurn) gamepageView.refreshCatchAndIncreaseUI(false,false);
                else gamepageView.refreshCatchAndIncreaseUI(false,true);
            }
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
