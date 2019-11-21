package com.GalleryDemo.AceGallery.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface AddressDao {
    @Query("select meidaAddress from address_table where mediaId = :mediaId")
    String getAddress(int mediaId);
    /*@Query("DELETE FROM address_table where mediaId=mediaId")
    void deleteAddress(int media);*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAddress(AddressInfo addressInfos);
}

