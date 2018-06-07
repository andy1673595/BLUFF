package com.andyhuang.bluff.GamPage.Listener;

import com.andyhuang.bluff.Callback.DiceTotalListenerCallback;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DiceTotalListener implements ValueEventListener {
    DiceTotalListenerCallback callback;
    List<Integer> diceTotal = new ArrayList<>();

    public DiceTotalListener(DiceTotalListenerCallback callbackInput) {
        callback = callbackInput;
    }
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            for (DataSnapshot diceData : dataSnapshot.getChildren()) {
                diceTotal.add (Integer.parseInt(String.valueOf((long) diceData.getValue())));
            }
            callback.getDiceTotalList(diceTotal);
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
    }
}
