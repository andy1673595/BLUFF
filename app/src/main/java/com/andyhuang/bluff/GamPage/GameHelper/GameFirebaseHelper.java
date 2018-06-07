package com.andyhuang.bluff.GamPage.GameHelper;

import com.andyhuang.bluff.Callback.DiceTotalListenerCallback;
import com.andyhuang.bluff.Callback.EndGameListenerCallback;
import com.andyhuang.bluff.Callback.PlayerGetRoomDataCallback;
import com.andyhuang.bluff.Callback.PlayerJoinedListenerCallback;
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
import com.andyhuang.bluff.GamPage.Listener.LoadPlayerTotalInvitedListener;
import com.andyhuang.bluff.GamPage.Listener.PlayerCurrentStateListener;
import com.andyhuang.bluff.GamPage.Listener.PlayerGetRoomDataListener;
import com.andyhuang.bluff.GamPage.Listener.PlayerHaveJoinedListener;
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
    private int currentPlayerNumber = 0;
    private String gameState;
    private String myUID;
    private boolean isHost;
    //presenter and view
    private GamePageContract.View gamePageView;
    private GamePagePresenter mPresenter;
    //other variables
    private Firebase gameRef;
    private CurrentStateHelper currentStateHelper;
    private GameEndInformation mGameEndInformation;
    private CurrentInformation currentInformation;
    private Dice dice = new Dice();
    private List<Gamer> gamerList = new ArrayList<Gamer>();
    private ArrayList<Gamer> mPlayerJoinedList = new ArrayList<>();
    private List<List<Integer>> diceListForEachPlayer;
    private List<Integer> diceTotal;
    //Firebase listeners
    private GameStateListener mGameStateListener;
    private RoomDataListener mRoomDataListener;
    private PlayerCurrentStateListener mPlayerCurrentStateListener;
    private PlayerGetRoomDataListener mPlayerGetRoomDataListener;
    private DiceTotalListener mDiceTotalListener;
    private CurrentPlayerInformationListener mCurrentPlayerInformationListener;
    private EndGameListener mEndGameListener;
    private PlayerHaveJoinedListener mPlayerHaveJoinedListener;
    private LoadPlayerTotalInvitedListener mLoadPlayerTotalInvitedListener;

    public GameFirebaseHelper(String roomID,GamePageContract.View gamePageViewInput,
                              boolean isHostInput,GamePagePresenter mPresenterInput) {
        myUID = UserManager.getInstance().getUserUID();
        isHost = isHostInput;
        gamePageView = gamePageViewInput;
        mPresenter = mPresenterInput;
        gameRef = new Firebase("https://myproject-556f6.firebaseio.com/GameData/"+roomID+"/");
        //Listeners init
        mGameStateListener = new GameStateListener(this,dice,gameRef,mPresenter,gamePageView);
        mRoomDataListener = new RoomDataListener(gamerList,gameRef,this,mRoomListenerCallback);
        mPlayerGetRoomDataListener = new PlayerGetRoomDataListener(gamerList,this,isHostInput,
                                                            gamePageView,mPlayerGetRoomDataCallback);
        mDiceTotalListener = new DiceTotalListener(mDiceTotalListenerCallback);
        mEndGameListener = new EndGameListener(mEndGameListenerCallback);
        mPlayerHaveJoinedListener = new PlayerHaveJoinedListener(mPlayerJoinedListenerCallback);
        mLoadPlayerTotalInvitedListener = new LoadPlayerTotalInvitedListener(mPresenter);
    }

    public void setGameState(String gameState) {
        gameRef.child(Constants.GAME_STATE).setValue(gameState);
    }
    //host read gamer and update it to server
    public void getRoomData() {
        gameRef.child(Constants.GAMER_FIREBASE).addListenerForSingleValueEvent(mRoomDataListener);
    }
    //all player read player list
    public void playerGetRoomData() {
        gamerList = new ArrayList<>();
        gameRef.child(Constants.GAMER_LIST).addListenerForSingleValueEvent(mPlayerGetRoomDataListener);
        //start listen current Information
        listenCurrentPlayerInformation();
    }

    //all player should listen game state and do the corresponding things
    public void listenGameState() {
        gameRef.child(Constants.GAME_STATE).addValueEventListener(mGameStateListener);
    }
    //host should listen all players' current state and update the game state to control game process
    public void hostListenPlayerCurrentState() {
        gameRef.child(Constants.CURRENT_STATE_LIST).addChildEventListener(mPlayerCurrentStateListener);
    }

    public void setCurrentState(String state) {
        gameRef.child(Constants.CURRENT_STATE_LIST).child(myUID).setValue(state);
    }

    public void listenToPlayerJoinedEvent() {
        gameRef.child(Constants.GAMER_FIREBASE).addChildEventListener(mPlayerHaveJoinedListener);

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
    //when game first start , get the initial game data
    public void getInitialGameData() {
        gameRef.child(Constants.DICE_TOTAL_LIST).addListenerForSingleValueEvent(mDiceTotalListener);
    }
    //when game restart , reset all variables used in game
    private void reset() {
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

    public void  updateCurrentInformation(CurrentInformation currentInformation) {
        gameRef.child(Constants.NEXT_PLAYER_INFORMATION).setValue(currentInformation);
    }
    //when a gamer is end,update the result of the game to the firebase
    public void updateGameEndInfromation(GameEndInformation gameEndInformation) {
        //update game end information
        gameRef.child(Constants.END_INFORMATION).setValue(gameEndInformation);
        //tell everyone should end game
        gameRef.child(Constants.GAME_STATE).setValue(Constants.LOAD_END_INFO);
        UpdateGameResult gameResult = new UpdateGameResult();
        //If this game is a 2 person game,upadte the win and lose result to server
        if(gamerList.size()==2) {
            gameResult.updateTwoPersonResultToFirebase(gamerList,gameEndInformation);
        }
        gameResult.updateOnlyTimesToFirebase(gamerList);
    }

    public void loadGameEndInfromation() {
        gameRef.child(Constants.END_INFORMATION).addListenerForSingleValueEvent(mEndGameListener);
    }
    public void removeListener() {
        if(mGameStateListener !=null)
            gameRef.child(Constants.GAME_STATE).removeEventListener(mGameStateListener);
        if(mPlayerCurrentStateListener!=null)
            gameRef.child(Constants.CURRENT_STATE_LIST).removeEventListener(mPlayerCurrentStateListener);
        if(mCurrentPlayerInformationListener!=null)
            gameRef.child(Constants.NEXT_PLAYER_INFORMATION).removeEventListener(mCurrentPlayerInformationListener);
        if(mPlayerHaveJoinedListener != null)
            gameRef.child(Constants.GAMER_FIREBASE).removeEventListener(mPlayerHaveJoinedListener);
    }
    //all players load the total number of invitees when get into game room
    public void loadPlayerInvitedTotal() {
        gameRef.child(Constants.PLAYED_TOTAL_INVITED).addListenerForSingleValueEvent(mLoadPlayerTotalInvitedListener);

    }
    //host should upload the total number of player invited to the firebase
    public void updatePlayInvitedCount(int count) {
        gameRef.child(Constants.PLAYED_TOTAL_INVITED).setValue(count);
    }
    //tell firebase I'm playing a game or not
    public void setIsGaming(boolean isGaming) {
        Firebase userDataRef = new Firebase("https://myproject-556f6.firebaseio.com/userData");
        userDataRef.child(myUID).child(Constants.IS_GAMING).setValue(isGaming);
    }

    public void setGameStateInHelper(String stateInput) {
        gameState = stateInput;
    }

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
    public List<Integer> getDiceTotal() {
        return diceTotal;
    }

    private void listenCurrentPlayerInformation() {
        mCurrentPlayerInformationListener = new CurrentPlayerInformationListener(this,gamePageView);
        gameRef.child(Constants.NEXT_PLAYER_INFORMATION).addValueEventListener(mCurrentPlayerInformationListener);
    }

    /*    Listeners' callbacks    */
    private RoomListenerCallback mRoomListenerCallback= new RoomListenerCallback() {
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
                //judge the room type is two persons or more players room
               if(gamerList.size() == 2) {
                   //this is a two person game
                   mPresenter.initVideoData();
               }else  {
                   //this is a multiple game
                   mPresenter.initMultipleData();
               }
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

    private EndGameListenerCallback mEndGameListenerCallback = new EndGameListenerCallback() {
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

    private PlayerJoinedListenerCallback mPlayerJoinedListenerCallback = new PlayerJoinedListenerCallback() {
        @Override
        public void freshShowPlayerCountUI(ArrayList<Gamer> gamersJoinedListInput,Gamer newGamer) {
            mPlayerJoinedList = gamersJoinedListInput;
            mPresenter.updatePlayerHaveJoinedList(mPlayerJoinedList,newGamer);
        }
    };
}
