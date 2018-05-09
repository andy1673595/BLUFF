package com.andyhuang.bluff.Callback;

import com.andyhuang.bluff.Object.Gamer;

import java.util.List;

public interface PlayerGetRoomDataCallback {
    void returnGamerList(List<Gamer> gamerList);
}
