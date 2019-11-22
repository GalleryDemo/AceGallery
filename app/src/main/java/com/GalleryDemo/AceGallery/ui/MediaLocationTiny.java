package com.GalleryDemo.AceGallery.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.GalleryDemo.AceGallery.database.MediaDao;
import com.GalleryDemo.AceGallery.database.MediaDatabase;

import org.json.JSONObject;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MediaLocationTiny {
    public static void setAddress(Context mContext, final int id, final float lat, final float lng, final TextView textView) {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                textView.setText((String)msg.obj);
            }
        };

        final MediaDao mediaDao = MediaDatabase.getInstance(mContext).getAddressDao();
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(2, 5, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

        tpe.execute(new Runnable() {
            @Override
            public void run() {
                /*String CityAddress  = "";*/
                //Todo: fix the getAddress;
                String  CityAddress = "test";
                if (CityAddress == null) {
                    try {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://api.map.baidu.com/reverse_geocoding/v3/?ak=V7a8Fv77avnOm28vK65FcPMnUhOaGCut&output=json&coordtype=wgs84ll&location=" + lat + "," + lng + "&mcode=29:32:22:63:CE:56:D4:8A:4C:D8:3A:DA:FB:24:0F:58:54:41:86:45;com.GalleryDemo.AceGallery")
                                .build();
                        Response response = client.newCall(request).execute();
                        String responseDate = response.body().string();

                        JSONObject jsonObject = new JSONObject(responseDate);
                        if (jsonObject.getInt("status") == 0) {
                            String result = jsonObject.getString("result");
                            JSONObject JsResult = new JSONObject(result);
                            String addressComponent = JsResult.getString("addressComponent");
                            JSONObject JsAddressComponent = new JSONObject(addressComponent);
                            String city = JsAddressComponent.getString("city");
                            CityAddress = city;
                            //Todo fix the method
                            //mediaDao.insertAddress(new MediaInfo(id, CityAddress));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                Message msg = Message.obtain();
                msg.obj = CityAddress;
                handler.sendMessage(msg);
            }
        });
    }
}
