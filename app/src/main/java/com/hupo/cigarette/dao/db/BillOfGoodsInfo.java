package com.hupo.cigarette.dao.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Gemini on 2018/11/9.
 *
 * 领货单基础信息
 */
@Entity
public class BillOfGoodsInfo {

    @Id(autoincrement = true)
    private Long id;
    private String uuid;
    private String billNo;//领货单单号
    /**
     * 当前没有正在配送的领货单情况，服务器领货单不为4，本地没有服务器对应领货单时，下载领货单概要信息
     * 当本地领货单状态比服务器状态小，但是状态小于4时，更新本地领货单信息为服务器领货单
     * 当服务器领货单状态为4，本地小于4时，删除本地领货单信息
     */
    private int status;//状态
    private String lastDate;//最后提交时间
    @Generated(hash = 1554990821)
    public BillOfGoodsInfo(Long id, String uuid, String billNo, int status,
            String lastDate) {
        this.id = id;
        this.uuid = uuid;
        this.billNo = billNo;
        this.status = status;
        this.lastDate = lastDate;
    }
    @Generated(hash = 343946699)
    public BillOfGoodsInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUuid() {
        return this.uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getBillNo() {
        return this.billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getLastDate() {
        return this.lastDate;
    }
    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

}
