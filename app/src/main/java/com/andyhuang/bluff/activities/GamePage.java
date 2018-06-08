package com.andyhuang.bluff.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.andyhuang.bluff.GamPage.GameEndDialog.ExitRoomCallback;
import com.andyhuang.bluff.GamPage.GameEndDialog.GameEndDialog;
import com.andyhuang.bluff.GamPage.GameObject.Gamer;
import com.andyhuang.bluff.GamPage.GamePageContract;
import com.andyhuang.bluff.GamPage.GamePagePresenter;
import com.andyhuang.bluff.GamPage.GamerLeaveDialog.GamerLeaveDialog;
import com.andyhuang.bluff.GamPage.LeaveRoomDialog.ExitGameDialog;
import com.andyhuang.bluff.GamPage.GamerJoinedDialog.PlayerJoinedDialog;
import com.andyhuang.bluff.R;
import com.andyhuang.bluff.GamPage.GameObject.CurrentInformation;
import com.andyhuang.bluff.Constant.ConstantForWebRTC;
import com.andyhuang.bluff.webRTC.PercentFrameLayout;
import org.webrtc.EglBase;
import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoRenderer;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class GamePage extends BaseActivity implements View.OnClickListener ,GamePageContract.View {
    //game page view layout elements
    private ImageView imageHomeBackButton;
    private ImageView imageReadyStateButton;
    private ImageView[] imageDiceArray;
    private TextView textShowInformation;
    private TextView textPlayerCountRightNow;
    private TextView textTellPlayerInfo;
    private ConstraintLayout layoutPlayerList;
    private ConstraintLayout layoutIncreaseDice;
    private ConstraintLayout layoutCatch;
    //layout,image,buttons.. for webRTC
    private ImageView imageVideoIcon;
    private ImageView imageVideobackground;
    private RelativeLayout layoutVideoBack;
    private ConstraintLayout layoutSwitchForVideoShow;
    private Switch switchForVideo;
    //webRTC的變數
    private SurfaceViewRenderer localRender;
    private SurfaceViewRenderer remoteRenderScreen;
    private PercentFrameLayout localRenderLayout;
    private PercentFrameLayout remoteRenderLayout;
    private final List<VideoRenderer.Callbacks> remoteRenderers =
            new ArrayList<VideoRenderer.Callbacks>();
    private EglBase rootEglBase;
    private RendererCommon.ScalingType scalingType;
    //其他變數
    private int countForPlayerInviteTotal;
    private int[] diceImageSourceArray;
    private int[] diceImageSourceForInfo;
    private String roomID;
    private boolean isHost;
    private GamePage thisActivity;
    private GamePagePresenter mPrsenter;
    private GameEndDialog mGameEndDialog;
    private ExitGameDialog mExitGameDialog;
    private ArrayList<Gamer> playerJoinedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);
        thisActivity = this;
        //Bind view by id and new Array
        initView();
        //set OnclickListener and Visibility
        layoutIncreaseDice.setVisibility(View.INVISIBLE);
        layoutIncreaseDice.setOnClickListener(this);
        layoutCatch.setVisibility(View.INVISIBLE);
        layoutCatch.setOnClickListener(this);
        imageReadyStateButton.setOnClickListener(this);
        imageHomeBackButton.setOnClickListener(this);
        layoutPlayerList.setOnClickListener(this);
        //set switch un clickable to avoid too frequency click
        switchForVideo.setClickable(false);
        //read bundle from MainActivity
        readIntent();
        mPrsenter = new GamePagePresenter(this);
        mPrsenter.init(roomID,isHost);
    }

    void readIntent() {
        Intent intent = this.getIntent();
        roomID = intent.getStringExtra("gameID");
        isHost = intent.getBooleanExtra("isHost",false);
        countForPlayerInviteTotal = intent.getIntExtra("playerCount",0);

    }

    void initView() {
        //bind View By Id
        imageDiceArray = new ImageView[]{
                findViewById(R.id.image_table_dice1),
                findViewById(R.id.image_table_dice2),
                findViewById(R.id.image_table_dice3),
                findViewById(R.id.image_table_dice4),
                findViewById(R.id.image_table_dice5)};
        imageReadyStateButton = findViewById(R.id.image_game_state);
        imageHomeBackButton = findViewById(R.id.image_home_button_gamepage);
        textShowInformation =findViewById(R.id.text_show_current_info);
        textTellPlayerInfo = findViewById(R.id.text_tell_player_information);
        textPlayerCountRightNow = findViewById(R.id.text_show_playerlist_button);
        layoutIncreaseDice = findViewById(R.id.layout_increase_dice);
        layoutCatch = findViewById(R.id.layout_catch);
        layoutPlayerList = findViewById(R.id.layout_show_player_list);
        //view for video
        imageVideoIcon = findViewById(R.id.image_video_button);
        imageVideobackground = findViewById(R.id.image_video_background);
        switchForVideo = findViewById(R.id.switchForVideo);
        //只偵測把switch和icon包在一起layout的click事件
        switchForVideo.setClickable(false);
        layoutVideoBack = findViewById(R.id.video_back_layout);
        layoutSwitchForVideoShow = findViewById(R.id.layout_for_video_switch);
        //layout for video
        scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;
        localRender = findViewById(R.id.local_video_view);
        localRenderLayout = findViewById(R.id.local_video_layout);
        remoteRenderScreen = findViewById(R.id.remote_video_view);
        remoteRenderLayout = findViewById(R.id.remote_video_layout);
        remoteRenderers.add(remoteRenderScreen);
        //set INVISIBLE first, if game is two persons' game set visible
        layoutSwitchForVideoShow.setVisibility(View.INVISIBLE);
        layoutVideoBack.setVisibility(View.INVISIBLE);
        //init dice drawable Array
        diceImageSourceArray = new int[] {R.drawable.table_dice1,R.drawable.table_dice2,R.drawable.table_dice3,
                R.drawable.table_dice4,R.drawable.table_dice5,R.drawable.table_dice6};
        diceImageSourceForInfo = new int[] {R.drawable.dice1,R.drawable.dice2,R.drawable.dice3,
                R.drawable.dice4,R.drawable.dice5,R.drawable.dice6};
    }

    public void creatVideoRenders() {
        scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;
        // Create video renderers.
        if(localRender ==null) {
            localRender = new SurfaceViewRenderer(this);
            localRender = findViewById(R.id.local_video_view);
        }
        if( remoteRenderScreen == null) {
            remoteRenderScreen = new SurfaceViewRenderer(this);
            remoteRenderScreen =findViewById(R.id.remote_video_view);
        }
        if(rootEglBase== null) {
            rootEglBase = EglBase.create();
            localRender.init(rootEglBase.getEglBaseContext(), null);
            remoteRenderScreen.init(rootEglBase.getEglBaseContext(), null);
        }
        updateVideoView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_game_state:
                mPrsenter.clickStateButton();
                break;
            case R.id.layout_increase_dice:
                mPrsenter.increaseDice();
                break;
            case R.id.layout_catch:
                mPrsenter.catchPlayer();
                break;
            case R.id.image_home_button_gamepage:
                mExitGameDialog = new ExitGameDialog(this,mExitRoomCallback);
                mExitGameDialog.show();
                break;
            case R.id.layout_for_video_switch:
                mPrsenter.touchVideoSwitch();
                break;
            case R.id.layout_show_player_list:
                showPlayerJoinedDialog();
                break;
        }
    }

    @Override
    public void freshPlayerHaveJoinedText(ArrayList<Gamer> joinedList, Gamer newGamer) {
        playerJoinedList = joinedList;
        //load init players data
        if(countForPlayerInviteTotal == 0 ){
            mPrsenter.loadPlayerInvitedTotal();
        }else {
            //set count to firebase For invitee use
            mPrsenter.updatePlayInvitedCountToFirebase(countForPlayerInviteTotal);
            textPlayerCountRightNow.setText(joinedList.size()+"/"+countForPlayerInviteTotal);
            Toast.makeText(thisActivity, "玩家 "+newGamer.getUserName()+" 已加入", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void freshTotalPlayerUI(int count) {
        countForPlayerInviteTotal = count;
        textPlayerCountRightNow.setText(playerJoinedList.size()+"/"+countForPlayerInviteTotal);
    }

    @Override
    public void freshTextInfo(String message) {
        textTellPlayerInfo.setText(message);
    }

    public void updateVideoView() {
        remoteRenderLayout.setPosition(ConstantForWebRTC.REMOTE_X, ConstantForWebRTC.REMOTE_Y
                , ConstantForWebRTC.REMOTE_WIDTH, ConstantForWebRTC.REMOTE_HEIGHT);
        remoteRenderScreen.setScalingType(scalingType);
        remoteRenderScreen.setMirror(false);

        if (mPrsenter.getIceConnectedInWebRTC()) {
            //show connected layout
            localRenderLayout.setPosition(
                    ConstantForWebRTC.LOCAL_X_CONNECTED, ConstantForWebRTC.LOCAL_Y_CONNECTED
                    , ConstantForWebRTC.LOCAL_WIDTH_CONNECTED, ConstantForWebRTC.LOCAL_HEIGHT_CONNECTED);
            localRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL);
        } else {
            localRenderLayout.setPosition(
                    ConstantForWebRTC.LOCAL_X_CONNECTING, ConstantForWebRTC.LOCAL_Y_CONNECTING
                    , ConstantForWebRTC.LOCAL_WIDTH_CONNECTING, ConstantForWebRTC.LOCAL_HEIGHT_CONNECTING);
            localRender.setScalingType(scalingType);
        }
        localRender.setMirror(true);
        localRender.requestLayout();
        remoteRenderScreen.requestLayout();

    }


    private void showPlayerJoinedDialog() {
        PlayerJoinedDialog playerJoinedDialog = new PlayerJoinedDialog();
        Bundle args = new Bundle();
        //add playerList infomation to dialog
        args.putStringArrayList("nameList",mPrsenter.getPlayerJoinedNameList());
        args.putStringArrayList("photoList",mPrsenter.getPlayerJoinedPhotoURLList());
        playerJoinedDialog.setArguments(args);
        //create FragmentTransaction for show dialog
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // Create and show the dialog.
        DialogFragment dialogFragment =playerJoinedDialog;
        dialogFragment.show(ft, "dialogPlayerJoined");
    }

    @Override
    public void setPresenter(Object presenter) {
        mPrsenter = (GamePagePresenter) presenter;
    }

    @Override
    public void freshStateButtonUI(String buttonType) {
        switch (buttonType) {
            case "start":
                imageReadyStateButton.setImageResource(R.drawable.start_button_gamepage);
                break;
            case "wait host start":
                imageReadyStateButton.setImageResource(R.drawable.wait_button_gampage);
                break;
            case "ready":
                imageReadyStateButton.setImageResource(R.drawable.ready_button_gamepage);
                break;
            case "get ready":
                imageReadyStateButton.setImageResource(R.drawable.cancel_button_gamepage);
                break;
        }
    }

    @Override
    public void freshDiceUI(List<Integer> diceList) {
        for(int diceInView =0;diceInView<5;diceInView++) {
            imageDiceArray[diceInView].setImageResource(diceImageSourceArray[diceList.get(diceInView)-1]);
        }
    }

    @Override
    public void freshCatchAndIncreaseUI(boolean increaseVisible, boolean catchVisible) {
        if(increaseVisible)  {
            layoutIncreaseDice.setVisibility(View.VISIBLE);
        }
        else  {
            layoutIncreaseDice.setVisibility(View.INVISIBLE);
        }

        if(catchVisible) {
            layoutCatch.setVisibility(View.VISIBLE);
        }
        else {
            layoutCatch.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void freshRecentDiceUI(CurrentInformation currentInformation) {
        textShowInformation.setText(currentInformation.getRecentDiceNumber()+" 個");
        imageReadyStateButton.setImageResource(diceImageSourceForInfo[currentInformation.getRecentDiceType()-1]);
    }

    @Override
    public void showEndInformation(String endText) {
        mGameEndDialog = new GameEndDialog(this,endText,mExitRoomCallback);
        mGameEndDialog.show();
    }
    //set the game element View and button for start a new game
    @Override
    public void resetView(boolean isNextPlayer) {
        imageReadyStateButton.setImageResource(R.drawable.ready_button_gamepage);
        layoutCatch.setVisibility(View.INVISIBLE);
        textShowInformation.setText(" ");
        for(int i=0;i<5;i++) {
            imageDiceArray[i].setImageResource(R.drawable.table_random_dice);
        }
        if(isNextPlayer) {
            layoutIncreaseDice.setVisibility(View.VISIBLE);
        } else {
            layoutIncreaseDice.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void showOtherGamerLeaveDialog() {
        GamerLeaveDialog gamerLeaveDialog = new GamerLeaveDialog(this,mExitRoomCallback);
        gamerLeaveDialog.show();
    }

    @Override
    public void showVideo() {
        layoutVideoBack.setVisibility(View.VISIBLE);
        imageVideobackground.setVisibility(View.INVISIBLE);
        //check Android Version
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //Version < Android 6 , directly start Video call
            startVideoCall();
        } else {
            //Version > Android 6, before video call should take the permission
            GamePagePermissionsDispatcher.startVideoCallWithPermissionCheck(this);
        }
    }

    @Override
    public void closeVideo() {
        releaseSurfaceView();
        freshSwitchUI(false);
        layoutVideoBack.setVisibility(View.INVISIBLE);
        imageVideobackground.setVisibility(View.VISIBLE);
    }

    @Override
    public void releaseSurfaceView() {
        if (localRender != null) {
            localRender.release();
            localRender = null;
        }
        if (remoteRenderScreen != null) {
            remoteRenderScreen.release();
            remoteRenderScreen = null;
        }
        if(rootEglBase != null) {
            rootEglBase.releaseSurface();
            rootEglBase.release();
            rootEglBase = null;
        }

    }

    @Override
    public void setVideoElement(boolean show) {
        if(show) {
          //this is two persons' game, show Video element
            layoutVideoBack.setVisibility(View.VISIBLE);
            //hide the background picture
            imageVideobackground.setVisibility(View.INVISIBLE);
            layoutSwitchForVideoShow.setVisibility(View.VISIBLE);
            layoutSwitchForVideoShow.setOnClickListener(this);
        } else {
          //TODO not two persons' game , set another UI
        }
    }

    @Override
    public void freshSwitchUI(boolean shouldOpen) {
        switchForVideo.setChecked(shouldOpen);
        imageVideoIcon.setImageResource(shouldOpen?R.drawable.ic_videocam:R.drawable.ic_videocam_off);
    }

    private ExitRoomCallback mExitRoomCallback = new ExitRoomCallback() {
        @Override
        public void exitRoom() {
            mPrsenter.tellServerNotInGame();
            if(switchForVideo.isChecked()) {
                //video is show ,set it disconnect from server
                mPrsenter.disconnectVideo();
            }
            thisActivity.finish();
        }
    };

    @Override
    public void onBackPressed() {
        mExitGameDialog = new ExitGameDialog(this,mExitRoomCallback);
        mExitGameDialog.show();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO})
    void startVideoCall() {
        mPrsenter.startVideo();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        GamePagePermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO})
    void onShowRationale(final PermissionRequest request) {
        new AlertDialog.Builder(GamePage.this)
                .setMessage("未允許「" + getString(R.string.app_name) + "」相機及音訊權限，將使「"
                        + getString(R.string.app_name) + "」無法正常運作，是否重新設定權限？")
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

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO})
    void onPermissionDenied() {
        Toast.makeText(this, "影音相關權限被拒絕", Toast.LENGTH_LONG).show();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO})
    void onNeverAskAgain() {
        Toast.makeText(this, "您可能無法使用視訊功能", Toast.LENGTH_LONG).show();
    }

    //return video sufaceView for creating PeerConnection
    public SurfaceViewRenderer getLocalRender() {
        return localRender;
    }
    public List<VideoRenderer.Callbacks> getRemoteRenderers() {
        return remoteRenderers;
    }
    public EglBase getRootEglBase() {
        return rootEglBase;
    }
}
