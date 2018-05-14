package com.andyhuang.bluff.GamPage.GameHelper;

import com.andyhuang.bluff.Callback.DiceTotalListenerCallback;
import com.andyhuang.bluff.Callback.EndGameListenerCallback;
import com.andyhuang.bluff.Callback.PlayerGetRoomDataCallback;
import com.andyhuang.bluff.Callback.RoomListenerCallback;
import com.andyhuang.bluff.GamPage.GameObject.CurrentInformation;
import com.andyhuang.bluff.GamPage.GameObject.Dice;
import com.andyhuang.bluff.GamPage.GameObject.GameEndInformation;
import com.andyhuang.bluff.GamPage.GamePageContract;
import com.andyhuang.bluff.GamPage.GamePagePresenter;
import com.andyhuang.bluff.GamPage.Listener.EndGameListener;
import com.andyhuang.bluff.GamPage.Listener.CurrentPlayerInformationListener;
import com.andyhuang.bluff.GamPage.Listener.DiceTotalListener;
import com.andyhuang.bluff.GamPage.Listener.GameStateListener;
import com.andyhuang.bluff.GamPage.Listener.PlayerCurrentStateListener;
import com.andyhuang.bluff.GamPage.Listener.PlayerGetRoomDataListener;
import com.andyhuang.bluff.GamPage.Listener.RoomDataListener;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
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
    private GamePagePresenter mPresenter;
    private List<Gamer> gamerList = new ArrayList<Gamer>();
    private GameEndInformation mGameEndInformation;
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
    private PlayerCurrentStateListener mPlayerCurrentStateListener;
    private PlayerGetRoomDataListener mPlayerGetRoomDataListener;
    private DiceTotalListener mDiceTotalListener;
    private CurrentPlayerInformationListener mCurrentPlayerInformationListener;
    private EndGameListener mEndGameListener;


    public GameFirebaseHelper(String roomID,GamePageContract.View gamePageViewInput,
                              boolean isHostInput,GamePagePresenter mPresenterInput) {
        gameRef = new Firebase("https://myproject-556f6.firebaseio.com/GameData/"+roomID+"/");
        gamePageView = gamePageViewInput;
        mPresenter = mPresenterInput;
        isHost = isHostInput;
        myUID = UserManager.getInstance().getUserUID();
        mGameFirebaseHelper = this;
        gameStateListener = new GameStateListener(this,dice,gameRef,mPresenter,gamePageView);
        roomDataListener = new RoomDataListener(gamerList,gameRef,this,mRoomListenerCallback);
        mPlayerGetRoomDataListener = new PlayerGetRoomDataListener(gamerList,this,isHostInput,
                                                            gamePageView,mPlayerGetRoomDataCallback);
        mDiceTotalListener = new DiceTotalListener(mDiceTotalListenerCallback);
        mEndGameListener = new EndGameListener(mEndGameListenerCallback);
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
        gameRef.child(Constants.GAMER_LIST).addListenerForSingleValueEvent(mPlayerGetRoomDataListener);
        //start listen current Information
        listenCurrentPlayerInformation();
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
                    //tell player to get the dice total list
                    setGameState(Constants.GET_INITIAL_GAME_DATA);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });

    }

    public void getInitailGameData() {
        gameRef.child(Constants.DICE_TOTAL_LIST).addListenerForSingleValueEvent(mDiceTotalListener);
    }
    public void listenCurrentPlayerInformation() {
        mCurrentPlayerInformationListener = new CurrentPlayerInformationListener(this,gamePageView);
        gameRef.child(Constants.NEXT_PLAYER_INFORMATION).addValueEventListener(mCurrentPlayerInformationListener);
    }

    public void setGameStateInHelper(String stateInput) {
        gameState = stateInput;
    }

    RoomListenerCallback mRoomListenerCallback= new RoomListenerCallback() {
            @Override
            public void returnCurrentHelper(CurrentStateHelper helperBack, List<Gamer> gamerListCallback) {
                currentStateHelper = helperBack;
                gamerList = gamerListCallback;
                mPlayerCurrentStateListener = new PlayerCurrentStateListener(currentStateHelper);
            }
        };


    private PlayerGetRoomDataCallback mPlayerGetRoomDataCallback= new PlayerGetRoomDataCallback() {
           @Override
           public void returnGamerList(List<Gamer> gamerListCallback) {
                gamerList = gamerListCallback;
           }
       };

    private DiceTotalListenerCallback mDiceTotalListenerCallback = new DiceTotalListenerCallback() {
        @Override
        public void getDiceTotalList(List<Integer> diceTotalCallback) {
            //get the diceTotalList from firebase listener callback
            diceTotal = diceTotalCallback;
            //tell server player got dice total, get ready to start game
            gameRef.child(Constants.CURRENT_STATE_LIST).child(myUID).setValue(Constants.READY_FOR_PLAYING);
        }
    };

    public EndGameListenerCallback mEndGameListenerCallback = new EndGameListenerCallback() {
        @Override
        public void completedLoadInfo(GameEndInformation gameEndInformationInput) {
            //completed load endgameInformation
            mGameEndInformation = gameEndInformationInput;
            //show Endgame informaion
            gamePageView.showEndInformation(mGameEndInformation.getTextHowToEnd());
            //reset to start new game
            reset();
            mPresenter.reset();
        }
    };

    public void setCurrentInformation(CurrentInformation currentInformationInput) {
        currentInformation = currentInformationInput;
        //one has been called
        if(currentInformation.getRecentDiceType()==1) {
            mPresenter.sethasTellOne(true);
        }
    }

    public List<Gamer> getGamerList() {
        return gamerList;
    }
    public CurrentInformation getCurrentInformation() {
        return currentInformation;
    }

    public void  updateCurrentInformation(CurrentInformation currentInformation) {
        gameRef.child(Constants.NEXT_PLAYER_INFORMATION).setValue(currentInformation);
    }

    public List<Integer> getDiceTotal() {
        return diceTotal;
    }

    public void updateGameEndInfromation(GameEndInformation gameEndInformation) {
        //update game end information
        gameRef.child(Constants.END_INFORMATION).setValue(gameEndInformation);
        //tell everyone should end game
        gameRef.child(Constants.GAME_STATE).setValue(Constants.LOAD_END_INFO);
    }

    public void loadGameEndInfromation() {
        gameRef.child(Constants.END_INFORMATION).addListenerForSingleValueEvent(mEndGameListener);
    }

    public void reset() {
        if(isHost) {
            currentStateHelper.resetCount();
            //start from who lose game
            currentInformation.setCurrentPlayer(mGameEndInformation.getLoserUID());
            currentInformation.setRecentDiceType(0);
            currentInformation.setRecentDiceNumber(0);
            currentInformation.setRecentPlayer(Constants.NODATA);
            gameRef.child(Constants.NEXT_PLAYER_INFORMATION).setValue(currentInformation);
        }
        if(currentInformation.getCurrentPlayer().equals(myUID)) {
            gamePageView.resetView(true);
        }else {
            gamePageView.resetView(false);
        }
        diceTotal.clear();
        //restart a game , wait for ready
        gameRef.child(Constants.CURRENT_STATE_LIST).child(myUID).setValue(Constants.COMPLETED_READ_INIT);
    }

    public void setIsGaming(boolean isGaming) {
        Firebase userDataRef = new Firebase("https://myproject-556f6.firebaseio.com/userData");
        userDataRef.child(myUID).child(Constants.IS_GAMING).setValue(isGaming);
    }
}
