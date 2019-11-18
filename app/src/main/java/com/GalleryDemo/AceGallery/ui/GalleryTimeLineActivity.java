package com.GalleryDemo.AceGallery.ui;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.GalleryDemo.AceGallery.GridItemDividerDecoration;
import com.GalleryDemo.AceGallery.MediaLoadDataCallBack;
import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.adapter.GalleryTimeLineAdapter;
import com.GalleryDemo.AceGallery.bean.MediaInfoBean;
import com.GalleryDemo.AceGallery.loader.MediaLoader;

import java.util.ArrayList;

public class GalleryTimeLineActivity extends AppCompatActivity implements MediaLoadDataCallBack {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private GalleryTimeLineAdapter mTimeLineAdapter;




    @Override
    public void onData(ArrayList<MediaInfoBean> list) {
        mTimeLineAdapter.updateAdapterList(list);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_time_line_activity);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTimeLineAdapter = new GalleryTimeLineAdapter(this);

        mRecyclerView = findViewById(R.id.photo_recycler_view);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new GridItemDividerDecoration(this, mTimeLineAdapter));

        mRecyclerView.setAdapter(mTimeLineAdapter);


        mToolbar =findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.left_sidebar);


        getSupportLoaderManager().initLoader(0, null, new MediaLoader(this, this));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_time_line, menu);
        return true;
    }



}
