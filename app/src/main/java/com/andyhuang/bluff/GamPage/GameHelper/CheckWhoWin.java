package com.andyhuang.bluff.GamPage.GameHelper;

import com.andyhuang.bluff.GamPage.GameObject.CurrentInformation;
import com.andyhuang.bluff.GamPage.GameObject.GameEndInformation;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.User.UserManager;

import java.util.List;

public class CheckWhoWin {
    int total =0;
    boolean hasTellOne;
    List<Integer> diceTotalList;
    List<Gamer> gamerList;
    GameEndInformation gameEndInformation;

    public CheckWhoWin(List<Integer> diceTotalListInput,boolean hasTellOneInput,List<Gamer> gamerListInput) {
        diceTotalList = diceTotalListInput;
        hasTellOne = hasTellOneInput;
        gamerList =gamerListInput;
    }

    public GameEndInformation getGameEndInformation (CurrentInformation currentInformation) {
        String gamerBeCatched ="";
        String gamerrBeCatchedUID="";
        String messageTailPart="";
        for(Gamer gamer : gamerList) {
            //find Name whose be can by UID
            if(gamer.getUserUID().equals(currentInformation.getRecentPlayer())) {
                gamerBeCatched = gamer.getUserName();
                gamerrBeCatchedUID =gamer.getUserUID();
                break;
            }
        }

        if(checkCatchSuccess(currentInformation)) {
            //catch successful , I win
            messageTailPart = "\n"+gamerBeCatched+" 輸了罰一杯";
            createNewGameEndInformation(gamerrBeCatchedUID,UserManager.getInstance().getUserUID(),
                    messageTailPart,gamerBeCatched,currentInformation);

        } else {
            //catch fail , I lose
            messageTailPart = "\n" + UserManager.getInstance().getUserName() +" 輸了罰一杯";
            createNewGameEndInformation(UserManager.getInstance().getUserUID(),gamerrBeCatchedUID,
                    messageTailPart,gamerBeCatched,currentInformation);
        }

        return gameEndInformation;
    }

    private void createNewGameEndInformation(String loserUID,String winnerUID,String messageTailPart,
                                             String gamerBeCatched,CurrentInformation currentInformation) {
        String message = UserManager.getInstance().getUserName()+" 抓了 "+ gamerBeCatched +
                "的"+currentInformation.getRecentDiceNumber()+"個"+currentInformation.getRecentDiceType()
                +"\n總共"+ total+ "個" + currentInformation.getRecentDiceType();
        message += messageTailPart;
        gameEndInformation = new GameEndInformation();
        gameEndInformation.setLoserUID(loserUID);
        gameEndInformation.setWinnerUID(winnerUID);
        gameEndInformation.setTextHowToEnd(message);
    }

    //檢查是喊得贏還是抓得贏
    private boolean checkCatchSuccess(CurrentInformation currentInformation) {
        int numberCatch = currentInformation.getRecentDiceType();
        int countCatch = currentInformation.getRecentDiceNumber();
        //沒有喊過1 , 1可以當所有的數目
        if(numberCatch !=1 && !hasTellOne) {
            total = diceTotalList.get(numberCatch-1)+diceTotalList.get(0);
        }else {
            //喊過1了  只拿自己當總數
            total = diceTotalList.get(numberCatch-1);
        }
        if(total >= countCatch) return false;
        else return true;
    }


}
