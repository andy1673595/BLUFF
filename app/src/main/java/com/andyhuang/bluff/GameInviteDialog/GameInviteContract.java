package com.andyhuang.bluff.GameInviteDialog;

import com.andyhuang.bluff.Object.Gamer;

public interface GameInviteContract {
    interface presenter {
        void acceptAndStartGame(Gamer inviter,String roomID);
        void removeInvite();
    }
}
