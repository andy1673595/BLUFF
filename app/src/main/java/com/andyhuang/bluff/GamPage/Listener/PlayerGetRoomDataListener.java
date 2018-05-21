package com.andyhuang.bluff.GamPage.Listener;

import com.andyhuang.bluff.Callback.PlayerGetRoomDataCallback;
import com.andyhuang.bluff.GamPage.GameHelper.GameFirebaseHelper;
import com.andyhuang.bluff.GamPage.GamePageContract;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.Util.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class PlayerGetRoomDataListener implements ValueEventListener {
    private List<Gamer> gamerList;
    private GameFirebaseHelper mGameFirebaseHelper;
    private boolean isHost;
    private GamePageContract.View gamePageView;
    PlayerGetRoomDataCallback callback;
    public PlayerGetRoomDataListener(List<Gamer> gamerListInput, GameFirebaseHelper helper,
                            boolean isHostInput, GamePageContract.View gamePageViewInput,
                            PlayerGetRoomDataCallback callbackInput) {
        gamerList = new LinkedList<Gamer>();
        mGameFirebaseHelper = helper;
        isHost = isHostInput;
        gamePageView =gamePageViewInput;
        callback= callbackInput;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            for (DataSnapshot gamerData:dataSnapshot.getChildren()) {
                Gamer gamer = new Gamer((String) gamerData.child(Constants.USER_UID_FIREBASE).getValue(),
                        (String)gamerData.child("userPhotoURL").getValue(),
                        (String)gamerData.child(Constants.USER_EMAIL_FIREBASE).getValue()) ;
                gamer.setUserName((String)gamerData.child(Constants.USER_NAME_FIREBASE).getValue());
                    gamerList.add(gamer);
            }
            //tell server have read data
            mGameFirebaseHelper.setCurrentState(Constants.COMPLETED_READ_INIT);
            //if I'm host , start to listen player current state
            if(isHost) mGameFirebaseHelper.hostListenPlayerCurrentState();
            //set button to ready
            gamePageView.freshStateButtonUI(Constants.BUTTON_READY);
            //return gamerlist
            callback.returnGamerList(gamerList);
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
