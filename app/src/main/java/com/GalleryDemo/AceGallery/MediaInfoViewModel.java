package com.GalleryDemo.AceGallery;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.GalleryDemo.AceGallery.bean.MediaInfoRepository;
import com.GalleryDemo.AceGallery.database.MediaInfoEntity;

import java.util.List;

public class MediaInfoViewModel extends AndroidViewModel {

    private MediaInfoRepository mRepository;
    private LiveData<List<MediaInfoEntity>> items;

    public MediaInfoViewModel(@NonNull Application application) {
        super(application);
        this.mRepository = new MediaInfoRepository(application);
    }

    public LiveData<List<MediaInfoEntity>> getAllItems() {
        if (items == null) {
            items = mRepository.getAllItems();
        }
        return items;
    }

    public MediaInfoEntity getItem(int mediaId) {
        return mRepository.getItem(mediaId);
    }

    public void insertAllItems(List<MediaInfoEntity> items) {
        mRepository.insertAllItems(items);
    }

    public void deleteItem(int mediaId) {
        mRepository.deleteItem(mediaId);
    }

}
