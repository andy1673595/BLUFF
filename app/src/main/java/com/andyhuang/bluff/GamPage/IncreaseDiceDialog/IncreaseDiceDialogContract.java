package com.andyhuang.bluff.GamPage.IncreaseDiceDialog;

import com.andyhuang.bluff.BasePresenter;
import com.andyhuang.bluff.BaseView;
import com.andyhuang.bluff.helper.CurrentInformation;

public interface IncreaseDiceDialogContract {
    interface Presenter extends BasePresenter{
        void chooseDiceNumber();
        void changeNumberCount(int numberCount);
        void clickOKButton();
        boolean isChooseLegal(int diceNumber,int numberCount);
    }
    interface View extends BaseView{
        void showNumberCountChooseDialog(String message);
        void showDiceNumber(int diceType);
        void showError();
    }
}
