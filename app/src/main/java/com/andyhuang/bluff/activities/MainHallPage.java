package com.andyhuang.bluff.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.andyhuang.bluff.R;

public class MainHallPage extends BaseActivity {

    private DrawerLayout myDrawerLayout;
    private NavigationView mNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_hall_page);
        myDrawerLayout = (DrawerLayout)findViewById(R.id.drawrlayout_main);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

    }

    public void setDrawerLayout() {
        myDrawerLayout.setFitsSystemWindows(true);
        myDrawerLayout.setClipToPadding(false);

    }

    public NavigationView.OnNavigationItemSelectedListener navigationViewListener() {
        return new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        }
    }

}
