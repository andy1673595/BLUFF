package com.andyhuang.bluff.GamPage.IncreaseDiceDialog;

public class IncreasrDiceDialogPresenter implements IncreaseDiceDialogContract.Presenter {
    IncreaseDiceDialogContract.View dialogView;
    int diceType = 1;
    int diceCount = 1;
    public IncreasrDiceDialogPresenter(IncreaseDiceDialogContract.View dialogViewInput) {
        dialogView = dialogViewInput;
    }

    @Override
    public void chooseDiceNumber() {
        //diceType 1~6
        diceType++;
        diceType = (diceType>6)?1:diceType;
        //Array form 0~5
        dialogView.showDiceNumber(diceType-1);
    }

    @Override
    public void changeNumberCount(int numberCount) {
        //dicecount 1~N
        diceCount += numberCount;
        diceCount = (diceCount<1)?1:diceCount;
        //refresh count UI
        dialogView.showNumberCountChooseDialog(" X "+diceCount);
    }

    @Override
    public void clickOKButton() {

    }

    @Override
    public boolean isChooseLegal(int diceNumber, int numberCount) {
        return false;
    }

    @Override
    public void start() {

    }
}
