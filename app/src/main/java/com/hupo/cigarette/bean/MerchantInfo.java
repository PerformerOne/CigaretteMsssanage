package com.hupo.cigarette.bean;

import java.io.Serializable;

/**
 * Created by Gemini on 2018/11/8.
 *
 * 用来给商户列表传值
 */
public class MerchantInfo implements Serializable {
    private int type;//用来判断是否显示底部布局
    private String uid;//领货单id
    private String sendNo;//领货单号

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSendNo() {
        return sendNo;
    }

    public void setSendNo(String sendNo) {
        this.sendNo = sendNo;
    }
}
