package com.andyhuang.bluff.FriendPage;

import com.andyhuang.bluff.Callback.GameInviteForFriendFragmentCallback;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class GameInviteListener implements com.google.firebase.database.ValueEventListener{
    private GameInviteForFriendFragmentCallback callback;
    private boolean isLast;
    public GameInviteListener(GameInviteForFriendFragmentCallback callbackInput,boolean isLastInput){
        callback =callbackInput;
        isLast = isLastInput;
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            for (com.google.firebase.database.DataSnapshot datafind : dataSnapshot.getChildren()) {
                String friendName = (String)datafind.child(Constants.USER_NAME_FIREBASE).getValue();
                String friendUID = (String)datafind.getKey();
                if(!(boolean)datafind.child(Constants.ONLINE_STATE).getValue()) {
                    //user is outline, don't send invite
                    String errorMessage = "玩家 "+friendName+"\n目前不在線上";
                    callback.showError(errorMessage);

                }else if((boolean) datafind.child(Constants.IS_GAMING).getValue()) {
                    //user is playing game, don't send invite
                    String errorMessage = "玩家 " + friendName + "\n目前正在進行一場遊戲";
                    callback.showError(errorMessage);

                }
                if(isLast) {
                    callback.openRoom();
                }
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
