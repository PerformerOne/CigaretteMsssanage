package com.hupo.cigarette.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.hupo.cigarette.R;
import com.hupo.cigarette.adapter.ReturnJourneyAdapter;
import com.hupo.cigarette.app.App;
import com.hupo.cigarette.bean.AreaBean;
import com.hupo.cigarette.bus.RxBus;
import com.hupo.cigarette.utils.IntentUtils;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Gemini on 2018/11/1.
 *
 * 返程
 */
public class ReturnJourneyActivity extends RxAppCompatActivity implements LocationSource,AMapNaviListener{

    @BindView(R.id.tv_title) TextView titleTv;//标题
    @BindView(R.id.lay_back) LinearLayout backLay;//返回
//    @BindView(R.id.tv_return_detail1) TextView returnDetail1;
//    @BindView(R.id.tv_return_time1) TextView returnTime1;
//    @BindView(R.id.tv_return_distance1) TextView returnDistance1;
//    @BindView(R.id.tv_return_detail2) TextView returnDetail2;
//    @BindView(R.id.tv_return_time2) TextView returnTime2;
//    @BindView(R.id.tv_return_distance2) TextView returnDistance2;
    @BindView(R.id.rv_road_info) RecyclerView roadInfoRv;//路线
    private ReturnJourneyAdapter returnJourneyAdapter;

    //AMap是地图对象
    private AMap aMap;
    @BindView(R.id.map) MapView mapView;
    //声明mListener对象，定位监听器
    private LocationSource.OnLocationChangedListener mListener = null;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    /**
     * 导航对象(单例)
     */
    private AMapNavi mAMapNavi;
//    private NaviLatLng endLatlng = new NaviLatLng(35.18, 113.52);

    private double latitude,//纬度
            longitude;//经度
    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();

    /**
     * 路线的权值，重合路线情况下，权值高的路线会覆盖权值低的路线
     **/
    private int zindex = 1;

    private int routeIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_journey);
        setStatusBar();
        ButterKnife.bind(this);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);
        initView();
        //初始化地组件
        initMapView();
        //导航
        initNavi();

        initListener();

    }

    private void initListener() {
        RxBus.getInstance()
                .toObservable(AMapLocation.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aMapLocation -> {
                    mapToCenter(aMapLocation);
                });
    }

    private void initView() {
        //获取传过来的经纬度信息
        Intent intent = getIntent();
        AreaBean areaBean = (AreaBean) intent.getSerializableExtra("areaBean");
        latitude = areaBean.getLatitude();
        longitude = areaBean.getLongitude();

        titleTv.setText(getString(R.string.title_return_journey));//设置标题
        backLay.setVisibility(View.VISIBLE);

        //初始化RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        roadInfoRv.setLayoutManager(layoutManager);
        roadInfoRv.setAdapter(returnJourneyAdapter = new ReturnJourneyAdapter(this));
    }

    private void initMapView() {
        if (aMap == null) {
            aMap = mapView.getMap();
            //设置显示定位按钮 并且可以点击
            UiSettings settings = aMap.getUiSettings();
            aMap.setLocationSource(this);//设置了定位的监听
            settings.setZoomControlsEnabled(false);//隐藏放大 缩小 按钮
            settings.setMyLocationButtonEnabled(false);//是否显示定位按钮
            aMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase

            settings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);//高德logo位置的移动
        }


    }

    private void mapToCenter(AMapLocation aMapLocation) {
        //定位成功回调信息，设置相关消息
        aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
        aMapLocation.getLatitude();//获取纬度
        aMapLocation.getLongitude();//获取经度
        aMapLocation.getAccuracy();//获取精度信息
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(aMapLocation.getTime());
        df.format(date);//定位时间
        aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
        aMapLocation.getCountry();//国家信息
        aMapLocation.getProvince();//省信息
        aMapLocation.getCity();//城市信息
        aMapLocation.getDistrict();//城区信息
        aMapLocation.getStreet();//街道信息
        aMapLocation.getStreetNum();//街道门牌号信息
        aMapLocation.getCityCode();//城市编码
        aMapLocation.getAdCode();//地区编码

        // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
        if (isFirstLoc) {
            // 获取轨迹坐标点
            List<LatLng> points = new ArrayList<LatLng>();

            points.add(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));

            points.add(new LatLng(latitude, longitude));
            LatLngBounds.Builder b = LatLngBounds.builder();
            for (int i = 0 ; i < points.size(); i++) {
                b.include(points.get(i));
            }
            LatLngBounds bounds = b.build();
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            //获取定位信息
            StringBuffer buffer = new StringBuffer();
            buffer.append(aMapLocation.getCountry() + ""
                    + aMapLocation.getProvince() + ""
                    + aMapLocation.getCity() + ""
                    + aMapLocation.getProvince() + ""
                    + aMapLocation.getDistrict() + ""
                    + aMapLocation.getStreet() + ""
                    + aMapLocation.getStreetNum());
            isFirstLoc = false;

            startList.clear();
            startList.add(new NaviLatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
            endList.clear();
            endList.add(new NaviLatLng(latitude, longitude));
            int strategyFlag = 0;
            try {
                strategyFlag = mAMapNavi.strategyConvert(true, false, false, false, true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
        }
    }

    private void initNavi() {
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
        clearRoute();

        AMapLocation location = App.getInstance().getLocation();
        if (location!=null){
            mapToCenter(location);
        }
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.tF87E3D));
        StatusBarUtil.setTranslucentForImageViewInFragment(ReturnJourneyActivity.this, 0,null);
    }

    @OnClick({R.id.btn_change})
    public void OnClick(View view){
        switch (view.getId()){

            case R.id.btn_change:
                IntentUtils.startAtyWithSingleParam(ReturnJourneyActivity.this,RouteNaviActivity.class,"gps",true);
                break;
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        startList.clear();
        wayList.clear();
        endList.clear();
        routeOverlays.clear();
        /**
         * 当前页面只是展示地图，activity销毁后不需要再回调导航的状态
         */
        mAMapNavi.removeAMapNaviListener(this);
        mAMapNavi.destroy();

        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }



    @Override
    public void onInitNaviSuccess() {
    }


    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {
        //清空上次计算的路径列表。
        routeOverlays.clear();
        HashMap<Integer, AMapNaviPath> paths = mAMapNavi.getNaviPaths();
        returnJourneyAdapter.setData(ints,paths);
        for (int i = 0; i < ints.length; i++) {
            AMapNaviPath path = paths.get(ints[i]);
            if (path != null) {
                drawRoutes(ints[i], path);
            }
        }
    }


    @Override
    public void onCalculateRouteFailure(int arg0) {
        Toast.makeText(getApplicationContext(), "计算路线失败，errorcode＝" + arg0, Toast.LENGTH_SHORT).show();
    }

    private void drawRoutes(int routeId, AMapNaviPath path) {
        aMap.moveCamera(CameraUpdateFactory.changeTilt(0));
        RouteOverLay routeOverLay = new RouteOverLay(aMap, path, this);
        routeOverLay.setTrafficLine(false);
        routeOverLay.addToMap();
        routeOverlays.put(routeId, routeOverLay);

        returnJourneyAdapter.setOnItemClickListener(new ReturnJourneyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                routeIndex = position;
                changeRoute();
                returnJourneyAdapter.setRouteID(position);
            }
        });
        changeRoute();
    }


    public void changeRoute() {
        int routeID = routeOverlays.keyAt(routeIndex);

        //突出选择的那条路
        for (int i = 0; i < routeOverlays.size(); i++) {
            int key = routeOverlays.keyAt(i);
            routeOverlays.get(key).setTransparency(0.4f);
        }
        routeOverlays.get(routeID).setTransparency(1);
        /**把用户选择的那条路的权值弄高，使路线高亮显示的同时，重合路段不会变的透明**/
        routeOverlays.get(routeID).setZindex(zindex++);
        //必须告诉AMapNavi 你最后选择的哪条路
        mAMapNavi.selectRouteId(routeID);
    }

    /**
     * 清除当前地图上算好的路线
     */
    private void clearRoute() {
        for (int i = 0; i < routeOverlays.size(); i++) {
            RouteOverLay routeOverlay = routeOverlays.valueAt(i);
            routeOverlay.removeFromMap();
        }
        routeOverlays.clear();
    }


    /**
     * ************************************************** 在算路页面，以下接口全不需要处理，在以后的版本中我们会进行优化***********************************************************************************************
     **/

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo arg0) {


    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo arg0) {


    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] arg0) {


    }

    @Override
    public void hideCross() {


    }

    @Override
    public void hideLaneInfo() {


    }


    @Override
    public void onCalculateRouteSuccess() {

    }

    @Override
    public void notifyParallelRoad(int arg0) {


    }

    @Override
    public void onArriveDestination() {


    }


    @Override
    public void onArrivedWayPoint(int arg0) {


    }

    @Override
    public void onEndEmulatorNavi() {


    }

    @Override
    public void onGetNavigationText(int arg0, String arg1) {


    }

    @Override
    public void onGpsOpenStatus(boolean arg0) {


    }

    @Override
    public void onInitNaviFailure() {


    }

    @Override
    public void onLocationChange(AMapNaviLocation arg0) {


    }

    @Override
    public void onNaviInfoUpdate(NaviInfo arg0) {


    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo arg0) {


    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] amapServiceAreaInfos) {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {


    }

    @Override
    public void onReCalculateRouteForYaw() {


    }

    @Override
    public void onStartNavi(int arg0) {


    }

    @Override
    public void onTrafficStatusUpdate() {


    }

    @Override
    public void showCross(AMapNaviCross arg0) {


    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] arg0, byte[] arg1, byte[] arg2) {


    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo arg0) {


    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat arg0) {


    }
    /**
     * 后退按钮响应事件
     */
    public void onBackClick(View v) {
        finish();
    }
}
