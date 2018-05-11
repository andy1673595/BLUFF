package com.andyhuang.bluff.GamPage.LeaveRoomDialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.andyhuang.bluff.GamPage.GameEndDialog.ExitRoomCallback;
import com.andyhuang.bluff.R;

public class ExitGameDialog extends Dialog implements View.OnClickListener{
    ExitRoomCallback callback;
    Button exitButton;
    Button returnButton;
    public ExitGameDialog(@NonNull Context context, ExitRoomCallback callbackInput) {
        super(context, R.style.MyDialogStyle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exit_game_dialog);
        exitButton = (Button)findViewById(R.id.button_exit_game_confirm_dialog);
        returnButton = (Button)findViewById(R.id.button_return_room);
        exitButton.setOnClickListener(this);
        returnButton.setOnClickListener(this);
        callback =callbackInput;
    }

    @Override
    public void onClick(View v) {
        switch ((v.getId())) {
            case R.id.button_exit_game_confirm_dialog:
                callback.exitRoom();
                dismiss();
                break;
            case R.id.button_return_room:
                dismiss();
                break;

        }
    }
}
