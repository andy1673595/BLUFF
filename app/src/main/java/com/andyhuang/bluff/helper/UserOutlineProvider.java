package com.andyhuang.bluff.helper;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.andyhuang.bluff.Bluff;
import com.andyhuang.bluff.R;

public class UserOutlineProvider extends ViewOutlineProvider {
    @Override
    public void getOutline(View view, Outline outline) {
        view.setClipToOutline(true);
        int radius = Bluff.getContext().getResources()
                .getDimensionPixelSize(R.dimen.radius_friend_list_item);
        outline.setOval(0, 0, radius, radius);
    }
}
