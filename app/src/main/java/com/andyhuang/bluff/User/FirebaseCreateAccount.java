package com.andyhuang.bluff.User;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.Callback.FirebaseLoginCallback;
import com.andyhuang.bluff.Object.GameResult;
import com.andyhuang.bluff.Constant.Constants;
import com.andyhuang.bluff.activities.CreateAccountPage;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseCreateAccount {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private CreateAccountPage mCreatePage;
    private String userEmail = Constants.NODATA;
    private String userPassword= Constants.NODATA;
    private String userUID = Constants.NODATA;
    private String userPhotoURL = Constants.NODATA;
    private String userName = Constants.NODATA;
    private DatabaseReference dataBaseRef = FirebaseDatabase.getInstance().getReference();
    private Firebase userDataRef;
    public FirebaseCreateAccount(Context context) {
        mCreatePage = (CreateAccountPage) context;
        mAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(context);
        userDataRef = new Firebase("https://myproject-556f6.firebaseio.com/userData");
    }

    public void creatAccount(final FirebaseLoginCallback callback, final String accountInput,
                             final String passwordInput,final String nameInput) {

        mAuth.createUserWithEmailAndPassword(accountInput,passwordInput)
                .addOnCompleteListener(mCreatePage, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mCreatePage, "success", Toast.LENGTH_SHORT).show();
                            userEmail = accountInput;
                            userPassword = passwordInput;
                            userName = nameInput;
                            user = mAuth.getCurrentUser();
                            userUID = user.getUid();
                            //update online state
                            userDataRef = new Firebase("https://myproject-556f6.firebaseio.com/userData");
                            userDataRef.child(userUID).child(Constants.ONLINE_STATE).setValue(true);
                            userDataRef.child(userUID).child(Constants.IS_GAMING).setValue(false);
                            //save data to firebase
                            updateToFireBase(callback);
                        } else {
                            Toast.makeText(mCreatePage, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            callback.loginFail();
                        }
                    }
                });
    }

    public void saveUserData(FirebaseLoginCallback callback) {
        Bluff.getContext().getSharedPreferences(Constants.TAG_FOR_SHAREDPREFREENCE,mCreatePage.MODE_PRIVATE).edit()
                .putString(Constants.USER_EMAIL_SHAREDPREFREENCE,userEmail)
                .putString(Constants.USER_PASSWORD_SHAREDPREFREENCE,userPassword)
                .putString(Constants.USER_UID_SHAREDPREFREENCE,userUID)
                .putString(Constants.USER_NAME_SHAREDPREFREENCE,userName)
                .commit();
        UserManager.getInstance().setEmail(userEmail);
        UserManager.getInstance().setPassword(userPassword);
        UserManager.getInstance().setUserUID(userUID);
        UserManager.getInstance().setUserName(userName);
        callback.completed();
    }

    public void updateToFireBase(FirebaseLoginCallback callback) {
        userDataRef.child(userUID).child(Constants.USER_EMAIL_FIREBASE).setValue(userEmail);
        userDataRef.child(userUID).child(Constants.USER_NAME_FIREBASE).setValue(userName);
        userDataRef.child(userUID).child(Constants.USER_PHOTO_FIREBASE).setValue(userPhotoURL);
        userDataRef.child(userUID).child(Constants.USER_COMMENT_FIREBASE).setValue(Constants.NODATA);
        //intial game result data
        userDataRef.child(userUID).child(Constants.GAME_RESULT)
                .setValue(new GameResult() );
        //completed create account , start MainActivity
        saveUserData(callback);

    }
}
