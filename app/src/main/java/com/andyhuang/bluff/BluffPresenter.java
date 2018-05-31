package com.andyhuang.bluff;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.StringDef;

import com.andyhuang.bluff.FriendPage.FriendFragment;
import com.andyhuang.bluff.Dialog.GameInviteDialog.GameInviteDialog;
import com.andyhuang.bluff.Object.InviteInformation;
import com.andyhuang.bluff.RankPage.RankPageFragment;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.Profile.ProfileFragment;
import com.andyhuang.bluff.RandomGame.RandomGameHelper;
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
    private RankPageFragment mRankPageFragment;
    private ProfileFragment mProfileFragment;
    private FriendFragment mFriendFragment;
    private ProfileFragment friendProfileFragment;
    private Firebase refUserData;
    private MainHallPage activity;
    private String UIDGameInvite = Constants.NODATA;
    private String numberOfGameRoom = Constants.NODATA;
    private String emailFromGameInvite = Constants.NODATA;
    private String userPhotoURLGameInvite = Constants.NODATA;
    private String nameInvite = Constants.NODATA;
    private boolean hasReadPhoto = false;
    private GameInviteDialog gameInviteDialog;


    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            MAIN, FRIEND, PROFILE
    })
    public @interface FragmentType {}
    public static final String MAIN     = "MAIN";
    public static final String FRIEND = "FRIEND";
    public static final String PROFILE  = "PROFILE";
    public static final String FRIEND_PROFILE  = "FRIEND_PROFILE";

    public BluffPresenter(BluffContract.View bluffViewInput, FragmentManager fragmentManagerInput,MainHallPage activityInput) {
        bluffView = bluffViewInput;
        fragmentManager = fragmentManagerInput;
        activity = activityInput;
        bluffView.setPresenter(this);
        mRankPageFragment = new RankPageFragment();
        mProfileFragment = new ProfileFragment();
        mFriendFragment = new FriendFragment();
        //set UID to profile fragment
        Bundle bundle = new Bundle();
        bundle.putString("UID",UserManager.getInstance().getUserUID());
        mProfileFragment.setArguments(bundle);
        //set UID to friend fragment
        Bundle bundleToFriendFragment = new Bundle();
        mProfileFragment.setArguments(bundle);
        //inti
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
        if(friendProfileFragment!=null) removeFriendProfileFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(mProfileFragment).hide(mFriendFragment)
                .show(mRankPageFragment).addToBackStack(null).commit();
    }

    @Override
    public void transToFriendPage() {
        if(friendProfileFragment!=null) removeFriendProfileFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(mProfileFragment).hide(mRankPageFragment)
                .show(mFriendFragment).commit();
    }

    @Override
    public void transToProfilePage() {
        if(friendProfileFragment!=null) removeFriendProfileFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(mFriendFragment).hide(mRankPageFragment)
                .show(mProfileFragment).addToBackStack(null).commit();
    }

    @Override
    public void transToFriendProfile(String friendUID) {
        friendProfileFragment = new ProfileFragment();
        //set UID to profile fragment
        Bundle bundle = new Bundle();
        bundle.putString("UID",friendUID);
        friendProfileFragment.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.mainFrameLayout,friendProfileFragment,FRIEND_PROFILE).show(friendProfileFragment)
                    .hide(mFriendFragment).hide(mRankPageFragment).hide(mProfileFragment)
                    .addToBackStack(null).commit();
    }

    @Override
    public void removeFriendProfileFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(friendProfileFragment).show(mFriendFragment)
                .hide(mRankPageFragment).hide(mProfileFragment).commit();
        friendProfileFragment = null;
    }

    @Override
    public void showGameInviteDialog() {
        Gamer inviter = new Gamer(UIDGameInvite,userPhotoURLGameInvite,emailFromGameInvite);
        inviter.setUserName(nameInvite);
        gameInviteDialog = new GameInviteDialog((Activity)bluffView,this,inviter,numberOfGameRoom);
        gameInviteDialog.setCanceledOnTouchOutside(false);
        gameInviteDialog.show();
        removeSequenceForRandomGame();
    }

    public void removeSequenceForRandomGame() {
        String sequenceID = UserManager.getInstance().getSequenceID();
        if(!sequenceID.equals(Constants.NODATA)) {
            //if I am invited from random game , remove the sequence from server
            Firebase randomGameRef =
                    new Firebase("https://myproject-556f6.firebaseio.com/"+ Constants.RANDOM_GAME);
            randomGameRef.child(sequenceID).removeValue();
            UserManager.getInstance().setSequenceID(Constants.NODATA);
        }
    }

    public void initFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.mainFrameLayout, mRankPageFragment, MAIN)
                .add(R.id.mainFrameLayout, mProfileFragment, PROFILE)
                .add(R.id.mainFrameLayout, mFriendFragment, FRIEND).hide(mProfileFragment).hide(mRankPageFragment)
                .show(mFriendFragment).commit();
    }

    public void listenGameInvite() {
        refUserData.child(UserManager.getInstance().getUserUID())
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(Constants.GAME)) {
                    InviteInformation inviteInformation = dataSnapshot.getValue(InviteInformation.class);
                    UIDGameInvite = inviteInformation.getGameInvite();
                    numberOfGameRoom = inviteInformation.getGameRoom();
                    emailFromGameInvite = inviteInformation.getUserEmail();
                    userPhotoURLGameInvite = inviteInformation.getUserPhoto();
                    nameInvite = inviteInformation.getUserName();
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
        nameInvite = Constants.NODATA;
        hasReadPhoto = false;
    }

    @Override
    public void setGameInformationAndGetIntoRoom(String RoomID,int playerInvitedTotal) {
        bluffView.showGamePage(RoomID,playerInvitedTotal,false);
    }

    @Override
    public void setDisconnectWhenGetOutline() {
        //set false when user is outline
        refUserData.child(UserManager.getInstance().getUserUID()).child(Constants.ONLINE_STATE).onDisconnect().setValue(false);
        refUserData.child(UserManager.getInstance().getUserUID()).child(Constants.IS_GAMING).onDisconnect().setValue(false);
    }

    @Override
    public void transToPrivacyPolicy() {

    }

    @Override
    public void startRandomGame() {
        RandomGameHelper randomGameHelper = new RandomGameHelper(this);
        randomGameHelper.updateMyDataToRandomSequence();
    }

    @Override
    public void showGamePageFromRandom(String gameRoomID, int personTotal) {
        //I'm the inviter from random game
        bluffView.showGamePage(gameRoomID,personTotal,true);
    }

    @Override
    public void showErrorDialogFromRandom(String message) {
        bluffView.showErrorInviteDialogFromRandom(message);
    }

    @Override
    public void cancelWaiting() {
        //cancel waiting , so remove the data from sequence List on server
       removeSequenceForRandomGame();
    }

}
