package com.andyhuang.bluff.Dialog.GameInviteDialog;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.Object.InviteInformation;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Constant.Constants;
import com.firebase.client.Firebase;

import static com.andyhuang.bluff.Constant.Constants.URL_GAME_ROOM_DATA;

public class GameInvitePrsenter implements GameInviteContract.presenter{
    Firebase refUserData;
    Firebase refGameData;
    GameInviteDialog dialog;
    public GameInvitePrsenter(GameInviteDialog dialogInput) {
        dialog = dialogInput;
        Firebase.setAndroidContext(Bluff.getContext());
        refUserData = new Firebase("https://myproject-556f6.firebaseio.com/userData/");
        refGameData = new Firebase(URL_GAME_ROOM_DATA);
    }
    @Override
    public void acceptAndStartGame(InviteInformation inviteInformation) {
        Gamer me = new Gamer(UserManager.getInstance().getUserUID(),
                UserManager.getInstance().getUserPhotoUrl(),
                UserManager.getInstance().getEmail());
        me.setUserName(UserManager.getInstance().getUserName());
        //accept , join the game
        refGameData.child(inviteInformation.gameRoom).child(Constants.GAMER_FIREBASE)
                .child(UserManager.getInstance().getUserUID()).setValue(me);
    }


    @Override
    public void removeInviteFromFirebase() {
        //remove the invite information
        refUserData.child(UserManager.getInstance().getUserUID()).child(Constants.GAME).removeValue();
    }

    @Override
    public void refuseInvite(String roomID) {
        refGameData.child(roomID).child(Constants.GAME_STATE).setValue(Constants.EXIT_GAME);
    }
}
