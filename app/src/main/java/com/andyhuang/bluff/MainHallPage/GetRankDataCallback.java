package com.andyhuang.bluff.MainHallPage;

import com.andyhuang.bluff.Object.UserDataForRank;

import java.util.LinkedList;

public interface GetRankDataCallback {
    void completedGetRankData(LinkedList<UserDataForRank> userListWinRank,
            LinkedList<UserDataForRank> userListTotalRank,
            LinkedList<UserDataForRank> userListRateRank);
}
