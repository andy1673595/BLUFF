package com.andyhuang.bluff.FriendPage;

import com.andyhuang.bluff.BasePresenter;
import com.andyhuang.bluff.BaseView;

public interface FriendContract {
    interface View extends BaseView {
        void freshFriendList();
        void setAdapter();
        void showGamePage();
    }
    interface Presenter extends BasePresenter {
        void inviteFriend();
        void inviteGame();
        void startGame();
        void acceptInvite();
        void readFriendDataFromFireBase();
        void updateFriendDataToFireBase();
        void inviteFriend(String email);
    }
}
