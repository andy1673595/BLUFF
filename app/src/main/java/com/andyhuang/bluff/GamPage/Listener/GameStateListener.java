package com.andyhuang.bluff.GamPage.Listener;

import com.andyhuang.bluff.GamPage.GameHelper.GameFirebaseHelper;
import com.andyhuang.bluff.GamPage.GameObject.Dice;
import com.andyhuang.bluff.GamPage.GamePageContract;
import com.andyhuang.bluff.GamPage.GamePagePresenter;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Constant.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class GameStateListener implements ValueEventListener {
    private String myUID = UserManager.getInstance().getUserUID();
    private String gameState;
    private Dice mDice;
    private GameFirebaseHelper mFirebaseHelper;
    private GamePagePresenter mPresenter;
    private GamePageContract.View mGamePageView;

    private Firebase gameRef;
    public GameStateListener(GameFirebaseHelper firebaseHelperInput,Dice diceInput,Firebase gameRefInput,
                             GamePagePresenter mPresenterInput,GamePageContract.View gamePageViewInput) {
        mFirebaseHelper = firebaseHelperInput;
        mDice = diceInput;
        mPresenter = mPresenterInput;
        mGamePageView = gamePageViewInput;
        gameRef = gameRefInput;
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        gameState = (String)dataSnapshot.getValue();
        mFirebaseHelper.setGameStateInHelper(gameState);
        switch (gameState) {
            case "wait host":
                mGamePageView.freshTextInfo("等候房主開始");
                break;
            case "read init data":
                mFirebaseHelper.playerGetRoomData();
                break;
            case "wait ready":
                mGamePageView.freshTextInfo("等候所有人Ready");
                mGamePageView.freshStateButtonUI(Constants.BUTTON_READY);
                mPresenter.setButtonType(Constants.BUTTON_READY);
                break;
            case "get new dice":
                mGamePageView.freshTextInfo("以下是你本局的骰子");
                mDice.getNewDice();
                gameRef.child(Constants.DICE_LIST).child(myUID).setValue(mDice.getList());
                mGamePageView.freshDiceUI(mDice.getList());
                mFirebaseHelper.setCurrentState(Constants.COMPLETED_NEW_DICE);
                break;
            case "get initial game data":
                mFirebaseHelper.getInitialGameData();
                break;
            case "playing":
                mPresenter.setIsplaying(true);
                mPresenter.setButtonType(Constants.BUTTON_PLAYING);
                break;
            case "load end information":
                mFirebaseHelper.loadGameEndInfromation();
                break;
            case "exit game":
                mGamePageView.showOtherGamerLeaveDialog();
                break;
        }

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
