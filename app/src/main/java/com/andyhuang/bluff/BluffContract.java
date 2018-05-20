package com.andyhuang.bluff;

public interface BluffContract {
    interface View extends BaseView {
        void showGamePage(String gameRoomID);

    }
    interface Presenter extends BasePresenter {
        void transToMainPage();
        void transToFriendPage();
        void transToProfilePage();
        void transToFriendProfile(String UID);
        void removeFriendProfileFragment();
        void showGameInviteDialog();
        void removeGameInvite();
        void setGameInformationAndGetIntoRoom(String RoomID);
        void setDisconnectWhenGetOutline();
    }
}
