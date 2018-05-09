package com.andyhuang.bluff.GamPage.GameHelper;

import com.andyhuang.bluff.GamPage.GameFirebaseHelper;
import com.andyhuang.bluff.Util.Constants;

public class CurrentStateHelper {
    private int countForCompletedRead=0;
    private int countForGetReady = 0;
    private int countForNewDice = 0;
    private int countForStartPlaying = 0;
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
                countForGetReady--;
                if(countForGetReady < 0) countForGetReady=0;
                break;
            case "completed new dice":
                countForNewDice++;
                if(countForNewDice == playerTotal) {
                    //all player completed new dice, host caculate dice list
                    firebaseHelper.hostGetEachDiceList();
                }
                break;
            case "ready for playing":
                countForStartPlaying++;
                if(countForStartPlaying==playerTotal) {
                    //all player ready to play game
                    firebaseHelper.setGameState(Constants.PLAYING);
                }
                break;
        }
    }

    public void resetCount() {
        countForCompletedRead=0;
        countForGetReady = 0;
        countForNewDice = 0;
        countForStartPlaying = 0;
    }
}
