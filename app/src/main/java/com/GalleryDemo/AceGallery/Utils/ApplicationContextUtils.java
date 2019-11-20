package com.GalleryDemo.AceGallery.Utils;

import android.app.Application;
import android.content.Context;

public class ApplicationContextUtils extends Application {
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
