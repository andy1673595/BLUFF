package com.andyhuang.bluff;

import com.andyhuang.bluff.BluffListener.LoadUserPhotoListener;
import com.andyhuang.bluff.Callback.LoadUserPhotoFromFirebaseCallback;
import com.andyhuang.bluff.User.UserManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class BluffFirebaseHelper {
    private Firebase userDataRef = new Firebase("https://myproject-556f6.firebaseio.com/userData/");
    public void loadUserPhoto(LoadUserPhotoFromFirebaseCallback callback) {
        LoadUserPhotoListener listener = new LoadUserPhotoListener(callback);
        userDataRef.child(UserManager.getInstance().getUserUID()).addListenerForSingleValueEvent(listener);
    }
}
