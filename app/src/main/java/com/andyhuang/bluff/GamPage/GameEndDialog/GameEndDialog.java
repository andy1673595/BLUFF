package com.andyhuang.bluff.GamPage.GameEndDialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.andyhuang.bluff.GamPage.GameHelper.GameEndInformation;
import com.andyhuang.bluff.GamPage.GamePagePresenter;
import com.andyhuang.bluff.R;

public class GameEndDialog extends Dialog implements View.OnClickListener{
    private TextView textInfo;
    private Button mButtonStartNewGame;
    private Button mButtonExitRoom;
    private ExitRoomCallback callback;
    public GameEndDialog(@NonNull Context context,
                         String gameEndStringInput,ExitRoomCallback callbackInput) {
        super(context, R.style.MyDialogStyle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.end_game_dialog);
        callback = callbackInput;
        textInfo = (TextView)findViewById(R.id.text_end_game_info);
        textInfo.setText(gameEndStringInput);
        mButtonStartNewGame = (Button)findViewById(R.id.button_new_game_dialog);
        mButtonExitRoom = (Button)findViewById(R.id.button_exit_room_dialog);
        mButtonStartNewGame.setOnClickListener(this);
        mButtonExitRoom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_new_game_dialog:
                dismiss();
                break;
            case R.id.button_exit_room_dialog:
                callback.exitRoom();
                dismiss();
                break;
        }
    }
}
