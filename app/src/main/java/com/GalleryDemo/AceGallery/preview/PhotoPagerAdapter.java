package com.GalleryDemo.AceGallery.preview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.GalleryDemo.AceGallery.preview.image.ImageAsyncTaskHelper;
import com.GalleryDemo.AceGallery.preview.image.ZoomImageView;
import com.GalleryDemo.AceGallery.preview.image.map.ImageSurfaceView;
import com.GalleryDemo.AceGallery.preview.video.VideoSurfaceFragment;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils.BODY_TYPE;

public class PhotoPagerAdapter extends PagerAdapter {

    private static final String TAG = "PhotoPagerAdapter";

    private Context mContext;
    private PreviewFragment previewFragment;

    private List<MediaInfoEntity> mPagerList;

    private LinkedList<View> mPhotoViews;
    private LinkedList<View> mVideoViews;


    public PhotoPagerAdapter(Context context, PreviewFragment previewFragment) {
        this.mContext = context;
        this.previewFragment = previewFragment;
        this.mPagerList = new ArrayList<>();
        this.mPhotoViews = new LinkedList<>();
        this.mVideoViews = new LinkedList<>();
    }

    class PhotoViewHolder {
        //ZoomImageView zoomImageView = null;
        ImageSurfaceView imageSurfaceView =null;
    }

    class VideoViewHolder {
        ImageView mVideoPick = null;
        ImageView mPlayVideo = null;
    }

    @Override
    public int getCount() {
        return mPagerList.size();

    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;

    }

    @NotNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int mediaType = mPagerList.get(position).getMediaType();
        if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
            Log.d(TAG, "instantiateItem: VideoPosition = " + position);
            View videoView = instantiateVideoItem(position);
            container.addView(videoView);
            return videoView;

        } else if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
            Log.d(TAG, "instantiateItem: PhotoPosition = " + position);

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
        Log.d(TAG, "destroyItem: View class is = " + object.getClass());
        if (mPagerList.get(position).getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
            Log.d(TAG, "destroyItem: destroy zoom");
            //ZoomImageView zoomImageView = ((PhotoViewHolder)view.getTag()).zoomImageView;
            ImageSurfaceView imageSurfaceView = ((PhotoViewHolder)view.getTag()).imageSurfaceView;
/*            Bitmap bitmap = ((BitmapDrawable)zoomImageView.getDrawable()).getBitmap();
            bitmap.recycle();*/
 /*           zoomImageView.setSourceImageBitmap(null, mContext);*/
            container.removeView(view);
            mPhotoViews.add(view);
        } else if (mPagerList.get(position).getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
            Log.d(TAG, "destroyItem: destroy video");
            ImageView imageView = ((VideoViewHolder)view.getTag()).mVideoPick;
            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            bitmap.recycle();
            imageView.setImageBitmap(null);
            container.removeView(view);
            mVideoViews.add(view);
        }
/*        if (mPagerList.get(position).getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
            ZoomImageView zoomImageView = ((PhotoViewHolder)mPhotoViews.getFirst().getTag()).zoomImageView;
            zoomImageView
        }*/
    }

    private View instantiatePhotoItem(int position) {
        View convertView;
        PhotoViewHolder photoViewHolder;
        if (mPhotoViews.size() == 0) {
            photoViewHolder = new PhotoViewHolder();
            convertView = LayoutInflater.from(ApplicationContextUtils.getContext()).inflate(R.layout.widget_zoom_image, null);
            photoViewHolder.imageSurfaceView = convertView.findViewById(R.id.photoImage);
            convertView.setTag(photoViewHolder);
        } else {
            convertView = mPhotoViews.removeFirst();
            photoViewHolder = (PhotoViewHolder) convertView.getTag();
        }

        final Uri photoUri = Uri.parse(mPagerList.get(position).getMediaStringUri());
/*        InputStream is = null;
        try {
            is = mContext.getContentResolver().openInputStream(photoUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        try {
            photoViewHolder.imageSurfaceView.setInputStream(mContext,photoUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        photoViewHolder.imageSurfaceView.setViewportCenter();

        //new ImageAsyncTaskHelper.photoAsyncTask(photoViewHolder.zoomImageView, mContext).execute(photoUri);
        return convertView;
    }

    private View instantiateVideoItem(int position) {
        View convertView;
        VideoViewHolder videoViewHolder;

        if (mVideoViews.size() == 0) {
            Log.d(TAG, "instantiateVideoItem: re");
            videoViewHolder = new VideoViewHolder();
            convertView = LayoutInflater.from(ApplicationContextUtils.getContext()).inflate(R.layout.video_preview, null);
            videoViewHolder.mVideoPick = convertView.findViewById(R.id.video_picture);
            videoViewHolder.mPlayVideo = convertView.findViewById(R.id.play_video);
            convertView.setTag(videoViewHolder);
        } else {
            convertView = mVideoViews.removeFirst();
            videoViewHolder = (VideoViewHolder) convertView.getTag();
        }

        final MediaInfoEntity videoItem = mPagerList.get(position);
        videoViewHolder.mPlayVideo.setImageResource(R.drawable.video_icon_normal);
        Uri videoUri = Uri.parse(videoItem.getMediaStringUri());
        MediaMetadataRetriever video = new MediaMetadataRetriever();
        video.setDataSource(mContext, videoUri);
        new ImageAsyncTaskHelper.videoPickAsyncTask(videoViewHolder.mVideoPick).execute(video);
        videoViewHolder.mPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoSurfaceFragment videoSurfaceFragment = VideoSurfaceFragment.newInstance(videoItem);
                Log.d(TAG, "onClick: activity is " + videoSurfaceFragment.hashCode());
                previewFragment.getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, videoSurfaceFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return convertView;
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


}

