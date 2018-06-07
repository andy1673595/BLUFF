package com.andyhuang.bluff.GamPage;

import com.andyhuang.bluff.BasePresenter;
import com.andyhuang.bluff.BaseView;
import com.andyhuang.bluff.GamPage.GameObject.CurrentInformation;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.webRTC.WebRTC;

import java.util.ArrayList;
import java.util.List;

public interface GamePageContract {
    interface View extends BaseView {
        //fresh UI
        void freshStateButtonUI(String buttonType);
        void freshDiceUI(List<Integer> diceList);
        void freshCatchAndIncreaseUI(boolean increaseVisible, boolean catchVisible);
        void freshRecentDiceUI(CurrentInformation currentInformation);
        void freshSwitchUI(boolean shouldOpen);
        void freshTextInfo(String message);
        void freshPlayerHaveJoinedText(ArrayList<Gamer> joinedList, Gamer newGamer);
        void freshTotalPlayerUI(int count);
        void showEndInformation(String endText);
        void resetView(boolean isNextPlayer);
        //show dialog
        void showOtherGamerLeaveDialog();
        //video methods
        void showVideo();
        void closeVideo();
        void setVideoElement(boolean show);
        void releaseSurfaceView();
        void creatVideoRenders();
    }
    interface Presenter extends BasePresenter {
        void init(String RoomID,boolean isHost);
        void reset();
        void increaseDice();
        void catchPlayer();
        void clickStateButton();
        void tellServerNotInGame();
        void initMultipleData();
        void updatePlayerHaveJoinedList(ArrayList<Gamer> joinedList,Gamer newGamer);
        void loadPlayerInvitedTotal();
        void updatePlayInvitedCountToUI(int count);
        void updatePlayInvitedCountToFirebase(int count);
        //WebRTC video
        void touchVideoSwitch();
        void disconnectVideo();
        void startVideo();
        void initVideoData();
        boolean getIceConnectedInWebRTC();


    }
}
