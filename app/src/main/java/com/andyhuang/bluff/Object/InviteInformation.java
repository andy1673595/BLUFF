package com.andyhuang.bluff.Object;

import com.andyhuang.bluff.Util.Constants;

public class InviteInformation {
    public String gameInvite = Constants.NODATA;
    public String gameRoom = Constants.NODATA;
    public String userEmail = Constants.NODATA;
    public String userPhoto = Constants.NODATA;
    public String userName = Constants.NODATA;

    public String getGameInvite() {
        return gameInvite;
    }

    public void setGameInvite(String gameInvite) {
        this.gameInvite = gameInvite;
    }

    public String getGameRoom() {
        return gameRoom;
    }

    public void setGameRoom(String gameRoom) {
        this.gameRoom = gameRoom;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
