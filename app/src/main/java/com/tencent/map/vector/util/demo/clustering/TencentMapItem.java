package com.tencent.map.vector.util.demo.clustering;

import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.vector.utils.clustering.ClusterItem;

/**
 * @author wangxiaokun on 16/9/5.
 */
public class TencentMapItem implements ClusterItem {

    private final LatLng mLatLng;

    /**
     *
	 */
    public TencentMapItem(double latitude, double longitude) {
        // TODO Auto-generated constructor stub
        mLatLng = new LatLng(latitude, longitude);
    }

    /* (non-Javadoc)
     * @see com.tencent.mapsdk.clustering.ClusterItem#getPosition()
     */
    @Override
    public LatLng getPosition() {
        // TODO Auto-generated method stub
        return mLatLng;
    }

}
