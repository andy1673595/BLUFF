package com.andyhuang.bluff.User;

import android.content.SharedPreferences;

import com.andyhuang.bluff.Util.Constants;
import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseUser;

public class UserManager {
    private String email = Constants.NODATA;
    private String password = Constants.NODATA;
    private SharedPreferences userData;
    private AccessToken fbToken;
    private String userPhotoUrl = Constants.NODATA;
    private FirebaseUser user;
    private String userUID = Constants.NODATA;
    private String userName = Constants.NODATA;
    private String facebookID = Constants.NODATA;

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
        return fbToken;
    }

    public void setFbtoken(AccessToken fbToken) {
        this.fbToken = fbToken;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }
}
