package com.andyhuang.bluff.FriendPage;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.andyhuang.bluff.R;

public class IniviteErrorDialog extends Dialog implements View.OnClickListener{
    private Button buttonConfirm;
    private TextView textErrorMessage;
    public IniviteErrorDialog(@NonNull Context context,String errorMessage) {
        super(context, R.style.MyDialogStyle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.invite_error_dialog);
        buttonConfirm = findViewById(R.id.button_confirm_invite_error);
        textErrorMessage = findViewById(R.id.text_error_invite);
        textErrorMessage.setText(errorMessage);
        buttonConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
