package com.andyhuang.bluff.GamPage.GameInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameInitState {
    public List<Integer> diceTotalList = new ArrayList<>();
    public Map<String,List<Integer>> playerDiceList = new HashMap<>();

    public GameInitState(List<Integer> diceTotalList,
                         Map<String, List<Integer>> playerDiceList) {
        this.diceTotalList = diceTotalList;
        this.playerDiceList = playerDiceList;
    }


    public List<Integer> getDiceTotalList() {
        return diceTotalList;
    }

    public void setDiceTotalList(List<Integer> diceTotalList) {
        this.diceTotalList = diceTotalList;
    }

    public Map<String, List<Integer>> getPlayerDiceList() {
        return playerDiceList;
    }

    public void setPlayerDiceList(Map<String, List<Integer>> playerDiceList) {
        this.playerDiceList = playerDiceList;
    }
}
