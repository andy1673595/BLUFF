package com.andyhuang.bluff.GamPage;

import com.andyhuang.bluff.Object.Gamer;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Util.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GameFirebaseHelper {
    private Firebase gameRef;
    private String gameState;
    private GamePageContract.View gamePageView;
    private List<Gamer> gamerList = new ArrayList<>();
    private int playerTotal;
    private String myUID;

    public GameFirebaseHelper(String roomID,GamePageContract.View gamePageViewInput) {
        gameRef = new Firebase("https://myproject-556f6.firebaseio.com/GameData/"+roomID+"/");
        gamePageView = gamePageViewInput;
        myUID = UserManager.getInstance().getUserUID();
    }

    public void setGameState(String gameState) {
        gameRef.child(Constants.GAME_STATE).setValue(gameState);
    }
    //host read gamer and update it to server
    public void getRoomData() {
        gameRef.child(Constants.GAMER_FIREBASE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //read all player
                    for (DataSnapshot gamerData : dataSnapshot.getChildren()) {

                        Gamer gamer = new Gamer((String) gamerData.child(Constants.USER_UID_FIREBASE).getValue(),
                                (String)gamerData.child(Constants.USER_PHOTO_FIREBASE).getValue(),
                                (String)gamerData.child(Constants.USER_EMAIL_FIREBASE).getValue()) ;
                        gamerList.add(gamer);
                    }
                    gameRef.child(Constants.GAMER_LIST).setValue(gamerList);
                    setGameState(Constants.READ_INIT_DATA);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }
    //all player read player list
    public void playerGetRoomData() {
        gameRef.child(Constants.GAMER_LIST).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                  gamerList = (List<Gamer>)dataSnapshot.getValue();
                  playerTotal = gamerList.size();
                  //tell server have read data
                  gameRef.child(Constants.CURRENT_STATE_LIST).child(myUID).setValue(Constants.COMPLETED_READ_INIT);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public void listenGameState() {
        gameRef.child(Constants.GAME_STATE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gameState = (String) dataSnapshot.getValue();
                switch (gameState) {
                    case "read init data":

                        break;
                    case "wait ready":
                        gamePageView.freshStateButtonUI(Constants.BUTTON_READY);
                        break;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


}
