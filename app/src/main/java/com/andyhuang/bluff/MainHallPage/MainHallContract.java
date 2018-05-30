package com.andyhuang.bluff.MainHallPage;

import com.andyhuang.bluff.BasePresenter;
import com.andyhuang.bluff.BaseView;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.Object.UserDataForRank;

import java.util.LinkedList;
import java.util.List;

public interface MainHallContract {
    interface view extends BaseView {
        void freshUI(LinkedList<UserDataForRank> userListWinRank, int type);
    }
    interface presenter extends BasePresenter{
        void getRankDataFromFirebase();
    }
}
