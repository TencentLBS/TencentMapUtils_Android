package com.tencent.tencentmap.mapsdk.vector.utils.demos;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//import com.tencent.tencentmap.mapsdk.vector.utils.projection.CameraPositionUtil;

/**
 * Created by nicolasli on 2016/9/28.
 */

public class StaticBmpProjectionActivity extends Activity {
    ImageView imgView = null;
    Button btn = null;
    int width = 0, height = 0;
    Bitmap staticBmp = null;
    private static final LatLng latlng_yinkedasha = new LatLng(39.98192, 116.306364);
    private static final LatLng latlng_disanji = new LatLng(39.984253, 116.307439);
    private static final LatLng latlng_xigema = new LatLng(39.977407, 116.337194);
    private static final LatLng latlng_fixed = new LatLng(39.980672, 116.329594);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.static_bm_projection);
        imgView = (ImageView) findViewById(R.id.imageView);
        btn = (Button) findViewById(R.id.save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                width=imgView.getWidth();
                height=imgView.getHeight();
//                String url = generateUrl(width, height, new LatLng[]{latlng_disanji, latlng_xigema, latlng_yinkedasha, latlng_fixed});
//                Log.e("StaticBmpProjection", "DownLoad StaticBmp url" + url);
//                new DownloadStaticBmpTask().execute(url);
            }
        });

    }

    private class DownloadStaticBmpTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            if (params == null || params.length <= 0)
                return null;
            String url = params[0];
            HttpURLConnection con = null;
            InputStream inputStream = null;
            try {
                con = (HttpURLConnection) new URL(url).openConnection();
                con.setRequestMethod("GET");
                con.setDoInput(true);
                inputStream = con.getInputStream();
                staticBmp = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Log.e("StaticBmpProjection", "DownLoad StaticBmp Failed! e:" + e.toString());
            } finally {
                if (con != null)
                    con.disconnect();
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            if (staticBmp != null) {
                imgView.setImageBitmap(staticBmp);
                btn.setEnabled(true);
            }
        }
    }
//
//    public String generateUrl(int width, int height, LatLng[] latLngs) {
//        CameraPosition result = CameraPositionUtil.getCameraPosition(latLngs, width, height);
//        StringBuilder sb = new StringBuilder("http://apis.map.qq.com/ws/staticmap/v2/?center=");
//        sb.append(result.target.latitude + "," + result.target.longitude);
//        sb.append("&zoom=").append(result.zoom);
//        sb.append("&size=").append(width + "*" + height);
//        sb.append("&maptype=roadmap");
//        for (LatLng latLng : latLngs) {
//            sb.append("&markers=size:large|color:red|label:y|");
//            sb.append(latLng.latitude + "," + latLng.longitude);
//        }
//        sb.append("&key=845b4b705088dd7f0b68cfc8d02196af");
//        return sb.toString();
//    }
}

