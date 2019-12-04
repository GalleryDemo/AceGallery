package com.GalleryDemo.AceGallery.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MediaInfoViewModel extends AndroidViewModel {

    private MediaInfoRepository mRepository;
    private LiveData<List<MediaInfoEntity>> items;

    private LiveData<List<Integer>> timeLineItems;

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

    public LiveData<List<Integer>> getTimeLineItems() {
        return timeLineItems;
    }

    public void setTimeLineItems(LiveData<List<Integer>> timeLineItems) {
        this.timeLineItems = timeLineItems;
    }

    public MediaInfoEntity getItem(int mediaId) throws ExecutionException, InterruptedException {
        return mRepository.getItem(mediaId);
    }

    public void insertItem(MediaInfoEntity item) {
        mRepository.insertItem(item);
    }

    public void deleteItem(int mediaId) {
        mRepository.deleteItem(mediaId);
    }

    public void update(MediaInfoEntity item) {
        mRepository.update(item);
    }

}
