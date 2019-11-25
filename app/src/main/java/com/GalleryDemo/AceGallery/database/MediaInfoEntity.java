package com.GalleryDemo.AceGallery.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mediaInfoEntity")
public class MediaInfoEntity {

    @PrimaryKey
    @ColumnInfo(name = "media_id")
    public int mediaId;

    @ColumnInfo(name = "media_address")
    public String mediaAddress;

    @ColumnInfo(name = "media_string_uri")
    public String mediaStringUri;

    @ColumnInfo(name = "media_name")
    public String mediaName;

    @ColumnInfo(name = "media_date")
    public String mediaDate;

    @ColumnInfo(name = "media_type")
    public int mediaType;

    @ColumnInfo(name = "video_duration")
    public String videoDuration;

    @ColumnInfo(name = "media_height")
    public int mediaHeight;

    @ColumnInfo(name = "media_width")
    public int mediaWidth;

    @ColumnInfo(name = "data_type")
    public int dataType;

    @ColumnInfo(name = "image_count")
    public int imageCount;

    @ColumnInfo(name = "video_count")
    public int videoCount;

    public MediaInfoEntity(int mediaId, String mediaAddress, String mediaStringUri, String mediaName,
                           String mediaDate, int mediaType, String videoDuration, int mediaHeight,
                           int mediaWidth, int dataType, int imageCount, int videoCount) {
        this.mediaId = mediaId;
        this.mediaAddress = mediaAddress;
        this.mediaStringUri = mediaStringUri;
        this.mediaName = mediaName;
        this.mediaDate = mediaDate;
        this.mediaType = mediaType;
        this.videoDuration = videoDuration;
        this.mediaHeight = mediaHeight;
        this.mediaWidth = mediaWidth;
        this.dataType = dataType;
        this.imageCount = imageCount;
        this.videoCount = videoCount;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaAddress() {
        return mediaAddress;
    }

    public void setMediaAddress(String mediaAddress) {
        this.mediaAddress = mediaAddress;
    }

    public String getMediaStringUri() {
        return mediaStringUri;
    }

    public void setMediaStringUri(String mediaStringUri) {
        this.mediaStringUri = mediaStringUri;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaDate() {
        return mediaDate;
    }

    public void setMediaDate(String mediaDate) {
        this.mediaDate = mediaDate;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public int getMediaHeight() {
        return mediaHeight;
    }

    public void setMediaHeight(int mediaHeight) {
        this.mediaHeight = mediaHeight;
    }

    public int getMediaWidth() {
        return mediaWidth;
    }

    public void setMediaWidth(int mediaWidth) {
        this.mediaWidth = mediaWidth;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(int videoCount) {
        this.videoCount = videoCount;
    }
}




