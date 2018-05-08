package com.andyhuang.bluff.GamPage;

import com.andyhuang.bluff.GamPage.GameInformation.CurrentStateHelper;
import com.andyhuang.bluff.Object.Gamer;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Util.Constants;
import com.firebase.client.ChildEventListener;
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
    private GamePagePresenter mPresenter;
    private List<Gamer> gamerList = new ArrayList<>();
    private int playerTotal;
    private String myUID;
    private boolean isHost;
    private CurrentStateHelper currentStateHelper;
    private GameFirebaseHelper mGameFirebaseHelper;


    public GameFirebaseHelper(String roomID,GamePageContract.View gamePageViewInput,
                              boolean isHostInput,GamePagePresenter mPresenterInput) {
        gameRef = new Firebase("https://myproject-556f6.firebaseio.com/GameData/"+roomID+"/");
        gamePageView = gamePageViewInput;
        mPresenter = mPresenterInput;
        isHost = isHostInput;
        myUID = UserManager.getInstance().getUserUID();
        mGameFirebaseHelper = this;
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
                                (String)gamerData.child("userPhotoURL").getValue(),
                                (String)gamerData.child(Constants.USER_EMAIL_FIREBASE).getValue()) ;
                        gamerList.add(gamer);
                    }
                    gameRef.child(Constants.GAMER_LIST).setValue(gamerList);
                    //use total player to creat current state helper
                    currentStateHelper = new CurrentStateHelper(mGameFirebaseHelper,gamerList.size());
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
                    setCurrentState(Constants.COMPLETED_READ_INIT);
                  //if I'm host , start to listen player current state
                  if(isHost) hostListenPlayerCurrentState();
                  //set button to ready
                  gamePageView.freshStateButtonUI(Constants.BUTTON_READY);
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
                        playerGetRoomData();
                        break;
                    case "wait ready":
                        gamePageView.freshStateButtonUI(Constants.BUTTON_READY);
                        mPresenter.setButtonType(Constants.BUTTON_READY);
                        break;
                    case "get new dice":
                        break;
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void hostListenPlayerCurrentState() {
        gameRef.child(Constants.CURRENT_STATE_LIST).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String state = (String)dataSnapshot.getValue();
                currentStateHelper.dealCurrentState(state);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String state = (String)dataSnapshot.getValue();
                currentStateHelper.dealCurrentState(state);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void setCurrentState(String state) {
        gameRef.child(Constants.CURRENT_STATE_LIST).child(myUID).setValue(state);
    }


}
