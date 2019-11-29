/**
 * @author Lumpy
 * @date 2019.11.14
 * @version 1.0
 *
 */

package com.GalleryDemo.AceGallery.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.database.MediaInfoEntity;
import com.GalleryDemo.AceGallery.loader.MediaLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GalleryTimeLineActivity extends BaseActivity {

    private static final String TAG = "GalleryTimeLineActivity";



    private List<MediaInfoEntity> mItemList = new ArrayList<>();

    private static final int STORAGE_PERMISSION_REQUEST = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initView(savedInstanceState);
        initData();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new GalleryTimeLineFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment)
                    .commit();
        }


    }

    @Override
    protected void onPause() {

        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onResume() {

        super.onResume();
        getSupportLoaderManager().restartLoader(0, null, new MediaLoader(this));
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

    }



    @Override
    protected void initView(Bundle savedInstanceState) {

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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        Log.d(TAG, "onKeyDown: stackCount = " + getSupportFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() <= 0) {
            finish();
        } else {
            getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);

        }
        return true;
    }
}
