package com.andyhuang.bluff.GamPage;
import com.andyhuang.bluff.GamPage.GameHelper.CheckWhoWin;
import com.andyhuang.bluff.GamPage.GameHelper.GameEndInformation;
import com.andyhuang.bluff.GamPage.IncreaseDiceDialog.IncreaseDiceDialog;
import com.andyhuang.bluff.Util.Constants;
import com.andyhuang.bluff.activities.GamePage;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamePagePresenter implements GamePageContract.Presenter{
    private GamePageContract.View gamePgaeView;
    private Map<String,String> playerStateMap = new HashMap<>();
    private String roomID;
    private boolean isHost;
    private String buttonType;
    private boolean isplaying = false;
    private GameFirebaseHelper firebaseHelper;
    private IncreaseDiceDialog mDialog;
    private boolean hasTellOne = false;

    @Override
    public void start() {

    }

    public GamePagePresenter(GamePageContract.View gamePgaeViewInput) {
        gamePgaeView = gamePgaeViewInput;
    }

    @Override
    public void init(String roomIDInput, boolean isHostInput) {
        roomID = roomIDInput;
        isHost = isHostInput;
        firebaseHelper = new GameFirebaseHelper(roomID,gamePgaeView,isHost,this);
        //set Button UI
        if(isHost) {
            buttonType = Constants.BUTTON_START;
            gamePgaeView.freshStateButtonUI(Constants.BUTTON_START);
            //host set game state to wait for host start
            firebaseHelper.setGameState(Constants.WAIT_HOST);
        } else {
            //invitee wait for host click start
            buttonType = "wait host start";
            gamePgaeView.freshStateButtonUI(Constants.BUTTON_READY);
        }

        firebaseHelper.listenGameState();
    }

    @Override
    public void newRandomDice() {

    }

    @Override
    public void listenPlayerCurrentState() {

    }

    @Override
    public void updateMyDiceState() {

    }

    @Override
    public void loadInitialGameData() {

    }

    @Override
    public void readCurrentData() {

    }

    @Override
    public void increaseDice() {
        if(isplaying) {
            mDialog = new IncreaseDiceDialog((GamePage)gamePgaeView,firebaseHelper);
            mDialog.show();
        }
    }

    @Override
    public void catchPlayer() {
        CheckWhoWin checkWhoWin = new CheckWhoWin(firebaseHelper.getDiceTotal(),hasTellOne, firebaseHelper.getGamerList());
        GameEndInformation gameEndInformation =checkWhoWin
                .getGameEndInformation(firebaseHelper.getCurrentInformation());
        firebaseHelper.updateGameEndInfromation(gameEndInformation);
    }

    @Override
    public void clickStateButton() {
        switch (buttonType) {
            case "start":
                //I'm host , get player list and update to server, everyone need to load it
                firebaseHelper.getRoomData();
                break;
            case "ready":
                //get ready
                buttonType = Constants.BUTTON_GET_READY;
                gamePgaeView.freshStateButtonUI(Constants.BUTTON_GET_READY);
                firebaseHelper.setCurrentState(Constants.GET_READY);
                break;
            case "get ready":
                //cancel Ready
                buttonType = Constants.BUTTON_READY;
                gamePgaeView.freshStateButtonUI(Constants.BUTTON_READY);
                firebaseHelper.setCurrentState(Constants.CANCEL_READY);
                break;
        }
    }

    public void setButtonType(String type) {
        buttonType = type;
    }
    public void setIsplaying(boolean isplayingInput) {isplaying = isplayingInput;}
    public void sethasTellOne(boolean hasTellOneInput) {hasTellOne = hasTellOneInput;}
}
