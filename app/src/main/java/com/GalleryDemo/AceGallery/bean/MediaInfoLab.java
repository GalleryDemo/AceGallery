package com.GalleryDemo.AceGallery.bean;

import android.content.Context;

import java.util.List;

public class MediaInfoLab {


    private static final String TAG = "MediaInfoLab";

    private static MediaInfoLab sMediaInfoLab;

    private List <MediaInfoBean> mMediaInfoBeanList;

    private Context mContext;

    public MediaInfoLab(Context context) {
        this.mContext = context;
    }

    public static MediaInfoLab get(Context context) {
        if (sMediaInfoLab == null) {
            sMediaInfoLab = new MediaInfoLab(context);
        }
        return sMediaInfoLab;
    }

    public void setmMediaInfoBeanList(List<MediaInfoBean> mMediaInfoBeanList) {
        this.mMediaInfoBeanList = mMediaInfoBeanList;
    }

    public List<MediaInfoBean> getmMediaInfoBeanList() {
        return mMediaInfoBeanList;
    }


}
