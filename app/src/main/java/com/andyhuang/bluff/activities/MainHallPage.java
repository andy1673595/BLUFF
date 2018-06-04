package com.andyhuang.bluff.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.andyhuang.bluff.BluffContract;
import com.andyhuang.bluff.BluffPresenter;
import com.andyhuang.bluff.Callback.ChangeUserPhotoCompletedCallback;
import com.andyhuang.bluff.Callback.LoadUserPhotoFromFirebaseCallback;
import com.andyhuang.bluff.Callback.WaitForRandomGameCallback;
import com.andyhuang.bluff.Dialog.GameInviteDialog.GameInviteDialog;
import com.andyhuang.bluff.Dialog.WaitForRandomGameDialog.WaitForRandomGameDialog;
import com.andyhuang.bluff.FriendPage.FragmentListener;
import com.andyhuang.bluff.FriendPage.IniviteErrorDialog;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
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
public class MainHallPage extends BaseActivity implements BluffContract.View, FragmentListener {
    private BluffPresenter mPresenter;
    private DrawerLayout myDrawerLayout;
    private NavigationView mNavigationView;
    private ImageView imageMenuButton;
    private ImageView imageUserPhotoForDrawer;
    private TextView textNameForDrawer;
    private ChangeUserPhotoHelper mChangeUserPhotoHelper;
    private Uri mImageUri;
    private ChangeUserPhotoCompletedCallback mPhotoCallback;
    private WaitForRandomGameDialog mWaitForRandomGameDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_hall_page);
        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawrlayout_main);
        imageMenuButton = (ImageView) findViewById(R.id.image_menu_button);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        textNameForDrawer = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.textUserNameInDrawer);
        imageUserPhotoForDrawer = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.imageUserPhotoInDrawer);
        mPresenter = new BluffPresenter(this, getFragmentManager(), this);
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
        mPresenter.loadUserPhoto();
    }

    private NavigationView.OnNavigationItemSelectedListener navigationViewListener() {
        return new NavigationView.OnNavigationItemSelectedListener() {
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
                        mWaitForRandomGameDialog = new WaitForRandomGameDialog(MainHallPage.this, mWaitForRandomGameCallback);
                        //避免玩家手殘點到外面取消
                        mWaitForRandomGameDialog.setCanceledOnTouchOutside(false);
                        mWaitForRandomGameDialog.show();
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
        mPresenter = (BluffPresenter) presenter;
    }

    @Override
    public void showGamePage(String gameID, int gamerInvitedTotal, boolean isHost) {
        if (mWaitForRandomGameDialog != null) {
            //hide the dialog when I'm invitee and I have already get into game
            mWaitForRandomGameDialog.dismiss();
            mWaitForRandomGameDialog = null;
        }
        Intent intent = new Intent();
        intent.setClass(this, GamePage.class);
        intent.putExtra("gameID", gameID);
        //I am invitee , set is Host false
        intent.putExtra("isHost", isHost);
        intent.putExtra("playerCount", gamerInvitedTotal);
        startActivity(intent);
    }

    @Override
    public void showErrorInviteDialogFromRandom(String message) {
        IniviteErrorDialog dialog = new IniviteErrorDialog(this, message);
        dialog.show();
    }

    @Override
    public void showInviteDialog(Gamer inviter,String numberOfGameRoom) {
        GameInviteDialog gameInviteDialog = new GameInviteDialog(MainHallPage.this,mPresenter,inviter,numberOfGameRoom);
        gameInviteDialog.setCanceledOnTouchOutside(false);
        gameInviteDialog.show();
        mPresenter.removeSequenceForRandomGame();
    }

    @Override
    public void setUserPhotoOnDrawer(String userPhotoURL) {
        if (!userPhotoURL.equals(Constants.NODATA)) {
            imageUserPhotoForDrawer.setTag(userPhotoURL);
            new ImageFromLruCache().set(imageUserPhotoForDrawer, userPhotoURL, 10000f);
        } else {
            imageUserPhotoForDrawer.setImageResource(R.mipmap.ic_launcher_round);
        }
    }


    @Override
    public void onBackPressed() {
        if (myDrawerLayout.isDrawerOpen(Gravity.LEFT)) myDrawerLayout.closeDrawers();
        else super.onBackPressed();
    }

    @Override
    public void showFriendProfile(String friendUID) {
        mPresenter.transToFriendProfile(friendUID);
    }

    public void changeUserPhotoForProfilePage(ChangeUserPhotoCompletedCallback callbackInput) {
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
        new AlertDialog.Builder(MainHallPage.this)
                .setMessage("未允許「" + getString(R.string.app_name) +
                        "」讀取裝置權限，將無法使用自訂相片，是否重新設定權限？")
                .setPositiveButton("重新設定權限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .create()
                .show();
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDenied() {
        Toast.makeText(this, "讀取儲存內容權限被拒絕", Toast.LENGTH_LONG).show();
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAsk() {
        Toast.makeText(this, "您將無法使用自訂相片功能", Toast.LENGTH_LONG).show();
    }

    private WaitForRandomGameCallback mWaitForRandomGameCallback = new WaitForRandomGameCallback() {
        @Override
        public void cancelWaiting() {
            mPresenter.cancelWaiting();
        }
    };


}
