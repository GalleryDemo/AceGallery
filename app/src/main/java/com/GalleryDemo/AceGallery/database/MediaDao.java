package com.GalleryDemo.AceGallery.database;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MediaDao {
    @Query("SELECT * FROM mediaInfo")
    List<MediaInfo> getAll();

    @Query("SELECT media_address FROM mediaInfo WHERE media_id = :mediaID")
    String getAddress(int mediaID);

/*    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAddress(int id, String address);

    @Insert
    void insertAll(List<MediaInfo> list);*/

    @Query("UPDATE mediaInfo SET media_address = :mediaAddress WHERE media_id = :mediaId")
    void updateAddress(int mediaId, String mediaAddress);

    @Query("DELETE FROM mediaInfo WHERE media_id = :mediaId")
    void delete(int mediaId);
}

