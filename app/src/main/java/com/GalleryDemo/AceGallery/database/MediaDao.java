package com.GalleryDemo.AceGallery.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MediaDao {
    @Query("SELECT * FROM MediaInfoEntity")
    LiveData<List<MediaInfoEntity>> getAllItems();

    @Query("SELECT media_address FROM MediaInfoEntity WHERE media_id = :mediaID")
    String getAddress(int mediaID);

    @Query("SELECT * FROM mediaInfoEntity WHERE media_id = :mediaId")
    MediaInfoEntity getItem(int mediaId);

    @Query("UPDATE MediaInfoEntity SET media_address = :mediaAddress WHERE media_id = :mediaId")
    void updateAddress(int mediaId, String mediaAddress);

    @Query("DELETE FROM MediaInfoEntity WHERE media_id = :mediaId")
    void delete(int mediaId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertItem(MediaInfoEntity item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllItem(List<MediaInfoEntity> items);

}

