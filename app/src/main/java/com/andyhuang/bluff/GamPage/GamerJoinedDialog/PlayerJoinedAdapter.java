package com.andyhuang.bluff.GamPage.GamerJoinedDialog;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andyhuang.bluff.R;
import com.andyhuang.bluff.Constant.Constants;
import com.andyhuang.bluff.helper.ImageFromLruCache;

import java.util.ArrayList;

public class PlayerJoinedAdapter extends RecyclerView.Adapter<PlayerJoinedAdapter.ViewHolder> {
    private ArrayList<String> nameList;
    private ArrayList<String> photoURLList;
    public PlayerJoinedAdapter(ArrayList<String> nameListInput,ArrayList<String> photoListInput) {
        nameList = nameListInput;
        photoURLList = photoListInput;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player_joined, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String photoURL = photoURLList.get(position);

        if (!photoURL.equals(Constants.NODATA)) {
            //if this is not a default URL
            holder.imageUserPhoto.setTag(photoURL);
            new ImageFromLruCache().set(holder.imageUserPhoto, photoURL,100f);
        } else {
            //set default photo
             holder.imageUserPhoto.setImageResource(R.mipmap.ic_launcher_round);
        }

        holder.textUserName.setText(nameList.get(position));

    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageUserPhoto;
        TextView textUserName;
        public ViewHolder(View itemView) {
            super(itemView);
            imageUserPhoto = itemView.findViewById(R.id.image_userphoto_newjoined_itemview);
            textUserName = itemView.findViewById(R.id.text_newplayer_joined_itemview);
        }
    }
    @Override
    public int getItemCount() {
        return nameList.size();
    }
}
