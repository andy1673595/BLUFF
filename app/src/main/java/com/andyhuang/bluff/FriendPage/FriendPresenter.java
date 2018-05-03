package com.andyhuang.bluff.FriendPage;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.Object.FriendInformation;
import com.andyhuang.bluff.Object.Gamer;
import com.andyhuang.bluff.Object.MapFromFirebaseToFriendList;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Util.Constants;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.andyhuang.bluff.Util.Constants.URL_GAME_ROOM_DATA;

public class FriendPresenter implements FriendContract.Presenter {
    private FriendContract.View friendPageView;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private Firebase myRef;
    private Firebase refGame;
    private Firebase refGameRoomID;
    private ArrayList<FriendInformation> friendlist = new ArrayList<>();
    private String myUID;
    private String friendUID;
    private Map<String,Object> friendInviteMap = new HashMap<>();
    private ArrayList<Map<String,Object>> friendMapListFromFirebase;
    private MapFromFirebaseToFriendList mapTransformer = new MapFromFirebaseToFriendList();
    private ArrayList<String> UIDlist;
    private String UIDfromFirebase;
    private int gameNumber =0;

    public FriendPresenter(FriendContract.View viewInput) {
        friendPageView = viewInput;
        friendPageView.setPresenter(this);
        init();

    }
    void init() {
        Firebase.setAndroidContext(Bluff.getContext());
        myRef = new Firebase("https://myproject-556f6.firebaseio.com/userData/");
        refGame = new Firebase(URL_GAME_ROOM_DATA);
        refGameRoomID = new Firebase(Constants.GAME_ROOM_ID_REF);
        myUID = UserManager.getInstance().getUserUID();
    //    firstReadData();
        readFriendDataFromFireBase();

    }
    @Override
    public void start() {
    }
    @Override
    public void inviteGame(FriendInformation friend) {
        friendUID = friend.getUID();
        myRef.child(friendUID).child(Constants.GAME).child(Constants.GAME_INVITE).setValue(myUID);
        //get RoomID then open the game's room
        getNumberOfGameRoom();
    }


    @Override
    public void acceptInvite(int position) {
        FriendInformation inviter = friendlist.get(position);
        String inviterUID = inviter.getUID();
        //add inviter as my friend
        myRef.child(myUID).child(Constants.FRIEND_LIST_FIREBASE)
                .child(inviterUID).child(Constants.FRIEND_INVITE_FIREBASE)
                .setValue(false);
        //add me to inviter friendlist
        addFriend(inviter);
        //fresh list
        friendlist.get(position).setFriendInvite(false);
        //set change to view
        friendPageView.updateItemInvite(position,false);
    }

    @Override
    public void refuseInvite(int position) {
        FriendInformation inviter = friendlist.get(position);
        String inviterUID = inviter.getUID();
        myRef.child(myUID).child(Constants.FRIEND_LIST_FIREBASE)
                .child(inviterUID).removeValue();
        friendlist.remove(position);
        friendPageView.removeItem(position);
    }


   /* public void firstReadData() {
        friendlist = new ArrayList<FriendInformation>();
        friendMapListFromFirebase = new ArrayList<Map<String,Object>>();
        UIDlist = new ArrayList<String>();
        myRef.child(myUID).child(Constants.FRIEND_LIST_FIREBASE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataFriend : dataSnapshot.getChildren()) {
                        UIDlist.add(dataFriend.getKey());
                        friendInviteMap = (Map<String,Object>) dataFriend.getValue();
                        friendMapListFromFirebase.add(friendInviteMap);
                    }
                    //transform maplist to friendlist
                    friendlist = mapTransformer.getFriendList(friendMapListFromFirebase,UIDlist);
                    friendPageView.setAdapter(friendlist);
                    readFriendDataFromFireBase();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }*/

    @Override
    public void readFriendDataFromFireBase() {
        friendlist = new ArrayList<>();
        myRef.child(myUID).child(Constants.FRIEND_LIST_FIREBASE).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                friendInviteMap = (Map<String,Object>) dataSnapshot.getValue();
                UIDfromFirebase = dataSnapshot.getKey();
                FriendInformation friendInformation = mapTransformer.getAddItem(friendInviteMap,UIDfromFirebase);
                friendlist.add(friendInformation);
                friendPageView.addItem(friendInformation);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                friendInviteMap = (Map<String,Object>) dataSnapshot.getValue();
                UIDfromFirebase = dataSnapshot.getKey();
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


    @Override
    public void inviteFriend(String email) {
        //reset map
        friendInviteMap = new HashMap<>();
        //add information to map
        friendInviteMap.put(Constants.USER_EMAIL_FIREBASE,UserManager.getInstance().getEmail());
        friendInviteMap.put(Constants.USER_PHOTO_FIREBASE,UserManager.getInstance().getUserPhotoUrl());
        friendInviteMap.put(Constants.USER_UID_FIREBASE,UserManager.getInstance().getUserUID());
        //if true, this is an invite , the other is add friend
        friendInviteMap.put(Constants.FRIEND_INVITE_FIREBASE,true);
        //get the route of invitee
        Query query = reference.child(Constants.USER_DATA_FIREBASE)
                .orderByChild(Constants.USER_EMAIL_FIREBASE).equalTo(email);

        query.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (com.google.firebase.database.DataSnapshot datafind : dataSnapshot.getChildren()) {
                        String friendUID =datafind.getKey();
                        //send invite
                        myRef.child(friendUID).child(Constants.FRIEND_LIST_FIREBASE).child(myUID)
                                .setValue(friendInviteMap);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void addFriend(FriendInformation inviter) {
        //reset map
        friendInviteMap = new HashMap<>();
        //add information to map
        friendInviteMap.put(Constants.USER_EMAIL_FIREBASE,UserManager.getInstance().getEmail());
        friendInviteMap.put(Constants.USER_PHOTO_FIREBASE,UserManager.getInstance().getUserPhotoUrl());
        friendInviteMap.put(Constants.USER_UID_FIREBASE,UserManager.getInstance().getUserUID());
        //if true, this is an invite , the other is add friend
        friendInviteMap.put(Constants.FRIEND_INVITE_FIREBASE,false);

        myRef.child(inviter.getUID()).child(Constants.FRIEND_LIST_FIREBASE)
                .child(myUID).setValue(friendInviteMap);
    }

    public void getNumberOfGameRoom() {
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
        //invite friend with room ID
        myRef.child(friendUID).child(Constants.GAME).child(Constants.GAME_ROOM).setValue(""+gameNumber);
        myRef.child(friendUID).child(Constants.GAME).child(Constants.USER_EMAIL_FIREBASE)
                .setValue(UserManager.getInstance().getEmail());
        myRef.child(friendUID).child(Constants.GAME).child(Constants.USER_PHOTO_FIREBASE)
                .setValue(UserManager.getInstance().getUserPhotoUrl());
        //increase the gameID to server
        refGameRoomID.setValue(gameNumber+1);
        //open the room
        Gamer me = new Gamer(myUID,UserManager.getInstance().getUserPhotoUrl(),UserManager.getInstance().getEmail());
        refGame.child(""+gameNumber).child(Constants.GAMER_FIREBASE).child(myUID).setValue(me);
    }
}
