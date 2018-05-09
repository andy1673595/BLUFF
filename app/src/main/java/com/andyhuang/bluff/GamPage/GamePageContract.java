package com.andyhuang.bluff.GamPage;

import com.andyhuang.bluff.BasePresenter;
import com.andyhuang.bluff.BaseView;

import java.util.ArrayList;
import java.util.List;

public interface GamePageContract {
    interface View extends BaseView {
        void freshStateButtonUI(String buttonType);
        void freshDiceUI(List<Integer> diceList);
        void setCurrentPlayerUI();
        void setOtherPlayerUI();
        void setRecentPlayerUI();
        void freshRecentDiceUI(int diceType , int number);
        void showEndInformation(String endText);
    }
    interface Presenter extends BasePresenter {
        void init(String RoomID,boolean isHost);
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
