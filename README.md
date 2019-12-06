# 腾讯地图 sdk Android 组件库
在腾讯地图 sdk for Android 基础上封装的工具类和方法库,主要提供的功能：

1. marker 平移动画
2. 点聚合

地图组件支持范围仅限demo中所示场景，该功能当前不做需求和技术支持，其他诉求等请联系商务。

## 配置
这个工具库必须配合腾讯地图 sdk 使用，所以必须在工程中[集成腾讯地图sdk](https://github.com/TencentLBS/TencentVectorMapDemo_Android)。
除了腾讯地图 sdk 申请的权限外此组件库不需要额外的权限和配置参数。

请在 module 中的 build.gradle 中添加工具包依赖：

```groovy
dependencies {
    implementation 'com.tencent.map:sdk-utilities:1.0.5'
}
```

## 点聚合
当地图需要表现大量点时，可能需要在地图上添加大量 marker, 这将导致下列问题：

 - UI 性能降低，地图帧率下降导致卡顿；
 - Marker 过于密集，不利于效果展示；
 - 对于小内存手机，内存占用过多导致 OOM

对于大量点的展现，我们可以选择点聚合的方式，将同类 Marker 聚合到一起（聚合策略可以是根据距离，腾讯地图提供的聚合策略目前只有距离）。下面是聚合功能的基本调用方式：

1. 要使用腾讯地图提供的聚合功能，需要实现 ClusterItem 接口：

    ```java
    public class TencentMapItem implements ClusterItem {
        private final LatLng mLatLng;

        public TencentMapItem(double latitude, double longitude) {
            mLatLng = new LatLng(latitude, longitude);
        }

        @Override
        public LatLng getPosition() {
            return mLatLng;
        }
    }
    ```

2. 在聚合实现的 activity 中调用聚合相关API
    ```java
    /clustering
    mClusterManager = new ClusterManager<TencentMapItem>(this, mTencentMap);
    
    //设置聚合渲染器, 默认 cluster manager 使用的就是 DefaultClusterRenderer 可以不调用下列代码
    renderer = new DefaultClusterRenderer<>(this, mTencentMap, mClusterManager);
    //如果需要修改聚合点生效阈值，需要调用这个方法，这里指定聚合中的点大于1个时才开始聚合，否则显示单个 marekr
    renderer.setMinClusterSize(1);
    mClusterManager.setRenderer(renderer);
    //添加聚合数据，
    List<TencentMapItem> items = new ArrayList<TencentMapItem>();
    items.add(new TencentMapItem(39.984059，116.307621));
    items.add(new TencentMapItem(39.981954，116.304703));
    items.add(new TencentMapItem(39.984355，116.312256));
    items.add(new TencentMapItem(39.980442，116.315346));
    items.add(new TencentMapItem(39.981527，116.308994));
    items.add(new TencentMapItem(39.979751，116.310539));
    items.add(new TencentMapItem(39.977252，116.305776));
    items.add(new TencentMapItem(39.984026，116.316419));
    items.add(new TencentMapItem(39.976956，116.314874));
    items.add(new TencentMapItem(39.978501，116.311827));
    items.add(new TencentMapItem(39.980277，116.312814));
    items.add(new TencentMapItem(39.980236，116.369022));
    items.add(new TencentMapItem(39.978838，116.368486));
    items.add(new TencentMapItem(39.977161，116.367488));
    items.add(new TencentMapItem(39.915398，116.396713));
    items.add(new TencentMapItem(39.937645，116.455421));
    items.add(new TencentMapItem(39.896304，116.321182));
    items.add(new TencentMapItem(31.254487，121.452827));
    items.add(new TencentMapItem(31.225133，121.485443));
    items.add(new TencentMapItem(31.216912，121.442528));
    items.add(new TencentMapItem(31.251552，121.500893));
    items.add(new TencentMapItem(31.249204，121.455917));
    items.add(new TencentMapItem(22.546885，114.042892));
    items.add(new TencentMapItem(22.538086，113.999805));
    items.add(new TencentMapItem(22.534756，114.082031));
    mClusterManager.addItems(items);
    //添加聚合
    mTencentMap.setOnCameraChangeListener(mClusterManager);
    ```

## marker 平移动画
此功能适用于出行、配送等业务中需要为用户动画展示车、货物等场景。

1. 添加marker
    ```java
    mMarker = mTencentMap.addMarker(
            new MarkerOptions(center)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.taxi))
                    //设置此属性 marker 会跟随地图旋转
                    .flat(true)
                    //marker 逆时针方向旋转
                    .clockwise(false));
    ```
2. 为添加的 marker 创建平移动画
    ```java
    MarkerTranslateAnimator mTranslateAnimator = new MarkerTranslateAnimator(
            //执行此平移动画的 marker
            mMarker, 
            //动画持续时间
            500 * 1000,
            //平移动画点串
            latlngs,
            //marker 是否会根据传入的点串计算并执行旋转动画, marker 方向将与移动方向保持一致
            true);
    ```