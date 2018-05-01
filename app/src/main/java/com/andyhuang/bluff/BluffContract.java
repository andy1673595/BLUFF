package com.andyhuang.bluff;

public interface BluffContract {
    interface View extends BaseView {
        void showProfilePage();
        void showFriendPage();
        void showMainPage();

    }
    interface Presenter extends BasePresenter {
        void transToMainPage();
        void transToFriendPage();
        void transToProfilePage();
    }
}
