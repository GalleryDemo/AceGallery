package com.GalleryDemo.AceGallery.Utils;

import android.app.Application;
import android.content.Context;

import com.GalleryDemo.AceGallery.bean.MediaInfoBean;
import com.GalleryDemo.AceGallery.bean.MediaInfoLab;

import java.util.List;

public class ApplicationContextUtils extends Application {

    private static Context mContext;

    private static MediaInfoLab sMediaInfoLab;

    private List<MediaInfoBean> mMediaInfoBeanList;


    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }


}
