package chary.nyist.com.baidumapdemo;

import android.os.Bundle;
import android.app.Activity;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;

public class MapActivity extends Activity {

    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private BitmapDescriptor mCurrentMarker = null;
    private MyLocationConfiguration.LocationMode mCurrentMode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.map_bd);
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        MyLocationData localData = new MyLocationData.Builder()
                .latitude(32.979844).longitude(112.556386).build();
//        MyLocationData localData = new MyLocationData(32.979844,112.556386, 0, 100, 0, 0);

        mBaiduMap.setMyLocationData(localData);

        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
        // icon
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_loc);
        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);


//        mMapView. showScaleControl(true);//比例尺
        mMapView. showZoomControls(true);

        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15));//默认显示比例
        mBaiduMap.setMyLocationConfiguration(config);


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
