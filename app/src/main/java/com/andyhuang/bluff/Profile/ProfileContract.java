package com.andyhuang.bluff.Profile;

import android.net.Uri;
import com.andyhuang.bluff.BasePresenter;
import com.andyhuang.bluff.BaseView;

public interface ProfileContract {
    interface Presenter extends BasePresenter {
        void loadUserData();
        void updateComment(String comment);
        void changeUserPhoto(Uri newImageUri);
    }
    interface View extends BaseView{
        void setUserDataToUI(String userName,String userEmail,String photoUrl,String Comment);
        void freshComment(String comment);
        void setGameResultToUI(String totalTimes,String timesForTwoPersonGame,String winRate);
        void setButtonInvisible();
    }
}
