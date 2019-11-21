package com.GalleryDemo.AceGallery.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.adapter.PhotoPagerAdapter;
import com.GalleryDemo.AceGallery.bean.MediaInfoBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PreviewActivity extends BaseActivity {

    private static final String TAG = "PreviewActivity";

    private static final String EXTRA_PAGER_POSITION = "com.GalleryDemo.AceGallery.ui.PreviewActivity.mPosition";
    private static final String EXTRA_PHOTO_LIST = "com.GalleryDemo.AceGallery.ui.PreviewActivity.list";


    private Toolbar mToolbar;

    private ViewPager mViewPager;
    private int mPosition = 0;
    private PhotoPagerAdapter mPhotoPagerAdapter;
    private List<MediaInfoBean> mPhotoList = new ArrayList<>();



    public static Intent newIntent(Context context, int position, List<MediaInfoBean> list, int requestCode) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra(EXTRA_PAGER_POSITION,position);
        intent.putExtra(EXTRA_PHOTO_LIST, (Serializable) list);

        return intent;
    }

    public static void startActivity(Activity activity, int position, List<MediaInfoBean> list, int requestCode) {
        Intent intent = new Intent(activity, PreviewActivity.class);
        intent.putExtra(EXTRA_PAGER_POSITION,position);
        intent.putExtra(EXTRA_PHOTO_LIST, (Serializable) list);
        activity.startActivityForResult(intent, requestCode);
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

        mPhotoList = (List<MediaInfoBean>) intent.getSerializableExtra(EXTRA_PHOTO_LIST);
        mPosition = intent.getIntExtra(EXTRA_PAGER_POSITION, 0);
        mPhotoPagerAdapter = new PhotoPagerAdapter(this, mPhotoList);
        mViewPager.setAdapter(mPhotoPagerAdapter);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected: mPosition: " + position);
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
