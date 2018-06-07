package com.andyhuang.bluff.Profile;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.andyhuang.bluff.Callback.GameResultCallback;
import com.andyhuang.bluff.Callback.ProfileUserDataCallback;
import com.andyhuang.bluff.Object.FriendInformation;
import com.andyhuang.bluff.Profile.Listener.GameResultListener;
import com.andyhuang.bluff.Profile.Listener.UserDataListener;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Constant.Constants;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class ProfileFirebaseHelper {
    private Firebase userRef = new Firebase("https://myproject-556f6.firebaseio.com/userData/");
    //get the game result data with user UID
    public void getGameResultData(String UID, GameResultCallback callback) {
        GameResultListener listener = new GameResultListener(callback);
        userRef.child(UID).child(Constants.GAME_RESULT).addListenerForSingleValueEvent(listener);
    }
    //load the user data with user UID
    public void getUserData(String UID, ProfileUserDataCallback callback) {
        UserDataListener listener = new UserDataListener(callback);
        userRef.child(UID).addListenerForSingleValueEvent(listener);
    }
    //update the new comment to the firebase
    public void updateComment(String UID,String comment) {
        userRef.child(UID).child(Constants.USER_COMMENT_FIREBASE).setValue(comment);
    }

    /** input the photo changed Uri, then upload it to firebase
        firebase will return a Url for getting image uploaded
        then update this url to database on firebase **/
    public void updateUserPhotoAfterChanged(Uri newPhotoUri) {
        Uri file = newPhotoUri;
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference riversRef = mStorageRef.child("Profile_images/" + UserManager.getInstance().getUserUID() + ".jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        UserManager.getInstance().setUserPhotoUrl(downloadUrl.toString());
                        //update my photo url on firebase
                        userRef.child(UserManager.getInstance().getUserUID()).child(Constants.USER_PHOTO_FIREBASE)
                                .setValue(downloadUrl.toString());
                        //update all my photo urls of my friends on firebase
                        updateAllFriendMyPhoto(downloadUrl.toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
    }

    //update all my photo urls of my friends on firebase
    private void updateAllFriendMyPhoto(String newPhotoURL) {
        List<FriendInformation> friendList = UserManager.getInstance().getFriendList();
        for(FriendInformation friend:friendList) {
            userRef.child(friend.getUID()).child(Constants.FRIEND_LIST_FIREBASE)
                    .child(UserManager.getInstance().getUserUID()).child(Constants.USER_PHOTO_FIREBASE)
                    .setValue(newPhotoURL);
        }

    }
}
