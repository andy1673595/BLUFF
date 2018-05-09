package com.andyhuang.bluff.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.andyhuang.bluff.GamPage.GamePageContract;
import com.andyhuang.bluff.GamPage.GamePagePresenter;
import com.andyhuang.bluff.R;

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
    private ImageView imageHomeBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);
        imageIncreaseDiceButton = (ImageView)findViewById(R.id.image_increae_dice);
        imageCatchButton = (ImageView)findViewById(R.id.image_catch);
        imageReadyStateButton = (ImageView)findViewById(R.id.image_game_state);
        imageDiceArray = new ImageView[]{
                (ImageView)findViewById(R.id.image_table_dice1),
                (ImageView)findViewById(R.id.image_table_dice2),
                (ImageView)findViewById(R.id.image_table_dice3),
                (ImageView)findViewById(R.id.image_table_dice4),
                (ImageView)findViewById(R.id.image_table_dice5)};
        diceImageSourceArray = new int[] {R.drawable.table_dice1,R.drawable.table_dice2,R.drawable.table_dice3,
                    R.drawable.table_dice4,R.drawable.table_dice5,R.drawable.table_dice6};

        imageHomeBackButton = (ImageView)findViewById(R.id.image_home_button_gamepage);
        imageIncreaseDiceButton.setOnClickListener(this);
        imageIncreaseDiceButton.setVisibility(View.INVISIBLE);
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

    }

    @Override
    public void freshStateButtonUI(String buttonType) {
        switch (buttonType) {
            case "start":
                imageReadyStateButton.setImageResource(R.drawable.start_button_gamepage);
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
    public void setCurrentPlayerUI() {
        imageIncreaseDiceButton.setVisibility(View.VISIBLE);
        imageCatchButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setOtherPlayerUI() {
        imageIncreaseDiceButton.setVisibility(View.INVISIBLE);
        imageCatchButton.setVisibility(View.VISIBLE);
    }
    @Override
    public void setRecentPlayerUI() {
        imageIncreaseDiceButton.setVisibility(View.INVISIBLE);
        imageCatchButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void freshRecentDiceUI(int diceType, int number) {

    }

    @Override
    public void showEndInformation(String endText) {

    }
}
