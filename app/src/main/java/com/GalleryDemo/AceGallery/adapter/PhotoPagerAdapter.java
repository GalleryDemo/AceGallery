package com.GalleryDemo.AceGallery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils;
import com.GalleryDemo.AceGallery.bean.MediaInfoBean;
import com.GalleryDemo.AceGallery.ui.view.VideoSurfaceView;
import com.GalleryDemo.AceGallery.ui.view.ZoomImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class PhotoPagerAdapter extends PagerAdapter {

    private static final String TAG = "PhotoPagerAdapter";

    private Context mContext;
    private List<MediaInfoBean> mPagerList;


    public PhotoPagerAdapter(Context context, List<MediaInfoBean> pagerList) {
        this.mContext = context;
        this.mPagerList = pagerList;
    }

    @Override
    public int getCount() {
        return mPagerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;

    }


    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int mediaType = mPagerList.get(position).getMediaType();
        if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {

            View videoView = instantiateVideoItem(position);
            container.addView(videoView);
            return videoView;

        } else if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {

            View photoView = instantiatePhotoItem(position);
            container.addView(photoView);
            return photoView;

        } else {
            return null;
        }

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }


    private View instantiatePhotoItem(int position) {
        View view = LayoutInflater.from(ApplicationContextUtils.getContext()).inflate(R.layout.widget_zoom_image, null);
        final ZoomImageView zoomImageView = view.findViewById(R.id.photoImage);
        Log.d(TAG, "instantiateItem: position = " + position);
        Uri photoUri = mPagerList.get(position).getMediaStringUri();
        Bitmap bitmap = null;
        try {
            InputStream inputStream = mContext.getContentResolver().openInputStream(photoUri);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        zoomImageView.setSourceImageBitmap(bitmap, ApplicationContextUtils.getContext());
        return view;
    }

    private View instantiateVideoItem(int position) {
        View view = LayoutInflater.from(ApplicationContextUtils.getContext()).inflate(R.layout.video_surface, null);
        final VideoSurfaceView videoSurfaceView = view.findViewById(R.id.video_surface_view);
        return view;
    }



}
