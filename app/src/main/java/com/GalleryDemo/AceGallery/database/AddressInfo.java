package com.GalleryDemo.AceGallery.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "address_table")
public class AddressInfo {
    @PrimaryKey
    @ColumnInfo(name = "mediaId")
    private int mediaId;
    @ColumnInfo(name = "meidaAddress")
    private String mediaAddress;

    public AddressInfo(int mediaId, String mediaAddress) {
        this.mediaId = mediaId;
        this.mediaAddress = mediaAddress;
    }

    public int getMediaId() {
        return mediaId;
    }

    public String getMediaAddress() {
        return mediaAddress;
    }

    public void setMediaId (int mediaId) {
        this.mediaId = mediaId;
    }

    public void setMediaAddress(String mediaAddress) {
        this.mediaAddress = mediaAddress;
    }
}


