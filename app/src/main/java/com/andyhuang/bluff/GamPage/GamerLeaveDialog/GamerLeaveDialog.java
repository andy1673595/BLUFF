package com.andyhuang.bluff.GamPage.GamerLeaveDialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Button;

import com.andyhuang.bluff.GamPage.GameEndDialog.ExitRoomCallback;

public class GamerLeaveDialog extends Dialog {
    ExitRoomCallback mCallback;
    private Button buttonConfirm;
    public GamerLeaveDialog(@NonNull Context context, ExitRoomCallback callbackInput) {
        super(context);
        mCallback =callbackInput;
    }
}
