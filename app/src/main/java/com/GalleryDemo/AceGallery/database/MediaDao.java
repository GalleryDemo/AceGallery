package com.GalleryDemo.AceGallery.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MediaDao {
    @Query("SELECT * FROM mediaInfo")
    List<MediaInfo> getAll();


    @Insert
    void insertAll(List<MediaInfo> list);

    @Delete
    void delete(MediaInfo mediaInfo);
}

