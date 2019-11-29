package com.GalleryDemo.AceGallery.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {


    protected abstract void initData();

    protected abstract void initView(Bundle savedInstanceState);

    public BaseActivity getBaseActivity() {
        return this;
    }

}
