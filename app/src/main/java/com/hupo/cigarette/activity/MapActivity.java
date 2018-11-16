package com.hupo.cigarette.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.bumptech.glide.Glide;
import com.hupo.cigarette.R;
import com.hupo.cigarette.adapter.MapShopAdapter;
import com.hupo.cigarette.app.App;
import com.hupo.cigarette.bean.MerchantInfo;
import com.hupo.cigarette.bus.RxBus;
import com.hupo.cigarette.dao.db.OrderModelInfo;
import com.hupo.cigarette.dao.helper.OrderDetailInfoHelper;
import com.hupo.cigarette.net.Constants;
import com.hupo.cigarette.oss.IMAGE_TYPE;
import com.hupo.cigarette.oss.OSSDownloadListener;
import com.hupo.cigarette.oss.OSSFileController;
import com.hupo.cigarette.utils.IntentUtils;
import com.hupo.cigarette.utils.ScreenUtil;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.utils.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yinglan.scrolllayout.ScrollLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gemini on 2018/10/31.
 *
 * 地图
 */
public class MapActivity extends RxAppCompatActivity implements LocationSource,SensorEventListener {

    @BindView(R.id.tv_title) TextView titleTv;//标题
    @BindView(R.id.lay_back) LinearLayout backLay;//返回
    //AMap是地图对象
    private AMap aMap;
    @BindView(R.id.map) MapView mapView;
    //声明mListener对象，定位监听器
    private OnLocationChangedListener mListener = null;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    private SensorManager mSM;
    private Sensor mSensor;

    @BindView(R.id.scroll_down_layout) ScrollLayout mScrollLayout;
    @BindView(R.id.list_view) ListView listView;
    private MapShopAdapter mapShopAdapter;

    @BindView(R.id.tv_map_shop_id) TextView shopIdTv;//商户id
    @BindView(R.id.iv_map_img) ImageView shopImgIv;//商户门头
    @BindView(R.id.tv_map_area) TextView shopAreaTv;//商户位置
    @BindView(R.id.tv_map_name) TextView shopNameTv;//商户名称

    private List<OrderModelInfo> orders = new ArrayList<>();//订单列表

    private String uid,//领货单id
            sendNo;//领货单单号

    private String custId;//订单编号

    @BindView(R.id.lay_map_start) LinearLayout mapStartLay;//导航底部
    @BindView(R.id.lay_map_over) LinearLayout mapOverLay;//结束导航

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(R.layout.activity_map);
        setStatusBar();
        ButterKnife.bind(this);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);
        initView();
        initData();
        //初始化地图组建
        initMapView();
        if (orders.size()>0){
            initShopInfo(0,orders.get(0));
        }
        //地图marker
        initMarker();
        //商户列表
        initShopList();

        initListener();
    }

    private void initListener() {
        RxBus.getInstance()
                .toObservable(AMapLocation.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aMapLocation -> {
                    if (mListener!=null){
                        mListener.onLocationChanged(aMapLocation);
                    }
                    mapToCenter(aMapLocation);

                });


    }

    private void mapToCenter(AMapLocation aMapLocation){
        if (isFirstLoc) {
            //设置缩放级别
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            //将地图移动到定位点
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));

            //添加图钉
            //  aMap.addMarker(getMarkerOptions(amapLocation));
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
            if (mListener!=null){
                //点击定位按钮 能够将地图的中心移动到定位点
                mListener.onLocationChanged(aMapLocation);
            }
        }
    }


    private void initView() {
        titleTv.setText(getString(R.string.title_map));//设置标题
        backLay.setVisibility(View.VISIBLE);

        mScrollLayout.setMinOffset(0);
        mScrollLayout.setMaxOffset((int) (ScreenUtil.getScreenHeight(this) * 0.5));
        mScrollLayout.setExitOffset(ScreenUtil.dip2px(this, 210));
        mScrollLayout.setIsSupportExit(true);
        mScrollLayout.setAllowHorizontalScroll(true);
        mScrollLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        mScrollLayout.setToExit();
        mScrollLayout.getBackground().setAlpha(0);
    }

    private void initData() {
        Intent intent = getIntent();
        MerchantInfo merchantInfo = (MerchantInfo) intent.getSerializableExtra("info");
        uid = merchantInfo.getUid();
        sendNo = merchantInfo.getSendNo();
        listView.setAdapter(mapShopAdapter = new MapShopAdapter(this,sendNo));

        List<OrderModelInfo> d = OrderDetailInfoHelper.getInstance().getDetail(uid);
        if (d.size() > 0){
            orders.clear();
            orders.addAll(d);
            mapShopAdapter.setData(orders);
        }

    }

    private void initMapView() {
        if (aMap == null) {
            aMap = mapView.getMap();

            //更改系统原有定位图标
            MyLocationStyle myLocationStyle = new MyLocationStyle();
//            myLocationStyle.myLocationIcon(BitmapDescriptorFactory
//                    .fromResource(R.drawable.gps_point));// 设置小蓝点的图标
            myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
            myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
            myLocationStyle.strokeWidth(0f);// 设置圆形的边框粗细
            aMap.setMyLocationStyle(myLocationStyle);

            //设置显示定位按钮 并且可以点击
            UiSettings settings = aMap.getUiSettings();
            aMap.setLocationSource(this);//设置了定位的监听
            settings.setZoomControlsEnabled(false);//隐藏放大 缩小 按钮
            settings.setMyLocationButtonEnabled(true);//是否显示定位按钮
            aMap.setMyLocationEnabled(true);//显示定位层并且可以触发定位,默认是flase

            settings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);//高德logo位置的移动

            mSM = (SensorManager) getSystemService(SENSOR_SERVICE);
            mSensor = mSM.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            mSM.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);//注册回调函数

        }
        AMapLocation location = App.getInstance().getLocation();
        if (location!=null){
            mapToCenter(location);
        }
    }

    private void initMarker() {
        for (int i = 0; i < orders.size(); i++) {
            MarkerOptions markerOption = new MarkerOptions();
            if (!("").equals(orders.get(i).getCustLatitude()) && !("").equals(orders.get(i).getCustLongitude())){
                markerOption.position(new LatLng(Double.valueOf(orders.get(i).getCustLatitude()),
                        Double.valueOf(orders.get(i).getCustLongitude())));
            }
            markerOption.draggable(false);//设置Marker可拖动
            markerOption.title(String.valueOf(i));
            View markerView = LayoutInflater.from(MapActivity.this).inflate(R.layout.map_marker_small,mapView,false);
            markerOption.icon(BitmapDescriptorFactory.fromView(markerView));
            aMap.addMarker(markerOption);
        }
    }

    private void initShopList() {

        mapShopAdapter.Onclick(new MapShopAdapter.OnClickInterface() {
            @Override
            public void onclick(View view, int position) {
                OrderModelInfo bean = orders.get(position);

               initShopInfo(position,bean);
                custId = bean.getCustId();

                mScrollLayout.scrollToExit();
                //清除地图上所有覆盖物
                aMap.clear();
                for (int i = 0; i < orders.size(); i++) {
                    MarkerOptions markerOption = new MarkerOptions();
                    if (("").equals(orders.get(i).getCustLatitude()) || ("").equals(orders.get(i).getCustLongitude())){
                        ToastUtils.show("该商户没有对应的经纬度信息！");
                        return;
                    }
                    markerOption.position(new LatLng(Double.valueOf(bean.getCustLatitude()),
                            Double.valueOf(bean.getCustLongitude())));
                    View markerView;
                    if (position == i){
                        markerView = LayoutInflater.from(MapActivity.this).inflate(R.layout.map_marker_big,mapView,false);
                        TextView markerName = markerView.findViewById(R.id.tv_marker_name);
                        markerName.setVisibility(View.VISIBLE);
                        markerName.setText(i+"："+bean.getManager());
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(Double.valueOf(bean.getCustLatitude()),
                                Double.valueOf(bean.getCustLongitude()))));
                    }else {
                        markerView = LayoutInflater.from(MapActivity.this).inflate(R.layout.map_marker_small,mapView,false);
                    }
                    markerOption.icon(BitmapDescriptorFactory.fromView(markerView));
                    aMap.addMarker(markerOption);
                }
            }
        });
    }

    private void initShopInfo(int position,OrderModelInfo bean) {
        if (position < 10){
            shopIdTv.setText("0" + (position+1));
        }else {
            shopIdTv.setText("" + (position+1));
        }
        shopAreaTv.setText(bean.getAddr());
        shopNameTv.setText(bean.getCustName());
        custId = bean.getCustId();
        OSSDownloadListener listener = new OSSDownloadListener() {
            @Override
            public void onProgress(int i, long currentSize, long totalSize) {

            }

            @Override
            public void onSuccess(int i, Drawable result) {

            }

            @Override
            public void onFailure(int i, String errMsg) {

            }

            @Override
            public void onSuccess(int i, File file) {
                Glide.with(MapActivity.this).load(file).placeholder(Constants.PLACEHOLD_RES).into(shopImgIv);

            }
        };

        OSSFileController.getController().getImageFile(position, IMAGE_TYPE.SHOP_HEAD,  "140402100046/","8D564D396C1E4269A3FDCE66A7C640AA.png", listener);
    }

    @OnClick({R.id.lay_map_error,R.id.lay_map_confirm,R.id.lay_map_navigation,R.id.lay_map_over})
    public void OnClick(View view){
        switch (view.getId()){
            //异常
            case R.id.lay_map_error:

                MerchantInfo info = new MerchantInfo();
                info.setUid(uid);
                info.setSendNo(custId);
                IntentUtils.startAtyWithSerialObj(MapActivity.this,SendErrorActivity.class,"info",info);
                break;
            //到货确认
            case R.id.lay_map_confirm:
                IntentUtils.startAtyWithSingleParam(MapActivity.this,OrderDetailActivity.class,"fragment",2);
                break;
            //导航
            case R.id.lay_map_navigation:
                mapStartLay.setVisibility(View.GONE);
                mapOverLay.setVisibility(View.VISIBLE);
                mScrollLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
                break;
            //结束导航
            case R.id.lay_map_over:
                mapStartLay.setVisibility(View.VISIBLE);
                mapOverLay.setVisibility(View.GONE);
                mScrollLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                break;
        }
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.tF87E3D));
        StatusBarUtil.setTranslucentForImageViewInFragment(MapActivity.this, 0,null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
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
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float degree = event.values[0];
            float bearing = aMap.getCameraPosition().bearing;
            if (degree + bearing > 360)
                aMap.setMyLocationRotateAngle(degree + bearing - 360);// 设置小蓝点旋转角度
            else
                aMap.setMyLocationRotateAngle(degree + bearing);//

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            if (currentProgress >= 0) {
                float precent = 255 * currentProgress;
                if (precent > 255) {
                    precent = 255;
                } else if (precent < 0) {
                    precent = 0;
                }
                mScrollLayout.getBackground().setAlpha(255 - (int) precent);
            }
//            if (text_foot.getVisibility() == View.VISIBLE)
//                text_foot.setVisibility(View.GONE);
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            if (currentStatus.equals(ScrollLayout.Status.EXIT)) {
//                text_foot.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onChildScroll(int top) {
        }
    };

    /**
     * 后退按钮响应事件
     */
    public void onBackClick(View v) {
        finish();
    }

}
