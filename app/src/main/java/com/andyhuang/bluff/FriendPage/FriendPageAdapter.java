package com.andyhuang.bluff.FriendPage;

import android.graphics.Color;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.Object.FriendInformation;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.Constant.Constants;
import com.andyhuang.bluff.helper.ImageFromLruCache;

import java.util.ArrayList;

public class FriendPageAdapter extends RecyclerView.Adapter<FriendPageAdapter.ViewHolder>{
    private boolean startReset = false;
    private ArrayList<FriendInformation> listFriend;
    private ArrayList<Boolean> checkBoxResetList = new ArrayList<>();
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
        String colorBackgroun[] = {"#E0F2F1","#B2DFDB"};
        String colorText[] = {"#00796B","#00695C"};
        if(position >= checkBoxResetList.size()) {
            checkBoxResetList.add(false);
        }
       FriendInformation friendInformation = listFriend.get(position);
       String friendPhoto = friendInformation.getPhotoURL();
       holder.textName.setText(friendInformation.getName());

       //reset checkboxResetList
        if(startReset && checkBoxResetList.get(position)) {
            checkBoxResetList.set(position,false);
            holder.checkBoxForInviteGame.setChecked(false);
           if(position == checkBoxResetList.size()) {
               startReset =false;
           }
        }

        if(checkBoxResetList.get(position)) {
            holder.checkBoxForInviteGame.setChecked(true);
        }else {
            holder.checkBoxForInviteGame.setChecked(false);
        }

        if (!friendPhoto.equals(Constants.NODATA)) {
            holder.imageFriendPhoto.setTag(friendPhoto);
            new ImageFromLruCache().set(holder.imageFriendPhoto, friendPhoto,10000f);
        } else {
            holder.imageFriendPhoto.setImageResource(R.mipmap.ic_launcher_round);
        }

        if(friendInformation.isFriendInvite()) {
            //this is friend invite item
            holder.imageLeftIcon.setVisibility(View.VISIBLE);
            holder.imageRightIcon.setVisibility(View.VISIBLE);
            holder.checkBoxForInviteGame.setVisibility(View.INVISIBLE);
        }else {
            //this is friend item
            holder.imageLeftIcon.setVisibility(View.INVISIBLE);
            holder.imageRightIcon.setVisibility(View.INVISIBLE);
            holder.checkBoxForInviteGame.setVisibility(View.VISIBLE);
        }

        //set color
        holder.itemView.setBackgroundColor(Color.parseColor(colorBackgroun[position%2]));
        holder.imageLeftIcon.setColorFilter(Color.parseColor(colorText[position%2]));
        holder.imageRightIcon.setColorFilter(Color.parseColor(colorText[position%2]));
        holder.checkBoxForInviteGame.setTextColor(Color.parseColor(colorText[position%2]));

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
        TextView textName;
        ImageView imageFriendPhoto;
        ImageView imageLeftIcon;
        ImageView imageRightIcon;
        CheckBox checkBoxForInviteGame;
        ConstraintLayout layoutItemBackground;
        public ViewHolder(View itemView) {
            super(itemView);
            //find view by id
            textName = itemView.findViewById(R.id.text_name_friend_listItem);
            imageFriendPhoto = itemView.findViewById(R.id.image_user_photo_friend_invite);
            imageLeftIcon = itemView.findViewById(R.id.image_left_icon_friend_listitem);
            imageRightIcon = itemView.findViewById(R.id.image_righ_icon_friend_listitem);
            layoutItemBackground = itemView.findViewById(R.id.layout_item_friend_background);
            checkBoxForInviteGame = itemView.findViewById(R.id.checkBox_item_friend);
            //set OnClickListeners
            imageLeftIcon.setOnClickListener(this);
            imageRightIcon.setOnClickListener(this);
            imageFriendPhoto.setOnClickListener(this);
            checkBoxForInviteGame.setOnCheckedChangeListener(this);

        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            switch (v.getId()){
                case R.id.image_righ_icon_friend_listitem:
                    if(listFriend.get(position).isFriendInvite()) {
                        //this is friend invite, right icon means cancel
                        mPresenter.refuseInvite(position);
                    }
                    break;
                case R.id.image_left_icon_friend_listitem:
                    if(listFriend.get(position).isFriendInvite()) {
                        //this is friend invite ,left icon means accept
                        mPresenter.acceptInvite(position);
                    }
                    break;
                case R.id.image_user_photo_friend_invite:
                    //click photo to  look friend Profile
                    friendPageView.showFriendProfile(listFriend.get(position).getUID());
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
