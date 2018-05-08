package com.andyhuang.bluff.GamPage;

import com.andyhuang.bluff.BasePresenter;
import com.andyhuang.bluff.BaseView;

import java.util.ArrayList;

public interface GamePageContract {
    interface View extends BaseView {
        void freshStateButtonUI(String buttonType);
        void freshDiceUI(ArrayList<Integer> diceList);
        void freshRecentDiceUI(int diceType , int number);
        void showEndInformation(String endText);
    }
    interface Presenter extends BasePresenter {
        void init(String RoomID,boolean isHost);
        void readRoomData();
        void newRandomDice();
        void listenPlayerCurrentState();
        void updateMyDiceState();
        void loadInitialGameData();
        void readCurrentData();
        void increaseDice();
        void catchPlayer();
        void clickStateButton();
    }
}
