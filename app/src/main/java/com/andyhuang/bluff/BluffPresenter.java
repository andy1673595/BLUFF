package com.andyhuang.bluff;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.andyhuang.bluff.Callback.LoadUserPhotoFromFirebaseCallback;
import com.andyhuang.bluff.FriendPage.FriendFragment;
import com.andyhuang.bluff.Object.InviteInformation;
import com.andyhuang.bluff.RankPage.RankPageFragment;
import com.andyhuang.bluff.Profile.ProfileFragment;
import com.andyhuang.bluff.RandomGame.RandomGameHelper;
import com.andyhuang.bluff.User.UserManager;
import com.firebase.client.Firebase;

public class BluffPresenter implements BluffContract.Presenter {
    //區域變數
    private int recentBackstackNumber = 0;
    private boolean isDoingPopAllstack = false;
    private InviteInformation inviteInformation;
    //fragment variables
    private FragmentManager fragmentManager;
    private RankPageFragment mRankPageFragment;
    private ProfileFragment mProfileFragment;
    private FriendFragment mFriendFragment;
    private ProfileFragment friendProfileFragment;
    //View , Firebase變數宣告
    private BluffContract.View bluffView;
    private BluffFirebaseHelper mFirebaseHelper;
   // private Firebase refUserData;


    //fragment的Tag
    public static final String MAIN     = "MAIN";
    public static final String FRIEND = "FRIEND";
    public static final String PROFILE  = "PROFILE";
    public static final String FRIEND_PROFILE  = "FRIEND_PROFILE";

    public BluffPresenter(BluffContract.View bluffViewInput, FragmentManager fragmentManagerInput) {
        bluffView = bluffViewInput;
        bluffView.setPresenter(this);
        mFirebaseHelper = new BluffFirebaseHelper(this);
        //init fragment variables
        fragmentManager = fragmentManagerInput;
        mRankPageFragment = new RankPageFragment();
        mFriendFragment = new FriendFragment();
        //拿到自己的ProfileFragment
        mProfileFragment = getProfileFragment(UserManager.getInstance().getUserUID());
        //init其他參數
        initFragment();
        Firebase.setAndroidContext(Bluff.getContext());
      //  refUserData = new Firebase("https://myproject-556f6.firebaseio.com/userData/");
        //監聽遊戲邀請
        listenGameInvite();
    }
    @Override
    public void start() {}

    @Override
    public void transToMainPage() {
        removeFriendProfileFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(mProfileFragment).hide(mFriendFragment)
                .show(mRankPageFragment).addToBackStack(MAIN).commit();
    }

    @Override
    public void transToFriendPage() {
        removeFriendProfileFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(mProfileFragment).hide(mRankPageFragment)
                .show(mFriendFragment).commit();
    }

    @Override
    public void transToProfilePage() {
        removeFriendProfileFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(mRankPageFragment).hide(mFriendFragment).
                show(mProfileFragment).addToBackStack(PROFILE).commit();
    }

    @Override
    public void transToFriendProfile(String friendUID) {
        friendProfileFragment = getProfileFragment(friendUID);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.mainFrameLayout,friendProfileFragment,FRIEND_PROFILE)
                   .show(friendProfileFragment).hide(mFriendFragment).hide(mRankPageFragment)
                   .hide(mProfileFragment).addToBackStack(FRIEND_PROFILE).commit();
    }

    @Override
    public void removeFriendProfileFragment() {
        if(friendProfileFragment!=null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(friendProfileFragment).commit();
            friendProfileFragment = null;
        }
    }

    @Override
    public void showGameInviteDialog(InviteInformation inviteInformationInput) {
        inviteInformation = inviteInformationInput;
        bluffView.showInviteDialog(inviteInformation);
    }

    @Override
    public void removeSequenceOnFirebaseForRandomGame() {
        mFirebaseHelper.removeSequenceOnFirebase();
    }

    @Override
    public void loadUserPhoto() {
        mFirebaseHelper.loadUserPhoto(mLoadUserPhotoFromFirebaseCallback);
    }

    public void initFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.mainFrameLayout, mRankPageFragment, MAIN)
                .add(R.id.mainFrameLayout, mProfileFragment, PROFILE)
                .add(R.id.mainFrameLayout, mFriendFragment, FRIEND)
                .hide(mProfileFragment).hide(mRankPageFragment)
                .show(mFriendFragment).commit();

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                //如果使用者按back鍵就把所有的backstack清空回到friend頁面
                if(fragmentManager.getBackStackEntryCount() < recentBackstackNumber&&!isDoingPopAllstack) {
                    popAllBackstack();
                }else {
                    //紀錄backstack裡面的堆疊數量,下次按下back鍵時可以判斷是否要清空stack
                    recentBackstackNumber = fragmentManager.getBackStackEntryCount();
                }
            }
        });
    }

    //清空backstack裡面的所有堆疊
    private void popAllBackstack() {
        //設定flag免得重複進行popAllBackstack()
        isDoingPopAllstack = true;
        for(int i=0;i<fragmentManager.getBackStackEntryCount();i++) {
            //清空堆疊
            fragmentManager.popBackStack();
        }
        //reset 計數器和flag
        recentBackstackNumber =0;
        isDoingPopAllstack = false;
    }

    private ProfileFragment getProfileFragment(String userUIDForProfilePage) {
        ProfileFragment profileFragment = new ProfileFragment();
        //set UID to profile fragment
        Bundle bundle = new Bundle();
        bundle.putString("UID",userUIDForProfilePage);
        profileFragment.setArguments(bundle);
        return profileFragment;
    }

    public void listenGameInvite() {
        mFirebaseHelper.listenGameInviteFromFirebase();
    }
    @Override
    public void removeGameInvite() {
       inviteInformation =null;
    }

    @Override
    public void setGameInformationAndGetIntoRoom(String RoomID,int playerInvitedTotal) {
        bluffView.showGamePage(RoomID,playerInvitedTotal,false);
    }

    @Override
    public void setDisconnectWhenGetOutline() {
        mFirebaseHelper.setDisconnectWhenGetOutlineOnFirebase();
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
        removeSequenceOnFirebaseForRandomGame();
    }

    private LoadUserPhotoFromFirebaseCallback mLoadUserPhotoFromFirebaseCallback =new LoadUserPhotoFromFirebaseCallback() {
        @Override
        public void completed(String userPhotoURL) {
            bluffView.setUserPhotoOnDrawer(userPhotoURL);
        }
    };

}
