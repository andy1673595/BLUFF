package com.andyhuang.bluff.GamPage.GamerLeaveDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.andyhuang.bluff.GamPage.GameEndDialog.ExitRoomCallback;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.Constant.Constants;

import static com.andyhuang.bluff.helper.ImageRounder.getRoundedCornerBitmap;

public class GamerLeaveDialog extends Dialog implements View.OnClickListener {
    ExitRoomCallback mCallback;
    private Button buttonConfirm;
    private ImageView imageBackground;
    public GamerLeaveDialog(@NonNull Context context, ExitRoomCallback callbackInput) {
        super(context, R.style.MyDialogStyle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gamer_leave_dialog);
        //set round Corner
        imageBackground = findViewById(R.id.image_player_left_dialog_back);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.increase_dialog_back_2);
        imageBackground.setImageBitmap(getRoundedCornerBitmap(bitmap, Constants.DIALOG_RADIUS));

        buttonConfirm = findViewById(R.id.button_confirm_player_left);
        buttonConfirm.setOnClickListener(this);
        mCallback =callbackInput;
    }

    @Override
    public void onClick(View v) {
        mCallback.exitRoom();
    }
}
