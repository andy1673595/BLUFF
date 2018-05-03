package com.andyhuang.bluff.FriendPage;

import com.andyhuang.bluff.BasePresenter;
import com.andyhuang.bluff.BaseView;
import com.andyhuang.bluff.Object.FriendInformation;

import java.util.ArrayList;

public interface FriendContract {
    interface View extends BaseView {
        void freshFriendList();
        void setAdapter(ArrayList<FriendInformation> listInput);
        void addItem(FriendInformation friendInformation);
        void removeItem(int positon);
        void showGamePage();
    }
    interface Presenter extends BasePresenter {
        void inviteFriend();
        void inviteGame();
        void startGame();
        void acceptInvite(int position,String inviterEmail);
        void refuseInvite(int position);
        void readFriendDataFromFireBase();
        void updateFriendDataToFireBase();
        void inviteFriend(String email);
    }
}
