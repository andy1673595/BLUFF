package com.andyhuang.bluff.GamPage.GameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dice {
    List<Integer> numberList = new ArrayList<>();
    List<Integer> totalList = new ArrayList<>();
    //get a new set of dices
    public void getNewDice() {
        numberList = new ArrayList<>();
        long time = System.currentTimeMillis();
        Random rnd = new Random();
        rnd.setSeed(time);
        for(int i=0;i<5;i++) {
            numberList.add((rnd.nextInt(6))+1);
        }
    }

    public List<Integer> getList() {
        return numberList;
    }
    //get the list of count of each number
    public List<Integer> getTotalList(List<List<Integer>> diceListForEachPlayer) {
        initTotalList();
        for(List<Integer> diceList : diceListForEachPlayer) {
            for(int i =0;i<5;i++) {
                int numberType = Integer.parseInt(String.valueOf(diceList.get(i)));
                //increase dice tatal
                int count =Integer.parseInt(String.valueOf(totalList.get(numberType-1)));
                totalList.set(numberType-1,count+1);
            }
        }
        return totalList;
    }

    public void initTotalList() {
        if(totalList.size()>0) {
            //total list is already be create , reset all elements to zero
            for (int i=0;i<6;i++) {
                totalList.set(i,0);
            }
        }else {
            //first time, create a total list with all elements default value zero
            for (int i=0;i<6;i++) {
                totalList.add(0);
            }
        }
    }
}
