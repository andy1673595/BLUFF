package com.andyhuang.bluff.RankPage;

import com.andyhuang.bluff.Object.UserDataForRank;
import com.andyhuang.bluff.Constant.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.LinkedList;

public class RankPageFirebaseHelper {
    RankPagePresenter mPresenter;
    private Firebase userDataRef = new Firebase("https://myproject-556f6.firebaseio.com/userData/");
    //list for three type rank
    LinkedList<UserDataForRank> userListWinTimesRank = new LinkedList<>();
    LinkedList<UserDataForRank> userListTotalRank= new LinkedList<>();
    LinkedList<UserDataForRank> userListRateRank= new LinkedList<>();
    public RankPageFirebaseHelper(RankPagePresenter presenter) {
        mPresenter = presenter;
    }

    public void getRankData(final GetRankDataCallback callback) {
        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot userDataSnapshot:dataSnapshot.getChildren()) {
                    if(userDataSnapshot.child(Constants.GAME_RESULT).exists()) {
                        String userName = userDataSnapshot.child(Constants.USER_NAME_FIREBASE).getValue(String.class);
                        String userEmail = userDataSnapshot.child(Constants.USER_EMAIL_FIREBASE).getValue(String.class);
                        String userPhotoURL = userDataSnapshot.child(Constants.USER_PHOTO_FIREBASE).getValue(String.class);
                        String userUID = userDataSnapshot.getKey();
                        int countWinTimes = (int)(long)userDataSnapshot.child(Constants.GAME_RESULT)
                                .child(Constants.WIN_TIMES).getValue();
                        int countLoseTimes = (int)(long)userDataSnapshot.child(Constants.GAME_RESULT)
                                .child(Constants.LOSE_TIMES).getValue();
                        int countTotalTimes =(int)(long)userDataSnapshot.child(Constants.GAME_RESULT)
                                .child(Constants.TOTAL_TIMES).getValue();
                        Double winRateDoubleType =0.0;
                        if ((countWinTimes+countLoseTimes)>=5) {
                            winRateDoubleType = Double.valueOf(countWinTimes)
                                    /Double.valueOf((countLoseTimes +countWinTimes));
                            winRateDoubleType *= 100;
                        }else {
                            winRateDoubleType = 0.0;
                        }
                        UserDataForRank userData = new UserDataForRank(userEmail,userName,userPhotoURL,userUID
                                ,countTotalTimes,countWinTimes,winRateDoubleType);
                        sortData(userData);
                    }
                }
                callback.completedGetRankData(userListWinTimesRank,userListTotalRank,userListRateRank);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void sortData(UserDataForRank userInput) {
        //insert first
        if(userListWinTimesRank.size() ==0) {
            userListWinTimesRank.add(userInput);
            userListRateRank.add(userInput);
            userListTotalRank.add(userInput);
            return;
        }
        sortTotalTimesList(userInput);
        sortWinRateList(userInput);
        sortWinTimesList(userInput);
    }


    private void sortTotalTimesList(UserDataForRank userInput) {
        for( int number = 0; number<3 ; number++) {
            if(userInput.totalTimes > userListTotalRank.get(number).totalTimes) {
                userListTotalRank.add(number,userInput);
                break;
            } else if(userListTotalRank.size()-1 ==  number) {
                //touch the last of list , add the user into list because size of list is less than 3
                userListTotalRank.add(number+1,userInput);
                break;
            }
        }
    }
    private void sortWinRateList(UserDataForRank userInput) {
        //check for winRateRank
        for( int number = 0; number<3 ; number++) {
            if(userInput.winRate > userListRateRank.get(number).winRate) {
                userListRateRank.add(number,userInput);
                break;
            } else if(userListRateRank.size()-1 ==  number) {
                //touch the last of list , add the user into list because size of list is less than 3
                userListRateRank.add(number+1,userInput);
                break;
            }
        }
    }
    private void sortWinTimesList(UserDataForRank userInput) {
        //check for winTotalTimes
        for( int number = 0; number<3 ; number++) {
            if(userInput.winTimes > userListWinTimesRank.get(number).winTimes) {
                userListWinTimesRank.add(number,userInput);
                break;
            } else if(userListWinTimesRank.size()-1 ==  number) {
                //touch the last of list , add the user into list because size of list is less than 3
                userListWinTimesRank.add(number+1,userInput);
                break;
            }
        }
    }

}
