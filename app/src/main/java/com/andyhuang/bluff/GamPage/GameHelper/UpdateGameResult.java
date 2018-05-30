package com.andyhuang.bluff.GamPage.GameHelper;

import com.andyhuang.bluff.GamPage.GameObject.GameEndInformation;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.Util.Constants;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;

public class UpdateGameResult {
    Firebase userDataRef = new Firebase("https://myproject-556f6.firebaseio.com/userData/");
    public void updateToFirebase(List<Gamer> gamerList, final GameEndInformation gameEndInformation) {
        for (final Gamer gamer :gamerList) {
            userDataRef.child(gamer.getUserUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //winner add win result record
                    if(dataSnapshot.getKey().equals(gameEndInformation.getWinnerUID())) {
                        int countWinTimes = (int)(long)dataSnapshot.child(Constants.GAME_RESULT)
                                .child(Constants.WIN_TIMES).getValue();
                        int countLoseTimes = (int)(long)dataSnapshot.child(Constants.GAME_RESULT)
                                .child(Constants.WIN_TIMES).getValue();
                        //update win times +1
                        userDataRef.child(gamer.getUserUID()).child(Constants.GAME_RESULT)
                                .child(Constants.WIN_TIMES).setValue(countWinTimes+1);

                    } else {
                        int countWinTimes = (int)(long)dataSnapshot.child(Constants.GAME_RESULT)
                                .child(Constants.WIN_TIMES).getValue();
                        int countLoseTimes = (int)(long)dataSnapshot.child(Constants.GAME_RESULT)
                                .child(Constants.LOSE_TIMES).getValue();
                        //update lose times +1
                        userDataRef.child(gamer.getUserUID()).child(Constants.GAME_RESULT)
                                .child(Constants.LOSE_TIMES).setValue(countLoseTimes+1);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }

    public void updateOnlyTimesToFirebase(List<Gamer> gamerList) {
        for (final Gamer gamer :gamerList) {
            userDataRef.child(gamer.getUserUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int countTotalTimes =(int)(long)dataSnapshot.child(Constants.GAME_RESULT)
                            .child(Constants.TOTAL_TIMES).getValue();
                    //total times +1
                    userDataRef.child(gamer.getUserUID()).child(Constants.GAME_RESULT)
                            .child(Constants.TOTAL_TIMES).setValue(countTotalTimes+1);
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }
}
