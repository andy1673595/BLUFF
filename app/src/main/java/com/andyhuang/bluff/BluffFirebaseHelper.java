package com.andyhuang.bluff;

import com.andyhuang.bluff.BluffListener.LoadUserPhotoListener;
import com.andyhuang.bluff.Callback.InviteListenCallback;
import com.andyhuang.bluff.Callback.LoadUserPhotoFromFirebaseCallback;
import com.andyhuang.bluff.Object.InviteInformation;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Util.Constants;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class BluffFirebaseHelper {
    //Firebase路徑
    private Firebase userDataRef = new Firebase("https://myproject-556f6.firebaseio.com/userData/");
    private Firebase randomGameRef =
            new Firebase("https://myproject-556f6.firebaseio.com/"+ Constants.RANDOM_GAME);
    //其他變數
    private BluffPresenter mBluffPresenter;
    public BluffFirebaseHelper(BluffPresenter presenter) {
        mBluffPresenter = presenter;
    }

    public void loadUserPhoto(LoadUserPhotoFromFirebaseCallback callback) {
        LoadUserPhotoListener listener = new LoadUserPhotoListener(callback);
        userDataRef.child(UserManager.getInstance().getUserUID()).addListenerForSingleValueEvent(listener);
    }


    //把firebase上random的排隊資料全砍掉
    public void removeSequenceOnFirebase() {
        String sequenceID = UserManager.getInstance().getSequenceID();
        if(!sequenceID.equals(Constants.NODATA)) {
            //if I am invited from random game , remove the sequence from server
            randomGameRef.child(sequenceID).removeValue();
            UserManager.getInstance().setSequenceID(Constants.NODATA);
        }
    }

    public void listenGameInviteFromFirebase() {
        userDataRef.child(UserManager.getInstance().getUserUID())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.getKey().equals(Constants.GAME)) {
                            InviteInformation inviteInformation =
                                    dataSnapshot.getValue(InviteInformation.class);
                            mBluffPresenter.showGameInviteDialog(inviteInformation);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {}
                });
    }

    public void setDisconnectWhenGetOutlineOnFirebase() {
        //set false when user is outline
        userDataRef.child(UserManager.getInstance().getUserUID())
                .child(Constants.ONLINE_STATE).onDisconnect().setValue(false);
        userDataRef.child(UserManager.getInstance().getUserUID())
                .child(Constants.IS_GAMING).onDisconnect().setValue(false);
    }
}
