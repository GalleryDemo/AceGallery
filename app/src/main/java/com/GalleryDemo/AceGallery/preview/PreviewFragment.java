package com.GalleryDemo.AceGallery.preview;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.GalleryDemo.AceGallery.BaseFragment;
import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.database.MediaInfoEntity;
import com.GalleryDemo.AceGallery.database.MediaInfoViewModel;

import java.util.ArrayList;
import java.util.List;

public class PreviewFragment extends BaseFragment {

    private static final String EXTRA_PAGER_MEDIA_ITEM = "com.GalleryDemo.AceGallery.preview.PreviewFragment.mediaInfoEntity";

    private Toolbar mToolbar;
    private ViewPager mViewPager;

    private Observer<List<MediaInfoEntity>> mListObserver;
    private PhotoPagerAdapter mPhotoPagerAdapter;

    private MediaInfoViewModel mViewModel;

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
        WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getActivity().getWindow().setAttributes(attrs);
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(Color.TRANSPARENT);
        mToolbar = view.findViewById(R.id.preview_toolbar);
        mToolbar.setVisibility(View.INVISIBLE);
        mToolbar.bringToFront();
        mToolbar.setNavigationIcon(R.drawable.back_button);
        mViewPager = view.findViewById(R.id.viewpager);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.getAllItems().removeObserver(mListObserver);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void initData(View view, Bundle savedInstanceState) {

        mPhotoPagerAdapter = new PhotoPagerAdapter(getContext(),this);
        mViewModel = ViewModelProviders.of(getActivity()).get(MediaInfoViewModel.class);

        mListObserver = new Observer<List<MediaInfoEntity>>() {
            @Override
            public void onChanged(List<MediaInfoEntity> mediaInfoEntities) {
                mPhotoPagerAdapter.updateItemList(mediaInfoEntities);
            }
        };
        mViewModel.getAllItems().observe(getActivity(), mListObserver);

        MediaInfoEntity mediaInfoEntity = (MediaInfoEntity) getArguments().getSerializable(EXTRA_PAGER_MEDIA_ITEM);
        mToolbar.setTitle(mediaInfoEntity.getMediaDate());

        mViewPager.setAdapter(mPhotoPagerAdapter);
        mPagerList = mPhotoPagerAdapter.getPagerList();
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(getPhotoPositionByItemPosition(mediaInfoEntity));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {

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
