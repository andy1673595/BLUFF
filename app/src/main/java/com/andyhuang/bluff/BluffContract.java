package com.andyhuang.bluff;

public interface BluffContract {
    interface View extends BaseView {
        void showGamePage(String gameRoomID,int gamerInvitedTotal,boolean isHost);
        void showErrorInviteDialogFromRandom(String message);
    }
    interface Presenter extends BasePresenter {
        void transToMainPage();
        void transToFriendPage();
        void transToProfilePage();
        void transToFriendProfile(String UID);
        void removeFriendProfileFragment();
        void showGameInviteDialog();
        void removeGameInvite();
        void setGameInformationAndGetIntoRoom(String RoomID,int playerInvitedTotal);
        void setDisconnectWhenGetOutline();
        void transToPrivacyPolicy();
        void startRandomGame();
        void showGamePageFromRandom(String s, int i);
        void showErrorDialogFromRandom(String message);
    }
}
