package com.andyhuang.bluff.Dialog.GameInviteDialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andyhuang.bluff.BluffPresenter;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.R;

public class GameInviteDialog extends Dialog implements View.OnClickListener{
    private GameInvitePrsenter mPresenter;
    private Context mContext;
    private BluffPresenter bluffPresenter;
    private Gamer inviter;
    private String roomID;
    public GameInviteDialog(@NonNull Context context, BluffPresenter bluffPresenterInput,
                            Gamer inviterInput,String roomIDInput) {
        super(context,R.style.MyDialogStyle);
        inviter = inviterInput;
        roomID = roomIDInput;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_game_invite);
        mContext = context;
        bluffPresenter = bluffPresenterInput;
        mPresenter = new GameInvitePrsenter(this);
        ((Button)findViewById(R.id.button_accept_invite)).setOnClickListener(this);
        ((Button)findViewById(R.id.button_reject_invite)).setOnClickListener(this);
        ((TextView)findViewById(R.id.text_invite_info_dialog)).setText(inviter.getUserName()+"\n邀請你進行吹牛");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_accept_invite:
                mPresenter.acceptAndStartGame(inviter,roomID);
                bluffPresenter.setGameInformationAndGetIntoRoom(roomID,0);
                dismiss();
                break;
            case R.id.button_reject_invite:
                 mPresenter.refuseInvite(roomID);
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
        mPresenter.removeInvite();
        super.dismiss();
    }
}
