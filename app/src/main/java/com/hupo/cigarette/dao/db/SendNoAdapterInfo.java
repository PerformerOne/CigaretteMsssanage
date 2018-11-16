package com.hupo.cigarette.dao.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 配送单
 */
@Entity
public class SendNoAdapterInfo {

    @Id(autoincrement = true)
    private Long id;

    private String no = "";
    private String sendCarNo = "";
    private String carId;
    private String carNo = "";
    private String carNum = "";
    private String deliveryid = "";
    private String deliveryName = "";
    private String driverId = "";
    private String driverName = "";
    private String sendAreaId = "";
    private String sendAreaName = "";
    private int orderCount;
    private int bagCount;
    private int affirmCount;
    private int goodsCount;
    private double totalPrice;
    private int custCount;
    private String sendDate = "";
    private int abnormalCount;
    private int status = -1;
    private String batchId = "";

    public SendNoAdapterInfo(String no, String sendCarNo, String carId, String carNo, String carNum, String deliveryid, String deliveryName, String driverId, String driverName, String sendAreaId, String sendAreaName, int orderCount, int bagCount, int affirmCount, int goodsCount, double totalPrice, int custCount, String sendDate, int abnormalCount, int status, String batchId) {
        this.no = no;
        this.sendCarNo = sendCarNo;
        this.carId = carId;
        this.carNo = carNo;
        this.carNum = carNum;
        this.deliveryid = deliveryid;
        this.deliveryName = deliveryName;
        this.driverId = driverId;
        this.driverName = driverName;
        this.sendAreaId = sendAreaId;
        this.sendAreaName = sendAreaName;
        this.orderCount = orderCount;
        this.bagCount = bagCount;
        this.affirmCount = affirmCount;
        this.goodsCount = goodsCount;
        this.totalPrice = totalPrice;
        this.custCount = custCount;
        this.sendDate = sendDate;
        this.abnormalCount = abnormalCount;
        this.status = status;
        this.batchId = batchId;
    }

    @Generated(hash = 1957199476)
    public SendNoAdapterInfo(Long id, String no, String sendCarNo, String carId,
            String carNo, String carNum, String deliveryid, String deliveryName,
            String driverId, String driverName, String sendAreaId,
            String sendAreaName, int orderCount, int bagCount, int affirmCount,
            int goodsCount, double totalPrice, int custCount, String sendDate,
            int abnormalCount, int status, String batchId) {
        this.id = id;
        this.no = no;
        this.sendCarNo = sendCarNo;
        this.carId = carId;
        this.carNo = carNo;
        this.carNum = carNum;
        this.deliveryid = deliveryid;
        this.deliveryName = deliveryName;
        this.driverId = driverId;
        this.driverName = driverName;
        this.sendAreaId = sendAreaId;
        this.sendAreaName = sendAreaName;
        this.orderCount = orderCount;
        this.bagCount = bagCount;
        this.affirmCount = affirmCount;
        this.goodsCount = goodsCount;
        this.totalPrice = totalPrice;
        this.custCount = custCount;
        this.sendDate = sendDate;
        this.abnormalCount = abnormalCount;
        this.status = status;
        this.batchId = batchId;
    }
    @Generated(hash = 1046603135)
    public SendNoAdapterInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNo() {
        return this.no;
    }
    public void setNo(String no) {
        this.no = no;
    }
    public String getSendCarNo() {
        return this.sendCarNo;
    }
    public void setSendCarNo(String sendCarNo) {
        this.sendCarNo = sendCarNo;
    }
    public String getCarId() {
        return this.carId;
    }
    public void setCarId(String carId) {
        this.carId = carId;
    }
    public String getCarNo() {
        return this.carNo;
    }
    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
    public String getCarNum() {
        return this.carNum;
    }
    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }
    public String getDeliveryid() {
        return this.deliveryid;
    }
    public void setDeliveryid(String deliveryid) {
        this.deliveryid = deliveryid;
    }
    public String getDeliveryName() {
        return this.deliveryName;
    }
    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
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
    public String getSendAreaId() {
        return this.sendAreaId;
    }
    public void setSendAreaId(String sendAreaId) {
        this.sendAreaId = sendAreaId;
    }
    public String getSendAreaName() {
        return this.sendAreaName;
    }
    public void setSendAreaName(String sendAreaName) {
        this.sendAreaName = sendAreaName;
    }
    public int getOrderCount() {
        return this.orderCount;
    }
    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
    public int getBagCount() {
        return this.bagCount;
    }
    public void setBagCount(int bagCount) {
        this.bagCount = bagCount;
    }
    public int getAffirmCount() {
        return this.affirmCount;
    }
    public void setAffirmCount(int affirmCount) {
        this.affirmCount = affirmCount;
    }
    public int getGoodsCount() {
        return this.goodsCount;
    }
    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }
    public double getTotalPrice() {
        return this.totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public int getCustCount() {
        return this.custCount;
    }
    public void setCustCount(int custCount) {
        this.custCount = custCount;
    }
    public String getSendDate() {
        return this.sendDate;
    }
    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }
    public int getAbnormalCount() {
        return this.abnormalCount;
    }
    public void setAbnormalCount(int abnormalCount) {
        this.abnormalCount = abnormalCount;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getBatchId() {
        return this.batchId;
    }
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
    

}
