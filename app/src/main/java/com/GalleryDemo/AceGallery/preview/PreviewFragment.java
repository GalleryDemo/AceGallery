package com.GalleryDemo.AceGallery.preview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

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

    private Window mWindow;


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
        mWindow = getActivity().getWindow();
        WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getActivity().getWindow().setAttributes(attrs);

        mToolbar = view.findViewById(R.id.preview_toolbar);
        mToolbar.bringToFront();
        mToolbar.setNavigationIcon(R.drawable.back_button);
        mViewPager = view.findViewById(R.id.viewpager);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        mWindow.setAttributes(attrs);
        mViewModel.getAllItems().removeObserver(mListObserver);
    }


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
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

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
