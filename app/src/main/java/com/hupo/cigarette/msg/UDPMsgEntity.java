package com.hupo.cigarette.msg;

import com.hupo.cigarette.R;
import com.hupo.cigarette.app.App;
import com.hupo.cigarette.net.Constants;
import com.hupo.cigarette.utils.PreferUtils;
import com.hupo.cigarette.utils.Utils;
import com.huposoft.commons.utils.MD5;

import java.text.SimpleDateFormat;

public class UDPMsgEntity {
    private String ip = Constants.IP;
    private int port = Constants.PORT;
    private String apVersion = Utils.getDeviceVersion();
    private String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
    private String mode = "vmspda";
    private String imei = Utils.getDeviceImei();
    private String token =App.getInstance().getUser()==null?"":App.getInstance().getUser().getCurLoginToken();
    private String pwd = MD5.GetMD5Code(PreferUtils.getString("pwd", ""));
    private String sendUid;
    private String msgUid;
    private String msgType;
    private String contentType = "DEFAULT";
    private String receiverUid;
    private String msg;

    public UDPMsgEntity() {
    }

    public UDPMsgEntity(String token, String sendUid, String msgUid, String msgType, String receiverUid, String msg) {
        this.token = token;
        this.sendUid = sendUid;
        this.msgUid = msgUid;
        this.msgType = msgType;
        this.receiverUid = receiverUid;
        this.msg = msg;

    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getApVersion() {
        return apVersion;
    }

    public void setApVersion(String apVersion) {
        this.apVersion = apVersion;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSendUid() {
        return sendUid;
    }

    public void setSendUid(String sendUid) {
        this.sendUid = sendUid;
    }

    public String getMsgUid() {
        return msgUid;
    }

    public void setMsgUid(String msgUid) {
        this.msgUid = msgUid;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }



    @Override
    public String toString() {
        return String.format(App.getInstance().getString(R.string.upd_msg),ip,port,apVersion,time,mode,imei,token,pwd,sendUid,receiverUid,msgUid,msgType,contentType,msg);
    }

}
