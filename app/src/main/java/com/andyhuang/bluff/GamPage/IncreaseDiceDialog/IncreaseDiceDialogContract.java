package com.andyhuang.bluff.GamPage.IncreaseDiceDialog;

import com.andyhuang.bluff.helper.CurrentInformation;

public interface IncreaseDiceDialogContract {
    interface Presenter {
        void chooseDiceNumber(int diceNumber);
        void chooseNumberCount(int numberCount);
        boolean isChooseLegal(int diceNumber,int numberCount);
    }
    interface View {
        void showNumberCountChooseDialog();
        void showError();
    }
}
