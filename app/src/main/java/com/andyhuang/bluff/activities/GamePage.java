package com.andyhuang.bluff.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
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
import com.andyhuang.bluff.Util.ConstantForWebRTC;
import com.andyhuang.bluff.Util.Constants;
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
    private String roomID;
    private boolean isHost;
    private GamePagePresenter mPrsenter;
    private ImageView imageIncreaseDiceButton;
    private ConstraintLayout layoutIncreaseDice;
    private ImageView imageCatchButton;
    private ConstraintLayout layoutCatch;
    private ImageView imageReadyStateButton;
    private ImageView[] imageDiceArray;
    private int[] diceImageSourceArray;
    private int[] diceImageSourceForInfo;
    private ImageView imageHomeBackButton;
    private TextView textShowInformation;
    private GameEndDialog mGameEndDialog;
    private ExitGameDialog mExitGameDialog;
    private GamePage thisActivity;
    //layout,image,buttons.. for webRTC
    private RelativeLayout layoutVideoBack;
    private ConstraintLayout layoutSwitchForVideoShow;
    private Switch switchForVideo;
    private ImageView imageVideoIcon;
    private ImageView imageVideobackground;
    private SurfaceViewRenderer localRender;
    private SurfaceViewRenderer remoteRenderScreen;
    private PercentFrameLayout localRenderLayout;
    private PercentFrameLayout remoteRenderLayout;
    private final List<VideoRenderer.Callbacks> remoteRenderers =
            new ArrayList<VideoRenderer.Callbacks>();
    private EglBase rootEglBase;
    //show player list layout elements
    private TextView textPlayerCountRightNow;
    private ConstraintLayout layoutPlayerList;
    int countForPlayerInviteTotal;
    private ArrayList<Gamer> playerJoinedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);
        thisActivity = this;
        //Bind view by id and new Array
        initView();
        //set OnclickListener and Visibility
        imageIncreaseDiceButton.setOnClickListener(this);
        layoutIncreaseDice.setVisibility(View.INVISIBLE);
        layoutCatch.setVisibility(View.INVISIBLE);
        imageCatchButton.setOnClickListener(this);
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
                (ImageView)findViewById(R.id.image_table_dice1),
                (ImageView)findViewById(R.id.image_table_dice2),
                (ImageView)findViewById(R.id.image_table_dice3),
                (ImageView)findViewById(R.id.image_table_dice4),
                (ImageView)findViewById(R.id.image_table_dice5)};
        imageIncreaseDiceButton = (ImageView)findViewById(R.id.image_increae_dice);
        layoutIncreaseDice = (ConstraintLayout)findViewById(R.id.layout_increase_dice);
        imageCatchButton = (ImageView)findViewById(R.id.image_catch);
        layoutCatch = (ConstraintLayout)findViewById(R.id.layout_catch);
        imageReadyStateButton = (ImageView)findViewById(R.id.image_game_state);
        textShowInformation =(TextView)findViewById(R.id.text_show_current_info);
        imageHomeBackButton = (ImageView)findViewById(R.id.image_home_button_gamepage);
        //view for video
        imageVideobackground = (ImageView)findViewById(R.id.image_video_background);
        layoutVideoBack = (RelativeLayout)findViewById(R.id.video_back_layout);
        layoutSwitchForVideoShow = (ConstraintLayout)findViewById(R.id.layout_for_video_switch);
        switchForVideo = (Switch)findViewById(R.id.switchForVideo);
        switchForVideo.setClickable(false);
        imageVideoIcon = (ImageView)findViewById(R.id.image_video_button);
        localRender = (SurfaceViewRenderer) findViewById(R.id.local_video_view);
        remoteRenderScreen = (SurfaceViewRenderer) findViewById(R.id.remote_video_view);
        localRenderLayout = (PercentFrameLayout) findViewById(R.id.local_video_layout);
        remoteRenderLayout = (PercentFrameLayout) findViewById(R.id.remote_video_layout);
        textPlayerCountRightNow = (TextView)findViewById(R.id.text_show_playerlist_button);
        layoutPlayerList = (ConstraintLayout)findViewById(R.id.layout_show_player_list);
        remoteRenderers.add(remoteRenderScreen);
        //set INVISIBLE first, if game is two persons' game set visible
        layoutSwitchForVideoShow.setVisibility(View.INVISIBLE);
        layoutVideoBack.setVisibility(View.INVISIBLE);
        //init dice Array
        diceImageSourceArray = new int[] {R.drawable.table_dice1,R.drawable.table_dice2,R.drawable.table_dice3,
                R.drawable.table_dice4,R.drawable.table_dice5,R.drawable.table_dice6};
        diceImageSourceForInfo = new int[] {R.drawable.dice1,R.drawable.dice2,R.drawable.dice3,
                R.drawable.dice4,R.drawable.dice5,R.drawable.dice6};
    }

    public void creatVideoRenders() {
        // Create video renderers.
        if(localRender ==null) {
            localRender = new SurfaceViewRenderer(this);
            localRender = (SurfaceViewRenderer) findViewById(R.id.local_video_view);
        }
        if( remoteRenderScreen == null) {
            remoteRenderScreen = new SurfaceViewRenderer(this);
            remoteRenderScreen = (SurfaceViewRenderer) findViewById(R.id.remote_video_view);
        }
        if(rootEglBase== null) {
            rootEglBase = EglBase.create();
            localRender.init(rootEglBase.getEglBaseContext(), null);
            remoteRenderScreen.init(rootEglBase.getEglBaseContext(), null);
            localRender.setZOrderMediaOverlay(true);
        }
        updateVideoView();

    }

    @Override
    public void updatePlayerHaveJoinedText(ArrayList<Gamer> joinedList,Gamer newGamer) {
        playerJoinedList = joinedList;
        if(countForPlayerInviteTotal == 0 ){
            mPrsenter.loadPlayerInvitedTotal();
        }else {
            //set count to firebase For invitee use
            mPrsenter.updatePlayInvitedCountToFirebase(countForPlayerInviteTotal);
            textPlayerCountRightNow.setText(joinedList.size()+"/"+countForPlayerInviteTotal);
            Toast.makeText(thisActivity, "玩家 "+newGamer.getUserName()+" 已加入", Toast.LENGTH_SHORT).show();
        }
    }

    public void inviteeSetTotalPlayerInivted(int count) {
        countForPlayerInviteTotal = count;
        textPlayerCountRightNow.setText(playerJoinedList.size()+"/"+countForPlayerInviteTotal);
    }

    public void updateVideoView() {
        RendererCommon.ScalingType scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;
        remoteRenderLayout.setPosition(ConstantForWebRTC.REMOTE_X, ConstantForWebRTC.REMOTE_Y
                , ConstantForWebRTC.REMOTE_WIDTH, ConstantForWebRTC.REMOTE_HEIGHT);
        remoteRenderScreen.setScalingType(scalingType);
        remoteRenderScreen.setMirror(false);

        if (mPrsenter.getIceConnectedInWebRTC()) {
            //show connected layout
            localRenderLayout.setPosition(
                    ConstantForWebRTC.LOCAL_X_CONNECTED, ConstantForWebRTC.LOCAL_Y_CONNECTED
                    , ConstantForWebRTC.LOCAL_WIDTH_CONNECTED, ConstantForWebRTC.LOCAL_HEIGHT_CONNECTED);
            localRender.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_game_state:
                mPrsenter.clickStateButton();
                break;
            case R.id.image_increae_dice:
                mPrsenter.increaseDice();
                break;
            case R.id.image_catch:
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
    public void refreshCatchAndIncreaseUI(boolean increaseVisible, boolean catchVisible) {
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
        imageCatchButton.setVisibility(View.INVISIBLE);
        textShowInformation.setText(" ");
        for(int i=0;i<5;i++) {
            imageDiceArray[i].setImageResource(R.drawable.table_random_dice);
        }
        if(isNextPlayer) {
            imageIncreaseDiceButton.setVisibility(View.VISIBLE);
        } else {
            imageIncreaseDiceButton.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void showOtherGamerLeaveDialog() {
        GamerLeaveDialog gamerLeaveDialog = new GamerLeaveDialog(this,mExitRoomCallback);
        gamerLeaveDialog.show();
    }

    @Override
    public void showTwoPlayerLayout() {

    }

    @Override
    public void showMultiplePlayerLayout() {

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
        Toast.makeText(this, "權限被拒絕", Toast.LENGTH_LONG).show();
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO})
    void onNeverAskAgain() {
        Toast.makeText(this, "可能無法使用視訊功能", Toast.LENGTH_LONG).show();
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
