package com.andyhuang.bluff;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.StringDef;
import android.util.Log;

import com.andyhuang.bluff.FriendPage.FriendFragment;
import com.andyhuang.bluff.GameInviteDialog.GameInviteDialog;
import com.andyhuang.bluff.MainHallPage.MainHallFragment;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.Profile.ProfileFragment;
import com.andyhuang.bluff.User.UserManager;
import com.andyhuang.bluff.Util.Constants;
import com.andyhuang.bluff.activities.MainHallPage;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class BluffPresenter implements BluffContract.Presenter {
    private BluffContract.View bluffView;
    private FragmentManager fragmentManager;
    private MainHallFragment mMainHallFragment;
    private ProfileFragment mProfileFragment;
    private FriendFragment mFriendFragment;
    private Firebase refUserData;
    private MainHallPage activity;
    private String UIDGameInvite = Constants.NODATA;
    private String numberOfGameRoom = Constants.NODATA;
    private String emailFromGameInvite = Constants.NODATA;
    private String userPhotoURLGameInvite = Constants.NODATA;
    private boolean hasReadPhoto = false;
    private GameInviteDialog gameInviteDialog;
    private Firebase userDataRef = new Firebase("https://myproject-556f6.firebaseio.com/userData");

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            MAIN, FRIEND, PROFILE
    })
    public @interface FragmentType {}
    public static final String MAIN     = "MAIN";
    public static final String FRIEND = "FRIEND";
    public static final String PROFILE  = "PROFILE";

    public BluffPresenter(BluffContract.View bluffViewInput, FragmentManager fragmentManagerInput,MainHallPage activityInput) {
        bluffView = bluffViewInput;
        fragmentManager = fragmentManagerInput;
        activity = activityInput;
        bluffView.setPresenter(this);
        mMainHallFragment = new MainHallFragment();
        mProfileFragment = new ProfileFragment();
        mFriendFragment = new FriendFragment();
        initFragment();
        Firebase.setAndroidContext(Bluff.getContext());
        refUserData = new Firebase("https://myproject-556f6.firebaseio.com/userData/");
        listenGameInvite();

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

    @Override
    public void showGameInviteDialog() {
        Gamer inviter = new Gamer(UIDGameInvite,userPhotoURLGameInvite,emailFromGameInvite);
        gameInviteDialog = new GameInviteDialog((Activity)bluffView,this,inviter,numberOfGameRoom);
        gameInviteDialog.show();
    }

    public void initFragment(){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.mainFrameLayout, mMainHallFragment, MAIN)
                .add(R.id.mainFrameLayout, mProfileFragment, PROFILE)
                .add(R.id.mainFrameLayout, mFriendFragment, FRIEND)
                .hide(mProfileFragment).hide(mFriendFragment).show(mMainHallFragment).commit();
    }

    public void listenGameInvite() {
        refUserData.child(UserManager.getInstance().getUserUID())
                .child(Constants.GAME).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(Constants.GAME_INVITE)) {
                    UIDGameInvite = (String) dataSnapshot.getValue();
                }else if(dataSnapshot.getKey().equals(Constants.GAME_ROOM)) {
                    numberOfGameRoom = (String) dataSnapshot.getValue();
                }else if(dataSnapshot.getKey().equals(Constants.USER_EMAIL_FIREBASE)) {
                    emailFromGameInvite = (String)dataSnapshot.getValue();
                }else if(dataSnapshot.getKey().equals(Constants.USER_PHOTO_FIREBASE)) {
                    userPhotoURLGameInvite = (String)dataSnapshot.getValue();
                    hasReadPhoto = true;
                }
                //get all game invite information, start to show dialog
                if(! ( UIDGameInvite.equals(Constants.NODATA)||
                        numberOfGameRoom.equals(Constants.NODATA)||
                        emailFromGameInvite.equals(Constants.NODATA)||
                        !hasReadPhoto)) {
                    showGameInviteDialog();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    @Override
    public void removeGameInvite() {
        UIDGameInvite = Constants.NODATA;
        numberOfGameRoom = Constants.NODATA;
        emailFromGameInvite = Constants.NODATA;
        userPhotoURLGameInvite = Constants.NODATA;
        hasReadPhoto = false;
    }

    @Override
    public void setGameInformationAndGetIntoRoom(String RoomID) {
        bluffView.showGamePage(RoomID);
    }

    @Override
    public void setDisconnectWhenGetOutline() {
        //set false when user is outline
        userDataRef.child(UserManager.getInstance().getUserUID()).child(Constants.ONLINE_STATE).onDisconnect().setValue(false);
        userDataRef.child(UserManager.getInstance().getUserUID()).child(Constants.IS_GAMING).onDisconnect().setValue(false);
    }


}
