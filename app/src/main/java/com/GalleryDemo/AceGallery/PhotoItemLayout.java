package com.GalleryDemo.AceGallery;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class PhotoItemLayout extends RelativeLayout {

    public PhotoItemLayout(Context context) {
        super(context);
    }

    public PhotoItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));

        int childWidthSize = getMeasuredWidth();

        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
