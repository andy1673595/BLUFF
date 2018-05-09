package com.andyhuang.bluff.GamPage.Listener;

import com.andyhuang.bluff.GamPage.GameFirebaseHelper;
import com.andyhuang.bluff.GamPage.GameHelper.Dice;
import com.andyhuang.bluff.GamPage.GamePageContract;
import com.andyhuang.bluff.GamPage.GamePagePresenter;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Util.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class GameStateListener implements ValueEventListener {
    private GameFirebaseHelper firebaseHelper;
    private Dice dice;
    private String gameState;
    private GamePagePresenter mPresenter;
    private GamePageContract.View gamePageView;
    private String myUID = UserManager.getInstance().getUserUID();
    private Firebase gameRef;
    public GameStateListener(GameFirebaseHelper firebaseHelperInput,Dice diceInput,Firebase gameRefInput,
                             GamePagePresenter mPresenterInput,GamePageContract.View gamePageViewInput) {
        firebaseHelper = firebaseHelperInput;
        dice = diceInput;
        mPresenter = mPresenterInput;
        gamePageView = gamePageViewInput;
        gameRef = gameRefInput;
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        gameState = (String)dataSnapshot.getValue();
        firebaseHelper.setGameStateInHelper(gameState);
        switch (gameState) {
            case "read init data":
                firebaseHelper.playerGetRoomData();
                break;
            case "wait ready":
                gamePageView.freshStateButtonUI(Constants.BUTTON_READY);
                mPresenter.setButtonType(Constants.BUTTON_READY);
                break;
            case "get new dice":
                dice.getNewDice();
                gameRef.child(Constants.DICE_LIST).child(myUID).setValue(dice.getList());
                firebaseHelper.setCurrentState(Constants.COMPLETED_NEW_DICE);
                break;
            case "load dice list":

                break;
        }

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
