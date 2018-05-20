package com.andyhuang.bluff.Profile.Listener;

import com.andyhuang.bluff.Callback.ProfileUserDataCallback;
import com.andyhuang.bluff.Util.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class UserDataListener implements ValueEventListener{
    ProfileUserDataCallback mUserDataCallback;
    public UserDataListener(ProfileUserDataCallback callback){
        mUserDataCallback = callback;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String userName = dataSnapshot.child(Constants.USER_NAME_FIREBASE).getValue(String.class);
        String userEmail = dataSnapshot.child(Constants.USER_EMAIL_FIREBASE).getValue(String.class);
        String userPhotoUrl = dataSnapshot.child(Constants.USER_PHOTO_FIREBASE).getValue(String.class);
        String userComment = dataSnapshot.child(Constants.USER_COMMENT_FIREBASE).getValue(String.class);
        mUserDataCallback.userDataReadCompleted(userName,userEmail,userPhotoUrl,userComment);
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
