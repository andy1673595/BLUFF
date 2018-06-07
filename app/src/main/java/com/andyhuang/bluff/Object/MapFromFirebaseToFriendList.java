package com.andyhuang.bluff.Object;

import com.andyhuang.bluff.Constant.Constants;
import java.util.Map;

public class MapFromFirebaseToFriendList {
    public FriendInformation getAddItem(Map<String,Object> map,String UID) {
        FriendInformation friendInformation = new FriendInformation(
                (String) map.get(Constants.USER_NAME_FIREBASE),
                UID,
                (String) map.get(Constants.USER_EMAIL_FIREBASE),
                (String) map.get(Constants.USER_PHOTO_FIREBASE),
                (boolean)map.get(Constants.FRIEND_INVITE_FIREBASE));
        return friendInformation;
    }


}
