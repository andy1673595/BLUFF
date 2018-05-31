package com.andyhuang.bluff.GamPage.GameEndDialog;

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
import com.andyhuang.bluff.Util.Constants;

import static com.andyhuang.bluff.helper.ImageRounder.getRoundedCornerBitmap;

public class GameEndDialog extends Dialog implements View.OnClickListener{
    private TextView textInfo;
    private Button mButtonStartNewGame;
    private Button mButtonExitRoom;
    private ExitRoomCallback callback;
    private ImageView imageBackground;
    public GameEndDialog(@NonNull Context context,
                         String gameEndStringInput,ExitRoomCallback callbackInput) {
        super(context, R.style.MyDialogStyle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.end_game_dialog);
        //set round Corner
        imageBackground = findViewById(R.id.image_game_end_dialog_background);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.increase_dialog_back_2);
        imageBackground.setImageBitmap(getRoundedCornerBitmap(bitmap, Constants.DIALOG_RADIUS));

        callback = callbackInput;
        textInfo = (TextView)findViewById(R.id.text_end_game_info);
        textInfo.setText(gameEndStringInput);
        mButtonStartNewGame = (Button)findViewById(R.id.button_new_game_dialog);
        mButtonExitRoom = (Button)findViewById(R.id.button_leave_room);
        mButtonStartNewGame.setOnClickListener(this);
        mButtonExitRoom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_new_game_dialog:
                dismiss();
                break;
            case R.id.button_leave_room:
                callback.exitRoom();
                dismiss();
                break;
        }
    }
}
