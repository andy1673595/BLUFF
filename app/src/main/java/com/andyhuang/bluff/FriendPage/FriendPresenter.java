package com.andyhuang.bluff.FriendPage;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.Object.FriendInformation;
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

public class FriendPresenter implements FriendContract.Presenter {
    private FriendContract.View friendPageView;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private Firebase myRef;
    private ArrayList<FriendInformation> friendlist = new ArrayList<FriendInformation>();
    private String myUID;
    private Map<String,Object> friendInviteMap = new HashMap<>();
    private ArrayList<Map<String,Object>> friendMapListFromFirebase;
    private MapFromFirebaseToFriendList mapTransformer = new MapFromFirebaseToFriendList();
    private ArrayList<String> UIDlist;
    private String UIDfromFirebase;

    public FriendPresenter(FriendContract.View viewInput) {
        friendPageView = viewInput;
        friendPageView.setPresenter(this);
        init();

    }
    void init() {
        Firebase.setAndroidContext(Bluff.getContext());
        myRef = new Firebase("https://myproject-556f6.firebaseio.com/userData/");
        myUID = UserManager.getInstance().getUserUID();
    //    firstReadData();
        readFriendDataFromFireBase();

    }
    @Override
    public void start() {
    }

    @Override
    public void inviteFriend() {

    }

    @Override
    public void inviteGame() {

    }

    @Override
    public void startGame() {

    }

    @Override
    public void acceptInvite() {

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
    public void updateFriendDataToFireBase() {

    }

    @Override
    public void inviteFriend(String email) {
        friendInviteMap.put(Constants.USER_EMAIL_FIREBASE,UserManager.getInstance().getEmail());
        friendInviteMap.put(Constants.USER_PHOTO_FIREBASE,UserManager.getInstance().getUserPhotoUrl());
        friendInviteMap.put(Constants.FRIEND_INVITE_FIREBASE,true);
        Query query = reference.child(Constants.USER_DATA_FIREBASE)
                .orderByChild(Constants.USER_EMAIL_FIREBASE).equalTo(email);

        query.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (com.google.firebase.database.DataSnapshot datafind : dataSnapshot.getChildren()) {
                        String friendUID =datafind.getKey();
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
}
