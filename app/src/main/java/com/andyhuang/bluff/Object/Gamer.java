package com.andyhuang.bluff.Object;

import com.andyhuang.bluff.Util.Constants;

public class Gamer {
    public String userUID = Constants.USER_UID_FIREBASE;
    public String userPhotoURL = Constants.USER_PHOTO_FIREBASE;
    public String userEmail = Constants.USER_EMAIL_FIREBASE;
    public Gamer(String userUIDInput,String userPhotoURLInput,String userEmailInput){
        userUID = userUIDInput;
        userPhotoURL = userPhotoURLInput;
        userEmail = userEmailInput;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getUserPhotoURL() {
        return userPhotoURL;
    }

    public void setUserPhotoURL(String userPhotoURL) {
        this.userPhotoURL = userPhotoURL;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
