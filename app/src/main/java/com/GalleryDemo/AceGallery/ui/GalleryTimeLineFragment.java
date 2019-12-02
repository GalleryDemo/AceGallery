package com.GalleryDemo.AceGallery.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.Utils.GridItemDividerDecoration;
import com.GalleryDemo.AceGallery.Utils.PermissionHelper;
import com.GalleryDemo.AceGallery.adapter.GalleryTimeLineAdapter;
import com.GalleryDemo.AceGallery.database.MediaInfoEntity;
import com.GalleryDemo.AceGallery.loader.MediaLoader;

import java.util.ArrayList;
import java.util.List;

import static com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils.BODY_TYPE;
import static com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils.FOOT_TYPE;
import static com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils.HEAD_TYPE;


public class GalleryTimeLineFragment extends BaseFragment {


    private RecyclerView mRecyclerView;
    private GalleryTimeLineAdapter mTimeLineAdapter;
    private MediaInfoViewModel mViewModel;
    private Toolbar mToolbar;

    private List<MediaInfoEntity> mItemList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected View resFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time_line, container, false);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        mToolbar = view.findViewById(R.id.time_line_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);
        mToolbar.setNavigationIcon(R.drawable.left_sidebar);
        mRecyclerView = view.findViewById(R.id.photo_recycler_view);

    }


    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(getActivity()).get(MediaInfoViewModel.class);
        mViewModel.getAllItems().observe(getActivity(), new Observer<List<MediaInfoEntity>>() {
            @Override
            public void onChanged(List<MediaInfoEntity> mediaInfoEntities) {
                mTimeLineAdapter.setAdapterList(mediaInfoEntities);
            }
        });

        mTimeLineAdapter = new GalleryTimeLineAdapter(getContext(), this);

        //网格布局部分：图片一列，时间轴三列
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int type = mTimeLineAdapter.getItemViewType(position);
                if (type == HEAD_TYPE) {
                    return 3;
                } else if (type == BODY_TYPE) {
                    return 1;
                } else if (type == FOOT_TYPE) {
                    return 3;
                } else {
                    return 0;
                }
            }
        });
        mRecyclerView.addItemDecoration(new GridItemDividerDecoration(getContext(), mTimeLineAdapter));
        mRecyclerView.setAdapter(mTimeLineAdapter);
/*        if(PermissionHelper.hasPermissions(getActivity())) {
            getActivity().getSupportLoaderManager().initLoader(0, null, new MediaLoader(getActivity()));
        }*/
    }


}
