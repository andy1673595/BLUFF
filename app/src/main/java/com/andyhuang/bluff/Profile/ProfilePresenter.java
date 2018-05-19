package com.andyhuang.bluff.Profile;

import com.andyhuang.bluff.Object.GameResult;

public class ProfilePresenter implements ProfileContract.Presenter {
    GameResult mGameResult = new GameResult();
    ProfileContract.View mProfileView;
    ProfileFirebaseHelper mFirebaseHelper;
    public ProfilePresenter(ProfileContract.View view) {
        mProfileView = view;
        mProfileView.setPresenter(this);
        mFirebaseHelper = new ProfileFirebaseHelper();
    }

    @Override
    public void start() {

    }

    @Override
    public void loadUserData() {

    }

    @Override
    public void updateComment() {

    }
}
