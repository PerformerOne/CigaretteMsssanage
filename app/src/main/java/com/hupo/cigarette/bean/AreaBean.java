package com.hupo.cigarette.bean;

import java.io.Serializable;

/**
 * Created by Gemini on 2018/11/5.
 *
 * 位置
 */
public class AreaBean implements Serializable {

    private double latitude;//纬度
    private double longitude;//经度

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
