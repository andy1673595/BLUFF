package com.andyhuang.bluff.FriendPage;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.Callback.GameInviteForFriendFragmentCallback;
import com.andyhuang.bluff.Object.FriendInformation;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.Object.InviteInformation;
import com.andyhuang.bluff.Object.MapFromFirebaseToFriendList;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Constant.Constants;
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
import java.util.List;
import java.util.Map;

import static com.andyhuang.bluff.Constant.Constants.URL_GAME_ROOM_DATA;

public class FriendPresenter implements FriendContract.Presenter {
    private FriendContract.View friendPageView;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private Firebase myRef;
    private Firebase refGame;
    private Firebase refGameRoomID;
    private ArrayList<FriendInformation> friendlist = new ArrayList<>();
    private String myUID;
    private Map<String,Object> friendInviteMap = new HashMap<>();
    private MapFromFirebaseToFriendList mapTransformer = new MapFromFirebaseToFriendList();
    private String UIDfromFirebase;
    private int gameNumber =0;
    private ArrayList<String> UIDlistForInvite = new ArrayList<>();
    private FriendPageAdapter adapter;
    private boolean creatRoom = true;

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
        String friendUID = friend.getUID();
        UIDlistForInvite.add(friendUID);
    }

    @Override
    public void removeInvite(FriendInformation friend) {
        String friendUID = friend.getUID();
        UIDlistForInvite.remove(friendUID);
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
                //update to UserManager for profile page upadate photo using
                UserManager.getInstance().setFriendList(friendlist);
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
    public void inviteFriend(final String email) {
        //reset map
        friendInviteMap = new HashMap<>();
        //add information to map
        friendInviteMap.put(Constants.USER_EMAIL_FIREBASE,UserManager.getInstance().getEmail());
        friendInviteMap.put(Constants.USER_PHOTO_FIREBASE,UserManager.getInstance().getUserPhotoUrl());
        friendInviteMap.put(Constants.USER_UID_FIREBASE,UserManager.getInstance().getUserUID());
        friendInviteMap.put(Constants.USER_NAME_FIREBASE,UserManager.getInstance().getUserName());
        //if true, this is an invite , the other is add friend
        friendInviteMap.put(Constants.FRIEND_INVITE_FIREBASE,true);
        //get the route of invitee
        Query query = reference.child(Constants.USER_DATA_FIREBASE)
                .orderByChild(Constants.USER_EMAIL_FIREBASE).equalTo(email);

        query.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                String friendUID = "";
                boolean error = false;
                if (dataSnapshot.exists()) {
                    for (com.google.firebase.database.DataSnapshot datafind : dataSnapshot.getChildren()) {
                        friendUID =datafind.getKey();
                        if(friendUID .equals(UserManager.getInstance().getUserUID())) {
                            //can't invite yourself
                            error = true;
                        } else {
                            //send invite
                            myRef.child(friendUID).child(Constants.FRIEND_LIST_FIREBASE).child(myUID)
                                    .setValue(friendInviteMap);
                        }
                    }
                    if(!error) {
                        //display dialog
                        friendPageView.showInviteFriendSuccessDialog(email);
                    }
                }
                else  {
                    //show invite error dialog
                    friendPageView.showInviteFriendErrorDialog(email);
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
        friendInviteMap.put(Constants.USER_NAME_FIREBASE,UserManager.getInstance().getUserName());
        //if true, this is an invite , the other is add friend
        friendInviteMap.put(Constants.FRIEND_INVITE_FIREBASE,false);

        myRef.child(inviter.getUID()).child(Constants.FRIEND_LIST_FIREBASE)
                .child(myUID).setValue(friendInviteMap);
    }

    public void getNumberOfGameRoom(FriendPageAdapter adapterInput) {
        adapter = adapterInput;
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
    public int getPlayerListCount() {
        return UIDlistForInvite.size();
    }

    public void openGameRoom() {
        //check and invite friend with room ID
        for(int i =0;i<UIDlistForInvite.size();i++) {
            String friendUID = UIDlistForInvite.get(i);
            boolean isLast =( i == UIDlistForInvite.size()-1);
            GameInviteListener listener = new GameInviteListener(gameInviteCallback,isLast);
            Query query = reference.child(Constants.USER_DATA_FIREBASE)
                    .orderByKey().equalTo(friendUID);
            query.addListenerForSingleValueEvent(listener);
        }

    }
    public void setAdapter(FriendPageAdapter adapterInput) {
        adapter = adapterInput;
    }

    private GameInviteForFriendFragmentCallback gameInviteCallback = new GameInviteForFriendFragmentCallback() {
        @Override
        public void openRoom() {
            if(creatRoom) {
                for(String friendUID : UIDlistForInvite) {
                    //send game invite
                    InviteInformation inviteInformation = new InviteInformation();
                    inviteInformation.setInviteUID(myUID);
                    inviteInformation.setGameRoom(gameNumber+myUID);
                    inviteInformation.setUserEmail(UserManager.getInstance().getEmail());
                    inviteInformation.setUserPhoto(UserManager.getInstance().getUserPhotoUrl());
                    inviteInformation.setUserName(UserManager.getInstance().getUserName());
                    myRef.child(friendUID).child(Constants.GAME).setValue(inviteInformation);
                }

                //increase the gameID to server
                refGameRoomID.setValue(gameNumber+1);
                //open the room
                Gamer me = new Gamer(myUID,UserManager.getInstance().getUserPhotoUrl(),UserManager.getInstance().getEmail());
                me.setUserName(UserManager.getInstance().getUserName());
                refGame.child(""+gameNumber+myUID).child(Constants.GAMER_FIREBASE).child(myUID).setValue(me);
                //reset invite Information
                int playerInvitedTotal = UIDlistForInvite.size();
                UIDlistForInvite.clear();
                adapter.resetCheck();
                creatRoom = true;
                friendPageView.showGamePage(""+gameNumber+myUID,playerInvitedTotal+1);
            } else {
                //reset invite Information
                UIDlistForInvite.clear();
                adapter.resetCheck();
                creatRoom = true;
            }
        }

        @Override
        public void showError(String message) {
            //don't create room
            creatRoom = false;
            //show error dialog
            friendPageView.showErrorDialog(message);
        }
    };

    public List<FriendInformation> getFriendList() {
        return friendlist;
    }
}
