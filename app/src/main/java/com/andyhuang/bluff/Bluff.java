package com.andyhuang.bluff;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.LruCache;
import android.view.inputmethod.InputMethodManager;

import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.io.FileSystem;
import java.io.File;

public class Bluff extends Application {
    private static Context context;
    private static DiskLruCache mDiskLruCache;
    private static LruCache mLruCache;
    private static InputMethodManager imm;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initLruCache();
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
    }
    public static InputMethodManager getImm() {
        return imm;
    }

    public static Context getContext() {
        return context;
    }

    public static LruCache getLruCache() {
        return mLruCache;
    }

    public static DiskLruCache getDiskLruCache() {
        return mDiskLruCache;
    }

    private void initDiskLruCache() {
        try {
            File cacheDir = getDiskCacheDir(context, "ImageCache");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.create(FileSystem.SYSTEM, cacheDir,
                    getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
    private void initLruCache() {

        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 2;

        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

}
