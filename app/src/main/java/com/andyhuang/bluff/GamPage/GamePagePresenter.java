package com.andyhuang.bluff.GamPage;
import android.util.Log;

import com.andyhuang.bluff.GamPage.GameHelper.CheckWhoWin;
import com.andyhuang.bluff.GamPage.GameObject.GameEndInformation;
import com.andyhuang.bluff.GamPage.GameHelper.GameFirebaseHelper;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.GamPage.IncreaseDiceDialog.IncreaseDiceDialog;
import com.andyhuang.bluff.Util.Constants;
import com.andyhuang.bluff.activities.GamePage;
import com.andyhuang.bluff.webRTC.WebRTC;

import java.util.ArrayList;
import java.util.List;

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
    private WebRTC mWebRTC;
    private ArrayList<Gamer> playerHaveJoinedList = new ArrayList<>();

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
        firebaseHelper.listenToPlayerJoinedEvent();
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
        //remove all listener
        firebaseHelper.removeListener();
    }

    @Override
    public void initVideoData() {
        gamePgaeView.setVideoElement(true);
    }

    @Override
    public void initMultipleData() {
        gamePgaeView.setVideoElement(false);

    }

    @Override
    public void startVideo() {
        gamePgaeView.creatVideoRenders();
        mWebRTC = new WebRTC(this,(GamePage) gamePgaeView,roomID);
        mWebRTC.startCall();
    }

    @Override
    public void disconnectVideo() {
        Log.d("errorTEST","presenter.disconnect");
        isVideoSwitchOn = false;
        mWebRTC.disconnectReset();
        mWebRTC =null;
        Log.d("errorTEST","webRTC = null");
        gamePgaeView.freshSwitchUI(isVideoSwitchOn);
        gamePgaeView.closeVideo();
        Log.d("errorTEST","close video");
    }
    //when touch Video chat swtich , it will call this method,and judge what to do
    @Override
    public void touchVideoSwitch() {
        if(isVideoSwitchOn) {
            //switch is On , close the Video
            isVideoSwitchOn = false;
            gamePgaeView.freshSwitchUI(isVideoSwitchOn);
            disconnectVideo();
            gamePgaeView.closeVideo();
        } else {
            //switch is Off, start to video chat
            isVideoSwitchOn = true;
            gamePgaeView.freshSwitchUI(isVideoSwitchOn);
            gamePgaeView.showVideo();
        }
    }

    @Override
    public boolean getIceConnectedInWebRTC() {
        if(mWebRTC == null) {
            return false;
        }else {
            return mWebRTC.getIceConnected();
        }
    }


    @Override
    public void updatePlayerHaveJoinedList(ArrayList<Gamer> joinedList,Gamer newGamer) {
        playerHaveJoinedList = joinedList;
        gamePgaeView.updatePlayerHaveJoinedText(joinedList,newGamer);
    }

    public ArrayList<String> getPlayerJoinedNameList() {
        ArrayList<String> plyerList = new ArrayList<>();
        for(Gamer gamer:playerHaveJoinedList) {
            plyerList.add(gamer.getUserName());
        }
        return plyerList;
    }

    public ArrayList<String> getPlayerJoinedPhotoURLList() {
        ArrayList<String> plyerList = new ArrayList<>();
        for(Gamer gamer:playerHaveJoinedList) {
            plyerList.add(gamer.getUserPhotoURL());
        }
        return plyerList;
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

    public void loadPlayerInvitedTotal() {
        firebaseHelper.loadPlayerInvitedTotal();
    }

    @Override
    public void updatePlayInvitedCountToUI(int count) {
        gamePgaeView.inviteeSetTotalPlayerInivted(count);
    }

    public void updatePlayInvitedCountToFirebase(int count) {
        firebaseHelper.updatePlayInvitedCount(count);
    }
}
