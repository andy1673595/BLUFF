package com.andyhuang.bluff;

import android.test.AndroidTestCase;

import com.andyhuang.bluff.GamPage.GameHelper.CheckWhoWin;
import com.andyhuang.bluff.GamPage.GameObject.CurrentInformation;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class BLUFFTest extends AndroidTestCase {
    public void test_add_first_1_second_2_equals_3(){
        //arrange
        List<Integer> diceTotalListInput = Arrays.asList(2,2,3,4,5,1);
        boolean hasTellOne = false;
        Gamer gamer1 = new Gamer(
                "UID","photoURL","Email","user1");
        Gamer gamer2 = new Gamer(
                "UID2","photoURL2","Email2","user2");
        List<Gamer> gamerList = Arrays.asList(gamer1,gamer2);
        CheckWhoWin checkWhoWin = new CheckWhoWin(diceTotalListInput,hasTellOne,gamerList);
        CurrentInformation currentInformation = new CurrentInformation();
        currentInformation.setCurrentPlayer("currentPlayer");
        currentInformation.setRecentPlayer("recentPlayer");
        currentInformation.setRecentDiceNumber(5);
        currentInformation.setRecentDiceType(4);
        //act
        boolean actual = checkWhoWin.checkCatchSuccess(currentInformation);
        boolean expect = false;

        //assert
        assertEquals(expect, actual);
    }
}
