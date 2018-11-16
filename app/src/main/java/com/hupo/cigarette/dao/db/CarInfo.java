package com.hupo.cigarette.dao.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CarInfo {
    @Id(autoincrement = true)
    private Long id;
    /**
     * 车辆编号
     */
    private String carId = "";

    /**
     * 车辆编号
     */
    private String no = "";

    /**
     * 车牌号
     */
    private String num = "";

    /**
     * 默认司机编号
     */
    private String defaultDriverId = "";

    /**
     * 默认司机姓名
     */
    private String defaultDriverName = "";

    /**
     * 在线状态
     */
    private boolean online;

    /**
     * 司机编号
     */
    private String driverId = "";

    /**
     * 司机姓名
     */
    private String driverName = "";

    @Generated(hash = 726049617)
    public CarInfo(Long id, String carId, String no, String num,
            String defaultDriverId, String defaultDriverName, boolean online,
            String driverId, String driverName) {
        this.id = id;
        this.carId = carId;
        this.no = no;
        this.num = num;
        this.defaultDriverId = defaultDriverId;
        this.defaultDriverName = defaultDriverName;
        this.online = online;
        this.driverId = driverId;
        this.driverName = driverName;
    }

    @Generated(hash = 850322869)
    public CarInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarId() {
        return this.carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getNo() {
        return this.no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNum() {
        return this.num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDefaultDriverId() {
        return this.defaultDriverId;
    }

    public void setDefaultDriverId(String defaultDriverId) {
        this.defaultDriverId = defaultDriverId;
    }

    public String getDefaultDriverName() {
        return this.defaultDriverName;
    }

    public void setDefaultDriverName(String defaultDriverName) {
        this.defaultDriverName = defaultDriverName;
    }

    public boolean getOnline() {
        return this.online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getDriverId() {
        return this.driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return this.driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

}
