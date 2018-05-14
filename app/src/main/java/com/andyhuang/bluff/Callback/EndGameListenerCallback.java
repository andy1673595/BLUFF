package com.andyhuang.bluff.Callback;

import com.andyhuang.bluff.GamPage.GameObject.GameEndInformation;

public interface EndGameListenerCallback {
    void completedLoadInfo(GameEndInformation gameEndInformation);
}
