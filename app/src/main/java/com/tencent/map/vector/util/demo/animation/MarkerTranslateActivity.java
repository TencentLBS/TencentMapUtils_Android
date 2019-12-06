package com.tencent.map.vector.util.demo.animation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.map.vector.util.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;
import com.tencent.tencentmap.mapsdk.vector.utils.animation.MarkerTranslateAnimator;

public class MarkerTranslateActivity extends AppCompatActivity implements View.OnClickListener {

    private final String line = "39.98409,116.30804,39.98409,116.3081,39.98409,116.3081,39.98397,116.30809,39.9823,116.30809,39.9811,116.30817,39.9811,116.30817,39.97918,116.308266,39.97918,116.308266,39.9791,116.30827,39.9791,116.30827,39.979008,116.3083,39.978756,116.3084,39.978386,116.3086,39.977867,116.30884,39.977547,116.308914,39.976845,116.308914,39.975826,116.308945,39.975826,116.308945,39.975666,116.30901,39.975716,116.310486,39.975716,116.310486,39.975754,116.31129,39.975754,116.31129,39.975784,116.31241,39.975822,116.31327,39.97581,116.31352,39.97588,116.31591,39.97588,116.31591,39.97591,116.31735,39.97591,116.31735,39.97593,116.31815,39.975967,116.31879,39.975986,116.32034,39.976055,116.32211,39.976086,116.323395,39.976105,116.32514,39.976173,116.32631,39.976254,116.32811,39.976265,116.3288,39.976345,116.33123,39.976357,116.33198,39.976418,116.33346,39.976418,116.33346,39.97653,116.333755,39.97653,116.333755,39.978157,116.333664,39.978157,116.333664,39.978195,116.33509,39.978195,116.33509,39.978226,116.33625,39.978226,116.33625,39.97823,116.33656,39.97823,116.33656,39.978256,116.33791,39.978256,116.33791,39.978016,116.33789,39.977047,116.33791,39.977047,116.33791,39.97706,116.33768,39.97706,116.33768,39.976967,116.33706,39.976967,116.33697";
//    private final String line =
//        "39.98409,116.30804," +
//        "39.98409,116.3082," +
//        "39.98409,116.3082," +
//        "39.98397,116.3081," +
//        "39.9823,116.30801," +
//        "39.9811,116.3083," +
//        "39.9811,116.3085," +
//        "39.9821,116.3085";

    private MapView mMapView;
    private TencentMap mTencentMap;
    private Marker mMarker;
    private MarkerTranslateAnimator mTranslateAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_translate);
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mMapView.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    protected void init() {
        mMapView = (MapView) findViewById(R.id.mapView);
        mTencentMap = mMapView.getMap();
        LatLng center = new LatLng(39.98409, 116.30804);
        mMarker = mTencentMap.addMarker(
                new MarkerOptions(center)
                        .anchor(0.5f, 0.5f)
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.taxi))
                        .flat(true)
                        .rotation(90)
                        .clockwise(false));

        CameraPosition.Builder builder = new CameraPosition.Builder();
        builder.target(center).zoom(16);
        mTencentMap.moveCamera(CameraUpdateFactory.newCameraPosition(builder.build()));

        Button btnStartAnimation = (Button) findViewById(R.id.btn_start_animation);
        Button btnCancelAnimation = (Button) findViewById(R.id.btn_cancel_animation);
        Button btnEndAnimation = (Button) findViewById(R.id.btn_end_animation);

        mTranslateAnimator = new MarkerTranslateAnimator(
                mMarker, 50 * 1000, parseLine(line), true);

        btnStartAnimation.setOnClickListener(this);
        btnCancelAnimation.setOnClickListener(this);
        btnEndAnimation.setOnClickListener(this);
    }

    private LatLng[] parseLine(String line) {
        String[] strs = line.split(",");
        LatLng[] latLngs = new LatLng[strs.length / 2];
        for (int i = 0; i < latLngs.length; i++) {
            double latitude = Double.parseDouble(strs[i * 2]);
            double longitude = Double.parseDouble(strs[i * 2 + 1]);
            latLngs[i] = new LatLng(latitude, longitude);
        }
        mTencentMap.addPolyline(new PolylineOptions().add(latLngs));
        return latLngs;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_animation:
                mTranslateAnimator.startAnimation();
                break;
            case R.id.btn_cancel_animation:
                mTranslateAnimator.cancelAnimation();
                break;
            case R.id.btn_end_animation:
                mTranslateAnimator.endAnimation();
                break;
            default:
                break;
        }
    }
}
