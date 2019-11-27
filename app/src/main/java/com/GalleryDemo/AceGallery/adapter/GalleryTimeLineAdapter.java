/**
 * @author Lumpy
 * @date 2019.11.14
 * @version 1.0
 *
 */


package com.GalleryDemo.AceGallery.adapter;

import android.content.Context;
import android.content.Intent;
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

import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.Utils.AlbumBitmapCacheHelper;
import com.GalleryDemo.AceGallery.database.MediaInfoEntity;
import com.GalleryDemo.AceGallery.ui.GalleryTimeLineActivity;
import com.GalleryDemo.AceGallery.ui.PreviewActivity;

import java.util.ArrayList;
import java.util.List;

import static com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils.BODY_TYPE;
import static com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils.FOOT_TYPE;
import static com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils.HEAD_TYPE;

public class GalleryTimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "GalleryTimeLineAdapter";

    private Context mContext;

    private List<MediaInfoEntity> mItemList = new ArrayList<>();
    private List<Integer> mHeadPositionList = new ArrayList<>();
    private List<MediaInfoEntity> mPhotoList = new ArrayList<>();

    private MediaInfoEntity bean;
    private int index;



    public GalleryTimeLineAdapter(GalleryTimeLineActivity mContext) {
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
            mPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaInfoEntity photoItem = mItemList.get(getAdapterPosition());
                    Intent intent = PreviewActivity.newIntent(mContext, photoItem.getMediaId(), 200);
                    mContext.startActivity(intent);
                }
            });
            mFavorImage = itemView.findViewById(R.id.favor_tiny);
            mPhotoDate = itemView.findViewById(R.id.media_date_tiny);
            mPhotoLocation = itemView.findViewById(R.id.media_location_tiny);
            mMediaType = itemView.findViewById(R.id.media_type_tiny);
            mMoreButton = itemView.findViewById(R.id.more_button_tiny);
            mMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(getContext(),itemView);
                    final MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.item_time_line, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.delete:
                                    Toast.makeText(getContext(), "Delete", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.exit:
                                    Toast.makeText(getContext(), "退出", Toast.LENGTH_SHORT).show();
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

        bean = mItemList.get(position);
        index = position;


        Log.d(TAG, "onBindViewHolder: position = " + position + ", ViewType = " + getItemViewType(position));

        if (holder instanceof HeadViewHolder) {

            ((HeadViewHolder)holder).time_line.setText(bean.getMediaDate());

        } else if (holder instanceof FootViewHolder) {

            ((FootViewHolder)holder).mStatView.setText("666 张图片、" + "666 个视频");

        } else if (holder instanceof BodyViewHolder) {

            final BodyViewHolder bodyHolder = (BodyViewHolder)holder;
            //todo:fix the location float[2];

/*            if (bean.getMediaLocation()[0] != 0 || bean.getMediaLocation()[1] != 0) {
                LocationUtils.setAddress(mContext, bean.getMediaId(), bean.getMediaLocation()[0], bean.getMediaLocation()[1], bodyHolder.mPhotoLocation);
            }
            else {
                bodyHolder.mPhotoLocation.setText("");
            }*/

            Uri imageUri = Uri.parse(bean.getMediaStringUri());

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

    public void setAdapterList(List<MediaInfoEntity> list) {
        List<Integer> headList = new ArrayList<>();
        String lastDate = null;
        for (int i = 0;i < list.size();i ++) {
            MediaInfoEntity item = list.get(i);
            Log.d(TAG, "setAdapterList: mediaId = " + item.getMediaId() + " mediaDate = " + item.getMediaDate());
            if (item.getDataType() == BODY_TYPE) {
                if (item.getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                    mPhotoList.add(item);
                }
                if (!item.getMediaDate().equals(lastDate)) {
                    MediaInfoEntity timeLineItem = new MediaInfoEntity(0,
                            0, null, null,
                            null, item.getMediaDate(), 0,
                            null, 0, 0, HEAD_TYPE);
                    list.add(i, timeLineItem);

                    lastDate = item.getMediaDate();
                }
            } else if (item.getDataType() == HEAD_TYPE) {
                list.remove(i);
            }
        }
        this.mItemList = list;
        notifyDataSetChanged();

        this.mHeadPositionList = headList;
    }


    public Context getContext() {
        return mContext;
    }


}
