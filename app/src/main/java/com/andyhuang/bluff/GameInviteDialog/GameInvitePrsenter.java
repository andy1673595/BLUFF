package com.andyhuang.bluff.GameInviteDialog;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Util.Constants;
import com.firebase.client.Firebase;

import static com.andyhuang.bluff.Util.Constants.URL_GAME_ROOM_DATA;

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
    public void acceptAndStartGame(Gamer inviter,String roomID) {
        Gamer me = new Gamer(UserManager.getInstance().getUserUID(),
                UserManager.getInstance().getUserPhotoUrl(),
                UserManager.getInstance().getEmail());
        //accept , join the game
        refGameData.child(roomID).child(Constants.GAMER_FIREBASE)
                .child(UserManager.getInstance().getUserUID()).setValue(me);
        //dismiss the dialog view
       // dialog.dismiss();


    }


    @Override
    public void removeInvite() {
        //remove the invite information
        refUserData.child(UserManager.getInstance().getUserUID()).child(Constants.GAME).removeValue();
    }
}
