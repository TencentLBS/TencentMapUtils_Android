package com.tencent.map.vector.util.demo.clustering;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.map.vector.util.demo.R;
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

public class ClusteringActivity extends AppCompatActivity {

    private MapView mMapView;
    private TencentMap mTencentMap;
    private RadioGroup mRGMinClusterSize;

    private ClusterManager<TencentMapItem> mClusterManager;
    private DefaultClusterRenderer<TencentMapItem> renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clustering);
        init();
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
        //调用这个方法, 用于清除聚合数据, 避免退出后, 继续计算聚合导致空指针。
        if (mClusterManager != null) {
            mClusterManager.cancel();
        }
        mMapView.onDestroy();
    }

    private void init() {
        mMapView = (MapView) findViewById(R.id.map);
        mRGMinClusterSize = (RadioGroup) findViewById(R.id.rb_minClusterSize);

        mTencentMap = mMapView.getMap();

        Button btnReset = (Button) findViewById(R.id.btn_reset);
        Button btnRemove = (Button) findViewById(R.id.btn_remove);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_reset:
                        if (mClusterManager != null) {
                            mClusterManager.addItems(getCoords());
                            mClusterManager.cluster();
                        }
                        break;
                    case R.id.btn_remove:
                        if (mClusterManager != null) {
                            mClusterManager.cancel();
                            mClusterManager.clearItems();
                            mClusterManager.cluster();
                        }
                        break;
                }
            }
        };
        btnReset.setOnClickListener(onClickListener);
        btnRemove.setOnClickListener(onClickListener);

        //clustering
        mClusterManager = new ClusterManager<TencentMapItem>(this, mTencentMap);
        //设置自定义的算法：仅供测试，没有实际使用意义
//        CustomAlgrithom<TencentMapItem> algrithom = new CustomAlgrithom();
//        mClusterManager.setAlgorithm(algrithom);

        //设置聚合渲染器, 默认 clustering manager 使用的就是 DefaultClusterRenderer 可以不调用下列代码
        renderer = new DefaultClusterRenderer<>(this, mTencentMap, mClusterManager);
        renderer.setMinClusterSize(1);
        NonHierarchicalDistanceBasedAlgorithm<TencentMapItem> ndba = new NonHierarchicalDistanceBasedAlgorithm<>(this.getApplicationContext());
        ndba.setMaxDistanceAtZoom(100);
        mClusterManager.setAlgorithm(ndba);
        mRGMinClusterSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_minSize2:
                        renderer.setMinClusterSize(2);
                        break;
                    case R.id.rb_minSize4:
                        renderer.setMinClusterSize(4);
                        break;
                    case R.id.rb_minSize12:
                        renderer.setMinClusterSize(12);
                        break;
                }
            }
        });
        mClusterManager.setRenderer(renderer);
        mClusterManager.addItems(getCoords());
        mTencentMap.setOnCameraChangeListener(mClusterManager);
    }

    protected List<TencentMapItem> getCoords() {
        List<TencentMapItem> items = new ArrayList<TencentMapItem>();
        try {
            InputStream is = getAssets().open("cluster_new");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while((line = br.readLine()) != null) {
                String[] data = line.split("\t");
                double longitude = Double.parseDouble(data[1]);
                double latitude = Double.parseDouble(data[0]);
                items.add(new TencentMapItem(latitude, longitude));
            }
            is.close();
            isr.close();
            br.close();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return items;
    }
}
