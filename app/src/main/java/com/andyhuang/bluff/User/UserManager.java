package com.andyhuang.bluff.User;

import android.content.SharedPreferences;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseUser;

public class UserManager {
    private String email;
    private String password;
    private SharedPreferences userData;
    private AccessToken fbtoken;
    private String userPhoto;
    private FirebaseUser user;

    private static final UserManager ourInstance = new UserManager();

    public static UserManager getInstance() {
        return ourInstance;
    }

    private UserManager() {
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SharedPreferences getUserData() {
        return userData;
    }

    public void setUserData(SharedPreferences userData) {
        this.userData = userData;
    }

    public AccessToken getFbtoken() {
        return fbtoken;
    }

    public void setFbtoken(AccessToken fbtoken) {
        this.fbtoken = fbtoken;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }
}
