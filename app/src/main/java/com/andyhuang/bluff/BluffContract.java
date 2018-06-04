package com.andyhuang.bluff;

import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.Object.InviteInformation;

public interface BluffContract {
    interface View extends BaseView {
        void showGamePage(String gameRoomID,int gamerInvitedTotal,boolean isHost);
        void showErrorInviteDialogFromRandom(String message);
        void showInviteDialog(InviteInformation inviteInformation);
        void setUserPhotoOnDrawer(String userPhotoURL);
    }
    interface Presenter extends BasePresenter {
        void transToMainPage();
        void transToFriendPage();
        void transToProfilePage();
        void transToFriendProfile(String UID);
        void removeFriendProfileFragment();
        void showGameInviteDialog(InviteInformation inviteInformation);
        void removeGameInvite();
        void setGameInformationAndGetIntoRoom(String RoomID,int playerInvitedTotal);
        void setDisconnectWhenGetOutline();
        void startRandomGame();
        void showGamePageFromRandom(String s, int i);
        void showErrorDialogFromRandom(String message);
        void cancelWaiting();
        void removeSequenceOnFirebaseForRandomGame();
        void loadUserPhoto();
    }
}
