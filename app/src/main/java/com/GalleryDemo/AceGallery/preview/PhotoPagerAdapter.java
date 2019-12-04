package com.GalleryDemo.AceGallery.preview;

import android.content.Context;
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
import com.GalleryDemo.AceGallery.preview.video.VideoSurfaceFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils.BODY_TYPE;

public class PhotoPagerAdapter extends PagerAdapter {

    private static final String TAG = "PhotoPagerAdapter";

    private Context mContext;
    private PreviewFragment previewFragment;

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
            Log.d(TAG, "instantiateItem: VideoPosition = " + position);
            View videoView = instantiateVideoItem(position);
            container.addView(videoView);
            return videoView;

        } else if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
            Log.d(TAG, "instantiateItem: PhotoPosition = " + position);
            View photoView = null;
            try {
                photoView = instantiatePhotoItem(position);
            } catch (IOException e) {
                e.printStackTrace();
            }

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

    private View instantiatePhotoItem(int position) throws IOException {
        View view = LayoutInflater.from(ApplicationContextUtils.getContext()).inflate(R.layout.widget_zoom_image, null);
        final ZoomImageView zoomImageView = view.findViewById(R.id.photoImage);
        final Uri photoUri = Uri.parse(mPagerList.get(position).getMediaStringUri());

        ImageAsyncTaskHelper imageAsyncTaskHelper = new ImageAsyncTaskHelper(zoomImageView, mContext);
        imageAsyncTaskHelper.execute(photoUri);
        return view;
    }

    private View instantiateVideoItem(int position) {
        final MediaInfoEntity videoItem = mPagerList.get(position);
        View view = LayoutInflater.from(ApplicationContextUtils.getContext()).inflate(R.layout.video_preview, null);

        ImageView mVideo = view.findViewById(R.id.video_picture);
        ImageView mPlayVideo = view.findViewById(R.id.play_video);
        mPlayVideo.setImageResource(R.drawable.video_iconl);
        mPlayVideo.setOnClickListener(new View.OnClickListener() {
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

        Uri videoUri = Uri.parse(videoItem.getMediaStringUri());
        MediaMetadataRetriever video = new MediaMetadataRetriever();
        video.setDataSource(mContext, videoUri);
        ImageAsyncTaskHelper imageAsyncTaskHelper = new ImageAsyncTaskHelper(mVideo, mContext);
        imageAsyncTaskHelper.execute(videoUri);

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


}
