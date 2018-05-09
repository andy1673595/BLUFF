package com.andyhuang.bluff.GamPage.Listener;

import com.andyhuang.bluff.Callback.RoomListenerCallback;
import com.andyhuang.bluff.GamPage.GameFirebaseHelper;
import com.andyhuang.bluff.GamPage.GameHelper.CurrentStateHelper;
import com.andyhuang.bluff.Object.Gamer;
import com.andyhuang.bluff.Util.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RoomDataListener implements ValueEventListener {
    private List<Gamer> gamerList = new ArrayList<Gamer>();
    private Firebase gameRef;
    private CurrentStateHelper currentStateHelper;
    private GameFirebaseHelper firebaseHelper;
    RoomListenerCallback callback;

    public RoomDataListener(List<Gamer> gamerListInput,Firebase gameRefInput,
                            GameFirebaseHelper firebaseHelperInput,RoomListenerCallback callbackInput) {
        gamerList = gamerListInput;
        gameRef = gameRefInput;
        firebaseHelper = firebaseHelperInput;
        callback = callbackInput;
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            //read all player
            for (DataSnapshot gamerData : dataSnapshot.getChildren()) {

                Gamer gamer = new Gamer((String) gamerData.child(Constants.USER_UID_FIREBASE).getValue(),
                        (String)gamerData.child("userPhotoURL").getValue(),
                        (String)gamerData.child(Constants.USER_EMAIL_FIREBASE).getValue()) ;
                gamerList.add(gamer);
            }

            gameRef.child(Constants.GAMER_LIST).setValue(gamerList);
            //use total player to creat current state helper
            currentStateHelper = new CurrentStateHelper(firebaseHelper,gamerList.size());
            firebaseHelper.setGameState(Constants.READ_INIT_DATA);
            callback.returnCurrentHelper(currentStateHelper,gamerList);
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

    public CurrentStateHelper getCurrentStateHelper() {
        return currentStateHelper;
    }
}
