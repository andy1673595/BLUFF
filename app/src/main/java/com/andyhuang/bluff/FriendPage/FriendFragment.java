package com.andyhuang.bluff.FriendPage;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.BluffPresenter;
import com.andyhuang.bluff.Object.FriendInformation;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.activities.GamePage;
import com.andyhuang.bluff.activities.MainHallPage;
import java.util.ArrayList;

public class FriendFragment extends Fragment implements FriendContract.View,View.OnClickListener {
    private FriendPresenter mPresenter;
    private EditText editEmailToFindFriend;
    private ImageView imageFindButton;
    private FriendPageAdapter mAdapter;
    private ArrayList<FriendInformation> friendList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView textOpenRoom;
    private FragmentListener friendFragmentListener;

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
        imageFindButton.setOnClickListener(this);
        textOpenRoom = (TextView)root.findViewById(R.id.text_open_room_friend_page);
        textOpenRoom.setOnClickListener(this);
        recyclerView = (RecyclerView)root.findViewById(R.id.recyclerview_friend_page);
        recyclerView.setLayoutManager(new LinearLayoutManager(Bluff.getContext()));
        mAdapter = new FriendPageAdapter(friendList,mPresenter,this);
        recyclerView.setAdapter(mAdapter);
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
    public void setAdapter(ArrayList<FriendInformation> listInput) {
        friendList = listInput;
        mAdapter = new FriendPageAdapter(listInput,mPresenter,this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void addItem(FriendInformation friendInformation) {
        mAdapter.addItem(friendInformation);
    }

    @Override
    public void removeItem(int positon) {
        mAdapter.removeItem(positon);
    }

    @Override
    public void updateItemInvite(int positon, boolean isInvite) {
        mAdapter.updateItemInvite(positon,isInvite);
    }


    @Override
    public void showGamePage(String gameID,int playerInvitedTotal) {
        Intent intent = new Intent();
        intent.setClass((MainHallPage)getActivity(),GamePage.class);
        intent.putExtra("gameID",gameID);
        intent.putExtra("isHost",true);
        intent.putExtra("playerCount",playerInvitedTotal);
        startActivity(intent);
    }

    @Override
    public void showErrorDialog(String errorMessage) {
        IniviteErrorDialog dialog = new IniviteErrorDialog(this.getActivity(),errorMessage);
        dialog.show();
    }

    @Override
    public void showFriendProfile(String friendUID) {
        friendFragmentListener.showFriendProfile(friendUID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_find_friend:
                mPresenter.inviteFriend(String.valueOf(editEmailToFindFriend.getText()));
                hideSoftInput();
                break;
            case R.id.text_open_room_friend_page:
                mPresenter.getNumberOfGameRoom(mAdapter);
                break;
        }
    }

    public void hideSoftInput() {
        Bluff.getImm().hideSoftInputFromWindow(getActivity()
                .getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

    }

    //send info to MainActivity
    @Override
    public void onAttach(Activity activity) {
        friendFragmentListener= (FragmentListener) activity;
        super.onAttach(activity);
    }
}
