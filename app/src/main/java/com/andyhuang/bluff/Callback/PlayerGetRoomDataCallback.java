package com.andyhuang.bluff.Callback;

import com.andyhuang.bluff.GamPage.GameObject.Gamer;

import java.util.List;

public interface PlayerGetRoomDataCallback {
    void returnGamerList(List<Gamer> gamerList);
}
