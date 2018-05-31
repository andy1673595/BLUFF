package com.andyhuang.bluff.Dialog.GameInviteDialog;

import com.andyhuang.bluff.GamPage.GameObject.Gamer;

public interface GameInviteContract {
    interface presenter {
        void acceptAndStartGame(Gamer inviter,String roomID);
        void removeInvite();
    }
}
