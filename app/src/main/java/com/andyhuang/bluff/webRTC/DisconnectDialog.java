package com.andyhuang.bluff.webRTC;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.andyhuang.bluff.Callback.DisconnectDialogCallback;
import com.andyhuang.bluff.R;

public class DisconnectDialog extends Dialog implements View.OnClickListener{
    private Button comfirmButton;
    private DisconnectDialogCallback mCallback;
    public DisconnectDialog(@NonNull Context context, DisconnectDialogCallback callback) {
        super(context, R.style.MyDialogStyle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.disconnect_dialog_layout);
        mCallback = callback;
        comfirmButton = findViewById(R.id.button_confirm_disconnect);
        comfirmButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        mCallback.confirm();
        dismiss();
    }


}
