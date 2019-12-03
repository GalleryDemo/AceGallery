package com.GalleryDemo.AceGallery.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "mediaInfoEntity",
        indices = {@Index(value = "media_id", unique = true)})
public class MediaInfoEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    private int item_id;

    @ColumnInfo(name = "media_id")
    private int mediaId;

    @ColumnInfo(name = "media_address")
    private String mediaAddress;

    @ColumnInfo(name = "media_string_uri")
    private String mediaStringUri;

    @ColumnInfo(name = "media_name")
    private String mediaName;

    @ColumnInfo(name = "media_date")
    private String mediaDate;

    @ColumnInfo(name = "media_type")
    private int mediaType;

    @ColumnInfo(name = "video_duration")
    private String videoDuration;

    @ColumnInfo(name = "media_height")
    private int mediaHeight;

    @ColumnInfo(name = "media_width")
    private int mediaWidth;

    @ColumnInfo(name = "data_type")
    private int dataType;

    @ColumnInfo(name = "favor_selected")
    private boolean favor;


    public MediaInfoEntity(int mediaId, String mediaAddress, String mediaStringUri, String mediaName,
                           String mediaDate, int mediaType, String videoDuration, int mediaHeight,
                           int mediaWidth, int dataType) {

        this.mediaId = mediaId;
        this.mediaAddress = mediaAddress;
        this.mediaStringUri = mediaStringUri;
        this.mediaName = mediaName;
        this.mediaDate = mediaDate; //1图片， 3视频
        this.mediaType = mediaType;
        this.videoDuration = videoDuration;
        this.mediaHeight = mediaHeight;
        this.mediaWidth = mediaWidth;
        this.dataType = dataType;
    }


    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
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

    public boolean isFavor() {
        return favor;
    }

    public void setFavor(boolean favor) {
        this.favor = favor;
    }
}




