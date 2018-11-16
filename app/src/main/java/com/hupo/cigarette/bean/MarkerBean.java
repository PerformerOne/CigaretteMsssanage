package com.hupo.cigarette.bean;

/**
 * Created by Gemini on 2018/11/6.
 *
 * marker坐标
 */
public class MarkerBean {

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

    public MarkerBean(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MarkerBean() {
    }
}
