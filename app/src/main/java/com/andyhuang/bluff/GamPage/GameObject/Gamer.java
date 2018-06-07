package com.andyhuang.bluff.GamPage.GameObject;

import com.andyhuang.bluff.Constant.Constants;

public class Gamer {
    public String userUID = Constants.USER_UID_FIREBASE;
    public String userPhotoURL = Constants.USER_PHOTO_FIREBASE;
    public String userEmail = Constants.USER_EMAIL_FIREBASE;
    public String userName = Constants.USER_NAME_FIREBASE;
    public Gamer(String userUIDInput,String userPhotoURLInput,String userEmailInput){
        userUID = userUIDInput;
        userPhotoURL = userPhotoURLInput;
        userEmail = userEmailInput;
    }
    public Gamer(String userUIDInput,String userPhotoURLInput,String userEmailInput,String userNameInput){
        userUID = userUIDInput;
        userPhotoURL = userPhotoURLInput;
        userEmail = userEmailInput;
        userName = userNameInput;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
