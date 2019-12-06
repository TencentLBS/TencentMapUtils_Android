/**
 *
 */
package com.tencent.map.vector.util.demo.clustering;

import android.content.Context;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.map.vector.util.demo.R;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.ClusterManager;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.view.DefaultClusterRenderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangxiaokun
 *
 */
public class LargeNumberClusteringActivity extends AppCompatActivity {

    private TencentMap mTencentMap;
    private MapView mMapView;
    private CheckBox mCbClusterSwitch;

    private ClusterManager<TencentMapItem> mClusterManager;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_number_clustering);
        initView();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#cancel()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //调用这个方法, 用于停止聚合, 避免退出后, 继续计算聚合导致空指针。
        if (mClusterManager != null) {
            mClusterManager.cancel();
        }
        mMapView.onDestroy();
    }

    protected void initView() {
        mMapView = (MapView) findViewById(R.id.map);
        mTencentMap = mMapView.getMap();
        mCbClusterSwitch = (CheckBox) findViewById(R.id.cb_cluster_enable);

        mClusterManager = new ClusterManager<TencentMapItem>(this, mTencentMap);

//        //这是默认聚合策略，调用时不必添加，如果需要其他聚合策略，可以按一下代码更改。
        NonHierarchicalDistanceBasedAlgorithm<TencentMapItem> ndba = new NonHierarchicalDistanceBasedAlgorithm<>(this);
//        //默认聚合生效距离，35dp
        ndba.setMaxDistanceAtZoom(35);
        mClusterManager.setAlgorithm(ndba);
        //设置聚合渲染器, 默认 clustering manager 使用的就是 DefaultClusterRenderer 可以不调用下列代码
        DefaultClusterRenderer<TencentMapItem> renderer = new DefaultClusterRenderer<>(this, mTencentMap, mClusterManager);
        //设置最小聚合数量，默认为 4，这里设置2，即 clustering 中有2个以上(不包括2) marker 才进行聚合操作。
        renderer.setMinClusterSize(1);
        //如果设置 null 聚合中的数字显示真实的聚合点数量
        //这里设置一个自定义的分段
        renderer.setBuckets(new int[]{5, 10, 20, 50});
        mClusterManager.setRenderer(renderer);

        /**********************CustomColorClusterRenderer**********************/
//        CustomColorClusterRenderer<TencentMapItem> customRenderer = new CustomColorClusterRenderer(this, mTencentMap, mClusterManager);
//        mClusterManager.setRenderer(customRenderer);
        /**********************************************************************/

        mTencentMap.setOnCameraChangeListener(mClusterManager);
        mTencentMap.moveCamera(CameraUpdateFactory.zoomTo(7));
        mClusterManager.addItems(getCoords());

        mCbClusterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //是否启用聚合功能
                mClusterManager.setClusterEnabled(b);
            }
        });
    }

    protected List<TencentMapItem> getCoords() {
        List<TencentMapItem> items = new ArrayList<TencentMapItem>();
        try {
            InputStream is = getAssets().open("datab");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");
                double longitude = Double.parseDouble(data[0]);
                double latitude = Double.parseDouble(data[1]);
                items.add(new TencentMapItem(latitude, longitude));
            }
            is.close();
            isr.close();
            br.close();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    class CustomColorClusterRenderer<T> extends DefaultClusterRenderer {

        private int[] colors = {0xff0000ff, 0xff3300aa, 0xff770077, 0xffaa0033, 0xffff0000};

        public CustomColorClusterRenderer(Context context, TencentMap map, ClusterManager clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        public int getColor(int clusterSize) {
            if (getBuckets() != null) {
                for (int i = 0; i < getBuckets().length - 1; i++) {
                    if (clusterSize < getBuckets()[i + 1]) {
                        if (i < colors.length) {
                            return colors[i];
                        }
                        return colors[colors.length - 1];
                    }
                }
            }
            return super.getColor(clusterSize);
        }
    }
}
