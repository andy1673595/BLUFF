package com.andyhuang.bluff.GamPage.Listener;

import com.andyhuang.bluff.GamPage.GameHelper.GameFirebaseHelper;
import com.andyhuang.bluff.GamPage.GameObject.Dice;
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
            case "wait host":
                gamePageView.freshTextInfo("等候房主開始");
                break;
            case "read init data":
                firebaseHelper.playerGetRoomData();
                break;
            case "wait ready":
                gamePageView.freshTextInfo("等候所有人Ready");
                gamePageView.freshStateButtonUI(Constants.BUTTON_READY);
                mPresenter.setButtonType(Constants.BUTTON_READY);
                break;
            case "get new dice":
                gamePageView.freshTextInfo("以下是你本局的骰子");
                dice.getNewDice();
                gameRef.child(Constants.DICE_LIST).child(myUID).setValue(dice.getList());
                gamePageView.freshDiceUI(dice.getList());
                firebaseHelper.setCurrentState(Constants.COMPLETED_NEW_DICE);
                break;
            case "get initial game data":
                firebaseHelper.getInitailGameData();
                break;
            case "playing":
                mPresenter.setIsplaying(true);
                mPresenter.setButtonType(Constants.BUTTON_PLAYING);
                break;
            case "load end information":
                firebaseHelper.loadGameEndInfromation();
                break;
            case "exit game":
                gamePageView.showOtherGamerLeaveDialog();
                break;
        }

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
