package com.GalleryDemo.AceGallery.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.adapter.PhotoPagerAdapter;
import com.GalleryDemo.AceGallery.database.MediaInfoEntity;

import java.util.List;


public class PreviewActivity extends BaseActivity {

    private static final String TAG = "PreviewActivity";

    private static final String EXTRA_PAGER_MEDIA_ID = "com.GalleryDemo.AceGallery.ui.PreviewActivity.mediaId";

    private Toolbar mToolbar;

    private ViewPager mViewPager;
    private int mMediaId = 0;
    private PhotoPagerAdapter mPhotoPagerAdapter;
    private MediaInfoViewModel mViewModel;



    public static Intent newIntent(Context context, int mediaId, int requestCode) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra(EXTRA_PAGER_MEDIA_ID, mediaId);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Log.d(TAG, "onCreate: ");

        mToolbar = findViewById(R.id.my_toolbar);
        mToolbar.setNavigationIcon(R.drawable.left_sidebar);
        setSupportActionBar(mToolbar);


        initView(savedInstanceState);
        initData();

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        
        mViewPager = findViewById(R.id.viewpager);

    }

    @Override
    protected void initData() {


        mViewModel = ViewModelProviders.of(this).get(MediaInfoViewModel.class);
        mViewModel.getAllItems().observe(this, new Observer<List<MediaInfoEntity>>() {
            @Override
            public void onChanged(List<MediaInfoEntity> mediaInfoEntities) {
                mPhotoPagerAdapter.updateItemList(mediaInfoEntities);
            }
        });

        Intent intent = getIntent();

        mMediaId = intent.getIntExtra(EXTRA_PAGER_MEDIA_ID, 0);
        Log.d(TAG, "initData: MediaId = " + mMediaId);
        mPhotoPagerAdapter = new PhotoPagerAdapter(this);

        mViewPager.setAdapter(mPhotoPagerAdapter);
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(indexItem2photo(mMediaId), false);
            }
        });

        Log.d(TAG, "initData: mViewPager currentItem = " + mViewPager.getCurrentItem());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected: mMediaId: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //菜单创建
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private int indexItem2photo (int mediaId) {
        for (int i = 0;i < mPhotoPagerAdapter.getPagerList().size();i ++) {
            final MediaInfoEntity item = mPhotoPagerAdapter.getPagerList().get(i);
            if (item.getMediaId() == mediaId) {
                return i;
            }
        }
        return -1;
    }

}
