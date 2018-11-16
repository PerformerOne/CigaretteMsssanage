package com.hupo.cigarette.dao.db;

import com.hupo.cigarette.dao.converter.HCustPhoneModelConverter;
import com.hupo.cigarette.dao.converter.HCustPhotoModelConverter;
import com.hupo.cigarette.dao.converter.HOrderBagModelConverter;
import com.hupo.cigarette.dao.converter.HOrderDetailModelConverter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HCustPhoneModel;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HCustPhotoModel;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HOrderBagModel;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HOrderDetailModel;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

@Entity
public class OrderModelInfo {
    @Id(autoincrement = true)
    private Long autoId;

    private String id = "";
    private String custId = "";
    private String custName = "";
    private String manager = "";
    private String managerTel = "";
    private String pwd = "";
    private String nfcCardId = "";
    private String addr = "";
    @Convert(converter = HCustPhoneModelConverter.class,columnType = String.class)
    private List<HCustPhoneModel> custPhones ;
    private String custLongitude = "0.0";
    private String custLatitude = "0.0";
    private int count;
    private double price;
    private int status;
    private String sendNo;
    private int bagCount;
    private String externalDrawInvNo = "";
    private String externalAreaId = "";
    private String externalAreaName = "";
    private String externalLineId = "";
    private String externalLineName = "";
    private int externalLineNo;
    private String areaId = "";
    private String areaName = "";
    private String lineId = "";
    private String lineName = "";
    private int lineNo;
    private int pmtStatus;
    private int abnormalCount;
    private int smokeBoxLendCount;
    private String custPhotoUpdateTime = "";
    private int custPhotoCount;
    @Convert(converter = HOrderBagModelConverter.class,columnType = String.class)
    private List<HOrderBagModel> bagCodes ;
    @Convert(converter = HOrderDetailModelConverter.class,columnType = String.class)
    private List<HOrderDetailModel> orderDetails ;
    @Convert(converter = HCustPhotoModelConverter.class,columnType = String.class)
    private List<HCustPhotoModel> custPhotos ;
    private String drawInvId;
    private String selectReason;
    private String customReason;

    public OrderModelInfo(String id, String custId, String custName, String manager, String managerTel, String pwd, String nfcCardId, String addr, List<HCustPhoneModel> custPhones, String custLongitude, String custLatitude, int count, double price, int status, int bagCount, String externalDrawInvNo, String externalAreaId, String externalAreaName, String externalLineId, String externalLineName, int externalLineNo, String areaId, String areaName, String lineId, String lineName, int lineNo, int pmtStatus, int abnormalCount, int smokeBoxLendCount, String custPhotoUpdateTime, int custPhotoCount, List<HOrderBagModel> bagCodes, List<HOrderDetailModel> orderDetails, List<HCustPhotoModel> custPhotos, String drawInvId,String sendNo) {
        this.id = id;
        this.custId = custId;
        this.custName = custName;
        this.manager = manager;
        this.managerTel = managerTel;
        this.pwd = pwd;
        this.nfcCardId = nfcCardId;
        this.addr = addr;
        this.custPhones = custPhones;
        this.custLongitude = custLongitude;
        this.custLatitude = custLatitude;
        this.count = count;
        this.price = price;
        this.status = status;
        this.bagCount = bagCount;
        this.externalDrawInvNo = externalDrawInvNo;
        this.externalAreaId = externalAreaId;
        this.externalAreaName = externalAreaName;
        this.externalLineId = externalLineId;
        this.externalLineName = externalLineName;
        this.externalLineNo = externalLineNo;
        this.areaId = areaId;
        this.areaName = areaName;
        this.lineId = lineId;
        this.lineName = lineName;
        this.lineNo = lineNo;
        this.pmtStatus = pmtStatus;
        this.abnormalCount = abnormalCount;
        this.smokeBoxLendCount = smokeBoxLendCount;
        this.custPhotoUpdateTime = custPhotoUpdateTime;
        this.custPhotoCount = custPhotoCount;
        this.bagCodes = bagCodes;
        this.orderDetails = orderDetails;
        this.custPhotos = custPhotos;
        this.drawInvId = drawInvId;
        this.sendNo=sendNo;
    }


    @Generated(hash = 1590896094)
    public OrderModelInfo(Long autoId, String id, String custId, String custName, String manager, String managerTel, String pwd, String nfcCardId, String addr, List<HCustPhoneModel> custPhones, String custLongitude, String custLatitude, int count, double price, int status, String sendNo, int bagCount, String externalDrawInvNo, String externalAreaId, String externalAreaName, String externalLineId, String externalLineName, int externalLineNo, String areaId, String areaName, String lineId, String lineName, int lineNo, int pmtStatus, int abnormalCount, int smokeBoxLendCount, String custPhotoUpdateTime, int custPhotoCount, List<HOrderBagModel> bagCodes, List<HOrderDetailModel> orderDetails, List<HCustPhotoModel> custPhotos,
            String drawInvId, String selectReason, String customReason) {
        this.autoId = autoId;
        this.id = id;
        this.custId = custId;
        this.custName = custName;
        this.manager = manager;
        this.managerTel = managerTel;
        this.pwd = pwd;
        this.nfcCardId = nfcCardId;
        this.addr = addr;
        this.custPhones = custPhones;
        this.custLongitude = custLongitude;
        this.custLatitude = custLatitude;
        this.count = count;
        this.price = price;
        this.status = status;
        this.sendNo = sendNo;
        this.bagCount = bagCount;
        this.externalDrawInvNo = externalDrawInvNo;
        this.externalAreaId = externalAreaId;
        this.externalAreaName = externalAreaName;
        this.externalLineId = externalLineId;
        this.externalLineName = externalLineName;
        this.externalLineNo = externalLineNo;
        this.areaId = areaId;
        this.areaName = areaName;
        this.lineId = lineId;
        this.lineName = lineName;
        this.lineNo = lineNo;
        this.pmtStatus = pmtStatus;
        this.abnormalCount = abnormalCount;
        this.smokeBoxLendCount = smokeBoxLendCount;
        this.custPhotoUpdateTime = custPhotoUpdateTime;
        this.custPhotoCount = custPhotoCount;
        this.bagCodes = bagCodes;
        this.orderDetails = orderDetails;
        this.custPhotos = custPhotos;
        this.drawInvId = drawInvId;
        this.selectReason = selectReason;
        this.customReason = customReason;
    }


    @Generated(hash = 94317126)
    public OrderModelInfo() {
    }

    
    public Long getAutoId() {
        return this.autoId;
    }
    public void setAutoId(Long autoId) {
        this.autoId = autoId;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCustId() {
        return this.custId;
    }
    public void setCustId(String custId) {
        this.custId = custId;
    }
    public String getCustName() {
        return this.custName;
    }
    public void setCustName(String custName) {
        this.custName = custName;
    }
    public String getManager() {
        return this.manager;
    }
    public void setManager(String manager) {
        this.manager = manager;
    }
    public String getManagerTel() {
        return this.managerTel;
    }
    public void setManagerTel(String managerTel) {
        this.managerTel = managerTel;
    }
    public String getPwd() {
        return this.pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getNfcCardId() {
        return this.nfcCardId;
    }
    public void setNfcCardId(String nfcCardId) {
        this.nfcCardId = nfcCardId;
    }
    public String getAddr() {
        return this.addr;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }
    public List<HCustPhoneModel> getCustPhones() {
        return this.custPhones;
    }
    public void setCustPhones(List<HCustPhoneModel> custPhones) {
        this.custPhones = custPhones;
    }
    public String getCustLongitude() {
        return this.custLongitude;
    }
    public void setCustLongitude(String custLongitude) {
        this.custLongitude = custLongitude;
    }
    public String getCustLatitude() {
        return this.custLatitude;
    }
    public void setCustLatitude(String custLatitude) {
        this.custLatitude = custLatitude;
    }
    public int getCount() {
        return this.count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public double getPrice() {
        return this.price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getBagCount() {
        return this.bagCount;
    }
    public void setBagCount(int bagCount) {
        this.bagCount = bagCount;
    }
    public String getExternalDrawInvNo() {
        return this.externalDrawInvNo;
    }
    public void setExternalDrawInvNo(String externalDrawInvNo) {
        this.externalDrawInvNo = externalDrawInvNo;
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
    public int getExternalLineNo() {
        return this.externalLineNo;
    }
    public void setExternalLineNo(int externalLineNo) {
        this.externalLineNo = externalLineNo;
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
    public int getLineNo() {
        return this.lineNo;
    }
    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }
    public int getPmtStatus() {
        return this.pmtStatus;
    }
    public void setPmtStatus(int pmtStatus) {
        this.pmtStatus = pmtStatus;
    }
    public int getAbnormalCount() {
        return this.abnormalCount;
    }
    public void setAbnormalCount(int abnormalCount) {
        this.abnormalCount = abnormalCount;
    }
    public int getSmokeBoxLendCount() {
        return this.smokeBoxLendCount;
    }
    public void setSmokeBoxLendCount(int smokeBoxLendCount) {
        this.smokeBoxLendCount = smokeBoxLendCount;
    }
    public String getCustPhotoUpdateTime() {
        return this.custPhotoUpdateTime;
    }
    public void setCustPhotoUpdateTime(String custPhotoUpdateTime) {
        this.custPhotoUpdateTime = custPhotoUpdateTime;
    }
    public int getCustPhotoCount() {
        return this.custPhotoCount;
    }
    public void setCustPhotoCount(int custPhotoCount) {
        this.custPhotoCount = custPhotoCount;
    }
    public List<HOrderBagModel> getBagCodes() {
        return this.bagCodes;
    }
    public void setBagCodes(List<HOrderBagModel> bagCodes) {
        this.bagCodes = bagCodes;
    }
    public List<HOrderDetailModel> getOrderDetails() {
        return this.orderDetails;
    }
    public void setOrderDetails(List<HOrderDetailModel> orderDetails) {
        this.orderDetails = orderDetails;
    }
    public List<HCustPhotoModel> getCustPhotos() {
        return this.custPhotos;
    }
    public void setCustPhotos(List<HCustPhotoModel> custPhotos) {
        this.custPhotos = custPhotos;
    }
    public String getDrawInvId() {
        return this.drawInvId;
    }
    public void setDrawInvId(String drawInvId) {
        this.drawInvId = drawInvId;
    }


    public String getSendNo() {
        return this.sendNo;
    }


    public void setSendNo(String sendNo) {
        this.sendNo = sendNo;
    }


    public String getSelectReason() {
        return this.selectReason;
    }


    public void setSelectReason(String selectReason) {
        this.selectReason = selectReason;
    }


    public String getCustomReason() {
        return this.customReason;
    }


    public void setCustomReason(String customReason) {
        this.customReason = customReason;
    }

}
