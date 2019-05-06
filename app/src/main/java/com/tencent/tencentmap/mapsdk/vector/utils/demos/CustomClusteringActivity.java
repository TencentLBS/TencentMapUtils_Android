package com.tencent.tencentmap.mapsdk.vector.utils.demos;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.Cluster;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.ClusterItem;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.ClusterManager;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.view.DefaultClusterRenderer;
import com.tencent.tencentmap.mapsdk.vector.utils.ui.IconGenerator;

public class CustomClusteringActivity extends Activity {

    private TencentMap mTencentMap;
    private MapView mMapView;

    private ClusterManager<PetalItem> mClusterManager;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_clustering);
        initView();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mMapView.onResume();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //调用这个方法, 用于清除聚合数据, 避免退出后, 继续计算聚合导致空指针。
        if (mClusterManager != null) {
            mClusterManager.cancel();
        }
        mMapView.onDestroy();
    }

    protected void initView() {
        mMapView = (MapView)findViewById(R.id.map);
        mTencentMap = mMapView.getMap();
        mClusterManager = new ClusterManager<PetalItem>(this, mTencentMap);
        mClusterManager.setRenderer(new CustomIconClusterRenderer(this, mTencentMap, mClusterManager));
        mTencentMap.setOnCameraChangeListener(mClusterManager);
        mTencentMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        addItem();
        mClusterManager.cluster();

        //设置点击回调
        mTencentMap.setOnMarkerClickListener(mClusterManager);
        mTencentMap.setOnInfoWindowClickListener(mClusterManager);

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<PetalItem>() {
            @Override
            public boolean onClusterClick(Cluster<PetalItem> cluster) {
                Toast.makeText(CustomClusteringActivity.this, "cluster clicked, cluster size:" + cluster.getSize(), Toast.LENGTH_SHORT).show();
                Marker marker = ((DefaultClusterRenderer)mClusterManager.getRenderer()).getMarker(cluster);
                if (marker == null) {
                    Log.e("test", "marker is null");
                } else {
                    Log.e("test", "marker not null");
                }
                return false;
            }
        });

        mClusterManager.setOnClusterInfoWindowClickListener(
                new ClusterManager.OnClusterInfoWindowClickListener<PetalItem>() {
                    @Override
                    public void onClusterInfoWindowClick(Cluster<PetalItem> cluster) {
                        Toast.makeText(CustomClusteringActivity.this,
                                "cluster infowindow clicked, cluster size:" +
                                        cluster.getSize(), Toast.LENGTH_SHORT).show();
                    }
                });

        mClusterManager.setOnClusterItemClickListener(
                new ClusterManager.OnClusterItemClickListener<PetalItem>() {
                    @Override
                    public boolean onClusterItemClick(PetalItem petalItem) {
                        Toast.makeText(CustomClusteringActivity.this,
                                "single marker clicked, position:" +
                                        petalItem.getPosition(), Toast.LENGTH_SHORT).show();
                        Marker marker = ((DefaultClusterRenderer)mClusterManager.getRenderer()).getMarker(petalItem);
                        if (marker == null) {
                            Log.e("test", "marker is null");
                        } else {
                            Log.e("test", "marker not null");
                        }
                        return false;
                    }
                });

        mClusterManager.setOnClusterItemInfoWindowClickListener(
                new ClusterManager.OnClusterItemInfoWindowClickListener<PetalItem>() {
                    @Override
                    public void onClusterItemInfoWindowClick(PetalItem petalItem) {
                        Toast.makeText(CustomClusteringActivity.this,
                                "single marker infowindow clicked, position:" +
                                        petalItem.getPosition(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    protected void addItem() {
        mClusterManager.addItem(new PetalItem(39.971595,116.294747, R.mipmap.petal_blue));

        mClusterManager.addItem(new PetalItem(39.971595,116.314316, R.mipmap.petal_red));

        mClusterManager.addItem(new PetalItem(39.967385,116.317063, R.mipmap.petal_green));

        mClusterManager.addItem(new PetalItem(39.951596,116.302300, R.mipmap.petal_yellow));

        mClusterManager.addItem(new PetalItem(39.970543,116.290627, R.mipmap.petal_orange));

        mClusterManager.addItem(new PetalItem(39.966333,116.311569, R.mipmap.petal_purple));
    }

    public class PetalItem implements ClusterItem {

        private final LatLng mLatLng;

        private int mDrawableResourceId;

        public PetalItem(double latitude, double longitude, int resourceId) {
            // TODO Auto-generated constructor stub
            mLatLng = new LatLng(latitude, longitude);
            mDrawableResourceId = resourceId;
        }

        /* (non-Javadoc)
         * @see com.tencent.mapsdk.clustering.ClusterItem#getPosition()
         */
        @Override
        public LatLng getPosition() {
            // TODO Auto-generated method stub
            return mLatLng;
        }

        public int getDrawableResourceId() {
            return mDrawableResourceId;
        }

    }

    class CustomIconClusterRenderer extends DefaultClusterRenderer<PetalItem> {

        private IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private ImageView mItemImageView = new ImageView(getApplicationContext());
        private ImageView mClusterImageView = new ImageView(getApplicationContext());

        public CustomIconClusterRenderer(
                Context context, TencentMap map, ClusterManager clusterManager) {
            super(context, map, clusterManager);
            mItemImageView.setLayoutParams(
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            mIconGenerator.setContentView(mItemImageView);

            mClusterImageView.setLayoutParams(
                    new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            mClusterIconGenerator.setContentView(mClusterImageView);

            setMinClusterSize(1);
        }

        @Override
        public void onBeforeClusterRendered(
                Cluster<PetalItem> cluster, MarkerOptions markerOptions) {
            int[] resources = new int[cluster.getItems().size()];
            int i = 0;
            for (PetalItem item : cluster.getItems()) {
                resources[i++] = item.getDrawableResourceId();
            }
            PetalDrawable drawable = new PetalDrawable(getApplicationContext(), resources);
            mClusterImageView.setImageDrawable(drawable);
            Bitmap icon = mClusterIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
            //不显示 infowindow
            markerOptions.infoWindowEnable(false);
        }

        @Override
        public void onBeforeClusterItemRendered(PetalItem item, MarkerOptions markerOptions) {
            mItemImageView.setImageResource(item.getDrawableResourceId());
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }
    }
}
