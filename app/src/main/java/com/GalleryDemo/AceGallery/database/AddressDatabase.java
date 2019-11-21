package com.GalleryDemo.AceGallery.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {AddressInfo.class}, version = 1,  exportSchema = false)
public abstract class AddressDatabase extends RoomDatabase {
    public abstract AddressDao getAddressDao();

    private static volatile AddressDatabase INSTANCE;

    public static AddressDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AddressDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AddressDatabase.class, "address_database")
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
