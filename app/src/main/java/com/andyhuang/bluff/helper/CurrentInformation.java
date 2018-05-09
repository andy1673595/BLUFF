package com.andyhuang.bluff.helper;
import com.andyhuang.bluff.Util.Constants;
public class CurrentInformation {
    public String currentPlayer = Constants.NODATA;
    public String recentPlayer = Constants.NODATA;
    public int recentDiceType = 0;
    public int recentDiceNumber = 0;

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
