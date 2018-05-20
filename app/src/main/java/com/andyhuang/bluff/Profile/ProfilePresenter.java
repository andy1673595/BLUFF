package com.andyhuang.bluff.Profile;

import com.andyhuang.bluff.Callback.GameResultCallback;
import com.andyhuang.bluff.Object.GameResult;
import com.andyhuang.bluff.User.UserManager;

public class ProfilePresenter implements ProfileContract.Presenter {
    GameResult mGameResult = new GameResult();
    ProfileContract.View mProfileView;
    ProfileFirebaseHelper mFirebaseHelper;
    String myUID = UserManager.getInstance().getUserUID();
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
        mFirebaseHelper.getGameResultData(myUID,mGameResultCallback);
    }

    @Override
    public void updateComment() {

    }

    private GameResultCallback mGameResultCallback = new GameResultCallback() {
        @Override
        public void getGameResultData(GameResult gameResult) {
            String totalTimes = ""+gameResult.getTotalTimes();
            String timesForTwoPersonGame = ""+(gameResult.getLoseTimes()+gameResult.getWinTimes());
            Double winRateDoubleType =0.0;
            if ((gameResult.getLoseTimes()+gameResult.getWinTimes())>0) {
                winRateDoubleType = Double.valueOf(gameResult.getWinTimes()/(gameResult.getLoseTimes()+gameResult.getWinTimes()));
            }
            String winRate = ""+winRateDoubleType;
            mProfileView.setGameResultToUI(totalTimes,timesForTwoPersonGame,winRate);
        }
    };
}
