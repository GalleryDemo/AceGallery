package com.GalleryDemo.AceGallery.Utils;

import android.app.Application;
import android.content.Context;


public class ApplicationContextUtils extends Application {

    private static Context sContext;

    private static Application sApplication;

    public static Context getContext() {
        return sContext;
    }

    public static Application getInstance(){
        return  sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sApplication = this;
    }


}
