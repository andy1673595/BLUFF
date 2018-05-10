package com.andyhuang.bluff.GamPage.GameHelper;

import com.andyhuang.bluff.Util.Constants;

public class GameEndInformation {
    public String winnerUID = Constants.NODATA;
    public String loserUID = Constants.NODATA;
    public String textHowToEnd = Constants.NODATA;

    public String getWinnerUID() {
        return winnerUID;
    }

    public void setWinnerUID(String winnerUID) {
        this.winnerUID = winnerUID;
    }

    public String getLoserUID() {
        return loserUID;
    }

    public void setLoserUID(String loserUID) {
        this.loserUID = loserUID;
    }

    public String getTextHowToEnd() {
        return textHowToEnd;
    }

    public void setTextHowToEnd(String textHowToEnd) {
        this.textHowToEnd = textHowToEnd;
    }
}
