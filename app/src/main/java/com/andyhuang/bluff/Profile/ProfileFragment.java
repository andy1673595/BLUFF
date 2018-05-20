package com.andyhuang.bluff.Profile;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.Object.GameResult;
import com.andyhuang.bluff.Profile.Dialog.ModifyCommentDialog;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.helper.ImageFromLruCache;

import org.w3c.dom.Text;

import static com.andyhuang.bluff.helper.ImageRounder.getRoundedCornerBitmap;

public class ProfileFragment extends Fragment implements View.OnClickListener,ProfileContract.View {
    private ProfilePresenter mPresenter;
    private ImageView imageUserPhoto;
    private String userPhotoURL = "https://graph.facebook.com/2164912640192440/picture?type=large";
    private TextView textTotalTimes;
    private TextView textTotalTwoPersonTimes;
    private TextView textWinRate;
    private TextView textUserName;
    private TextView textUserEmail;
    private TextView textComment;
    private ImageView imageEditCommentButton;
    private String userUID;
    private Activity mActivity;
    private ImageView imageEmailIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mActivity = getActivity();
        Bundle bundle = getArguments();
        userUID = bundle.getString("UID");
        textTotalTimes = root.findViewById(R.id.text_total_count_games);
        textTotalTwoPersonTimes = root.findViewById(R.id.text_two_person_games_count_profile);
        textWinRate = root.findViewById(R.id.text_win_rate_profile);
        textUserName = root.findViewById(R.id.text_user_name_profile);
        textUserEmail= root.findViewById(R.id.text_email_profile);
        textComment= root.findViewById(R.id.text_comment_profile);
        imageEditCommentButton = root.findViewById(R.id.image_modify_comment_button);
        imageEditCommentButton.setOnClickListener(this);
        imageUserPhoto = root.findViewById(R.id.image_user_photo_profile);
        imageEmailIcon = root.findViewById(R.id.image_email_icon_profile);
        //set icon to round rectangle imageView
        Bitmap iconEmail = BitmapFactory.decodeResource(Bluff.getContext().getResources(),
                R.drawable.email_profile);
        imageEmailIcon.setImageBitmap(getRoundedCornerBitmap(iconEmail,20f));
        mPresenter = new ProfilePresenter(this,userUID);
        mPresenter.loadUserData();
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_modify_comment_button:
                ModifyCommentDialog modifyCommentDialog = new ModifyCommentDialog(mActivity,this,mPresenter);
                modifyCommentDialog.show();
                break;
        }
    }

    @Override
    public void setPresenter(Object presenter) {
        mPresenter = (ProfilePresenter) presenter;
    }

    @Override
    public void setUserDataToUI(String userName, String userEmail, String photoUrl, String comment) {
        imageUserPhoto.setTag(photoUrl);
        new ImageFromLruCache().set(imageUserPhoto, photoUrl,20f);
        textUserName.setText(userName);
        textComment.setText(comment);
        textUserEmail.setText(userEmail);
    }

    @Override
    public void freshComment(String comment) {
        textComment.setText(comment);
    }

    @Override
    public void setGameResultToUI(String totalTimes,String timesForTwoPersonGame,String winRate) {
        textTotalTimes.setText(totalTimes);
        textTotalTwoPersonTimes.setText(timesForTwoPersonGame);
        textWinRate.setText(winRate+"%");
    }

    @Override
    public void setButtonInvisible() {
        imageEditCommentButton.setVisibility(View.INVISIBLE);
    }
}
