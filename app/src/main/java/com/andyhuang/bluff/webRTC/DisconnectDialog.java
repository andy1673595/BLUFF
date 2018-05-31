package com.andyhuang.bluff.webRTC;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.andyhuang.bluff.Callback.DisconnectDialogCallback;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.Util.Constants;

import static com.andyhuang.bluff.helper.ImageRounder.getRoundedCornerBitmap;

public class DisconnectDialog extends Dialog implements View.OnClickListener{
    private Button comfirmButton;
    private DisconnectDialogCallback mCallback;
    private ImageView imageBackground;
    public DisconnectDialog(@NonNull Context context, DisconnectDialogCallback callback) {
        super(context, R.style.MyDialogStyle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.disconnect_dialog_layout);
        mCallback = callback;
        //set round Corner
        imageBackground = findViewById(R.id.image_player_left_dialog_back);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.increase_dialog_back_2);
        imageBackground.setImageBitmap(getRoundedCornerBitmap(bitmap, Constants.DIALOG_RADIUS));
        comfirmButton = findViewById(R.id.button_confirm_player_left);
        comfirmButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void dismiss() {
       mCallback.confirm();
       super.dismiss();
    }


}
