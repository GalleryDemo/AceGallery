package com.GalleryDemo.AceGallery.bean;

import android.net.Uri;

import java.io.Serializable;

public class MediaInfoBean implements Serializable {
    private Uri mediaUri;
    private String mediaName;
    private String mediaDate;
    private int mediaType;
    private int mediaId;
    private String videoDuration;
    private int mediaHeight;
    private int mediaWidth;
    private float[] mediaLocation;
    private String mediaAddress;
    private int dataType; //文件数据类型，0表示日期数据， 1表示media数据

    public MediaInfoBean(int dataType, String mediaDate) {
        this.dataType = dataType;
        this.mediaDate = mediaDate;
    }

    public MediaInfoBean(Uri mediaUri, String mediaName, String mediaDate, int mediaType, int mediaHeight, int mediaWidth, float[] mediaLocation, int dataType) {
        this.mediaUri = mediaUri;
        this.mediaName = mediaName;
        this.mediaDate = mediaDate;
        this.mediaType = mediaType;
        this.mediaHeight = mediaHeight;
        this.mediaWidth = mediaWidth;
        this.mediaLocation = mediaLocation;
        this.dataType = dataType;
    }

    public void setMediaAddress(String mediaAddress) {
        this.mediaAddress = mediaAddress;
    }

    public void setMediaUri(Uri mediaUri) {
        this.mediaUri = mediaUri;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public void setMediaDate(String mediaDate) {
        this.mediaDate = mediaDate;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public void setMediaHeight(int mediaHeight) {
        this.mediaHeight = mediaHeight;
    }

    public void setMediaWidth(int mediaWidth) {
        this.mediaWidth = mediaWidth;
    }

    public void setMediaLocation(float[] mediaLocation) {
        this.mediaLocation = mediaLocation;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getDataType() {
        return dataType;
    }

    public int getMediaHeight() {
        return mediaHeight;
    }

    public int getMediaWidth() {
        return mediaWidth;
    }

    public int getMediaType() {
        return mediaType;
    }

    public String getMediaDate() {
        return mediaDate;
    }

    public String getvideoDuration() {
        return videoDuration;
    }

    public float[] getMediaLocation() {
        return mediaLocation;
    }

    public Uri getMediaUri() {
        return mediaUri;
    }

    public String getMediaName() {
        return mediaName;
    }

    public int getMediaId() {
        return mediaId;
    }

    public String getMediaAddress() {
        return mediaAddress;
    }
}
