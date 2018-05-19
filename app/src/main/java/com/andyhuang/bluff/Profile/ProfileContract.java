package com.andyhuang.bluff.Profile;

import com.andyhuang.bluff.BasePresenter;
import com.andyhuang.bluff.BaseView;

public interface ProfileContract {
    interface Presenter extends BasePresenter {
        void loadUserData();
        void updateComment();
    }
    interface View extends BaseView{
        void setUserDataToUI(String userName,String userEmail,String photoUrl,String Comment);
        void freshComment(String comment);
    }
}
