package com.andyhuang.bluff.GamPage.IncreaseDiceDialog;

import android.app.Dialog;

import com.andyhuang.bluff.GamPage.GameHelper.GameFirebaseHelper;
import com.andyhuang.bluff.Object.Gamer;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.helper.CurrentInformation;

import java.util.List;

public class IncreasrDiceDialogPresenter implements IncreaseDiceDialogContract.Presenter {
    private IncreaseDiceDialogContract.View dialogView;
    private int diceType = 1;
    private int diceCount = 1;
    private CurrentInformation mCurrentInformation;
    GameFirebaseHelper mGameFirebaseHelper;
    public IncreasrDiceDialogPresenter(IncreaseDiceDialogContract.View dialogViewInput,
                                       GameFirebaseHelper helper) {
        dialogView = dialogViewInput;
        mCurrentInformation = helper.getCurrentInformation();
        mGameFirebaseHelper = helper;
    }

    @Override
    public void chooseDiceNumber() {
        //diceType 1~6
        diceType++;
        diceType = (diceType>6)?1:diceType;
        //Array form 0~5
        dialogView.showDiceNumber(diceType-1);
    }

    @Override
    public void changeNumberCount(int numberCount) {
        //dicecount 1~N
        diceCount += numberCount;
        diceCount = (diceCount<1)?1:diceCount;
        //refresh count UI
        dialogView.showNumberCountChooseDialog(" X "+diceCount);
    }

    @Override
    public void clickOKButton() {
        boolean check = isChooseLegal(diceType,diceCount);
        if(check) {
            List<Gamer> gamerList = mGameFirebaseHelper.getGamerList();
            int total = gamerList.size();
            String myUID = UserManager.getInstance().getUserUID();
            int nextPlayerNumber =0;
            for(int i=0;i<total;i++) {
               Gamer gamer = gamerList.get(i);
               String currentPlayer = gamer.getUserUID();
               if(currentPlayer.equals(myUID)) {
                   nextPlayerNumber = (i==total-1)?0:i+1;
                   break;
               }
            }
            mCurrentInformation.setRecentPlayer(myUID);
            mCurrentInformation.setRecentDiceType(diceType);
            mCurrentInformation.setRecentDiceNumber(diceCount);
            mCurrentInformation.setCurrentPlayer(gamerList.get(nextPlayerNumber).getUserUID());
            //set Next player Informationto firebase
            mGameFirebaseHelper.updateCurrentInformation(mCurrentInformation);
            //dismiss dialog
            ((Dialog)dialogView).dismiss();

        } else {
            String message = "小於"+mCurrentInformation.getRecentDiceNumber()+"個"+
                    mCurrentInformation.getRecentDiceType() +",請重選";
            dialogView.showError(message);
        }
    }

    @Override
    public boolean isChooseLegal(int diceNumber, int numberCount) {
        if(diceNumber <= mCurrentInformation.getRecentDiceType() &&
                numberCount <= mCurrentInformation.getRecentDiceNumber()) return false;
        else if(numberCount < mCurrentInformation.getRecentDiceNumber()) return false;
        else return true;
    }

    @Override
    public void start() {

    }
}
