package com.GalleryDemo.AceGallery.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {MediaInfo.class}, version = 1,  exportSchema = false)
public abstract class MediaDatabase extends RoomDatabase {
    public abstract MediaDao getAddressDao();

    private static volatile MediaDatabase INSTANCE;

    public static MediaDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (MediaDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MediaDatabase.class, "address_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback(){
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                }

                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
