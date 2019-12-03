package com.GalleryDemo.AceGallery.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.database.MediaInfoEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AlbumBitmapCacheHelper {
    private static final String TAG = "AlbumBitmapCacheHelper";
    //单例模式
    private Context mContext;
    private volatile static AlbumBitmapCacheHelper instance = null;
    private LruCache<String, Bitmap> cache;
    private ArrayList<String> currentShowString;
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(2, 5, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

    private AlbumBitmapCacheHelper(Context mContext) {
        this.mContext = mContext;
        final int memory = (int) (Runtime.getRuntime().maxMemory() / 1024 / 4);
        Log.d(TAG, "AlbumBitmapCacheHelper: " + memory);

        cache = new LruCache<String, Bitmap>(memory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };

        currentShowString = new ArrayList<String>();
    }

    public static AlbumBitmapCacheHelper getInstance(Context mContext) {
        if (instance == null) {
            synchronized (AlbumBitmapCacheHelper.class) {
                if (instance == null) {
                    instance = new AlbumBitmapCacheHelper(mContext);
                }
            }
        }
        return instance;
    }

    public Bitmap getBitmap(final Uri uri, final MediaInfoEntity mediaInfoEntity, final ImageView imageView, final ILoadImageCallback callback){
        Bitmap bitmap = getBitmapFromCache(uri.toString(), mediaInfoEntity.getMediaWidth(), mediaInfoEntity.getMediaHeight());
        if (bitmap == null) {
            decodeBitmapFromPath(uri, mediaInfoEntity, callback, imageView);
        }
        return bitmap;
    }

    private void decodeBitmapFromPath(final Uri uri, final MediaInfoEntity mediaInfoEntity, final ILoadImageCallback callback, final ImageView imageView) throws OutOfMemoryError {

        final int width = mediaInfoEntity.getMediaWidth();
        final int height = mediaInfoEntity.getMediaHeight();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                callback.onLoadImageCallBack((Bitmap) msg.obj, uri, imageView);
            }
        };

        Future<?> task = tpe.submit(new Runnable() {
            @Override
            public void run() {
                File picFile = null;
                Bitmap bitmap = null;
                int screenWidthMetrics = ThumbnailUtils.getScreenMetrics(mContext).widthPixels / 3;
                //创建存储缩略图路径
                File file = new File(ThumbnailUtils.getDataPath());

                if (!file.exists()) {
                    file.mkdir();
                }
                String tempPath = ThumbnailUtils.getDataPath() + ThumbnailUtils.toHash(uri.toString()) + ".temp";

                picFile = new File(uri.toString());


                File tempFile = new File(tempPath);
                if (tempFile.exists() && (picFile.lastModified() <= tempFile.lastModified())) {
                    bitmap = BitmapFactory.decodeFile(tempPath);
                } else {
                    //制作缩略图
                    if(mediaInfoEntity.getMediaType() == 1) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        try {
                            InputStream imageStream = mContext.getContentResolver().openInputStream(uri);
                            BitmapFactory.decodeStream(imageStream,null,  options);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        int test = options.outWidth / screenWidthMetrics;
                        int test11 = options.outWidth;
                        int test2 = ThumbnailUtils.getScreenMetrics(mContext).widthPixels / 3;
                        Log.d(TAG, "run: " + test + test2 + test11);
                        int sample = (int) options.outWidth > options.outHeight ? options.outHeight / screenWidthMetrics
                                : options.outWidth / screenWidthMetrics;
                        if (sample < 1) {
                            sample = 1;
                        }
                        options.inSampleSize = sample;
                        options.inJustDecodeBounds = false;
                        try {
                            InputStream imageStream = mContext.getContentResolver().openInputStream(uri);
                            bitmap = BitmapFactory.decodeStream(imageStream, null, options);
                            imageStream.close();
                        } catch (FileNotFoundException e) {
                            bitmap = null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                            retriever.setDataSource(mContext, uri);
                            bitmap = retriever.getFrameAtIndex(0);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if (bitmap != null) {
                        if (bitmap.getHeight() >= screenWidthMetrics && bitmap.getWidth() >= screenWidthMetrics ) {
                            bitmap = ThumbnailUtils.centerSquareScaleBitmap(bitmap, screenWidthMetrics);
                        }
                        else {
                            int length = bitmap.getHeight() > bitmap.getWidth() ? bitmap.getWidth() : bitmap.getHeight();
                            bitmap = ThumbnailUtils.centerSquareScaleBitmap(bitmap, length);
                        }

                    }

                    try {
                        if (!tempFile.exists()) {
                            tempFile.createNewFile();
                        } else {
                            tempFile.delete();
                            tempFile.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(tempFile);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        if (bitmap != null) {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            fos.write(baos.toByteArray());
                            fos.flush();
                            fos.close();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bitmap != null) {
                    cache.put(uri.toString() + "_" + width + "_" + height, bitmap);
                }
                Message msg = Message.obtain();
                msg.obj = bitmap;
                handler.sendMessage(msg);
            }
        });
        imageView.setTag(R.id.tag_first, task);
    }

    public interface ILoadImageCallback {
        void onLoadImageCallBack(Bitmap bitmap, Uri uri, ImageView imageView);
    }

    private Bitmap getBitmapFromCache(final String path, int width, int height) {
        return cache.get(path + "_" + width + "_" +height);
    }
}
