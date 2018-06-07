package com.andyhuang.bluff.GamPage;

import com.andyhuang.bluff.GamPage.GameHelper.CheckWhoWin;
import com.andyhuang.bluff.GamPage.GameObject.GameEndInformation;
import com.andyhuang.bluff.GamPage.GameHelper.GameFirebaseHelper;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.GamPage.IncreaseDiceDialog.IncreaseDiceDialog;
import com.andyhuang.bluff.Constant.Constants;
import com.andyhuang.bluff.activities.GamePage;
import com.andyhuang.bluff.webRTC.WebRTC;

import java.util.ArrayList;

public class GamePagePresenter implements GamePageContract.Presenter{
    private String roomID;
    private String buttonType;
    private boolean isHost;
    private boolean isplaying = false;
    private boolean hasTellOne = false;
    private boolean isVideoSwitchOn = false;
    private IncreaseDiceDialog mDialog;
    private ArrayList<Gamer> playerHaveJoinedList = new ArrayList<>();
    private GamePage mGamePageView;
    private GameFirebaseHelper firebaseHelper;
    private WebRTC mWebRTC;

    @Override
    public void start() {

    }

    public GamePagePresenter(GamePage gamePgaeViewInput) {
        mGamePageView = gamePgaeViewInput;
    }

    @Override
    public void init(String roomIDInput, boolean isHostInput) {
        roomID = roomIDInput;
        isHost = isHostInput;
        firebaseHelper = new GameFirebaseHelper(roomID, mGamePageView,isHost,this);
        //set Button UI
        setButtonUI();
        firebaseHelper.listenGameState();
        //tell other I'm gaming
        firebaseHelper.setIsGaming(true);
        firebaseHelper.listenToPlayerJoinedEvent();
    }


    @Override
    public void increaseDice() {
        if(isplaying) {
            mDialog = new IncreaseDiceDialog((GamePage) mGamePageView,firebaseHelper);
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
                mGamePageView.freshStateButtonUI(Constants.BUTTON_GET_READY);
                firebaseHelper.setCurrentState(Constants.GET_READY);
                break;
            case "get ready":
                //cancel Ready
                buttonType = Constants.BUTTON_READY;
                mGamePageView.freshStateButtonUI(Constants.BUTTON_READY);
                firebaseHelper.setCurrentState(Constants.CANCEL_READY);
                break;
        }
    }
    //leave the game room , tell server I'm not in game room and remove the listener of firebase
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
        mGamePageView.setVideoElement(true);
    }

    @Override
    public void initMultipleData() {
        mGamePageView.setVideoElement(false);

    }

    @Override
    public void startVideo() {
        mGamePageView.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGamePageView.creatVideoRenders();
            }
        });

        mWebRTC = new WebRTC(this,(GamePage) mGamePageView,roomID);
        mWebRTC.startCall();
    }
    //when click video icon layout or leave game , disconnect video
    @Override
    public void disconnectVideo() {
        isVideoSwitchOn = false;
        mWebRTC.disconnectReset();
        mWebRTC =null;
        mGamePageView.freshSwitchUI(isVideoSwitchOn);
        mGamePageView.closeVideo();
    }
    //when touch Video chat switch , it will call this method,and judge what to do
    @Override
    public void touchVideoSwitch() {
        if(isVideoSwitchOn) {
            //switch is On , close the Video
            isVideoSwitchOn = false;
            mGamePageView.freshSwitchUI(isVideoSwitchOn);
            disconnectVideo();
            mGamePageView.closeVideo();
        } else {
            //switch is Off, start to video chat
            isVideoSwitchOn = true;
            mGamePageView.freshSwitchUI(isVideoSwitchOn);
            mGamePageView.showVideo();
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
        mGamePageView.freshPlayerHaveJoinedText(joinedList,newGamer);
    }

    private void setButtonUI() {
        if(isHost) {
            buttonType = Constants.BUTTON_START;
            mGamePageView.freshStateButtonUI(Constants.BUTTON_START);
            //host set game state to wait for host start
            firebaseHelper.setGameState(Constants.WAIT_HOST);
        } else {
            //invitee wait for host click start
            buttonType =Constants.BUTTON_WAIT;
            mGamePageView.freshStateButtonUI(Constants.BUTTON_WAIT);
        }
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

    @Override
    public void updatePlayInvitedCountToUI(int count) {
        mGamePageView.freshTotalPlayerUI(count);
    }

    public void setButtonType(String type) {
        buttonType = type;
    }
    public void setIsplaying(boolean isplayingInput) {isplaying = isplayingInput;}
    public void sethasTellOne(boolean hasTellOneInput) {hasTellOne = hasTellOneInput;}
    public void loadPlayerInvitedTotal() {firebaseHelper.loadPlayerInvitedTotal(); }
    public void updatePlayInvitedCountToFirebase(int count) {firebaseHelper.updatePlayInvitedCount(count);}
}
