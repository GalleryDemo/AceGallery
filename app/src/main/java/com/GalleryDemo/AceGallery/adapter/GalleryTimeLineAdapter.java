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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.Utils.AlbumBitmapCacheHelper;
import com.GalleryDemo.AceGallery.database.MediaInfoEntity;
import com.GalleryDemo.AceGallery.ui.GalleryTimeLineFragment;
import com.GalleryDemo.AceGallery.ui.MediaInfoViewModel;
import com.GalleryDemo.AceGallery.ui.PreviewFragment;

import java.util.ArrayList;
import java.util.List;

import static com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils.BODY_TYPE;
import static com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils.FOOT_TYPE;
import static com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils.HEAD_TYPE;

public class GalleryTimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "GalleryTimeLineAdapter";

    private Context mContext;
    private GalleryTimeLineFragment fragment;

    private List<MediaInfoEntity> mItemList = new ArrayList<>();
    private List<Integer> mHeadPositionList = new ArrayList<>();
    private MediaInfoViewModel mViewModel;

    //剔除头结点 待优化 todo
    private List<MediaInfoEntity> mPhotoList = new ArrayList<>();

    private MediaInfoEntity entity;

    public GalleryTimeLineAdapter(Context mContext, GalleryTimeLineFragment fragment) {
        this.mContext = mContext;
        this.fragment = fragment;
        this.mViewModel = ViewModelProviders.of(fragment).get(MediaInfoViewModel.class);

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

            //点击图片进入大图的监听
            mPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaInfoEntity photoItem = mItemList.get(getAdapterPosition());
                    PreviewFragment previewFragment = PreviewFragment.newInstance(photoItem);

                    fragment.getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragment_container, previewFragment)
                            .addToBackStack(null)
                            .commit();
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

            mFavorImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaInfoEntity favorEntity = mItemList.get(getAdapterPosition());
                    if (favorEntity.isFavor()) {
                        mFavorImage.clearColorFilter();
                        favorEntity.setFavor(false);
                    } else {
                        mFavorImage.setColorFilter(Color.RED);
                        favorEntity.setFavor(true);
                    }
                    mViewModel.update(favorEntity);
                }
            });
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
            View footView = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_bottom_item, parent, false);

            return new FootViewHolder(footView);
        } else {
            Log.d(TAG, "onCreateViewHolder: wrong type!");
            return null;
        }

    }




    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        entity = mItemList.get(position);

        if (holder instanceof HeadViewHolder) {

            ((HeadViewHolder)holder).time_line.setText(entity.getMediaDate());

        } else if (holder instanceof FootViewHolder) {

            ((FootViewHolder)holder).mStatView.setText("666 张图片、" + "666 个视频");

        } else if (holder instanceof BodyViewHolder) {

            final BodyViewHolder bodyHolder = (BodyViewHolder)holder;
            //todo:fix the location float[2];

/*            if (entity.getMediaLocation()[0] != 0 || entity.getMediaLocation()[1] != 0) {
                LocationUtils.setAddress(mContext, entity.getMediaId(), entity.getMediaLocation()[0], entity.getMediaLocation()[1], bodyHolder.mPhotoLocation);
            }
            else {
                bodyHolder.mPhotoLocation.setText("");
            }*/

            Uri imageUri = Uri.parse(entity.getMediaStringUri());

            bodyHolder.mPhoto.setTag(imageUri.toString());
            String string= bodyHolder.mPhoto.getTag().toString();

            Bitmap bitmap = AlbumBitmapCacheHelper.getInstance(getContext()).getBitmap(imageUri, mItemList.get(position).getMediaWidth(),
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

            bodyHolder.mPhotoDate.setText(entity.getMediaDate());
            bodyHolder.mPhotoLocation.setText(entity.getMediaAddress());

            //Log.d(TAG, "onBindViewHolder: MediaType = " + entity.getMediaType());
            if (entity.getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                bodyHolder.mMediaType.setVisibility(View.VISIBLE);
                bodyHolder.mPhotoType.setVisibility(View.INVISIBLE);
                bodyHolder.mVideoLength.setVisibility(View.VISIBLE);
                bodyHolder.mVideoLength.setText(entity.getVideoDuration());
            } else {
                bodyHolder.mMediaType.setVisibility(View.INVISIBLE);
                bodyHolder.mVideoLength.setVisibility(View.INVISIBLE);
                bodyHolder.mPhotoType.setVisibility(View.VISIBLE);
                final String mediaName = entity.getMediaName();
                bodyHolder.mPhotoType.setText(mediaName.substring(mediaName.lastIndexOf('.')+1));
            }

            if (entity.isFavor()) {
                bodyHolder.mFavorImage.setColorFilter(Color.RED);
            } else {
                bodyHolder.mFavorImage.clearColorFilter();
            }
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

    //把全部数据的列表分成body和head
    public void setAdapterList(List<MediaInfoEntity> list) {
        List<Integer> headList = new ArrayList<>();
        String lastDate = null;
        for (int i = 0;i < list.size();i ++) {
            MediaInfoEntity item = list.get(i);
            if (item.getDataType() == BODY_TYPE) {
                if (item.getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                    mPhotoList.add(item);
                }
                //对比时间，时间不同，插入时间节点
                if (!item.getMediaDate().equals(lastDate)) {
                    MediaInfoEntity timeLineItem = new MediaInfoEntity(
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
