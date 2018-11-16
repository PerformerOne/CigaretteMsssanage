package com.hupo.cigarette.location;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hupo.cigarette.app.App;

public class LocationManager {

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption ;


    public LocationManager(AMapLocationListener listener){
        mlocationClient = new AMapLocationClient(App.getInstance());
        mLocationOption = new AMapLocationClientOption();
        mlocationClient.setLocationListener(listener);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(5000);
        mlocationClient.setLocationOption(mLocationOption);
    }

    public LocationManager startLocation(){
        mlocationClient.startLocation();
        return this;
    }

    public void destroy(){
        stop();
        mlocationClient.onDestroy();
    }

    public void stop(){
        mlocationClient.stopLocation();
    }

}
