/**
 * @author Lumpy
 * @date 2019.11.14
 * @version 1.0
 *
 */


package com.GalleryDemo.AceGallery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.GalleryDemo.AceGallery.Utils.AlbumBitmapCacheHelper;
import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.Utils.Latlong2Address;
import com.GalleryDemo.AceGallery.bean.MediaInfoBean;

import java.util.ArrayList;
import java.util.List;

public class GalleryTimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "GalleryTimeLineAdapter";

    private Context mContext;

    private List<MediaInfoBean> mItemList = new ArrayList<>();
    private List<Integer> mHeadPositionList = new ArrayList<>();

    public static final int HEAD_TYPE = 0;
    public static final int BODY_TYPE = 1;
    public static final int FOOT_TYPE = 2;

    public GalleryTimeLineAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public class HeadViewHolder extends RecyclerView.ViewHolder {

        public TextView time_line;

        public HeadViewHolder(View itemView) {
            super(itemView);
            time_line = itemView.findViewById(R.id.time_line_title);
        }
    }

    public class BodyViewHolder extends RecyclerView.ViewHolder {

        public ImageView mPhoto;
        public ImageView mFavorImage;
        public TextView mPhotoDate;
        public TextView mPhotoLocation;
        public ImageView mMediaType;
        public ImageView mMoreButton;
        public TextView mVideoLength;
        public TextView mPhotoType;

        public BodyViewHolder(final View itemView) {
            super(itemView);
            mPhoto = itemView.findViewById(R.id.photo_image);
            mFavorImage = itemView.findViewById(R.id.favor_tiny);
            mPhotoDate = itemView.findViewById(R.id.media_date_tiny);
            mPhotoLocation = itemView.findViewById(R.id.media_location_tiny);
            mMediaType = itemView.findViewById(R.id.media_type_tiny);
            mMoreButton = itemView.findViewById(R.id.more_button_tiny);
            mMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(getmContext(),itemView);
                    final MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.item_time_line, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.delete:
                                    Toast.makeText(getmContext(), "Delete", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.exit:
                                    Toast.makeText(getmContext(), "退出", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });

                    popupMenu.show();
                }

            });
            mVideoLength = itemView.findViewById(R.id.video_length_tiny);
            mPhotoType = itemView.findViewById(R.id.photo_type_tiny);
        }


    }

    public class FootViewHolder extends RecyclerView.ViewHolder {

        public TextView mStatView;

        public FootViewHolder(@NonNull View itemView) {
            super(itemView);
            mStatView = itemView.findViewById(R.id.stat_bottom);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position).getDataType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == HEAD_TYPE) {
            View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_line_item, parent, false);
            return new HeadViewHolder(headView);
        } else if (viewType == BODY_TYPE) {
            View bodyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
            return new BodyViewHolder(bodyView);
        } else if (viewType == FOOT_TYPE) {
            Log.d(TAG, "onCreateViewHolder: FootViewCreated!");
            View footView = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_bottom_item, parent, false);
            return new FootViewHolder(footView);
        } else {
            Log.d(TAG, "onCreateViewHolder: wrong type!");
            return null;
        }

    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MediaInfoBean bean = mItemList.get(position);
        Log.d(TAG, "onBindViewHolder: position = " + position + ", ViewType = " + getItemViewType(position));

        if (holder instanceof HeadViewHolder) {

            ((HeadViewHolder)holder).time_line.setText(bean.getMediaDate());

        } else if (holder instanceof FootViewHolder) {

            ((FootViewHolder)holder).mStatView.setText(bean.getImageCount() + " 张图片、" + bean.getVideoCount() + " 个视频");

        } else if (holder instanceof BodyViewHolder) {

            if (bean.getMediaLocation()[0] != 0 || bean.getMediaLocation()[1] != 0) {
                new Thread(new Latlong2Address(mItemList, position)).start();
            }

            final BodyViewHolder bodyHolder = (BodyViewHolder)holder;
            Uri imageUri = bean.getMediaUri();

            Log.d(TAG, "onBindViewHolder: Uri = " + imageUri.toString());
            bodyHolder.mPhoto.setTag(imageUri.toString());
            String string= bodyHolder.mPhoto.getTag().toString();
            Log.d(TAG, "onBindViewHolder: " + string);

            Bitmap bitmap = AlbumBitmapCacheHelper.getInstance(mContext).getBitmap(imageUri, mItemList.get(position).getMediaWidth(),
                    mItemList.get(position).getMediaHeight(), bodyHolder.mPhoto,  new AlbumBitmapCacheHelper.ILoadImageCallback() {
                        @Override
                        public void onLoadImageCallBack(Bitmap bitmap, Uri uri, ImageView imageView) {
                            if (bitmap == null) {
                                return;
                            }
                            if (imageView.getTag().equals(uri.toString())) {
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                    });
            if (bitmap != null) {
                bodyHolder.mPhoto.setImageBitmap(bitmap);
            }

            bodyHolder.mPhotoDate.setText(bean.getMediaDate());
            bodyHolder.mPhotoLocation.setText(bean.getMediaAddress());

            //Log.d(TAG, "onBindViewHolder: MediaType = " + bean.getMediaType());
            if (bean.getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                bodyHolder.mMediaType.setVisibility(View.VISIBLE);
                bodyHolder.mPhotoType.setVisibility(View.INVISIBLE);
                bodyHolder.mVideoLength.setVisibility(View.VISIBLE);
                bodyHolder.mVideoLength.setText(bean.getVideoDuration());
            } else {
                bodyHolder.mMediaType.setVisibility(View.INVISIBLE);
                bodyHolder.mVideoLength.setVisibility(View.INVISIBLE);
                bodyHolder.mPhotoType.setVisibility(View.VISIBLE);
                final String mediaName = bean.getMediaName();
                bodyHolder.mPhotoType.setText(mediaName.substring(mediaName.lastIndexOf('.')+1));
            }

            bodyHolder.mFavorImage.setImageResource(R.drawable.favor);
            bodyHolder.mFavorImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bodyHolder.mFavorImage.setColorFilter(Color.RED);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void updateAdapterList(List<MediaInfoBean> list) {
        List<Integer> headList = new ArrayList<>();
        if (list != null) {
            this.mItemList = list;
            notifyDataSetChanged();
            Log.d(TAG, "updateAdapterList: ItemList.size() = " + mItemList.size());
        }
        for (int i = 0;i < list.size(); i++) {
            if (list.get(i).getDataType() == HEAD_TYPE) {
                headList.add(i);
            }
        }
       this.mHeadPositionList = headList;
    }

    public List<MediaInfoBean> getItemList() {
        return mItemList;
    }

    public Context getmContext() {
        return mContext;
    }
}
