/**
 * @author Lumpy
 * @date 2019.11.14
 * @version 1.0
 *
 */


package com.GalleryDemo.AceGallery.timeline;

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
import com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils;
import com.GalleryDemo.AceGallery.database.MediaInfoEntity;
import com.GalleryDemo.AceGallery.database.MediaInfoViewModel;
import com.GalleryDemo.AceGallery.preview.PreviewFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

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

    private MediaInfoEntity entity;

    GalleryTimeLineAdapter(Context mContext, GalleryTimeLineFragment fragment) {
        this.mContext = mContext;
        this.fragment = fragment;
        this.mViewModel = ViewModelProviders.of(fragment.getActivity()).get(MediaInfoViewModel.class);

    }

    public class HeadViewHolder extends RecyclerView.ViewHolder {

        TextView time_line;

        HeadViewHolder(View itemView) {
            super(itemView);
            time_line = itemView.findViewById(R.id.time_line_title);
        }
    }

    public class BodyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mPhoto;
        ImageView mFavorImage;
        TextView mPhotoDate;
        TextView mPhotoLocation;
        ImageView mMediaType;
        ImageView mMoreButton;
        TextView mMediaTag;

        BodyViewHolder(final View itemView) {
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
            mMoreButton.setOnClickListener(this);
            mMediaTag = itemView.findViewById(R.id.media_info_tag);
            mFavorImage.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.more_button_tiny:
                    PopupMenu popupMenu = new PopupMenu(getContext(),itemView);
                    final MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.item_time_line, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.delete:
                                    removeData(getAdapterPosition());
                                    Toast.makeText(getContext(), "Delete", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.exit:
                                    Toast.makeText(getContext(), "Exit", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                    break;
                case R.id.favor_tiny:
                    MediaInfoEntity favorEntity = mItemList.get(getAdapterPosition());
                    if (favorEntity.isFavor()) {
                        mFavorImage.clearColorFilter();
                        favorEntity.setFavor(false);
                    } else {
                        mFavorImage.setColorFilter(Color.RED);
                        favorEntity.setFavor(true);
                    }
                    mViewModel.update(favorEntity);
                    break;
            }
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {

        TextView mStatView;

        FootViewHolder(@NonNull View itemView) {
            super(itemView);
            mStatView = itemView.findViewById(R.id.stat_bottom);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position).getDataType();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof BodyViewHolder) {
            Future<?> task = (Future<?>) ((BodyViewHolder)holder).mPhoto.getTag(R.id.tag_first);
            if (task != null) {
                task.cancel(true);
            }
        }
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

            ((FootViewHolder)holder).mStatView.setText(entity.getMediaHeight() + " 张图片、" + entity.getMediaWidth() + " 个视频");

        } else if (holder instanceof BodyViewHolder) {

            final BodyViewHolder bodyHolder = (BodyViewHolder)holder;
            //todo:fix the location float[2];

/*            if (entity.getMediaLocation()[0] != 0 || entity.getMediaLocation()[1] != 0) {
                LocationUtils.setAddress(mContext, entity.getMediaId(), entity.getMediaLocation()[0], entity.getMediaLocation()[1], bodyHolder.mPhotoLocation);
            }
            else {
                bodyHolder.mPhotoLocation.setText("");
            }*/
            bodyHolder.mPhoto.setImageResource(R.mipmap.ic_launcher);

            Uri imageUri = Uri.parse(entity.getMediaStringUri());

            bodyHolder.mPhoto.setTag(imageUri.toString());

            AlbumBitmapCacheHelper.getInstance(ApplicationContextUtils.getContext()).getBitmap(imageUri, mItemList.get(position), bodyHolder.mPhoto, new AlbumBitmapCacheHelper.ILoadImageCallback() {
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

            bodyHolder.mPhotoDate.setText(entity.getMediaDate());
            bodyHolder.mPhotoLocation.setText(entity.getMediaAddress());

            if (entity.getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                bodyHolder.mMediaType.setVisibility(View.VISIBLE);
                bodyHolder.mMediaTag.setText(entity.getVideoDuration());

            } else {
                bodyHolder.mMediaType.setVisibility(View.INVISIBLE);
                final String mediaName = entity.getMediaName();
                bodyHolder.mMediaTag.setText(mediaName.substring(mediaName.lastIndexOf('.')+1));
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
    void setAdapterList(List<MediaInfoEntity> list) {
        int videoCount = 0;
        int photoCount = 0;
        final List<Integer> headList = new ArrayList<>();
        String lastDate = null;
        for (int i = 0;i < list.size();i ++) {
            MediaInfoEntity item = list.get(i);
            if (item.getDataType() == BODY_TYPE) {
                //对比时间，时间不同，插入时间节点
                if (item.getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                    videoCount ++;
                } else {
                    photoCount ++;
                }
                if (!item.getMediaDate().equals(lastDate)) {
                    MediaInfoEntity timeLineItem = new MediaInfoEntity(
                            0, null, null,
                            null, item.getMediaDate(), -1,
                            null, 0, 0, HEAD_TYPE);
                    list.add(i, timeLineItem);

                    lastDate = item.getMediaDate();
                }
            } else if (item.getDataType() == HEAD_TYPE) {
                list.remove(i);
            }
        }
        this.mItemList = list;
        mViewModel.setPhotoCount(photoCount);
        mViewModel.setVideoCount(videoCount);
        final MediaInfoEntity statItem;
        if (this.mItemList.size() != 0) {
            statItem= this.mItemList.get(this.mItemList.size() - 1);
            if (statItem.getDataType() == FOOT_TYPE) {
                statItem.setMediaHeight(mViewModel.getPhotoCount());
                statItem.setMediaWidth(mViewModel.getVideoCount());
            } else {
                this.mItemList.add(new MediaInfoEntity(0, null, null,
                        null, null, -1,
                        null, mViewModel.getPhotoCount(), mViewModel.getVideoCount(), FOOT_TYPE));
            }
        }
        notifyDataSetChanged();

        this.mHeadPositionList = headList;
    }

    public Context getContext() {
        return mContext;
    }

    private void removeData(final int pos) {
        if (pos > 0){
            final MediaInfoEntity mItem = mItemList.get(pos);
            if (mItem != null) {
                mContext.getContentResolver().delete(Uri.parse(mItem.getMediaStringUri()), null, null);
                mViewModel.deleteItem(mItem);
                int i = mItemList.get(pos - 1).getDataType();
                Log.d(TAG, "run: " + i);
                int b = mItemList.size();
                Log.d(TAG, "run: " + b);
                if (((pos == mItemList.size()-1) ||(mItemList.get(pos + 1).getDataType() == 0)) && (mItemList.get(pos - 1).getDataType() == 0)) {
                    mItemList.remove(pos);
                    notifyItemRemoved(pos);
                    mItemList.remove(pos - 1);
                    notifyItemRemoved(pos - 1);

                } else {
                    mItemList.remove(pos);
                    notifyItemRemoved(pos);
                }
            }
            notifyDataSetChanged();

        }

    }
}
