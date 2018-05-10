package com.andyhuang.bluff.Callback;

import com.andyhuang.bluff.GamPage.GameHelper.CurrentStateHelper;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;

import java.util.List;

public interface RoomListenerCallback {
    void returnCurrentHelper(CurrentStateHelper helperBack,List<Gamer> gamerList);
}
