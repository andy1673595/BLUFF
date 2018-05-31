package com.andyhuang.bluff.RankPage;

import com.andyhuang.bluff.Object.UserDataForRank;

import java.util.LinkedList;
import java.util.List;

public class RankPagePresenter implements RankPageContract.presenter {
    RankPageContract.view mainHallview;
    RankPageFirebaseHelper mFirebaseHelper;
    public RankPagePresenter(RankPageContract.view view) {
        mainHallview = view;
        mFirebaseHelper = new RankPageFirebaseHelper(this);
    }
    @Override
    public void start() {

    }

    @Override
    public void getRankDataFromFirebase() {
        mFirebaseHelper.getRankData(mGetRankDataCallback);
    }

    private GetRankDataCallback mGetRankDataCallback = new GetRankDataCallback() {
        @Override
        public void completedGetRankData(LinkedList<UserDataForRank> userListWinRank, LinkedList<UserDataForRank> userListTotalRank, LinkedList<UserDataForRank> userListRateRank) {
            List<UserDataForRank> winTimesList = userListWinRank.subList(0,2);
            for(int i =0;i<3;i++) {
                //get not effect(total times < 5) win rate
                if(userListRateRank.get(i).winRate == 0) break;
                List<UserDataForRank> rateList = userListRateRank.subList(0,i);
            }
            List<UserDataForRank> TotalTimeList = userListTotalRank.subList(0,2);
            mainHallview.freshUI(userListTotalRank,0);
            mainHallview.freshUI(userListRateRank,1);
            mainHallview.freshUI(userListWinRank,2);
        }
    };
}
