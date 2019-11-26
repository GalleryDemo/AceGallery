package com.GalleryDemo.AceGallery;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.GalleryDemo.AceGallery.bean.MediaInfoRepository;
import com.GalleryDemo.AceGallery.database.MediaInfoEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MediaInfoViewModel extends AndroidViewModel {

    private MediaInfoRepository mRepository;
    private LiveData<List<MediaInfoEntity>> items;

    public MediaInfoViewModel(@NonNull Application application) {
        super(application);
        this.mRepository = new MediaInfoRepository(application);
    }

    public LiveData<List<MediaInfoEntity>> getAllItems() throws ExecutionException, InterruptedException {
        if (items == null) {
            items = mRepository.getAllItems();
        }
        return items;
    }

    public MediaInfoEntity getItem(int mediaId) throws ExecutionException, InterruptedException {
        return mRepository.getItem(mediaId);
    }

    public void insertAllItems(List<MediaInfoEntity> items) {
        mRepository.insertAllItems(items);
    }

    public void insertItem(MediaInfoEntity item) {
        mRepository.insertItem(item);
    }

    public void deleteItem(int mediaId) {
        mRepository.deleteItem(mediaId);
    }

}
