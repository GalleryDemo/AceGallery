package com.GalleryDemo.AceGallery.bean;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.GalleryDemo.AceGallery.database.MediaDao;
import com.GalleryDemo.AceGallery.database.MediaDatabase;
import com.GalleryDemo.AceGallery.database.MediaInfoEntity;

import java.util.List;

public class MediaInfoRepository {

    private static final String TAG = "MediaInfoRepository";

    private static MediaInfoRepository sMediaInfoRepository;

    private LiveData<List <MediaInfoEntity>> mMediaInfoList;

    private MediaDao mMediaDao;

    private Context mContext;

    public MediaInfoRepository(Application application) {
        MediaDatabase db = MediaDatabase.getInstance(application);
        mMediaDao = db.getMediaDao();
        mMediaInfoList = mMediaDao.getAllItems();

    }

    public LiveData<List<MediaInfoEntity>> getAllItems() {
        new getAllItemsAsync(mMediaDao).execute();
        return null;
    }


    public MediaInfoEntity getItem(int mediaId) {
        new getItemAsync(mMediaDao).execute(mediaId);
        return null;
    }

    public void insertItem(MediaInfoEntity item) {
        new insertItemAsync(mMediaDao).execute(item);
    }


    public void insertAllItems(List<MediaInfoEntity> items) {
        new insertAllItemsAsync(mMediaDao).execute(items);
    }

    public void deleteItem(int mediaId) {
        new deleteItemAsync(mMediaDao).execute(mediaId);
    }

    private static class getAllItemsAsync extends AsyncTask<Void, Void, Void> {
        private MediaDao mMediaDaoAsync;

        getAllItemsAsync(MediaDao mediaDao) {
            mMediaDaoAsync = mediaDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mMediaDaoAsync.getAllItems();
            return null;
        }
    }


    private static class getItemAsync extends AsyncTask<Integer, Void, Void> {

        private MediaDao mMediaDaoAsync;

        getItemAsync(MediaDao mediaDao) {
            mMediaDaoAsync = mediaDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            mMediaDaoAsync.getItem(integers[0]);
            return null;
        }
    }

    private static class insertItemAsync extends AsyncTask<MediaInfoEntity, Void, Void> {

        private MediaDao mMediaDaoAsync;

        insertItemAsync(MediaDao mediaDao) {
            mMediaDaoAsync = mediaDao;
        }

        @Override
        protected Void doInBackground(MediaInfoEntity... mediaInfoEntities) {
            mMediaDaoAsync.insertItem(mediaInfoEntities[0]);
            return null;
        }
    }

    private static class insertAllItemsAsync extends AsyncTask<List<MediaInfoEntity>, Void, Void> {

        private MediaDao mMediaDaoAsync;

        insertAllItemsAsync(MediaDao mediaDao) {
            mMediaDaoAsync = mediaDao;
        }

        @Override
        protected Void doInBackground(List<MediaInfoEntity>... lists) {
            mMediaDaoAsync.insertAllItem(lists[0]);
            return null;
        }
    }

    private static class deleteItemAsync extends AsyncTask<Integer, Void, Void> {

        private MediaDao mMediaDaoAsync;

        deleteItemAsync(MediaDao mediaDao) {
            mMediaDaoAsync = mediaDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            mMediaDaoAsync.delete(integers[0]);
            return null;
        }
    }
}
