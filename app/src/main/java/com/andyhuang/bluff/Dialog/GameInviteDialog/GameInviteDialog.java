package com.andyhuang.bluff.Dialog.GameInviteDialog;

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

import com.andyhuang.bluff.BluffPresenter;
import com.andyhuang.bluff.Object.InviteInformation;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.Constant.Constants;

import static com.andyhuang.bluff.helper.ImageRounder.getRoundedCornerBitmap;

public class GameInviteDialog extends Dialog implements View.OnClickListener{
    private InviteInformation mInviteInformation;
    private GameInvitePrsenter mPresenter;
    private Context mContext;
    private BluffPresenter bluffPresenter;
    private ImageView imageBackground;
    public GameInviteDialog(@NonNull Context context, BluffPresenter bluffPresenterInput,
                            InviteInformation inviteInformation) {
        super(context,R.style.MyDialogStyle);
        mInviteInformation = inviteInformation;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_game_invite);
        imageBackground = findViewById(R.id.image_invite_game_background);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.increase_dialog_back_2);
        imageBackground.setImageBitmap(getRoundedCornerBitmap(bitmap, Constants.DIALOG_RADIUS));
        mContext = context;
        bluffPresenter = bluffPresenterInput;
        mPresenter = new GameInvitePrsenter(this);
        ((Button)findViewById(R.id.button_accept_invite)).setOnClickListener(this);
        ((Button)findViewById(R.id.button_reject_invite)).setOnClickListener(this);
        ((TextView)findViewById(R.id.text_invite_info_dialog)).
                setText(inviteInformation.getUserName()+"\n邀請你進行吹牛");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_accept_invite:
                mPresenter.acceptAndStartGame(mInviteInformation);
                bluffPresenter.setGameInformationAndGetIntoRoom(mInviteInformation.getGameRoom(),0);
                dismiss();
                break;
            case R.id.button_reject_invite:
                 mPresenter.refuseInvite(mInviteInformation.getGameRoom());
                dismiss();
                break;
            default:
                dismiss();
                break;
        }

    }

    @Override
    public void dismiss() {
        bluffPresenter.removeGameInvite();
        mPresenter.removeInviteFromFirebase();
        super.dismiss();
    }
}
