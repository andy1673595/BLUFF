package com.andyhuang.bluff.Profile.Listener;

import com.andyhuang.bluff.Callback.ProfileUserDataCallback;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Constant.Constants;
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

        if((userName==null||userEmail==null||userPhotoUrl==null||userComment==null) ) {
            //because of delay from server when create account, it will load the null data
            //so set the data from UserManager
            userName = UserManager.getInstance().getUserName();
            userEmail = UserManager.getInstance().getEmail();
            userPhotoUrl =UserManager.getInstance().getEmail();
            userComment = Constants.NODATA;
        }
        mUserDataCallback.userDataReadCompleted(userName,userEmail,userPhotoUrl,userComment);
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
