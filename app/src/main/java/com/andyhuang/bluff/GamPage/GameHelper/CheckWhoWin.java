package com.andyhuang.bluff.GamPage.GameHelper;

import com.andyhuang.bluff.Object.Gamer;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.helper.CurrentInformation;

import java.util.List;

public class CheckWhoWin {
    List<Integer> diceTotalList;
    boolean hasTellOne;
    List<Gamer> gamerList;
    int total =0;
    GameEndInformation gameEndInformation;
    public CheckWhoWin(List<Integer> diceTotalListInput,boolean hasTellOneInput,List<Gamer> gamerListInput) {
        diceTotalList = diceTotalListInput;
        hasTellOne = hasTellOneInput;
        gamerList =gamerListInput;
    }

    public GameEndInformation getGameEndInformation (CurrentInformation currentInformation) {
        String gamerBeCatched ="";
        for(Gamer gamer : gamerList) {
            //find Email whose be can by UID
            if(gamer.getUserUID().equals(currentInformation.getRecentPlayer())) {
                gamerBeCatched = gamer.getUserEmail();
                break;
            }
        }



        if(checkCatchSuccess(currentInformation)) {
            //catch successful , I win
            String message = UserManager.getInstance().getEmail()+" 抓了 "+ gamerBeCatched +
                    "的"+currentInformation.getRecentDiceNumber()+"個"+currentInformation.getRecentDiceType()
                    +"\n總共"+ total+ "個" + currentInformation.getRecentDiceType();
            message += "\n"+gamerBeCatched+" 輸了罰一杯";
            gameEndInformation = new GameEndInformation(UserManager.getInstance().getUserUID(),
                    gamerBeCatched,message);
        } else {
            //catch fail , I lose
            String message = UserManager.getInstance().getEmail()+" 抓了 "+ gamerBeCatched +
                    "的"+currentInformation.getRecentDiceNumber()+"個"+currentInformation.getRecentDiceType()
                    +"\n總共"+ total+ "個" + currentInformation.getRecentDiceType();
            message += "\n" + UserManager.getInstance().getEmail() +" 輸了罰一杯";
            gameEndInformation = new GameEndInformation( gamerBeCatched,
                    UserManager.getInstance().getUserUID(),message);
        }

        return gameEndInformation;
    }

    private boolean checkCatchSuccess(CurrentInformation currentInformation) {
        int numberCatch = currentInformation.getRecentDiceType();
        int countCatch = currentInformation.getRecentDiceNumber();
        if(numberCatch !=1 && !hasTellOne) {
            int x =  diceTotalList.get(numberCatch-1);
            total = diceTotalList.get(numberCatch-1)+diceTotalList.get(0);
        }else {
            int x =  diceTotalList.get(numberCatch-1);
            total = diceTotalList.get(numberCatch-1);
        }
        if(total >= countCatch) return false;
        else return true;
    }
}
