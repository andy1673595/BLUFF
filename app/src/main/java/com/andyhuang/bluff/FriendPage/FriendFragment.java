package com.andyhuang.bluff.FriendPage;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.andyhuang.bluff.R;

public class FriendFragment extends Fragment implements FriendContract.View,View.OnClickListener {
    private FriendPresenter mPresenter;
    private EditText editEmailToFindFriend;
    private ImageView imageFindButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new FriendPresenter(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friend, container, false);
        editEmailToFindFriend = (EditText)root.findViewById(R.id.edit_find_friend);
        imageFindButton = (ImageView)root.findViewById(R.id.image_find_friend);

        return root;
    }

    @Override
    public void setPresenter(Object presenter) {
        mPresenter = (FriendPresenter) presenter;
    }

    @Override
    public void freshFriendList() {

    }

    @Override
    public void setAdapter() {

    }

    @Override
    public void showGamePage() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_find_friend:
                mPresenter.inviteFriend(String.valueOf(editEmailToFindFriend.getText()));
                break;
        }
    }
}
