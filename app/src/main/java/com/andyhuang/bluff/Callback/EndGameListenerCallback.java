package com.andyhuang.bluff.Callback;

import com.andyhuang.bluff.GamPage.GameHelper.GameEndInformation;

public interface EndGameListenerCallback {
    void completedLoadInfo(GameEndInformation gameEndInformation);
}
