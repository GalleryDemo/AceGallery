package com.GalleryDemo.AceGallery.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.adapter.PhotoPagerAdapter;
import com.GalleryDemo.AceGallery.database.MediaInfoEntity;

import java.util.ArrayList;
import java.util.List;

public class PreviewFragment extends BaseFragment {

    private static final String EXTRA_PAGER_MEDIA_ITEM = "com.GalleryDemo.AceGallery.ui.PreviewFragment.mediaInfoEntity";

    private Toolbar mToolbar;
    private ViewPager mViewPager;

    private PhotoPagerAdapter mPhotoPagerAdapter;

    private MediaInfoViewModel mViewModel;

    private MediaInfoEntity mediaInfoEntity;
    private List<MediaInfoEntity> mPagerList = new ArrayList<>();


    public static PreviewFragment newInstance(MediaInfoEntity entity) {
        PreviewFragment previewFragment = new PreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_PAGER_MEDIA_ITEM, entity);
        previewFragment.setArguments(bundle);
        return previewFragment;
    }

    @Override
    protected View resFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preview, container, false);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mToolbar = view.findViewById(R.id.preview_toolbar);
        mToolbar.setNavigationIcon(R.drawable.left_sidebar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);
        mViewPager = view.findViewById(R.id.viewpager);
    }


    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        mPhotoPagerAdapter = new PhotoPagerAdapter(getContext(),this);
        mViewModel = ViewModelProviders.of(getActivity()).get(MediaInfoViewModel.class);
        mViewModel.getAllItems().observe(getActivity(), new Observer<List<MediaInfoEntity>>() {
            @Override
            public void onChanged(List<MediaInfoEntity> mediaInfoEntities) {
                mPhotoPagerAdapter.updateItemList(mediaInfoEntities);
            }
        });

        mediaInfoEntity = (MediaInfoEntity) getArguments().getSerializable(EXTRA_PAGER_MEDIA_ITEM);


        mViewPager.setAdapter(mPhotoPagerAdapter);
        mPagerList = mPhotoPagerAdapter.getPagerList();
        mViewPager.setCurrentItem(getPhotoPositionByItemPosition(mediaInfoEntity));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private int getPhotoPositionByItemPosition (MediaInfoEntity item) {
        for (int i = 0;i < mPagerList.size();i ++) {
            MediaInfoEntity photoItem = mPagerList.get(i);
            if (photoItem.getMediaId() == item.getMediaId()) {
                return i;
            }
        }
        return -1;
    }


}
