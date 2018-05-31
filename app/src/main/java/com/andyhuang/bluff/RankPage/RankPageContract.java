package com.andyhuang.bluff.RankPage;

import com.andyhuang.bluff.BasePresenter;
import com.andyhuang.bluff.BaseView;
import com.andyhuang.bluff.Object.UserDataForRank;

import java.util.LinkedList;

public interface RankPageContract {
    interface view extends BaseView {
        void freshUI(LinkedList<UserDataForRank> userListWinRank, int type);
    }
    interface presenter extends BasePresenter{
        void getRankDataFromFirebase();
    }
}
