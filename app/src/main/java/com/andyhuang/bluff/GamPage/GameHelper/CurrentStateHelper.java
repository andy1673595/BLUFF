package com.andyhuang.bluff.GamPage.GameHelper;

import com.andyhuang.bluff.GamPage.GameFirebaseHelper;
import com.andyhuang.bluff.Util.Constants;

public class CurrentStateHelper {
    private int countForCompletedRead=0;
    private int countForGetReady = 0;
    GameFirebaseHelper firebaseHelper;
    int playerTotal;
    public CurrentStateHelper(GameFirebaseHelper firebaseHelperInput,int playerTotalInput) {
        firebaseHelper = firebaseHelperInput;
        playerTotal = playerTotalInput;
    }
    public void dealCurrentState(String state) {
        switch (state) {
            case "completed read init":
                countForCompletedRead++;
                //all player read Room data
                if(countForCompletedRead == playerTotal) {
                    firebaseHelper.setGameState(Constants.WAIT_READY);

                }
                break;
            case "get ready" :
                countForGetReady ++;
                //all player get ready, get new dice set
                if(countForGetReady == playerTotal) {
                    firebaseHelper.setGameState(Constants.NEW_DICE);
                }
                break;
            case "cancel ready":
                if(countForGetReady < 0) countForGetReady=0;
                countForGetReady--;
                break;
        }
    }
}
