package com.GalleryDemo.AceGallery.timeline;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.GalleryDemo.AceGallery.Utils.ApplicationContextUtils;


public class ThumbnailUtils {
    private static final String TAG = "ThumbnailUtils";
    public static String getDataPath() {
        String path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = ApplicationContextUtils.getContext().getExternalFilesDir(null) + "/Gallery";
        }
        else
            path = ApplicationContextUtils.getContext().getFilesDir().getPath();
        if (!path.endsWith("/")) {
            path = path + "/";
        }
       // Log.d(TAG, "getDataPath: " + path);
        return path;
        
    }

    /*public static String getFileName(String pathName) {
        int start = pathName.lastIndexOf("/");
        if (start != -1) {
            return pathName.substring(start+1);
        } else {
            return null;
        }
    }*/

    public static int toHash(String key) {
        int hashCode = 0;
        for (int i = 0; i < key.length(); i++) {
            int letterValue = key.charAt(i) - 96;
            hashCode = (hashCode << 5) + letterValue;
        }
        return hashCode;
    }

    public static DisplayMetrics getScreenMetrics(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int length) {
        if (bitmap == null || length <= 0) {
            return null;
        }
        Bitmap result = bitmap;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int left = (width - length) / 2;
        int top = (height - length) / 2;

        if (left == 0 && top == 0) {
            return bitmap;
        }

        try {
            result = Bitmap.createBitmap(bitmap, left, top, length, length);
            bitmap.recycle();
        } catch (OutOfMemoryError e) {
            return result;
        }
        return result;
    }
}
