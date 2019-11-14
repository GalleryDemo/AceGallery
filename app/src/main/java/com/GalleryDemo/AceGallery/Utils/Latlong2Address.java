package com.GalleryDemo.AceGallery.Utils;

import android.util.Log;

import com.GalleryDemo.AceGallery.bean.MediaInfoBean;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Latlong2Address implements Runnable{
    private List<MediaInfoBean> mList;
    private int position;

    public Latlong2Address (List<MediaInfoBean> mList, int position) {
        this.mList = mList;
        this.position = position;
    }
    @Override
    public void run() {
        long start = System.currentTimeMillis();
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://api.map.baidu.com/reverse_geocoding/v3/" +
                            "?ak=V7a8Fv77avnOm28vK65FcPMnUhOaGCut&output=json&coord" +
                            "type=wgs84ll&location=" + mList.get(position).getMediaLocation()[0] + "," + mList.get(position).getMediaLocation()[1] + "&mcode=" +
                            "29:32:22:63:CE:56:D4:8A:4C:D8:3A:DA:FB:24:0F:58:54:41:86:45;com.GalleryDemo.AceGallery")
                    .build();
            Response response = client.newCall(request).execute();
            String responseDate = response.body().string();

            JSONObject jsonObject = new JSONObject(responseDate);
            if (jsonObject.getInt("status") == 0) {
                String result = jsonObject.getString("result");
                JSONObject location = new JSONObject(result);
                mList.get(position).setMediaAddress(location.getString("formatted_address"));
                Log.d(mList.get(position).getMediaAddress(), "run: ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        Log.d((end - start) + " ", "run: ");
    }
}
