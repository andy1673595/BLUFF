package com.andyhuang.bluff.Object;

import com.andyhuang.bluff.Constant.Constants;

public class UserDataForRank {
    public String userEmail = Constants.NODATA;
    public String userName = Constants.NODATA;
    public String userPhoto = Constants.NODATA;
    public String userUID = Constants.NODATA;
    public int totalTimes =0;
    public int winTimes = 0;
    public double winRate = 0.0;

    public UserDataForRank(String userEmail, String userName, String userPhoto, String userUID,
                            int totalTimes,int winTimes,double winRate) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.userUID = userUID;
        this.totalTimes = totalTimes;
        this.winRate = winRate;
        this.winTimes = winTimes;
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

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }
}
