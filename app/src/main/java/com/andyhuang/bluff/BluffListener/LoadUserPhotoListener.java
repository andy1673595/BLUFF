package com.andyhuang.bluff.BluffListener;

import com.andyhuang.bluff.Callback.LoadUserPhotoFromFirebaseCallback;
import com.andyhuang.bluff.Util.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class LoadUserPhotoListener implements ValueEventListener {
    LoadUserPhotoFromFirebaseCallback mCallback;
    public LoadUserPhotoListener(LoadUserPhotoFromFirebaseCallback callback) {
        mCallback = callback;
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()) {
            String userPhotURL = dataSnapshot.child(Constants.USER_PHOTO_FIREBASE).getValue(String.class);
            mCallback.completed(userPhotURL);
        }
    }
    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
