package com.andyhuang.bluff.Object;

public class FriendInformation {
    private String name;
    private String UID;
    private String email;
    private String photoURL;
    private boolean isFriendInvite;

    public FriendInformation(String nameInput,String UIDInput,String emailInput,String photoURLInput){
        name = nameInput;
        UID = UIDInput;
        email = emailInput;
        photoURL = photoURLInput;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public boolean isFriendInvite() {
        return isFriendInvite;
    }

    public void setFriendInvite(boolean friendInvite) {
        isFriendInvite = friendInvite;
    }
}
