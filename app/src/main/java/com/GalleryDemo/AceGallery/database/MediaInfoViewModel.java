package com.GalleryDemo.AceGallery.database;

import android.app.Application;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MediaInfoViewModel extends AndroidViewModel {

    private int videoCount = 0;
    private int photoCount = 0;

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

    public MediaInfoEntity getItem(int mediaId) throws ExecutionException, InterruptedException {
        return mRepository.getItem(mediaId);
    }

    public void insertItem(MediaInfoEntity item) {
        mRepository.insertItem(item);
    }

    public void deleteItem(MediaInfoEntity item) {
        if (item.getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
            videoCount--;
        } else {
            photoCount--;
        }
        mRepository.deleteItem(item);
    }

    public void update(MediaInfoEntity item) {
        mRepository.update(item);
    }

    public int getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(int videoCount) {
        this.videoCount = videoCount;
    }

    public int getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }
}
