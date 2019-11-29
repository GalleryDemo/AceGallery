package com.GalleryDemo.AceGallery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils;
import com.GalleryDemo.AceGallery.database.MediaInfoEntity;
import com.GalleryDemo.AceGallery.ui.PreviewFragment;
import com.GalleryDemo.AceGallery.ui.VideoSurfaceFragment;
import com.GalleryDemo.AceGallery.ui.view.ZoomImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils.BODY_TYPE;

public class PhotoPagerAdapter extends PagerAdapter implements View.OnClickListener {

    private static final String TAG = "PhotoPagerAdapter";

    private Context mContext;
    private PreviewFragment previewFragment;

    private MediaInfoEntity mediaItem;
    private List<MediaInfoEntity> mPagerList = new ArrayList<>();


    public PhotoPagerAdapter(Context context, PreviewFragment previewFragment) {
        this.mContext = context;
        this.previewFragment = previewFragment;
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
            Log.d(TAG, "instantiateItem: position = " + position);
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
        Uri photoUri = Uri.parse(mPagerList.get(position).getMediaStringUri());
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
        mediaItem = mPagerList.get(position);
        /*View view = LayoutInflater.from(ApplicationContextUtils.getContext()).inflate(R.layout.fragment_video_surface, null);*/
        View view = LayoutInflater.from(ApplicationContextUtils.getContext()).inflate(R.layout.video_preview, null);
        final ImageView mVideo = view.findViewById(R.id.video_picture);
        final ImageView mPlay = view.findViewById(R.id.play_video);
        mPlay.setImageResource(R.drawable.video_iconl);
        mPlay.setOnClickListener(this);
        Uri videoUri = Uri.parse(mediaItem.getMediaStringUri());
        MediaMetadataRetriever video = new MediaMetadataRetriever();
        video.setDataSource(mContext, videoUri);
        mVideo.setImageBitmap(video.getFrameAtTime());

        return view;
    }


    public void updateItemList(List<MediaInfoEntity> list) {
        for (int i = 0;i < list.size();i ++) {
            MediaInfoEntity item = list.get(i);
            if (item.getDataType() == BODY_TYPE) {
                this.mPagerList.add(item);
            }
        }
        notifyDataSetChanged();
    }


    public List<MediaInfoEntity> getPagerList() {
        return mPagerList;
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_video:
                VideoSurfaceFragment videoSurfaceFragment = VideoSurfaceFragment.newInstance(mediaItem);
                Log.d(TAG, "onClick: activity is " + videoSurfaceFragment.hashCode());
                previewFragment.getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, videoSurfaceFragment)
                        .addToBackStack(null)
                        .commit();
        }
    }
}
