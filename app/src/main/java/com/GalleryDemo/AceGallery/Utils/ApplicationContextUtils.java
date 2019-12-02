package com.GalleryDemo.AceGallery.Utils;

import android.app.Application;
import android.content.Context;


public class ApplicationContextUtils extends Application {
    //todo enum
    public static final int HEAD_TYPE = 0;

    public static final int BODY_TYPE = 1;

    public static final int FOOT_TYPE = 2;


    private static Context sContext;

    private static Application sApplication;

    public static Context getContext() {
        return sContext;
    }

    public static Application getInstance() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sApplication = this;
    }


}
