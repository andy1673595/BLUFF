package com.andyhuang.bluff.GamPage;
import com.andyhuang.bluff.GamPage.GameHelper.CheckWhoWin;
import com.andyhuang.bluff.GamPage.GameObject.GameEndInformation;
import com.andyhuang.bluff.GamPage.GameHelper.GameFirebaseHelper;
import com.andyhuang.bluff.GamPage.IncreaseDiceDialog.IncreaseDiceDialog;
import com.andyhuang.bluff.Util.Constants;
import com.andyhuang.bluff.activities.GamePage;

public class GamePagePresenter implements GamePageContract.Presenter{
    private GamePageContract.View gamePgaeView;
    private String roomID;
    private boolean isHost;
    private String buttonType;
    private boolean isplaying = false;
    private GameFirebaseHelper firebaseHelper;
    private IncreaseDiceDialog mDialog;
    private boolean hasTellOne = false;
    private boolean isVideoSwitchOn = false;

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
            buttonType =Constants.BUTTON_WAIT;
            gamePgaeView.freshStateButtonUI(Constants.BUTTON_WAIT);
        }

        firebaseHelper.listenGameState();
        //tell other I'm gaming
        firebaseHelper.setIsGaming(true);
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

    @Override
    public void tellServerNotInGame() {
        firebaseHelper.setCurrentState(Constants.EXIT_GAME);
        firebaseHelper.setGameState(Constants.EVERYONE_EXIT_GAME);
        firebaseHelper.setIsGaming(false);
    }

    @Override
    public void initVideoData() {
        gamePgaeView.setVideoElement(true);
        gamePgaeView.showVideo();
    }

    @Override
    public void initMultipleData() {
        gamePgaeView.setVideoElement(false);

    }

    @Override
    public void startVideo() {

    }

    @Override
    public void disconnectVideo() {

    }
    //when touch Video chat swtich , it will call this method,and judge what to do
    @Override
    public void touchVideoSwitch() {
        if(isVideoSwitchOn) {
            //switch is On , close the Video
            isVideoSwitchOn = false;
            gamePgaeView.freshSwitchUI(isVideoSwitchOn);
            gamePgaeView.closeVideo();
        } else {
            //switch is Off, start to video chat
            isVideoSwitchOn = true;
            gamePgaeView.freshSwitchUI(isVideoSwitchOn);
            gamePgaeView.showVideo();
        }
    }

    //when each game is end ,reset game variables
    @Override
    public void reset() {
        hasTellOne =false;
        isplaying = false;
        buttonType = Constants.BUTTON_READY;
    }

    public void setButtonType(String type) {
        buttonType = type;
    }
    public void setIsplaying(boolean isplayingInput) {isplaying = isplayingInput;}
    public void sethasTellOne(boolean hasTellOneInput) {hasTellOne = hasTellOneInput;}
}
