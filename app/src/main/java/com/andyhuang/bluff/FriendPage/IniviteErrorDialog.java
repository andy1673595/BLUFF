package com.andyhuang.bluff.FriendPage;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andyhuang.bluff.R;
import com.andyhuang.bluff.Constant.Constants;

import static com.andyhuang.bluff.helper.ImageRounder.getRoundedCornerBitmap;

public class IniviteErrorDialog extends Dialog implements View.OnClickListener{
    private Button buttonConfirm;
    private TextView textErrorMessage;
    private ImageView imageBackground;
    public IniviteErrorDialog(@NonNull Context context,String errorMessage) {
        super(context, R.style.MyDialogStyle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.invite_error_dialog);
        //set round Corner
        imageBackground = findViewById(R.id.invite_error_dialog_background);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.increase_dialog_back_2);
        imageBackground.setImageBitmap(getRoundedCornerBitmap(bitmap, Constants.DIALOG_RADIUS));

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
