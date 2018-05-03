package com.andyhuang.bluff.GameInviteDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;

import com.andyhuang.bluff.BluffPresenter;
import com.andyhuang.bluff.Object.Gamer;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.Util.Constants;

public class GameInviteDialog extends Dialog implements View.OnClickListener{
    private GameInvitePrsenter mPresenter;
    private Context mContext;
    private BluffPresenter bluffPresenter;
    private Gamer inviter;
    private String roomID;
    public GameInviteDialog(@NonNull Context context, BluffPresenter bluffPresenterInput, Gamer inviterInput,String roomIDInput) {
        super(context);
        inviter = inviterInput;
        roomID = roomIDInput;
        setContentView(R.layout.dialog_game_invite);
        mContext = context;
        bluffPresenter = bluffPresenterInput;
        mPresenter = new GameInvitePrsenter(this);
        ((ConstraintLayout)findViewById(R.id.constraintLayout_dialog_background)).setOnClickListener(this);
        ((ImageView)findViewById(R.id.image_ok_invite_game)).setOnClickListener(this);
        ((ImageView)findViewById(R.id.image_refuse_invite_game)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_ok_invite_game:
                mPresenter.acceptAndStartGame(inviter,roomID);
                dismiss();
                break;
            case R.id.image_refuse_invite_game:
              //  mPresenter.refuseInvite();
                dismiss();
                break;
            default:
              //  mPresenter.refuseInvite();
                dismiss();
                break;
        }

    }

    @Override
    public void dismiss() {
        bluffPresenter.removeGameInvite();
        mPresenter.removeInvite();
        super.dismiss();
    }
}
