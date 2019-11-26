package com.GalleryDemo.AceGallery.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.adapter.PhotoPagerAdapter;
import com.GalleryDemo.AceGallery.database.MediaInfoEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PreviewActivity extends BaseActivity {

    private static final String TAG = "PreviewActivity";

    private static final String EXTRA_PAGER_MEDIA_ID = "com.GalleryDemo.AceGallery.ui.PreviewActivity.mediaId";
    private static final String EXTRA_PHOTO_LIST = "com.GalleryDemo.AceGallery.ui.PreviewActivity.list";


    private Toolbar mToolbar;

    private ViewPager mViewPager;
    private int mMediaId = 0;
    private PhotoPagerAdapter mPhotoPagerAdapter;
    private List<MediaInfoEntity> mPhotoList = new ArrayList<>();



    public static Intent newIntent(Context context, int position, List<MediaInfoEntity> list, int requestCode) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra(EXTRA_PAGER_MEDIA_ID,position);
        intent.putExtra(EXTRA_PHOTO_LIST, (Serializable) list);

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
        Intent intent = getIntent();

        mPhotoList = (List<MediaInfoEntity>) intent.getSerializableExtra(EXTRA_PHOTO_LIST);
        mMediaId = intent.getIntExtra(EXTRA_PAGER_MEDIA_ID, 0);
        mPhotoPagerAdapter = new PhotoPagerAdapter(this, mPhotoList);
        mViewPager.setAdapter(mPhotoPagerAdapter);
        mViewPager.setCurrentItem(mMediaId);
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


}
