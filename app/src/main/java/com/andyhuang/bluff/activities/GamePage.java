package com.andyhuang.bluff.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andyhuang.bluff.GamPage.GameEndDialog.ExitRoomCallback;
import com.andyhuang.bluff.GamPage.GameEndDialog.GameEndDialog;
import com.andyhuang.bluff.GamPage.GamePageContract;
import com.andyhuang.bluff.GamPage.GamePagePresenter;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.helper.CurrentInformation;

import java.util.ArrayList;
import java.util.List;

public class GamePage extends BaseActivity implements View.OnClickListener ,GamePageContract.View {
    private String roomID;
    private boolean isHost;
    private GamePagePresenter mPrsenter;
    private ImageView imageIncreaseDiceButton;
    private ImageView imageCatchButton;
    private ImageView imageReadyStateButton;
    private ImageView[] imageDiceArray;
    private int[] diceImageSourceArray;
    private int[] diceImageSourceForInfo;
    private ImageView imageHomeBackButton;
    private TextView textShowInformation;
    private GameEndDialog mGameEndDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);
        imageIncreaseDiceButton = (ImageView)findViewById(R.id.image_increae_dice);
        imageCatchButton = (ImageView)findViewById(R.id.image_catch);
        imageReadyStateButton = (ImageView)findViewById(R.id.image_game_state);
        textShowInformation =(TextView)findViewById(R.id.text_show_current_info);
        imageDiceArray = new ImageView[]{
                (ImageView)findViewById(R.id.image_table_dice1),
                (ImageView)findViewById(R.id.image_table_dice2),
                (ImageView)findViewById(R.id.image_table_dice3),
                (ImageView)findViewById(R.id.image_table_dice4),
                (ImageView)findViewById(R.id.image_table_dice5)};
        diceImageSourceArray = new int[] {R.drawable.table_dice1,R.drawable.table_dice2,R.drawable.table_dice3,
                    R.drawable.table_dice4,R.drawable.table_dice5,R.drawable.table_dice6};
        diceImageSourceForInfo = new int[] {R.drawable.dice1,R.drawable.dice2,R.drawable.dice3,
                R.drawable.dice4,R.drawable.dice5,R.drawable.dice6};

        imageHomeBackButton = (ImageView)findViewById(R.id.image_home_button_gamepage);
        imageIncreaseDiceButton.setOnClickListener(this);
        imageIncreaseDiceButton.setVisibility(View.INVISIBLE);
        imageCatchButton.setVisibility(View.INVISIBLE);
        imageCatchButton.setOnClickListener(this);
        imageReadyStateButton.setOnClickListener(this);
        imageHomeBackButton.setOnClickListener(this);
        //read bundle from MainActivity
        readIntent();
        mPrsenter = new GamePagePresenter(this);
        mPrsenter.init(roomID,isHost);
    }

    void readIntent() {
        Intent intent = this.getIntent();
        roomID = intent.getStringExtra("gameID");
        isHost = intent.getBooleanExtra("isHost",false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_game_state:
                mPrsenter.clickStateButton();
                break;
            case R.id.image_increae_dice:
                mPrsenter.increaseDice();
                break;
            case R.id.image_catch:
                mPrsenter.catchPlayer();
                break;
            case R.id.image_home_button_gamepage:
                break;
        }
    }

    @Override
    public void setPresenter(Object presenter) {
        mPrsenter = (GamePagePresenter) presenter;
    }

    @Override
    public void freshStateButtonUI(String buttonType) {
        switch (buttonType) {
            case "start":
                imageReadyStateButton.setImageResource(R.drawable.start_button_gamepage);
                break;
            case "wait host start":
                imageReadyStateButton.setImageResource(R.drawable.wait_button_gampage);
                break;
            case "ready":
                imageReadyStateButton.setImageResource(R.drawable.ready_button_gamepage);
                break;
            case "get ready":
                imageReadyStateButton.setImageResource(R.drawable.cancel_button_gamepage);
                break;
        }
    }

    @Override
    public void freshDiceUI(List<Integer> diceList) {
        for(int diceInView =0;diceInView<5;diceInView++) {
            imageDiceArray[diceInView].setImageResource(diceImageSourceArray[diceList.get(diceInView)-1]);
        }
    }

    @Override
    public void refreshCatchAndIncreaseUI(boolean increaseVisible, boolean catchVisible) {
        if(increaseVisible)  imageIncreaseDiceButton.setVisibility(View.VISIBLE);
        else  imageIncreaseDiceButton.setVisibility(View.INVISIBLE);

        if(catchVisible) imageCatchButton.setVisibility(View.VISIBLE);
        else imageCatchButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void freshRecentDiceUI(CurrentInformation currentInformation) {
        textShowInformation.setText(currentInformation.getRecentDiceNumber()+" 個");
        imageReadyStateButton.setImageResource(diceImageSourceForInfo[currentInformation.getRecentDiceType()-1]);
    }

    @Override
    public void showEndInformation(String endText) {
        mGameEndDialog = new GameEndDialog(this,endText,mExitRoomCallback);
        mGameEndDialog.show();
    }

    @Override
    public void resetView(boolean isNextPlayer) {
        imageReadyStateButton.setImageResource(R.drawable.ready_button_gamepage);
        imageCatchButton.setVisibility(View.INVISIBLE);
        textShowInformation.setText(" ");
        for(int i=0;i<5;i++) {
            imageDiceArray[i].setImageResource(R.drawable.table_random_dice);
        }
        if(isNextPlayer) {
            imageIncreaseDiceButton.setVisibility(View.VISIBLE);
        } else {
            imageIncreaseDiceButton.setVisibility(View.INVISIBLE);
        }

    }

    private ExitRoomCallback mExitRoomCallback = new ExitRoomCallback() {
        @Override
        public void exitRoom() {

        }
    };
}
