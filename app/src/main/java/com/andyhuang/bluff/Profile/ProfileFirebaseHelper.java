package com.andyhuang.bluff.Profile;

import com.andyhuang.bluff.Callback.GameResultCallback;
import com.andyhuang.bluff.Callback.ProfileUserDataCallback;
import com.andyhuang.bluff.Object.GameResult;
import com.andyhuang.bluff.Profile.Listener.GameResultListener;
import com.andyhuang.bluff.Profile.Listener.UserDataListener;
import com.andyhuang.bluff.Util.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class ProfileFirebaseHelper {
    private GameResult mGameResultData;
    private Firebase userRef = new Firebase("https://myproject-556f6.firebaseio.com/userData/");
    public void getGameResultData(String UID, GameResultCallback callback) {
        GameResultListener listener = new GameResultListener(callback);
        userRef.child(UID).child(Constants.GAME_RESULT).addListenerForSingleValueEvent(listener);
    }
    public void getUserData(String UID, ProfileUserDataCallback callback) {
        UserDataListener listener = new UserDataListener(callback);
        userRef.child(UID).addListenerForSingleValueEvent(listener);
    }

    public void updateComment(String UID,String comment) {
        userRef.child(UID).child(Constants.USER_COMMENT_FIREBASE).setValue(comment);
    }

}
