package com.andyhuang.bluff.Profile.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andyhuang.bluff.Profile.ProfileFragment;
import com.andyhuang.bluff.Profile.ProfilePresenter;
import com.andyhuang.bluff.R;

public class ModifyCommentDialog extends Dialog implements View.OnClickListener{
    Button saveEditButton;
    Button quitEditButton;
    EditText editComment;
    TextView textCommentDialogTitle;
    ProfileFragment mProfileFragmentView;
    ProfilePresenter mProfilePresenter;
    public ModifyCommentDialog(@NonNull Context context, ProfileFragment fragmentView, ProfilePresenter presenter) {
        super(context, R.style.MyDialogStyle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modify_comment_dialog_layout);
        mProfileFragmentView = fragmentView;
        mProfilePresenter = presenter;
        saveEditButton = findViewById(R.id.button_save_comment);
        quitEditButton = findViewById(R.id.button_quit_edit_comment);
        textCommentDialogTitle = findViewById(R.id.text_modify_comment_title);
        editComment = findViewById(R.id.edit_comment_modify_dialog);
        saveEditButton.setOnClickListener(this);
        quitEditButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save_comment:
                String commentAfterEdit = "";
                commentAfterEdit = String.valueOf(editComment.getText())+"";
                if(commentAfterEdit.equals("")) {textCommentDialogTitle.setText("輸入不能為空白");}
                else {
                    mProfilePresenter.updateComment(commentAfterEdit);
                    mProfileFragmentView.freshComment(commentAfterEdit);
                    dismiss();
                }

                break;
            case R.id.button_quit_edit_comment:
                dismiss();
                break;
        }
    }
}
