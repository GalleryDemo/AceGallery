package com.GalleryDemo.AceGallery.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MediaDao {
    @Query("SELECT * FROM MediaInfoEntity ORDER BY media_date DESC, media_id DESC")
    LiveData<List<MediaInfoEntity>> getAllItems();

    @Query("SELECT media_address FROM MediaInfoEntity WHERE media_id = :mediaID")
    String getAddress(int mediaID);

    @Query("SELECT * FROM mediaInfoEntity WHERE media_id = :mediaId")
    MediaInfoEntity getItem(int mediaId);

    @Query("UPDATE MediaInfoEntity SET media_address = :mediaAddress WHERE media_id = :mediaId")
    void updateAddress(int mediaId, String mediaAddress);

    @Delete
    void delete(MediaInfoEntity item);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertItem(MediaInfoEntity item);

    @Update
    void update(MediaInfoEntity item);
}

