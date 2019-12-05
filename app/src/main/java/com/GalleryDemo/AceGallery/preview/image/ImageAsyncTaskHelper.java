package com.GalleryDemo.AceGallery.preview.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class ImageAsyncTaskHelper  {

    private static final String TAG = "ImageAsyncTaskHelper";

    public static class photoAsyncTask extends AsyncTask<Uri, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;
        private final WeakReference<Context> contextWeakReference;

        public photoAsyncTask(ImageView imageView, Context context) {
            this.imageViewReference = new WeakReference<>(imageView);
            this.contextWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Bitmap doInBackground(Uri... uris) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = contextWeakReference.get().getContentResolver().openInputStream(uris[0]);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                if (imageViewReference.get() instanceof  ZoomImageView) {
                    final ZoomImageView zoomImageView = (ZoomImageView) imageViewReference.get();
                    if (zoomImageView != null) {
                        Log.d(TAG, "onPostExecute: fuck you!");
                        zoomImageView.setSourceImageBitmap(bitmap, ApplicationContextUtils.getContext());
                    }
                }
            }
        }
    }

    public static class videoPickAsyncTask extends AsyncTask<MediaMetadataRetriever, Void, Bitmap> {

        private final WeakReference<ImageView> retrieverViewReference;

        public videoPickAsyncTask(ImageView imageView) {
            this.retrieverViewReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(MediaMetadataRetriever... mediaMetadataRetrievers) {
            return mediaMetadataRetrievers[0].getFrameAtTime();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (retrieverViewReference != null && bitmap != null) {
                final ImageView imageView = retrieverViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                    Log.d(TAG, "onPostExecute: this should work");
                }
            }
        }
    }

}
