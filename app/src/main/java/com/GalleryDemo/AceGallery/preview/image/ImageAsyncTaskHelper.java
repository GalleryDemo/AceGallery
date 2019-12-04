package com.GalleryDemo.AceGallery.preview.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class ImageAsyncTaskHelper extends AsyncTask<Uri, Void, Bitmap> {


    private static final String TAG = "ImageAsyncTaskHelper";

    private final WeakReference<ImageView> imageViewReference;
    private Context mContext;

    public ImageAsyncTaskHelper(ImageView imageView, Context context) {
        this.imageViewReference = new WeakReference<>(imageView);
        this.mContext = context;
    }


    @Override
    protected Bitmap doInBackground(Uri... uris) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = mContext.getContentResolver().openInputStream(uris[0]);
            bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                Log.d(TAG, "doInBackground: fuck you!");
            }
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
            } else {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
