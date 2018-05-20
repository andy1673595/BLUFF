package com.andyhuang.bluff.Profile;

import com.andyhuang.bluff.BasePresenter;
import com.andyhuang.bluff.BaseView;
import com.andyhuang.bluff.Object.GameResult;

public interface ProfileContract {
    interface Presenter extends BasePresenter {
        void loadUserData();
        void updateComment(String comment);
    }
    interface View extends BaseView{
        void setUserDataToUI(String userName,String userEmail,String photoUrl,String Comment);
        void freshComment(String comment);
        void setGameResultToUI(String totalTimes,String timesForTwoPersonGame,String winRate);
        void setButtonInvisible();
    }
}
