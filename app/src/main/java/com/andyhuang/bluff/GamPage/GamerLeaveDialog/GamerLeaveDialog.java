package com.andyhuang.bluff.GamPage.GamerLeaveDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.andyhuang.bluff.GamPage.GameEndDialog.ExitRoomCallback;
import com.andyhuang.bluff.R;

public class GamerLeaveDialog extends Dialog implements View.OnClickListener {
    ExitRoomCallback mCallback;
    private Button buttonConfirm;
    public GamerLeaveDialog(@NonNull Context context, ExitRoomCallback callbackInput) {
        super(context, R.style.MyDialogStyle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gamer_leave_dialog);
        buttonConfirm = findViewById(R.id.button_confirm_gamer_leave);
        buttonConfirm.setOnClickListener(this);
        mCallback =callbackInput;
    }

    @Override
    public void onClick(View v) {
        mCallback.exitRoom();
    }
}
