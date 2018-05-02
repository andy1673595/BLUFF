package com.andyhuang.bluff;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.andyhuang.bluff.Util.Constants;
import com.firebase.client.Firebase;

public class Bluff extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }

}
