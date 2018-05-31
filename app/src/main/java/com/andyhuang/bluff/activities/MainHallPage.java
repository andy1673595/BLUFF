package com.andyhuang.bluff.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.andyhuang.bluff.BluffContract;
import com.andyhuang.bluff.BluffPresenter;
import com.andyhuang.bluff.Callback.ChangeUserPhotoCompletedCallback;
import com.andyhuang.bluff.FriendPage.FragmentListener;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.Util.Constants;
import com.andyhuang.bluff.helper.ChangeUserPhotoHelper;
import com.andyhuang.bluff.helper.ImageFromLruCache;

import java.io.File;
import java.io.IOException;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainHallPage extends BaseActivity implements BluffContract.View,FragmentListener {
    private BluffContract.Presenter mPresenter;
    private DrawerLayout myDrawerLayout;
    private NavigationView mNavigationView;
    private ImageView imageMenuButton;
    private ImageView imageUserPhotoForDrawer;
    private TextView textNameForDrawer;
    private ChangeUserPhotoHelper mChangeUserPhotoHelper;
    private Uri mImageUri;
    private Uri mNewPhotoUri;
    private ChangeUserPhotoCompletedCallback mPhotoCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_hall_page);
        myDrawerLayout = (DrawerLayout)findViewById(R.id.drawrlayout_main);
        imageMenuButton = (ImageView)findViewById(R.id.image_menu_button);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        textNameForDrawer = (TextView)mNavigationView.getHeaderView(0).findViewById(R.id.textUserNameInDrawer);
        imageUserPhotoForDrawer = (ImageView)mNavigationView.getHeaderView(0).findViewById(R.id.imageUserPhotoInDrawer);
        mPresenter = new BluffPresenter(this,getFragmentManager(),this);
        mNavigationView.setNavigationItemSelectedListener(navigationViewListener());
        imageMenuButton.setOnClickListener(mainClickListener);
        setDrawerLayout();
        mPresenter.setDisconnectWhenGetOutline();
        //helper for changing user photo in the profile page
        mChangeUserPhotoHelper = new ChangeUserPhotoHelper(this);
    }

    public void setDrawerLayout() {
        myDrawerLayout.setFitsSystemWindows(true);
        myDrawerLayout.setClipToPadding(false);
        textNameForDrawer.setText(com.andyhuang.bluff.User.UserManager.getInstance().getUserName());
        //set userphoto in drawer
        String photoURL = com.andyhuang.bluff.User.UserManager.getInstance().getUserPhotoUrl();
        if (!photoURL.equals(Constants.NODATA)) {
            imageUserPhotoForDrawer.setTag(photoURL);
            new ImageFromLruCache().set(imageUserPhotoForDrawer, photoURL,10000f);
        } else {
            imageUserPhotoForDrawer.setImageResource(R.mipmap.ic_launcher_round);
        }

    }

    private NavigationView.OnNavigationItemSelectedListener navigationViewListener() {
        return new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_home:
                        mPresenter.transToMainPage();
                        break;
                    case R.id.item_friend:
                        mPresenter.transToFriendPage();
                        break;
                    case R.id.item_profile:
                        mPresenter.transToProfilePage();
                        break;
                    case R.id.item_random:
                        mPresenter.startRandomGame();
                        break;
                }
                myDrawerLayout.closeDrawers();
                return false;
            }
        };
    }

    private View.OnClickListener mainClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            myDrawerLayout.openDrawer(Gravity.LEFT);
        }
    };

    @Override
    public void setPresenter(Object presenter) {
        mPresenter = (BluffContract.Presenter)presenter;
    }

    @Override
    public void showGamePage(String gameID,int gamerInvitedTotal) {
        Intent intent = new Intent();
        intent.setClass(this,GamePage.class);
        intent.putExtra("gameID",gameID);
        //I am invitee , set is Host false
        intent.putExtra("isHost",false);
        intent.putExtra("playerCount",gamerInvitedTotal);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(myDrawerLayout.isDrawerOpen(Gravity.LEFT)) myDrawerLayout.closeDrawers();
        else super.onBackPressed();
    }

    @Override
    public void showFriendProfile(String friendUID) {
        mPresenter.transToFriendProfile(friendUID);
    }
    public void changeUserPhotoForProfilePage(ChangeUserPhotoCompletedCallback callbackInput){
        mPhotoCallback = callbackInput;
        MainHallPagePermissionsDispatcher.getPhotoFromGalleryWithPermissionCheck(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.GET_PHOTO_FROM_GALLERY:  //取得圖片後進行裁剪
                if (resultCode == RESULT_OK) {
                    String path = mChangeUserPhotoHelper.getRealPathFromURI(data.getData());
                    File imageFile = new File(path);
                    mChangeUserPhotoHelper.doCropPhoto(mImageUri,
                            FileProvider.getUriForFile(this, "com.andyhuang.bluff.fileprovider", imageFile));
                }
                break;
            case Constants.GET_PHOTO_CROP:  //裁剪完的圖片Uri傳給Profile fragment
                if (resultCode == RESULT_OK) {
                    mPhotoCallback.completedChange(mImageUri);
                }
                break;
        }
    }


    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void getPhotoFromGallery() {
        //讀取圖片，使用 Intent.ACTION_GET_CONTENT 這個 Action
        Intent intentPhotoGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        //開啟 Pictures 畫面 Type 設定為 image
        intentPhotoGallery.setType("image/*");
        if (intentPhotoGallery.resolveActivity(this.getPackageManager()) != null) {
            File imageFile = null;
            try {
                imageFile = mChangeUserPhotoHelper.createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (imageFile != null) {
                mImageUri = FileProvider.getUriForFile(this, "com.andyhuang.bluff.fileprovider", imageFile); //mImageCameraTempUri
                intentPhotoGallery.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(intentPhotoGallery, Constants.GET_PHOTO_FROM_GALLERY);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainHallPagePermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationale(final PermissionRequest request) {
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDenied() {
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAsk() {
    }
}
