package com.andyhuang.bluff.GamPage.GameObject;
import com.andyhuang.bluff.Constant.Constants;
public class CurrentInformation {
    public int recentDiceType = 0;
    public int recentDiceNumber = 0;
    public String currentPlayer = Constants.NODATA;
    public String recentPlayer = Constants.NODATA;

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getRecentPlayer() {
        return recentPlayer;
    }

    public void setRecentPlayer(String recentPlayer) {
        this.recentPlayer = recentPlayer;
    }

    public int getRecentDiceType() {
        return recentDiceType;
    }

    public void setRecentDiceType(int recentDiceType) {
        this.recentDiceType = recentDiceType;
    }

    public int getRecentDiceNumber() {
        return recentDiceNumber;
    }

    public void setRecentDiceNumber(int recentDiceNumber) {
        this.recentDiceNumber = recentDiceNumber;
    }
}
