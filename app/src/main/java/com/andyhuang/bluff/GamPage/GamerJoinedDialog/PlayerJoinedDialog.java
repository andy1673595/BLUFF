package com.andyhuang.bluff.GamPage.GamerJoinedDialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.R;

import java.util.ArrayList;

public class PlayerJoinedDialog extends DialogFragment implements View.OnClickListener{
    ArrayList<Gamer> playerJoinedList;
    private RecyclerView recyclerView;
    private Button buttonConfirm;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       //inflate layout with recycler view
       View v = inflater.inflate(R.layout.player_joined_dialog_layout, container, false);
       buttonConfirm=  v.findViewById(R.id.button_confirm_player_left);
       buttonConfirm.setOnClickListener(this);
       recyclerView= (RecyclerView) v.findViewById(R.id.recyclerview_player_joined);
       recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       //get plyer joined list from gamepage
       ArrayList<String> nameList = getArguments().getStringArrayList("nameList");
       ArrayList<String> photoList = getArguments().getStringArrayList("photoList");
       //setadapter
       PlayerJoinedAdapter adapter = new PlayerJoinedAdapter(nameList,photoList);
       recyclerView.setAdapter(adapter);
       return v;
   }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
