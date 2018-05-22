package com.andyhuang.bluff.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.andyhuang.bluff.BluffContract;
import com.andyhuang.bluff.BluffPresenter;
import com.andyhuang.bluff.FriendPage.FragmentListener;
import com.andyhuang.bluff.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class MainHallPage extends BaseActivity implements BluffContract.View,FragmentListener {
    private BluffContract.Presenter mPresenter;
    private DrawerLayout myDrawerLayout;
    private NavigationView mNavigationView;
    private ImageView imageMenuButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_hall_page);
        myDrawerLayout = (DrawerLayout)findViewById(R.id.drawrlayout_main);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        imageMenuButton = (ImageView)findViewById(R.id.image_menu_button);

        mPresenter = new BluffPresenter(this,getFragmentManager(),this);
        mNavigationView.setNavigationItemSelectedListener(navigationViewListener());
        imageMenuButton.setOnClickListener(mainClickListener);
        setDrawerLayout();
        mPresenter.setDisconnectWhenGetOutline();
    }

    public void setDrawerLayout() {
        myDrawerLayout.setFitsSystemWindows(true);
        myDrawerLayout.setClipToPadding(false);
    }

    private NavigationView.OnNavigationItemSelectedListener navigationViewListener() {
        return new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_one:
                        mPresenter.transToMainPage();
                        break;
                    case R.id.item_two:
                        mPresenter.transToFriendPage();
                        break;
                    case R.id.item_three:
                        mPresenter.transToProfilePage();
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
    public void showGamePage(String gameID) {
        Intent intent = new Intent();
        intent.setClass(this,GamePage.class);
        intent.putExtra("gameID",gameID);
        //I am invitee , set is Host false
        intent.putExtra("isHost",false);
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
}
