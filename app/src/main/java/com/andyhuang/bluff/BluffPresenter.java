package com.andyhuang.bluff;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.StringDef;

import com.andyhuang.bluff.FriendPage.FriendFragment;
import com.andyhuang.bluff.MainHallPage.MainHallFragment;
import com.andyhuang.bluff.Profile.ProfileFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class BluffPresenter implements BluffContract.Presenter {
    private BluffContract.View bluffView;
    private FragmentManager fragmentManager;
    private MainHallFragment mMainHallFragment;
    private ProfileFragment mProfileFragment;
    private FriendFragment mFriendFragment;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            MAIN, FRIEND, PROFILE
    })
    public @interface FragmentType {}
    public static final String MAIN     = "MAIN";
    public static final String FRIEND = "FRIEND";
    public static final String PROFILE  = "PROFILE";

    public BluffPresenter(BluffContract.View bluffViewInput, FragmentManager fragmentManagerInput) {
        bluffView = bluffViewInput;
        fragmentManager = fragmentManagerInput;
        bluffView.setPresenter(this);
        mMainHallFragment = new MainHallFragment();
        mProfileFragment = new ProfileFragment();
        mFriendFragment = new FriendFragment();
        initFragment();

    }

    @Override
    public void start() {

    }

    @Override
    public void transToMainPage() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(mProfileFragment).hide(mFriendFragment)
                .show(mMainHallFragment).commit();

        bluffView.showMainPage();
    }

    @Override
    public void transToFriendPage() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(mProfileFragment).hide(mMainHallFragment)
                .show(mFriendFragment).commit();

        bluffView.showFriendPage();
    }

    @Override
    public void transToProfilePage() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(mFriendFragment).hide(mMainHallFragment)
                .show(mProfileFragment).commit();
        bluffView.showProfilePage();
    }

    public void initFragment(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.mainFrameLayout, mMainHallFragment, MAIN)
                .add(R.id.mainFrameLayout, mProfileFragment, PROFILE)
                .add(R.id.mainFrameLayout, mFriendFragment, FRIEND)
                .hide(mProfileFragment).hide(mFriendFragment).show(mMainHallFragment).commit();
    }
}
