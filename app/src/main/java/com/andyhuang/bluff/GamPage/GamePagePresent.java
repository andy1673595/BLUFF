package com.andyhuang.bluff.GamPage;

import com.andyhuang.bluff.GameInviteDialog.GameInviteContract;
import com.andyhuang.bluff.Util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamePagePresent implements GamePageContract.Presenter{
    private GamePageContract.View gamePgaeView;
    private Map<String,String> playerStateMap = new HashMap<>();
    private List<String> playerOrderList = new ArrayList<>();
    private int playerTotal = 0;
    private String roomID;
    private boolean isHost;
    private String buttonType;
    private GameFirebaseHelper firebaseHelper;

    @Override
    public void start() {

    }

    public GamePagePresent(GamePageContract.View gamePgaeViewInput) {
        gamePgaeView = gamePgaeViewInput;
    }

    @Override
    public void init(String roomIDInput, boolean isHostInput) {
        roomID = roomIDInput;
        isHost = isHostInput;
        firebaseHelper = new GameFirebaseHelper(roomID,gamePgaeView);
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

    }

    @Override
    public void catchPlayer() {

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
                break;
            case "cancel":
                //cancel Ready
                break;
        }
    }
}
