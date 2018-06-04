package com.andyhuang.bluff.Dialog.GameInviteDialog;

import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.Object.InviteInformation;

public interface GameInviteContract {
    interface presenter {
        void acceptAndStartGame(InviteInformation inviteInformation);
        void removeInviteFromFirebase();
        void refuseInvite(String roomID);
    }
}
