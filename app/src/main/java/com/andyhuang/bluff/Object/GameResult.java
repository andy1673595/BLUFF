package com.andyhuang.bluff.Object;

public class GameResult {
    public int loseTimes = 0;
    public int winTimes = 0;
    public int totalTimes =0;

    public int getLoseTimes() {
        return loseTimes;
    }

    public void setLoseTimes(int loseTimes) {
        this.loseTimes = loseTimes;
    }

    public int getWinTimes() {
        return winTimes;
    }

    public void setWinTimes(int winTimes) {
        this.winTimes = winTimes;
    }

    public int getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(int totalTimes) {
        this.totalTimes = totalTimes;
    }
}
