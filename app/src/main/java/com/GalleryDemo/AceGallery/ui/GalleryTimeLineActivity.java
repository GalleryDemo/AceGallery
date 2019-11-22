/**
 * @author Lumpy
 * @date 2019.11.14
 * @version 1.0
 *
 */

package com.GalleryDemo.AceGallery.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.GalleryDemo.AceGallery.MediaLoadDataCallBack;
import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.adapter.GalleryTimeLineAdapter;
import com.GalleryDemo.AceGallery.bean.MediaInfoBean;
import com.GalleryDemo.AceGallery.loader.MediaLoader;
import com.GalleryDemo.AceGallery.ui.layout.GridItemDividerDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GalleryTimeLineActivity extends BaseActivity implements MediaLoadDataCallBack {

    private static final String TAG = "GalleryTimeLineActivity";

    private static final String path = "1";





    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private GalleryTimeLineAdapter mTimeLineAdapter;

    private List<MediaInfoBean> mItemList = new ArrayList<>();

    private final static int HEAD_TYPE = 0;
    private final static int BODY_TYPE = 1;
    private final static int FOOT_TYPE = 2;


    @Override
    public void onData(ArrayList<MediaInfoBean> list) {
        mTimeLineAdapter.updateAdapterList(list);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_time_line_activity);

       initView(savedInstanceState);
       initData();

    }

    @Override
    protected void onPause() {

        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onResume() {

        getSupportLoaderManager().initLoader(0, null, new MediaLoader(this, this));
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
        
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("list", (Serializable) mItemList);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mItemList = (List<MediaInfoBean>) savedInstanceState.getSerializable("list");
    }

    @Override
    protected void initData() {
        mTimeLineAdapter = new GalleryTimeLineAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
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
        mRecyclerView.addItemDecoration(new GridItemDividerDecoration(this, mTimeLineAdapter));
        mRecyclerView.setAdapter(mTimeLineAdapter);
        //getSupportLoaderManager().initLoader(0, null, new MediaLoader(this, this));

        Log.d(TAG, "initData: mItemList.size() = " + mItemList.size());
    }



    @Override
    protected void initView(Bundle savedInstanceState) {
        mRecyclerView = findViewById(R.id.photo_recycler_view);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.left_sidebar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.submenu) {
        }
        return true;
    }


    public void getItemList(List <MediaInfoBean> list) {
        mItemList = list;
    }

}
