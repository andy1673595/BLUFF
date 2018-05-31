package com.andyhuang.bluff.RankPage;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andyhuang.bluff.Object.UserDataForRank;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.helper.ImageFromLruCache;

import java.util.LinkedList;

public class RankPageFragment extends Fragment implements RankPageContract.view {
    RankPagePresenter mPresenter;
    ImageView imageUserPhotoArray[];
    TextView textUserNameArray[];
    TextView textInfoArray[];
    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_rank_page, container, false);
        mPresenter = new RankPagePresenter(this);
        initVIew();
        //get the rank data from firebase
        mPresenter.getRankDataFromFirebase();
        return root;
    }
    private void initVIew() {
        imageUserPhotoArray = new ImageView[]{
                root.findViewById(R.id.image_user_photo_total_number1),
                root.findViewById(R.id.image_user_photo_total_number2),
                root.findViewById(R.id.image_user_photo_total_number3),
                root.findViewById(R.id.image_user_photo_rate_number1),
                root.findViewById(R.id.image_user_photo_rate_number2),
                root.findViewById(R.id.image_user_photo_rate_number3),
                root.findViewById(R.id.image_user_photo_win_number1),
                root.findViewById(R.id.image_user_photo_win_number2),
                root.findViewById(R.id.image_user_photo_win_number3)};

        textUserNameArray = new TextView[]{
                root.findViewById(R.id.text_name_total_number1),
                root.findViewById(R.id.text_name_total_number2),
                root.findViewById(R.id.text_name_total_number3),
                root.findViewById(R.id.text_name_rate_number1),
                root.findViewById(R.id.text_name_rate_number2),
                root.findViewById(R.id.text_name_rate_number3),
                root.findViewById(R.id.text_name_win_number1),
                root.findViewById(R.id.text_name_win_number2),
                root.findViewById(R.id.text_name_win_number3)};

        textInfoArray = new TextView[] {
                root.findViewById(R.id.text_info_total_number1),
                root.findViewById(R.id.text_info_total_number2),
                root.findViewById(R.id.text_info_total_number3),
                root.findViewById(R.id.text_info_rate_number1),
                root.findViewById(R.id.text_info_rate_number2),
                root.findViewById(R.id.text_info_rate_number3),
                root.findViewById(R.id.text_info_win_number1),
                root.findViewById(R.id.text_info_win_number2),
                root.findViewById(R.id.text_info_win_number3)};
    }

    @Override
    public void setPresenter(Object presenter) {

    }


    @Override
    public void freshUI(LinkedList<UserDataForRank> userList, int type) {
        for(int number =0 ; number <3;number++) {
            imageUserPhotoArray[type*3+number].setTag(userList.get(number).getUserPhoto());
            new ImageFromLruCache().set(imageUserPhotoArray[type*3+number],
                                        userList.get(number).getUserPhoto(),100f);
            textUserNameArray[type*3+number].setText(userList.get(number).getUserName());

            if(type == 0) {
                //this is total rank list
                textInfoArray[type*3+number].setText(userList.get(number).totalTimes+"場");
            }else if(type == 1) {
                //this is a win rate rank list
                String winRate = String.format("%.1f", userList.get(number).winRate);
                textInfoArray[type*3+number].setText(winRate+"%");
            }else {
                //this is win times rank list
                textInfoArray[type*3+number].setText(userList.get(number).winTimes+"場");
            }

        }
    }
}
