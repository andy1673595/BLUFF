package com.andyhuang.bluff.User;


import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.Util.Constants;
import com.andyhuang.bluff.activities.Login;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseAccount {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Firebase mRef;
    private Firebase myRef;
    private Login login;
    private String userEmail;
    private String userPassword;
    private String userUID;
    DatabaseReference dataBaseRef = FirebaseDatabase.getInstance().getReference();

    public FirebaseAccount(Context context) {
        login = (Login) context;
        mAuth = FirebaseAuth.getInstance();
    }


    public void creatAccount(final String accountInput, final String passwordInput) {

        mAuth.createUserWithEmailAndPassword(accountInput,passwordInput)
                .addOnCompleteListener(login, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(login, "success", Toast.LENGTH_SHORT).show();
                            //set account and password into UserManager
                            UserManager.getInstance().setEmail(accountInput);
                            UserManager.getInstance().setPassword(passwordInput);
                            userEmail = accountInput;
                            userPassword = passwordInput;
                            user = mAuth.getCurrentUser();
                            userUID = user.getUid();

                            //save data to sharedPrefrence
                            saveUserData();
                            //String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            //save data to firebase
                            updateToFireBase();

                        } else {
                            Toast.makeText(login, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void login(String accountInput,String passwordInput) {
        mAuth.signInWithEmailAndPassword(accountInput,passwordInput)
                .addOnCompleteListener(login, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(login, "success", Toast.LENGTH_SHORT).show();
                            //save data to sharedPrefrence
                            saveUserData();
                            //String myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            //save data to firebase
                            updateToFireBase();
                        } else {
                            Toast.makeText(login, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void saveUserData() {
        Context context = Bluff.getContext();
        Bluff.getContext().getSharedPreferences(Constants.TAG_FOR_SHAREDPREFREENCE,login.MODE_PRIVATE).edit()
                .putString(Constants.USER_EMAIL_SHAREDPREFREENCE,userEmail)
                .putString(Constants.USER_PASSWORD_SHAREDPREFREENCE,userPassword)
                .commit();
    }
    public void updateToFireBase() {
        dataBaseRef.child(Constants.USER_DATA_FIREBASE).child(userUID).child(Constants.USER_EMAIL_FIREBASE).setValue(userEmail);
    }
}
