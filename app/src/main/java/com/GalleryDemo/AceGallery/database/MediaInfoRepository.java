package com.GalleryDemo.AceGallery.database;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

        return mMediaInfoList;
        /*return new getAllItemsAsync(mMediaDao).execute().get();*/

    }


    public MediaInfoEntity getItem(int mediaId) throws ExecutionException, InterruptedException {
        return new getItemAsync(mMediaDao).execute(mediaId).get();
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

/*    private static class getAllItemsAsync extends AsyncTask<Void, Void, LiveData<List<MediaInfoEntity>>> {
        private MediaDao mMediaDaoAsync;

        getAllItemsAsync(MediaDao mediaDao) {
            mMediaDaoAsync = mediaDao;
        }

        @Override
        protected LiveData<List<MediaInfoEntity>> doInBackground(Void... voids) {
            mMediaDaoAsync.getAllItems();
            return null;
        }
    }*/


    private static class getItemAsync extends AsyncTask<Integer, Void, MediaInfoEntity> {

        private MediaDao mMediaDaoAsync;

        getItemAsync(MediaDao mediaDao) {
            mMediaDaoAsync = mediaDao;
        }

        @Override
        protected MediaInfoEntity doInBackground(Integer... integers) {
            return mMediaDaoAsync.getItem(integers[0]);
        }
    }

    private static class insertItemAsync extends AsyncTask<MediaInfoEntity, Void, Long> {

        private MediaDao mMediaDaoAsync;

        insertItemAsync(MediaDao mediaDao) {
            mMediaDaoAsync = mediaDao;
        }

        @Override
        protected Long doInBackground(MediaInfoEntity... mediaInfoEntities) {
            return mMediaDaoAsync.insertItem(mediaInfoEntities[0]);
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
