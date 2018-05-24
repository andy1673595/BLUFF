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
        void freshStateButtonUI(String buttonType);
        void freshDiceUI(List<Integer> diceList);
        void refreshCatchAndIncreaseUI(boolean increaseVisible,boolean catchVisible);
        void freshRecentDiceUI(CurrentInformation currentInformation);
        void showEndInformation(String endText);
        void resetView(boolean isNextPlayer);
        void showOtherGamerLeaveDialog();
        void showTwoPlayerLayout();
        void showMultiplePlayerLayout();
        void showVideo();
        void closeVideo();
        void setVideoElement(boolean show);
        void freshSwitchUI(boolean shouldOpen);
        void releaseSurfaceView();
        void creatVideoRenders();
        void updatePlayerHaveJoinedText(ArrayList<Gamer> joinedList,Gamer newGamer);
    }
    interface Presenter extends BasePresenter {
        void init(String RoomID,boolean isHost);
        void reset();
        void increaseDice();
        void catchPlayer();
        void clickStateButton();
        void tellServerNotInGame();
        void initVideoData();
        void initMultipleData();
        void startVideo();
        void disconnectVideo();
        void touchVideoSwitch();
        boolean getIceConnectedInWebRTC();
        void updatePlayerHaveJoinedList(ArrayList<Gamer> joinedList,Gamer newGamer);
    }
}
