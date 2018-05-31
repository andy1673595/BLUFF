package com.andyhuang.bluff.Dialog.WaitForRandomGameDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.andyhuang.bluff.Callback.WaitForRandomGameCallback;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.Util.Constants;
import static com.andyhuang.bluff.helper.ImageRounder.getRoundedCornerBitmap;

public class WaitForRandomGameDialog extends Dialog implements View.OnClickListener{
    private ImageView imageBackground;
    private Button cancelButton;
    private ConstraintLayout layoutFullBackground;
    private WaitForRandomGameCallback mCallback;
    public WaitForRandomGameDialog(@NonNull Context context, WaitForRandomGameCallback callback) {
        super(context, R.style.MyDialogStyle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wait_random_game_dialog);
        mCallback = callback;
        //set round Corner
        imageBackground = findViewById(R.id.image_wait_random_game_back);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.increase_dialog_back_2);
        imageBackground.setImageBitmap(getRoundedCornerBitmap(bitmap, Constants.DIALOG_RADIUS));
        cancelButton = findViewById(R.id.button_cancel_waiting);
        layoutFullBackground = findViewById(R.id.layout_wait_random_game_full_background);
        cancelButton.setOnClickListener(this);
        layoutFullBackground.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel_waiting:
                mCallback.cancelWaiting();
                dismiss();
                break;
            case R.id.layout_wait_random_game_full_background:
                //別做任何事情, 避免使用者點到其他地方跳出
                break;
        }
    }
}
