package com.GalleryDemo.AceGallery.ui;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.GalleryDemo.AceGallery.R;
import com.GalleryDemo.AceGallery.Utils.CommUtils;
import com.GalleryDemo.AceGallery.database.MediaInfoEntity;
import com.GalleryDemo.AceGallery.ui.view.VideoSurfaceView;

public class VideoSurfaceFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "VideoSurfaceFragment";

    private static final String EXTRA_PAGER_VIDEO_ITEM = "package com.GalleryDemo.AceGallery.ui.VideoSurfaceFragment.mVideoInfoEntity";

    private MediaInfoEntity mVideoInfoEntity;
    private ImageView mPlay;
    private ImageView mBack;
    private SeekBar mSeekBar;
    private TextView mTotalTime;
    private TextView mPlayTime;
    private ImageView ivAll;
    private VideoSurfaceView mSurfaceView;
    private boolean mIsFull;


    public static VideoSurfaceFragment newInstance(MediaInfoEntity entity) {
        VideoSurfaceFragment videoSurfaceFragment = new VideoSurfaceFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_PAGER_VIDEO_ITEM, entity);
        videoSurfaceFragment.setArguments(bundle);
        return videoSurfaceFragment;
    }


    @Override
    protected View resFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_surface, container, false);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mBack = view.findViewById(R.id.test_sur_iv_back);
        mBack.setOnClickListener(this);
        mPlay = view.findViewById(R.id.test_sur_iv_play);
        mPlay.setOnClickListener(this);
        ivAll = view.findViewById(R.id.test_sur_iv_full);
        ivAll.setOnClickListener(this);
        mSeekBar = view.findViewById(R.id.test_sur_seekbar);

        mTotalTime = view.findViewById(R.id.test_sur_tv_total_time);
        mPlayTime = view.findViewById(R.id.test_sur_tv_start_time);
        mSurfaceView = view.findViewById(R.id.video_surface_view);
        mSurfaceView.setOnVideoPlayingListener(new VideoSurfaceView.OnVideoPlayingListener() {

            @Override
            public void onVideoSizeChanged(int vWidth, int vHeight) {

            }

            @Override
            public void onPlaying(int duration, int percent) {
                mSeekBar.setMax(duration);
                mSeekBar.setProgress(percent);
                mPlayTime.setText(CommUtils.LongToHms(percent));
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onPlayOver() {

            }

            @Override
            public void onVideoSize(int videoSize) {
                mTotalTime.setText(CommUtils.LongToHms(videoSize));
                mSeekBar.setMax(videoSize);
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = mSeekBar.getProgress();
                mSurfaceView.seekTo(progress);
                mPlayTime.setText(CommUtils.LongToHms(progress));
            }
        });
    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        mVideoInfoEntity = (MediaInfoEntity) getArguments().get(EXTRA_PAGER_VIDEO_ITEM);
        Uri videoUri = Uri.parse(mVideoInfoEntity.getMediaStringUri());
        Log.d(TAG, "initData: " + videoUri.getPath());
        mSurfaceView.setUri(videoUri);
    }



    @Override
    public void onResume() {
        super.onResume();
        mSurfaceView.setUri(Uri.parse(mVideoInfoEntity.getMediaStringUri()));
        mSurfaceView.play();
    }

/*    @Override
    public void finish() {
        super.finish();
        mysurfaceView.finishVideo();
    }*/



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.test_sur_iv_back:
                //finish();
                break;
            case R.id.test_sur_iv_play:
                mSurfaceView.pause();
                break;
            case R.id.test_sur_iv_full:
                isFull();
                break;

        }
    }

    private void isFull(){
        if (mIsFull){
            mSurfaceView.setHalfScreen();
            mIsFull=false;
        }else {
            mSurfaceView.setFullScreen();
            mIsFull=true;
        }
    }


}
