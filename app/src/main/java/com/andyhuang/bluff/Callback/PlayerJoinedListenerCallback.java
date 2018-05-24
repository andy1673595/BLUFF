package com.andyhuang.bluff.Callback;

import com.andyhuang.bluff.GamPage.GameObject.Gamer;

import java.util.ArrayList;

public interface PlayerJoinedListenerCallback {
    void freshShowPlayerCountUI(ArrayList<Gamer> gamersJoinedListInput,Gamer newGamer);
}
