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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.adapter.GalleryTimeLineAdapter;
import com.GalleryDemo.AceGallery.database.MediaInfoEntity;
import com.GalleryDemo.AceGallery.loader.MediaLoader;
import com.GalleryDemo.AceGallery.Utils.GridItemDividerDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GalleryTimeLineActivity extends BaseActivity {

    private static final String TAG = "GalleryTimeLineActivity";

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private GalleryTimeLineAdapter mTimeLineAdapter;
    private MediaInfoViewModel mViewModel;

    private List<MediaInfoEntity> mItemList = new ArrayList<>();

    private final static int HEAD_TYPE = 0;
    private final static int BODY_TYPE = 1;
    private final static int FOOT_TYPE = 2;


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
    protected void initData() {
        mViewModel = ViewModelProviders.of(this).get(MediaInfoViewModel.class);
        try {
            mViewModel.getAllItems().observe(this, new Observer<List<MediaInfoEntity>>() {
                @Override
                public void onChanged(List<MediaInfoEntity> mediaInfoEntities) {
                    mTimeLineAdapter.setAdapterList(mediaInfoEntities);
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
        getSupportLoaderManager().initLoader(0, null, new MediaLoader(this));

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


    public void getItemList(List <MediaInfoEntity> list) {
        mItemList = list;
    }

}
