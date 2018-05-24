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
        void updateItemInvite(int positon,boolean isInvite);
        void showGamePage(String GameRoomID,int playerInvitedTotal);
        void showErrorDialog(String message);
        void showFriendProfile(String friendUID);
    }
    interface Presenter extends BasePresenter {
        void inviteGame(FriendInformation friend);
        void removeInvite(FriendInformation friend);
        void acceptInvite(int position);
        void refuseInvite(int position);
        void readFriendDataFromFireBase();
        void inviteFriend(String email);
        void addFriend(FriendInformation friendInformation);
    }
}
