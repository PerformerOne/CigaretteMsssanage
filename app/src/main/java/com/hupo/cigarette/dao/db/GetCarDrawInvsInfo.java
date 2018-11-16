package com.hupo.cigarette.dao.db;

import com.hupo.cigarette.dao.converter.HOrderModelsConverter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HOrderModel;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

@Entity
public class GetCarDrawInvsInfo {

    @Id(autoincrement = true)
    private Long id;
    private String uid;
    private String no = "";
    private String externalNo = "";
    private String externalAreaId = "";
    private String externalAreaName = "";
    private String externalLineId = "";
    private String externalLineName = "";
    private String cages = "";
    private double totalPrice;
    private int bagCount;
    private int totalCount;
    private String carId;
    private String carNo = "";
    private String carNum = "";
    private boolean carOnline;
    private String deliveryId = "";
    private String deliveryName = "";
    private String driverId = "";
    private String driverName = "";
    @Convert(converter = HOrderModelsConverter.class,columnType = String.class)
    private List<HOrderModel> orders;
    private int status;
    private String bornDate = "";
    private String sendDate = "";
    private int goodsCount;
    private String sendNo = "";
    private String lineId = "";
    private String lineName = "";
    private String areaId = "";
    private String areaName = "";
    private int customerCount;
    private int abnormalCount;
    private String lastUpdateTime;
    private String beforeUpdateTime;
    private int packingType;
    @Generated(hash = 1253334951)
    public GetCarDrawInvsInfo(Long id, String uid, String no, String externalNo,
            String externalAreaId, String externalAreaName, String externalLineId,
            String externalLineName, String cages, double totalPrice, int bagCount,
            int totalCount, String carId, String carNo, String carNum,
            boolean carOnline, String deliveryId, String deliveryName,
            String driverId, String driverName, List<HOrderModel> orders,
            int status, String bornDate, String sendDate, int goodsCount,
            String sendNo, String lineId, String lineName, String areaId,
            String areaName, int customerCount, int abnormalCount,
            String lastUpdateTime, String beforeUpdateTime, int packingType) {
        this.id = id;
        this.uid = uid;
        this.no = no;
        this.externalNo = externalNo;
        this.externalAreaId = externalAreaId;
        this.externalAreaName = externalAreaName;
        this.externalLineId = externalLineId;
        this.externalLineName = externalLineName;
        this.cages = cages;
        this.totalPrice = totalPrice;
        this.bagCount = bagCount;
        this.totalCount = totalCount;
        this.carId = carId;
        this.carNo = carNo;
        this.carNum = carNum;
        this.carOnline = carOnline;
        this.deliveryId = deliveryId;
        this.deliveryName = deliveryName;
        this.driverId = driverId;
        this.driverName = driverName;
        this.orders = orders;
        this.status = status;
        this.bornDate = bornDate;
        this.sendDate = sendDate;
        this.goodsCount = goodsCount;
        this.sendNo = sendNo;
        this.lineId = lineId;
        this.lineName = lineName;
        this.areaId = areaId;
        this.areaName = areaName;
        this.customerCount = customerCount;
        this.abnormalCount = abnormalCount;
        this.lastUpdateTime = lastUpdateTime;
        this.beforeUpdateTime = beforeUpdateTime;
        this.packingType = packingType;
    }
    @Generated(hash = 1853769505)
    public GetCarDrawInvsInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUid() {
        return this.uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getNo() {
        return this.no;
    }
    public void setNo(String no) {
        this.no = no;
    }
    public String getExternalNo() {
        return this.externalNo;
    }
    public void setExternalNo(String externalNo) {
        this.externalNo = externalNo;
    }
    public String getExternalAreaId() {
        return this.externalAreaId;
    }
    public void setExternalAreaId(String externalAreaId) {
        this.externalAreaId = externalAreaId;
    }
    public String getExternalAreaName() {
        return this.externalAreaName;
    }
    public void setExternalAreaName(String externalAreaName) {
        this.externalAreaName = externalAreaName;
    }
    public String getExternalLineId() {
        return this.externalLineId;
    }
    public void setExternalLineId(String externalLineId) {
        this.externalLineId = externalLineId;
    }
    public String getExternalLineName() {
        return this.externalLineName;
    }
    public void setExternalLineName(String externalLineName) {
        this.externalLineName = externalLineName;
    }
    public String getCages() {
        return this.cages;
    }
    public void setCages(String cages) {
        this.cages = cages;
    }
    public double getTotalPrice() {
        return this.totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public int getBagCount() {
        return this.bagCount;
    }
    public void setBagCount(int bagCount) {
        this.bagCount = bagCount;
    }
    public int getTotalCount() {
        return this.totalCount;
    }
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
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
    public boolean getCarOnline() {
        return this.carOnline;
    }
    public void setCarOnline(boolean carOnline) {
        this.carOnline = carOnline;
    }
    public String getDeliveryId() {
        return this.deliveryId;
    }
    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
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
    public List<HOrderModel> getOrders() {
        return this.orders;
    }
    public void setOrders(List<HOrderModel> orders) {
        this.orders = orders;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getBornDate() {
        return this.bornDate;
    }
    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }
    public String getSendDate() {
        return this.sendDate;
    }
    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }
    public int getGoodsCount() {
        return this.goodsCount;
    }
    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }
    public String getSendNo() {
        return this.sendNo;
    }
    public void setSendNo(String sendNo) {
        this.sendNo = sendNo;
    }
    public String getLineId() {
        return this.lineId;
    }
    public void setLineId(String lineId) {
        this.lineId = lineId;
    }
    public String getLineName() {
        return this.lineName;
    }
    public void setLineName(String lineName) {
        this.lineName = lineName;
    }
    public String getAreaId() {
        return this.areaId;
    }
    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
    public String getAreaName() {
        return this.areaName;
    }
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    public int getCustomerCount() {
        return this.customerCount;
    }
    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }
    public int getAbnormalCount() {
        return this.abnormalCount;
    }
    public void setAbnormalCount(int abnormalCount) {
        this.abnormalCount = abnormalCount;
    }
    public String getLastUpdateTime() {
        return this.lastUpdateTime;
    }
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    public String getBeforeUpdateTime() {
        return this.beforeUpdateTime;
    }
    public void setBeforeUpdateTime(String beforeUpdateTime) {
        this.beforeUpdateTime = beforeUpdateTime;
    }
    public int getPackingType() {
        return this.packingType;
    }
    public void setPackingType(int packingType) {
        this.packingType = packingType;
    }

}
