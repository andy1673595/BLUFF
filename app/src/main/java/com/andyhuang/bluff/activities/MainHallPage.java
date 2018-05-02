package com.andyhuang.bluff.activities;

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
import com.andyhuang.bluff.R;

public class MainHallPage extends BaseActivity implements BluffContract.View {
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
        imageMenuButton = (ImageView)findViewById(R.id.toolBarMenuButton);

        mPresenter = new BluffPresenter(this,getFragmentManager());
        mNavigationView.setNavigationItemSelectedListener(navigationViewListener());
        imageMenuButton.setOnClickListener(mainClickListener);
        setDrawerLayout();
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
    public void showProfilePage() {

    }

    @Override
    public void showFriendPage() {

    }

    @Override
    public void showMainPage() {

    }

    @Override
    public void onBackPressed() {
        if(myDrawerLayout.isDrawerOpen(Gravity.LEFT)) myDrawerLayout.closeDrawers();
        else super.onBackPressed();
    }

}
