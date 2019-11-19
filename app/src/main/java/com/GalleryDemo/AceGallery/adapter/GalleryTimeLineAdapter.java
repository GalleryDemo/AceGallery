/**
 * @author Lumpy
 * @date 2019.11.14
 * @version 1.0
 *
 */


package com.GalleryDemo.AceGallery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.Utils.Latlong2Address;
import com.GalleryDemo.AceGallery.bean.MediaInfoBean;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GalleryTimeLineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "GalleryTimeLineAdapter";

    private Context mContext;

    private List<MediaInfoBean> mItemList = new ArrayList<>();
    private List<Integer> mHeadPositionList = new ArrayList<>();

    public static final int HEAD_TYPE = 0;
    public static final int BODY_TYPE = 1;



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

        public BodyViewHolder(View itemView) {
            super(itemView);
            mPhoto = itemView.findViewById(R.id.photo_image);
            mFavorImage = itemView.findViewById(R.id.favor_tiny);
            mPhotoDate = itemView.findViewById(R.id.media_date_tiny);
            mPhotoLocation = itemView.findViewById(R.id.media_location_tiny);
            mMediaType = itemView.findViewById(R.id.media_type_tiny);
            mMoreButton = itemView.findViewById(R.id.more_button_tiny);
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
        } else {
            Log.d(TAG, "onCreateViewHolder: wrong type!");
            return null;
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MediaInfoBean bean = mItemList.get(position);

        if (holder instanceof HeadViewHolder) {

            ((HeadViewHolder)holder).time_line.setText(bean .getMediaDate());

        } else if (holder instanceof BodyViewHolder) {

            if (bean.getMediaLocation()[0] != 0 || bean.getMediaLocation()[1] != 0) {
                new Thread(new Latlong2Address(mItemList, position)).start();
            }

            final BodyViewHolder bodyHolder = (BodyViewHolder)holder;

            Uri imageUri = bean.getMediaUri();

            Bitmap bitmap = null;
            try {
                InputStream imageStream = mContext.getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(imageStream);
            } catch (FileNotFoundException e) {
                Log.d(TAG, "onBindViewHolder: Image file not found");
                e.printStackTrace();
            }

            bodyHolder.mPhoto.setImageBitmap(bitmap);

            bodyHolder.mPhotoDate.setText(bean.getMediaDate());

            bodyHolder.mPhotoLocation.setText(bean.getMediaAddress());

            Log.d(TAG, "onBindViewHolder: MediaType = " + bean.getMediaType());
            if (bean.getMediaType() == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                bodyHolder.mMediaType.setVisibility(View.VISIBLE);
            } else {
                bodyHolder.mMediaType.setVisibility(View.INVISIBLE);
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

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    if (type == HEAD_TYPE) {
                        return 3;
                    } else if (type == BODY_TYPE) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
        }


    }

    public void updateAdapterList(List<MediaInfoBean> list) {
        //Log.d(TAG, "updateAdapterList: list size is " + list.size());
        List<Integer> headList = new ArrayList<>();
        if (list != null) {
            this.mItemList = list;
            notifyDataSetChanged();
        }
        for (int i = 0;i < list.size(); i++) {
            if (list.get(i).getDataType() == HEAD_TYPE) {
                headList.add(i);
            }
        }

       this.mHeadPositionList = headList;


    }

    public List<MediaInfoBean> getmItemList() {
        return mItemList;
    }
}
