package com.tencent.tencentmap.mapsdk.vector.utils.demos;


import com.tencent.tencentmap.mapsdk.vector.utils.clustering.Cluster;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.ClusterItem;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.algo.Algorithm;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.algo.StaticCluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wangxiaokun on 16/9/5.
 */
public class CustomAlgrithom <T extends ClusterItem> implements Algorithm<T> {

    private List<T> mItems = new ArrayList<>();

    @Override
    public void addItem(T t) {
        synchronized (mItems) {
            mItems.add(t);
        }
    }

    @Override
    public void addItems(Collection<T> collection) {
        synchronized (mItems) {
            mItems.addAll(collection);
        }
    }

    @Override
    public void clearItems() {
        synchronized (mItems) {
            mItems.clear();
        }
    }

    @Override
    public void removeItem(T t) {
        synchronized (mItems) {
            mItems.remove(t);
        }
    }

    /**
     * Only demonstrate the implementation for Algorithm, may be useless in reality.
     * @param v This parameter use for calculate the cluster, may be zoom level
     *          of the map or other values.
     * @return
     */
    @Override
    public Set<? extends Cluster<T>> getClusters(double v) {
        HashSet<Cluster<T>> clusters = new HashSet<>();
        //Clustering every 2 item in mItems.
        //The default renderer clustering the marker together only if the item size large than 4,
        //so you should set the mini clustering size equals 1 to see the clustering effect.
        synchronized (mItems) {
            for (int i = 0; i < mItems.size() / 2; i++) {
                StaticCluster<T> cluster = new StaticCluster<>(mItems.get(i * 2).getPosition());
                cluster.add(mItems.get(i * 2));
                if (mItems.get(i * 2 + 1) != null) {
                    cluster.add(mItems.get(i * 2 + 1));
                }
                clusters.add(cluster);
            }
        }
        return clusters;
    }

    @Override
    public Collection<T> getItems() {
        return mItems;
    }
}
