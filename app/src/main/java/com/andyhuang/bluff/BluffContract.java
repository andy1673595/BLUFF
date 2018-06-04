package com.andyhuang.bluff;

import com.andyhuang.bluff.GamPage.GameObject.Gamer;

public interface BluffContract {
    interface View extends BaseView {
        void showGamePage(String gameRoomID,int gamerInvitedTotal,boolean isHost);
        void showErrorInviteDialogFromRandom(String message);
        void showInviteDialog(Gamer inviter,String numberOfGameRoom);
        void setUserPhotoOnDrawer(String userPhotoURL);
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
        void cancelWaiting();
        void removeSequenceForRandomGame();
        void loadUserPhoto();
    }
}
