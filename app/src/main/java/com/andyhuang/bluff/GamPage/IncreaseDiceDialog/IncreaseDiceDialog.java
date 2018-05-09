package com.andyhuang.bluff.GamPage.IncreaseDiceDialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andyhuang.bluff.GamPage.GameFirebaseHelper;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.helper.CurrentInformation;

public class IncreaseDiceDialog extends Dialog
        implements IncreaseDiceDialogContract.View ,View.OnClickListener{
    private Context mContext;
    private TextView textInfoToPlayer;
    private TextView textDiceCount;
    private ImageView imageOk;
    private ImageView imageDiceNumber;
    private Button buttonIncreaseFive;
    private Button buttonIncreaseOne;
    private Button buttonDecreaseFive;
    private Button buttonDecreaseOne;
    private int[] diceImageSourceArray;
    private IncreasrDiceDialogPresenter mPresenter;

    public IncreaseDiceDialog(@NonNull Context context,GameFirebaseHelper helper) {
        super(context, R.style.MyDialogStyle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.increase_dice_dialog);
        mContext = context;
        textInfoToPlayer = (TextView)findViewById(R.id.text_increase_dice_dialog);
        textDiceCount = (TextView)findViewById(R.id.text_dice_count);
        imageOk = (ImageView)findViewById(R.id.image_ok_button_dice_dialog);
        imageDiceNumber = (ImageView)findViewById(R.id.image_dice_choose);
        buttonIncreaseFive = (Button)findViewById(R.id.button_puls_5);
        buttonDecreaseFive = (Button)findViewById(R.id.button_decrease_5);
        buttonIncreaseOne = (Button)findViewById(R.id.button_puls_1);
        buttonDecreaseOne = (Button)findViewById(R.id.button_decrase_1);
        imageOk.setOnClickListener(this);
        imageDiceNumber.setOnClickListener(this);
        buttonDecreaseOne.setOnClickListener(this);
        buttonIncreaseOne.setOnClickListener(this);
        buttonDecreaseFive.setOnClickListener(this);
        buttonIncreaseFive.setOnClickListener(this);
        diceImageSourceArray = new int[] {R.drawable.dice1,R.drawable.dice2,R.drawable.dice3,
                R.drawable.dice4,R.drawable.dice5,R.drawable.dice6};
        mPresenter = new IncreasrDiceDialogPresenter(this, helper);
    }


    @Override
    public void showNumberCountChooseDialog(String message) {
        textDiceCount.setText(message);
    }

    @Override
    public void showDiceNumber(int diceType) {
        imageDiceNumber.setImageResource(diceImageSourceArray[diceType]);
    }

    @Override
    public void showError(String message) {
        textInfoToPlayer.setText(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_ok_button_dice_dialog:
                mPresenter.clickOKButton();
                break;
            case R.id.image_dice_choose:
                mPresenter.chooseDiceNumber();
                break;
            case R.id.button_puls_5:
                mPresenter.changeNumberCount(5);
                break;
            case R.id.button_puls_1:
                mPresenter.changeNumberCount(1);
                break;
            case R.id.button_decrease_5:
                mPresenter.changeNumberCount(-5);
                break;
            case R.id.button_decrase_1:
                mPresenter.changeNumberCount(-1);
                break;
        }
    }

    @Override
    public void setPresenter(Object presenter) {
        mPresenter = (IncreasrDiceDialogPresenter) presenter;
    }
}
