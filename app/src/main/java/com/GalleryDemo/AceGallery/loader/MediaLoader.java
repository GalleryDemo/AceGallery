package com.GalleryDemo.AceGallery.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils;
import com.GalleryDemo.AceGallery.Utils.LoaderUtils;
import com.GalleryDemo.AceGallery.database.MediaInfoEntity;
import com.GalleryDemo.AceGallery.database.MediaInfoRepository;

import static com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils.BODY_TYPE;


public class MediaLoader implements LoaderManager.LoaderCallbacks {

    private static final String TAG = "MediaLoader";


    private Context mContext;

    private MediaInfoRepository mRepository;

    public MediaLoader(Context context) {
        this.mContext = context;
    }
    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        Uri queryUri = MediaStore.Files.getContentUri("external");
        CursorLoader cursorLoader = new CursorLoader(mContext, queryUri, null, selection,
                null, MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC");
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader loader, Object data) {

        mRepository = new MediaInfoRepository(ApplicationContextUtils.getInstance());

        Cursor mCursor = (Cursor) data; //接收返回的cursor
        while (mCursor.moveToNext()) {
            //拼接获取多媒体资源的Uri
            int mediaId = mCursor.getInt(mCursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));
            Uri mediaUri = Uri.withAppendedPath(MediaStore.Files.getContentUri("external"), mediaId + "");
            //Todo: recover the location method
/*            //如果运行在android Q
            float[] latLong = new float[2]; //获取地理位置的经度纬度
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                //创建资源文件的输入流
                try {
                    mediaUri = MediaStore.setRequireOriginal(mediaUri);
                    *//*mediaUri = MediaStore.setRequireOriginal(mediaUri);*//*
                    InputStream stream = mContext.getContentResolver().openInputStream(mediaUri);
                    if (stream != null) {
                        ExifInterface exifInterface = new ExifInterface(stream);
                        boolean resultForLocation = exifInterface.getLatLong(latLong);
                        //如果地理位置返回空，为空的话返回(0,0)
                        if (!resultForLocation) {
                            latLong = new float[]{0, 0};
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace(); //文件不存在，异常处理，暂定打印堆栈信息
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                latLong = new float[]{
                        mCursor.getFloat(mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.LATITUDE)),
                        mCursor.getFloat(mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.LONGITUDE))
                };
            }*/
            String mediaName = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME));
            long mediaTime = mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_TAKEN)); //单位是秒
            int mediaType = mCursor.getInt(mCursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE));
            int mediaHeight = mCursor.getInt(mCursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.HEIGHT));
            int mediaWidth = mCursor.getInt(mCursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.WIDTH));

            String mediaDate = LoaderUtils.time2Date(mediaTime);//日期long转String

            MediaInfoEntity bodyEntity = new MediaInfoEntity(mediaId, null,
                    mediaUri.toString(), mediaName, mediaDate, mediaType,
                    null, mediaHeight, mediaWidth, BODY_TYPE);


            if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                long duration = mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION));
                bodyEntity.setVideoDuration(LoaderUtils.stringForTime(duration));
            } else {

            }

            mRepository.insertItem(bodyEntity);
            //Log.d(TAG, "onLoadFinished: insertBody: offset = " + bodyEntity.getItem_id() + " mediaId = " + bodyEntity.getMediaId() + " mediaDate = " + bodyEntity.getMediaDate());

/*            boolean isSameDay = LoaderUtils.isSameDay(lastTime, mediaTime);
            if (!isSameDay) {
                offset++;
                MediaInfoEntity headEntity = new MediaInfoEntity(mediaId + offset, mediaId + offset, null,
                        null, null, mediaDate, 0,
                        null, 0, 0, HEAD_TYPE);
                mRepository.insertItem(headEntity);
                Log.d(TAG, "onLoadFinished: insertHead: offset = " + headEntity.getItem_id() + " mediaId = " + headEntity.getMediaId() + " mediaDate = " + headEntity.getMediaDate());
                lastTime = mediaTime;
            }*/
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }
}
