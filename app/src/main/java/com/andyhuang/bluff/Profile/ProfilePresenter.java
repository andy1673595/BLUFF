package com.andyhuang.bluff.Profile;


import android.net.Uri;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.Callback.GameResultCallback;
import com.andyhuang.bluff.Callback.ProfileUserDataCallback;
import com.andyhuang.bluff.Object.GameResult;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Constant.Constants;

public class ProfilePresenter implements ProfileContract.Presenter {
    ProfileContract.View mProfileView;
    ProfileFirebaseHelper mFirebaseHelper;
    String mUserUID;
    public ProfilePresenter(ProfileContract.View view,String userUID) {
        mProfileView = view;
        mProfileView.setPresenter(this);
        mFirebaseHelper = new ProfileFirebaseHelper();
        mUserUID = userUID;
        //this is friend profile ,set modify button INVISIBLE
        if(!userUID.equals(UserManager.getInstance().getUserUID())) {
            mProfileView.setButtonInvisible();
        }
    }

    @Override
    public void start() {}

    @Override
    public void loadUserData() {
        mFirebaseHelper.getGameResultData(mUserUID,mGameResultCallback);
        mFirebaseHelper.getUserData(mUserUID,mProfileUserDataCallback);
    }

    @Override
    public void updateComment(String commentAfterModify) {
        mFirebaseHelper.updateComment(mUserUID,commentAfterModify);
    }

    @Override
    public void changeUserPhoto(Uri newPhotoUri) {
        mFirebaseHelper.updateUserPhotoAfterChanged(newPhotoUri);

    }

    GameResultCallback mGameResultCallback = new GameResultCallback() {
        @Override
        public void getGameResultData(GameResult gameResult) {
            String totalTimes = ""+gameResult.getTotalTimes();
            String timesForTwoPersonGame = ""+(gameResult.getLoseTimes()+gameResult.getWinTimes());
            Double winRateDoubleType =0.0;
            if ((gameResult.getLoseTimes()+gameResult.getWinTimes())>0) {
                winRateDoubleType = Double.valueOf(gameResult.getWinTimes())/Double.valueOf((gameResult.getLoseTimes()+gameResult.getWinTimes()));
                winRateDoubleType *= 100;
            }
            String winRate = String.format("%.1f", winRateDoubleType);
            mProfileView.setGameResultToUI(totalTimes,timesForTwoPersonGame,winRate);
        }
    };

    ProfileUserDataCallback mProfileUserDataCallback = new ProfileUserDataCallback() {
        @Override
        public void userDataReadCompleted(String userName, String userEmail, String userPhotoUrl, String userComment) {
            if(userComment.equals(Constants.NODATA)) {
                userComment = Bluff.getContext().getString(R.string.comment_default);
            }
            mProfileView.setUserDataToUI(userName,userEmail,userPhotoUrl,userComment);
        }
    };
}
