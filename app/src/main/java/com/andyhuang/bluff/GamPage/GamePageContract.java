package com.andyhuang.bluff.GamPage;

import com.andyhuang.bluff.BasePresenter;
import com.andyhuang.bluff.BaseView;
import com.andyhuang.bluff.helper.CurrentInformation;

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
    }
    interface Presenter extends BasePresenter {
        void init(String RoomID,boolean isHost);
        void reset();
        void increaseDice();
        void catchPlayer();
        void clickStateButton();
    }
}
