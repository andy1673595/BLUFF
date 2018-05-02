package com.andyhuang.bluff.FriendPage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andyhuang.bluff.Object.FriendInformation;
import com.andyhuang.bluff.R;
import java.util.ArrayList;

public class FriendPageAdapter extends RecyclerView.Adapter{
    private ArrayList<FriendInformation> listFriend;
    private FriendContract.Presenter mPresenter;
    private FriendContract.View friendPageView;
    public FriendPageAdapter(ArrayList<FriendInformation> listInput,FriendContract.Presenter mPresenterInput, FriendContract.View friendPageViewInput) {
        mPresenter = mPresenterInput;
        friendPageView = friendPageViewInput;
        listFriend = listInput;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textEmail;
        ImageView imageFriendPhoto;
        ImageView imageLeftIcon;
        ImageView imageRightIcon;
        public ViewHolder(View itemView) {
            super(itemView);
            textEmail = (TextView)itemView.findViewById(R.id.text_email_friend_listItem);
            imageFriendPhoto = (ImageView)itemView.findViewById(R.id.image_user_photo_friend_invite);
            imageLeftIcon = (ImageView)itemView.findViewById(R.id.image_left_icon_friend_listitem);
            imageRightIcon = (ImageView)itemView.findViewById(R.id.image_righ_icon_friend_listitem);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
