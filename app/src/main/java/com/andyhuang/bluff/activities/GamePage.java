package com.andyhuang.bluff.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.andyhuang.bluff.GamPage.GamePageContract;
import com.andyhuang.bluff.GamPage.GamePagePresent;
import com.andyhuang.bluff.GameInviteDialog.GameInviteContract;
import com.andyhuang.bluff.GameInviteDialog.GameInvitePrsenter;
import com.andyhuang.bluff.Object.GameRoom;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.Util.Constants;

import java.util.ArrayList;

public class GamePage extends BaseActivity implements View.OnClickListener ,GamePageContract.View {
    private String roomID;
    private boolean isHost;
    private GamePagePresent mPrsenter;
    private ImageView imageIncreaseDiceButton;
    private ImageView imageCatchButton;
    private ImageView imageReadyStateButton;
    private ImageView imageDice1;
    private ImageView imageDice2;
    private ImageView imageDice3;
    private ImageView imageDice4;
    private ImageView imageDice5;
    private ImageView imageHomeBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);
        imageIncreaseDiceButton = (ImageView)findViewById(R.id.image_increae_dice);
        imageCatchButton = (ImageView)findViewById(R.id.image_catch);
        imageReadyStateButton = (ImageView)findViewById(R.id.image_game_state);
        imageDice1 = (ImageView)findViewById(R.id.image_table_dice1);
        imageDice2 = (ImageView)findViewById(R.id.image_table_dice2);
        imageDice3 = (ImageView)findViewById(R.id.image_table_dice3);
        imageDice4 = (ImageView)findViewById(R.id.image_table_dice4);
        imageDice5 = (ImageView)findViewById(R.id.image_table_dice5);
        imageHomeBackButton = (ImageView)findViewById(R.id.image_home_button_gamepage);
        imageIncreaseDiceButton.setOnClickListener(this);
        imageCatchButton.setOnClickListener(this);
        imageReadyStateButton.setOnClickListener(this);
        imageHomeBackButton.setOnClickListener(this);
        //read bundle from MainActivity
        readIntent();
        mPrsenter = new GamePagePresent(this);
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
                break;
            case R.id.image_catch:
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
            case "cancel":
                imageReadyStateButton.setImageResource(R.drawable.cancel_button_gamepage);
                break;
        }
    }

    @Override
    public void freshDiceUI(ArrayList<Integer> diceList) {

    }

    @Override
    public void freshRecentDiceUI(int diceType, int number) {

    }

    @Override
    public void showEndInformation(String endText) {

    }
}
