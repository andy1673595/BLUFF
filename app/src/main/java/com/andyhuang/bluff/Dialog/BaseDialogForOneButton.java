package com.andyhuang.bluff.Dialog;

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

import com.andyhuang.bluff.Constant.Constants;
import com.andyhuang.bluff.R;

import static com.andyhuang.bluff.helper.ImageRounder.getRoundedCornerBitmap;

public class BaseDialogForOneButton extends Dialog implements View.OnClickListener {
    private Button buttonConfirm;
    private TextView textErrorMessage;
    private TextView textTitle;
    private ImageView imageBackground;
    public BaseDialogForOneButton(@NonNull Context context,String title,String message) {
        super(context, R.style.MyDialogStyle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.base_dialog_one_button);
        //set round Corner
        imageBackground = findViewById(R.id.base_dialog_one_button_background);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.increase_dialog_back_2);
        imageBackground.setImageBitmap(getRoundedCornerBitmap(bitmap, Constants.DIALOG_RADIUS));

        buttonConfirm = findViewById(R.id.button_base_dialog_one_button);
        textErrorMessage = findViewById(R.id.text_content_base_dialog_one_button);
        textTitle = findViewById(R.id.text_base_dialog_one_button_title);
        textErrorMessage.setText(message);
        textTitle.setText(title);
        buttonConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
