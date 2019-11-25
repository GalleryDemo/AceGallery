package com.GalleryDemo.AceGallery.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mediaInfo")
public class MediaInfo {

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

    public MediaInfo(int mediaId, String mediaAddress, String mediaStringUri, String mediaName,
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
}




