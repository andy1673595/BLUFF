package com.andyhuang.bluff.Profile;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.Callback.ChangeUserPhotoCompletedCallback;
import com.andyhuang.bluff.Profile.Dialog.ModifyCommentDialog;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.activities.BluffMainActivity;
import com.andyhuang.bluff.helper.ImageFromLruCache;

import java.io.IOException;

import static com.andyhuang.bluff.helper.ImageRounder.getRoundedCornerBitmap;

public class ProfileFragment extends Fragment implements View.OnClickListener,ProfileContract.View {
    //view variables
    private ImageView imageUserPhoto;
    private ImageView imageEditCommentButton;
    private ImageView imageCommentBackground;
    private TextView textTotalTimes;
    private TextView textTotalTwoPersonTimes;
    private TextView textWinRate;
    private TextView textUserName;
    private TextView textUserEmail;
    private TextView textComment;
    //other variables
    private String userUID;
    private Activity mActivity;
    private ProfilePresenter mPresenter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mActivity = getActivity();
        //get the uid whose profile page should be showed
        Bundle bundle = getArguments();
        userUID = bundle.getString("UID");
        initView(root);
        //let the comment background has round corners
        Bitmap bitmap = BitmapFactory.decodeResource(Bluff.getContext().getResources(),
                R.drawable.profile_comment_background);
        imageCommentBackground.setImageBitmap(getRoundedCornerBitmap(bitmap, 40f));
        //set OnClickListeners
        imageEditCommentButton.setOnClickListener(this);
        imageUserPhoto.setOnClickListener(this);
        //init presenter
        mPresenter = new ProfilePresenter(this,userUID);
        mPresenter.loadUserData();
        return root;
    }

    private void initView(View root) {
        imageUserPhoto = root.findViewById(R.id.image_user_photo_profile);
        imageCommentBackground = root.findViewById(R.id.image_comment_background);
        imageEditCommentButton = root.findViewById(R.id.image_modify_comment_button);
        textTotalTimes = root.findViewById(R.id.text_total_count_games);
        textTotalTwoPersonTimes = root.findViewById(R.id.text_two_person_games_count_profile);
        textWinRate = root.findViewById(R.id.text_win_rate_profile);
        textUserName = root.findViewById(R.id.text_user_name_profile);
        textUserEmail= root.findViewById(R.id.text_email_profile);
        textComment= root.findViewById(R.id.text_comment_profile);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_modify_comment_button:
                ModifyCommentDialog modifyCommentDialog = new ModifyCommentDialog(mActivity,this,mPresenter);
                modifyCommentDialog.show();
                break;
            case R.id.image_user_photo_profile:
                ((BluffMainActivity)getActivity()).changeUserPhotoForProfilePage(mPhotoCallback);
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
        new ImageFromLruCache().set(imageUserPhoto, photoUrl,10000f);
        textUserName.setText(userName);
        textComment.setText(comment);
        textUserEmail.setText("Email : "+userEmail);
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
    //if this is a friend's profile page , we can't modify comment and change the photo
    @Override
    public void setButtonInvisible() {
        imageUserPhoto.setClickable(false);
        imageEditCommentButton.setVisibility(View.INVISIBLE);
    }

    private ChangeUserPhotoCompletedCallback mPhotoCallback = new ChangeUserPhotoCompletedCallback() {
        @Override
        public void completedChange(Uri photoUri) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                imageUserPhoto.setImageBitmap(getRoundedCornerBitmap(bitmap,1000));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //update new Image to firebase
            mPresenter.changeUserPhoto(photoUri);
        }
    };
}
