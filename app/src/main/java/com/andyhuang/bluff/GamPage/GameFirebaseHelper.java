package com.andyhuang.bluff.GamPage;

import com.andyhuang.bluff.Callback.RoomListenerCallback;
import com.andyhuang.bluff.GamPage.GameHelper.CurrentInformation;
import com.andyhuang.bluff.GamPage.GameHelper.CurrentStateHelper;
import com.andyhuang.bluff.GamPage.GameHelper.Dice;
import com.andyhuang.bluff.GamPage.Listener.GameStateListener;
import com.andyhuang.bluff.GamPage.Listener.PlayerCurrentStateListener;
import com.andyhuang.bluff.GamPage.Listener.RoomDataListener;
import com.andyhuang.bluff.Object.Gamer;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Util.Constants;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameFirebaseHelper {
    private Firebase gameRef;
    private String gameState;
    private GamePageContract.View gamePageView;
    private GamePagePresenter mPresenter;
    private List<Gamer> gamerList = new ArrayList<Gamer>();
    private String myUID;
    private boolean isHost;
    private CurrentStateHelper currentStateHelper;
    private GameFirebaseHelper mGameFirebaseHelper;
    private Dice dice = new Dice();
    private List<List<Integer>> diceListForEachPlayer;
    private List<Integer> diceTotal;
    private CurrentInformation currentInformation;
    private int currentPlayerNumber = 0;
    private GameStateListener gameStateListener;
    private RoomDataListener roomDataListener;
    private RoomListenerCallback mRoomListenerCallback;
    private PlayerCurrentStateListener mPlayerCurrentStateListener;


    public GameFirebaseHelper(String roomID,GamePageContract.View gamePageViewInput,
                              boolean isHostInput,GamePagePresenter mPresenterInput) {
        gameRef = new Firebase("https://myproject-556f6.firebaseio.com/GameData/"+roomID+"/");
        gamePageView = gamePageViewInput;
        mPresenter = mPresenterInput;
        isHost = isHostInput;
        myUID = UserManager.getInstance().getUserUID();
        mGameFirebaseHelper = this;
        gameStateListener = new GameStateListener(this,dice,gameRef,mPresenter,gamePageView);
        mRoomListenerCallback = getRoomListenerCallback();
        roomDataListener = new RoomDataListener(gamerList,gameRef,this,mRoomListenerCallback);
    }

    public void setGameState(String gameState) {
        gameRef.child(Constants.GAME_STATE).setValue(gameState);
    }
    //host read gamer and update it to server
    public void getRoomData() {
        gameRef.child(Constants.GAMER_FIREBASE).addListenerForSingleValueEvent(roomDataListener);
    }
    //all player read player list
    public void playerGetRoomData() {
        gamerList = new ArrayList<>();
        gameRef.child(Constants.GAMER_LIST).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot gamerData:dataSnapshot.getChildren()) {
                        Gamer gamer = new Gamer((String) gamerData.child(Constants.USER_UID_FIREBASE).getValue(),
                                (String)gamerData.child("userPhotoURL").getValue(),
                                (String)gamerData.child(Constants.USER_EMAIL_FIREBASE).getValue()) ;
                      gamerList.add(gamer);
                    }
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
        gameRef.child(Constants.GAME_STATE).addValueEventListener(gameStateListener);
    }

    public void hostListenPlayerCurrentState() {
        gameRef.child(Constants.CURRENT_STATE_LIST).addChildEventListener(mPlayerCurrentStateListener);
    }

    public void setCurrentState(String state) {
        gameRef.child(Constants.CURRENT_STATE_LIST).child(myUID).setValue(state);
    }

    //host collect each player's dice set and caculate total,then update it to server
    public void hostGetEachDiceList() {
        diceListForEachPlayer = new ArrayList<>();
        gameRef.child(Constants.DICE_LIST).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dicedata : dataSnapshot.getChildren()) {
                        List<Integer> dice = (List<Integer>)dicedata.getValue();
                        diceListForEachPlayer.add(dice);
                    }
                    diceTotal = dice.getTotalList(diceListForEachPlayer);
                    //update to server
                    gameRef.child(Constants.DICE_TOTAL_LIST).setValue(diceTotal);
                    //set first gamer first time
                    if(currentInformation ==null) {
                        currentInformation = new CurrentInformation();
                        Gamer gamer = gamerList.get(currentPlayerNumber);
                       currentInformation.setCurrentPlayer(gamer.getUserUID());
                    }
                    gameRef.child(Constants.NEXT_PLAYER_INFORMATION).setValue(currentInformation);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });

    }

    public void setGameStateInHelper(String stateInput) {
        gameState = stateInput;
    }

    RoomListenerCallback getRoomListenerCallback() {
        return new RoomListenerCallback() {
            @Override
            public void returnCurrentHelper(CurrentStateHelper helperBack, List<Gamer> gamerListCallback) {
                currentStateHelper = helperBack;
                gamerList = gamerListCallback;
                mPlayerCurrentStateListener = new PlayerCurrentStateListener(currentStateHelper);
            }
        };
    }

}
