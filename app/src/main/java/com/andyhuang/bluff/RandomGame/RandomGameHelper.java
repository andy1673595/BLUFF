package com.andyhuang.bluff.RandomGame;

import com.andyhuang.bluff.BluffPresenter;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Util.Constants;
import com.firebase.client.Firebase;

public class RandomGameHelper {
    BluffPresenter mBluffPresenter;
    Firebase randomGameRef;
    public RandomGameHelper(BluffPresenter presenter) {
        mBluffPresenter = presenter;
        randomGameRef = new Firebase("https://myproject-556f6.firebaseio.com/"+ Constants.RANDOM_GAME);
    }

    public void updateMyDataToRandomSequence(){
        //set my uid to sequence to tell that I'm ready for random games
        randomGameRef.setValue(UserManager.getInstance().getUserUID());
    }
}
