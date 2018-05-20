package com.andyhuang.bluff.FriendPage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.andyhuang.bluff.Object.FriendInformation;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.Util.Constants;
import com.andyhuang.bluff.helper.ImageFromLruCache;
import com.andyhuang.bluff.helper.UserOutlineProvider;

import java.util.ArrayList;

public class FriendPageAdapter extends RecyclerView.Adapter<FriendPageAdapter.ViewHolder>{
    private ArrayList<FriendInformation> listFriend;
    private ArrayList<Boolean> checkBoxResetList = new ArrayList<>();
    private boolean startReset = false;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position >= checkBoxResetList.size()) {
            checkBoxResetList.add(false);
        }
       FriendInformation friendInformation = listFriend.get(position);
       String friendPhoto = friendInformation.getPhotoURL();
       holder.textEmail.setText(friendInformation.getEmail());

       //reset checkboxResetList
        if(startReset && checkBoxResetList.get(position)) {
            checkBoxResetList.set(position,false);
            holder.checkBoxForInviteGame.setChecked(false);
           if(position == checkBoxResetList.size()) {
               startReset =false;
           }
        }

      // holder.imageFriendPhoto.setOutlineProvider(new UserOutlineProvider());
        if (!friendPhoto.equals(Constants.NODATA)) {
            holder.imageFriendPhoto.setTag(friendPhoto);
            new ImageFromLruCache().set(holder.imageFriendPhoto, friendPhoto,40f);
        } else {
            holder.imageFriendPhoto.setImageResource(R.mipmap.ic_launcher_round);
        }

        if(friendInformation.isFriendInvite()) {
            //this is friend invite item
            holder.imageLeftIcon.setImageResource(R.drawable.ic_ok);
            holder.imageRightIcon.setImageResource(R.drawable.ic_refuse);
        }else {
            //this is friend item
            holder.imageLeftIcon.setImageResource(R.drawable.ic_drink);
            holder.imageRightIcon.setImageResource(R.drawable.ic_profile);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
        TextView textEmail;
        ImageView imageFriendPhoto;
        ImageView imageLeftIcon;
        ImageView imageRightIcon;
        CheckBox checkBoxForInviteGame;
        public ViewHolder(View itemView) {
            super(itemView);
            textEmail = (TextView)itemView.findViewById(R.id.text_email_friend_listItem);
            imageFriendPhoto = (ImageView)itemView.findViewById(R.id.image_user_photo_friend_invite);
            imageLeftIcon = (ImageView)itemView.findViewById(R.id.image_left_icon_friend_listitem);
            imageRightIcon = (ImageView)itemView.findViewById(R.id.image_righ_icon_friend_listitem);
            checkBoxForInviteGame = (CheckBox)itemView.findViewById(R.id.checkBox_item_friend);
            checkBoxForInviteGame.setOnCheckedChangeListener(this);
            imageLeftIcon.setOnClickListener(this);
            imageRightIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            switch (v.getId()){
                case R.id.image_righ_icon_friend_listitem:

                    if(listFriend.get(position).isFriendInvite()) {
                        //this is friend invite, right icon means cancel
                        mPresenter.refuseInvite(position);
                    }else {
                        //this is friend item, right icon means look friend Profile
                        friendPageView.showFriendProfile(listFriend.get(position).getUID());
                    }
                    break;
                case R.id.image_left_icon_friend_listitem:
                    if(listFriend.get(position).isFriendInvite()) {
                        //this is friend invite ,left icon means accept
                        mPresenter.acceptInvite(position);
                    }
                    break;
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = getAdapterPosition();
           if(checkBoxForInviteGame.isChecked()) {
                mPresenter.inviteGame(listFriend.get(position));
                checkBoxResetList.set(position,true);

            } else {
                mPresenter.removeInvite(listFriend.get(position));
           }
        }
    }

    @Override
    public int getItemCount() {
        return listFriend.size();
    }

    public void addItem(FriendInformation friendInformation) {
        listFriend.add(friendInformation);
        notifyItemInserted(listFriend.size());
    }

    public void removeItem(int position) {
        listFriend.remove(position);
        notifyItemRemoved(position);
    }
    public void updateItemInvite(int positon, boolean isInvite) {
        listFriend.get(positon).setFriendInvite(isInvite);
        notifyItemChanged(positon);
    }

    public void resetCheck() {
        startReset = true;
        notifyDataSetChanged();
    }
}
