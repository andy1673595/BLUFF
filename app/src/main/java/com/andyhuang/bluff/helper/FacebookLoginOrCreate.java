package com.andyhuang.bluff.helper;

import com.firebase.client.Firebase;

public class FacebookLoginOrCreate {
    Firebase userDataRef = new Firebase("https://myproject-556f6.firebaseio.com/userData");
    boolean isCreate() {
        return true;
    }
}
