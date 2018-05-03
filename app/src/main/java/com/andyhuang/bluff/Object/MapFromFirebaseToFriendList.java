package com.andyhuang.bluff.Object;

import com.andyhuang.bluff.Util.Constants;

import java.util.ArrayList;
import java.util.Map;

public class MapFromFirebaseToFriendList {
    private ArrayList<FriendInformation> friendList;
    public ArrayList<FriendInformation> getFriendList(ArrayList<Map<String,Object>> mapList,ArrayList<String> UIDlist) {
        friendList = new ArrayList<FriendInformation>();
        for(int i=0;i<mapList.size();i++) {
            FriendInformation friendInformation = new FriendInformation(Constants.NODATA,
                    UIDlist.get(i),
                    (String) mapList.get(i).get(Constants.USER_EMAIL_FIREBASE),
                    (String)mapList.get(i).get(Constants.USER_PHOTO_FIREBASE),
                    (boolean)mapList.get(i).get(Constants.FRIEND_INVITE_FIREBASE));
            friendList.add(friendInformation);
        }
        return friendList;
    }

    public FriendInformation getAddItem(Map<String,Object> map,String UID) {
        FriendInformation friendInformation = new FriendInformation(Constants.NODATA,
                UID,
                (String) map.get(Constants.USER_EMAIL_FIREBASE),
                (String) map.get(Constants.USER_PHOTO_FIREBASE),
                (boolean)map.get(Constants.FRIEND_INVITE_FIREBASE));
        return friendInformation;
    }


}
