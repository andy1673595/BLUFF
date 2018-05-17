package com.andyhuang.bluff.Profile;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.helper.ImageFromLruCache;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private ImageView imageUserPhoto;
    private String userPhotoURL = "https://graph.facebook.com/2164912640192440/picture?type=large";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        imageUserPhoto = root.findViewById(R.id.image_user_photo_profile);
        imageUserPhoto.setOnClickListener(this);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        new ImageFromLruCache().set(imageUserPhoto, userPhotoURL,20f);

    }

    @Override
    public void onClick(View v) {

    }
}
