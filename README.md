# 腾讯地图 sdk Android 组件库
在腾讯地图 sdk for Android 基础上封装的工具类和方法库,主要提供的功能：

1. marker 平移动画
2. 点聚合

地图组件支持范围仅限demo中所示场景，该功能当前不做需求和技术支持，其他诉求等请联系商务。

## 配置
这个工具库必须配合腾讯地图 sdk 使用，所以必须在工程中[集成腾讯地图sdk](https://github.com/TencentLBS/TencentVectorMapDemo_Android)。除了腾讯地图 sdk 申请的权限外此组件库不需要额外的权限和配置参数。

1. 下载[腾讯地图 sdk Android 组件库](https://wecar.myapp.com/myapp/mapwecar/shuttle/open_platform/Android/TencentMapUtils_Android_Vector_v1.0.4.zip)
2. 压缩包解压后将其中的 TencentmapVectorUtils jar 包放入工程中的 libs 目录下，并在 build.gradle 中添加依赖

## marker 平移动画
此功能适用于出行、配送等业务中需要为用户动画展示车、货物等场景。

1. 添加marker
```
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
```
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
