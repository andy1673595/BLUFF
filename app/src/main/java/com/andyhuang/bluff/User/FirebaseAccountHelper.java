package com.andyhuang.bluff.User;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.Callback.CheckIsCreateCallback;
import com.andyhuang.bluff.Callback.FacebookLoginCallback;
import com.andyhuang.bluff.Callback.FirebaseLoginCallback;
import com.andyhuang.bluff.Login.LoginPresenter;
import com.andyhuang.bluff.Object.GameResult;
import com.andyhuang.bluff.Constant.Constants;
import com.andyhuang.bluff.activities.Login;
import com.facebook.AccessToken;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.andyhuang.bluff.Constant.Constants.TAG;

public class FirebaseAccountHelper {
    private String userEmail = Constants.NODATA;
    private String userPassword= Constants.NODATA;
    private String userUID = Constants.NODATA;
    private String userPhotoURL = Constants.NODATA;
    private String userName = Constants.NODATA;

    private Login login;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference dataBaseRef = FirebaseDatabase.getInstance().getReference();
    private Firebase userDataRef;


    public FirebaseAccountHelper(Context context, LoginPresenter loginPresenter) {
        login = (Login) context;
        mAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(context);
        userDataRef = new Firebase("https://myproject-556f6.firebaseio.com/userData");
    }

    //use email and password to log in
    public void login(String accountInput, final String passwordInput, final FirebaseLoginCallback callback) {
        mAuth.signInWithEmailAndPassword(accountInput,passwordInput)
                .addOnCompleteListener(login, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(login, "success", Toast.LENGTH_SHORT).show();
                            user = mAuth.getCurrentUser();
                            userUID = user.getUid();
                            userEmail= user.getEmail();
                            userPassword = passwordInput;
                            //update online state
                            userDataRef = new Firebase("https://myproject-556f6.firebaseio.com/userData");
                            userDataRef.child(userUID).child(Constants.ONLINE_STATE).setValue(true);
                            userDataRef.child(userUID).child(Constants.IS_GAMING).setValue(false);
                            //get user name and photoURL from Firebase
                            getUserNameFromFirebase(callback);

                        } else {
                            Toast.makeText(login, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            callback.loginFail();
                        }
                    }
                });

    }

    public void getUserNameFromFirebase(final FirebaseLoginCallback callback) {
        dataBaseRef.child(Constants.USER_DATA_FIREBASE).child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName =dataSnapshot.child(Constants.USER_NAME_FIREBASE).getValue(String.class);
                userPhotoURL = dataSnapshot.child(Constants.USER_PHOTO_FIREBASE).getValue(String.class);
                //save data
                saveUserData();
                callback.completed();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void saveUserData() {
        Bluff.getContext().getSharedPreferences(Constants.TAG_FOR_SHAREDPREFREENCE,login.MODE_PRIVATE).edit()
                .putString(Constants.USER_EMAIL_SHAREDPREFREENCE,userEmail)
                .putString(Constants.USER_PASSWORD_SHAREDPREFREENCE,userPassword)
                .putString(Constants.USER_UID_SHAREDPREFREENCE,userUID)
                .commit();
        UserManager.getInstance().setEmail(userEmail);
        UserManager.getInstance().setPassword(userPassword);
        UserManager.getInstance().setUserUID(userUID);
        UserManager.getInstance().setUserName(userName);
        UserManager.getInstance().setUserPhotoUrl(userPhotoURL);
    }

    public void updateToFireBase() {
        dataBaseRef.child(Constants.USER_DATA_FIREBASE).child(userUID).child(Constants.USER_EMAIL_FIREBASE).setValue(userEmail);
        dataBaseRef.child(Constants.USER_DATA_FIREBASE).child(userUID).child(Constants.USER_NAME_FIREBASE).setValue(userName);
        dataBaseRef.child(Constants.USER_DATA_FIREBASE).child(userUID).child(Constants.USER_PHOTO_FIREBASE).setValue(userPhotoURL);
        dataBaseRef.child(Constants.USER_DATA_FIREBASE).child(userUID).child(Constants.USER_COMMENT_FIREBASE).setValue(Constants.NODATA);
        //intial game result data
        dataBaseRef.child(Constants.USER_DATA_FIREBASE).child(userUID).child(Constants.GAME_RESULT)
                .setValue(new GameResult() );
    }

    public void facebookLogin(final AccessToken accessToken, final FacebookLoginCallback callback) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(login, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            user = mAuth.getCurrentUser();
                            userUID = user.getUid();
                            userEmail = user.getEmail();
                            userPhotoURL = UserManager.getInstance().getUserPhotoUrl();
                            userName = UserManager.getInstance().getUserName();
                            //update online state
                            userDataRef.child(userUID).child(Constants.ONLINE_STATE).setValue(true);
                            userDataRef.child(userUID).child(Constants.IS_GAMING).setValue(false);
                            saveUserData();
                            //sharedPrefrence is use for email and password login ,
                            // so set hint password to tell app that is not real email account
                            Bluff.getContext().getSharedPreferences(Constants.TAG_FOR_SHAREDPREFREENCE,Login.MODE_PRIVATE).edit()
                                    .putString(Constants.USER_PASSWORD_SHAREDPREFREENCE,Constants.FACEBOOK_HINT).commit();
                            //save to UserManager
                            UserManager.getInstance().setEmail(userEmail);
                            UserManager.getInstance().setFbtoken(accessToken);
                            UserManager.getInstance().setUserUID(userUID);
                            UserManager.getInstance().setUserName(userName);
                            //check is first Create or not and update To FireBase
                            isCreate(mCheckIsCreateCallback,callback);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                        // ...
                    }
                });
    }

    //判斷此帳號是不是第一次創建,如果不是就上傳default值
    public void isCreate(final CheckIsCreateCallback callback, final FacebookLoginCallback fbCallback) {
        dataBaseRef.child(Constants.USER_DATA_FIREBASE).child(userUID).child(Constants.GAME_RESULT).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) {
                    callback.thisIsFirstCreate(fbCallback);
                }else {
                    callback.notFirstCreate();
                    fbCallback.loginSuccess();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private CheckIsCreateCallback mCheckIsCreateCallback = new CheckIsCreateCallback() {
        @Override
        public void thisIsFirstCreate(FacebookLoginCallback fbCallback) {
            updateToFireBase();
            fbCallback.loginSuccess();
        }

        @Override
        public void notFirstCreate() {
            //do nothing
        }
    };
}
