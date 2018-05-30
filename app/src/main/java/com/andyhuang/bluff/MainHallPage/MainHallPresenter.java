package com.andyhuang.bluff.MainHallPage;

import com.andyhuang.bluff.Object.UserDataForRank;
import com.andyhuang.bluff.activities.MainHallPage;

import java.util.LinkedList;
import java.util.List;

public class MainHallPresenter implements MainHallContract.presenter {
    MainHallContract.view mainHallview;
    MainHallFirebaseHelper mFirebaseHelper;
    public MainHallPresenter(MainHallContract.view view) {
        mainHallview = view;
        mFirebaseHelper = new MainHallFirebaseHelper(this);
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
