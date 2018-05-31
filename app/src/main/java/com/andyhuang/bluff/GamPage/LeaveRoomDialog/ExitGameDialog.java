package com.andyhuang.bluff.GamPage.LeaveRoomDialog;

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
import com.andyhuang.bluff.Util.Constants;

import static com.andyhuang.bluff.helper.ImageRounder.getRoundedCornerBitmap;

public class ExitGameDialog extends Dialog implements View.OnClickListener{
    private ExitRoomCallback callback;
    private Button exitButton;
    private Button returnButton;
    private ImageView imageBackground;
    public ExitGameDialog(@NonNull Context context, ExitRoomCallback callbackInput) {
        super(context, R.style.MyDialogStyle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exit_game_dialog);
        //set round Corner
        imageBackground = findViewById(R.id.exit_room_dialog_background);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.increase_dialog_back_2);
        imageBackground.setImageBitmap(getRoundedCornerBitmap(bitmap, Constants.DIALOG_RADIUS));

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
