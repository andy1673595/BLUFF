package com.andyhuang.bluff.RandomGame;

import android.util.Log;

import com.andyhuang.bluff.BluffPresenter;
import com.andyhuang.bluff.Callback.GameInviteForFriendFragmentCallback;
import com.andyhuang.bluff.FriendPage.FriendPageAdapter;
import com.andyhuang.bluff.FriendPage.GameInviteListener;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Util.Constants;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Date;

import static com.andyhuang.bluff.Util.Constants.URL_GAME_ROOM_DATA;

public class RandomGameHelper {
    private BluffPresenter mBluffPresenter;
    private Firebase randomGameRef;
    private long time;
    private String myUID = UserManager.getInstance().getUserUID();
    private String recentUserUID = "";
    private boolean isHost = false;
    private int gameNumber;
    private boolean creatRoom =true;
    private Firebase refGameRoomID;
    private Firebase myRef = new Firebase("https://myproject-556f6.firebaseio.com/userData/");
    private Firebase refGame = new Firebase(URL_GAME_ROOM_DATA);

    public RandomGameHelper(BluffPresenter presenter) {
        mBluffPresenter = presenter;
        randomGameRef = new Firebase("https://myproject-556f6.firebaseio.com/"+ Constants.RANDOM_GAME);
    }

    public void updateMyDataToRandomSequence(){
        //set my uid to sequence to tell that I'm ready for random games
        long time = new Date().getTime();
        randomGameRef.child(time+myUID).setValue(myUID);
        randomGameRef.child(time+myUID).onDisconnect().removeValue();
        listenSequence();
    }
    public void listenSequence() {
        randomGameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userUID = userSnapshot.getValue(String.class);
                    if(userUID.equals(myUID)) {
                        //save the sequenceID
                        UserManager.getInstance().setSequenceID(userSnapshot.getKey());
                        //find my UID,start to check I should invite another user or not
                        if(!recentUserUID.equals("") && isHost) {
                            //I'm a host , invite recent person
                            Log.d("randomLog","I'm host, invite "+recentUserUID);
                            getNumberOfGameRoom();
                            mBluffPresenter.removeSequenceForRandomGame();

                        } else {
                            Log.d("randomLog","I'm invitee" );
                        }
                        return;
                    } else {
                        //record current user UID, I may be the next and invite this user
                        recentUserUID = userUID;
                    }
                    //invitee and inviter
                    isHost = !isHost;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void getNumberOfGameRoom() {
        refGameRoomID = new Firebase(Constants.GAME_ROOM_ID_REF);
        refGameRoomID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long longID = (Long)dataSnapshot.getValue();
                    gameNumber = Integer.parseInt(String.valueOf(longID));
                    openGameRoom();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    public void openGameRoom() {
        //check and invite friend with room ID
        GameInviteListener listener = new GameInviteListener(gameInviteCallback,true);
        Query query = FirebaseDatabase.getInstance().getReference().child(Constants.USER_DATA_FIREBASE)
                .orderByKey().equalTo(recentUserUID);
        query.addListenerForSingleValueEvent(listener);
    }

    private GameInviteForFriendFragmentCallback gameInviteCallback = new GameInviteForFriendFragmentCallback() {
        @Override
        public void openRoom() {
            if(creatRoom) {
                    //send game invite
                    myRef.child(recentUserUID).child(Constants.GAME).child(Constants.GAME_INVITE)
                            .setValue(myUID);
                    myRef.child(recentUserUID).child(Constants.GAME).child(Constants.GAME_ROOM)
                            .setValue(""+gameNumber+myUID);
                    myRef.child(recentUserUID).child(Constants.GAME).child(Constants.USER_EMAIL_FIREBASE)
                            .setValue(UserManager.getInstance().getEmail());
                    myRef.child(recentUserUID).child(Constants.GAME).child(Constants.USER_PHOTO_FIREBASE)
                            .setValue(UserManager.getInstance().getUserPhotoUrl());
                    myRef.child(recentUserUID).child(Constants.GAME).child(Constants.USER_NAME_FIREBASE)
                            .setValue(UserManager.getInstance().getUserName());


                //increase the gameID to server
                refGameRoomID.setValue(gameNumber+1);
                //open the room
                Gamer me = new Gamer(myUID,UserManager.getInstance().getUserPhotoUrl(),UserManager.getInstance().getEmail());
                me.setUserName(UserManager.getInstance().getUserName());
                refGame.child(""+gameNumber+myUID).child(Constants.GAMER_FIREBASE).child(myUID).setValue(me);
                creatRoom = true;
                mBluffPresenter.showGamePageFromRandom(""+gameNumber+myUID,2);
            } else {
                //reset invite Information
                creatRoom = true;
            }
        }

        @Override
        public void showError(String message) {
            //don't create room
            creatRoom = false;
            //show error dialog
            mBluffPresenter.showErrorDialogFromRandom(message);
        }
    };
}
