package com.hupo.cigarette.bean;

import com.huposoft.softs.chuanchuan.platform.app.api.models.HUserModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gemini on 2018/10/26.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean loginFailed = false;
    private boolean loginOnOtherClient = false;
    private HUserModel loginUser = new HUserModel();
    private String curLoginToken = "";
    private String curImei = "";
    private Map<String, String> sysCfgMap = new HashMap();

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isLoginFailed() {
        return loginFailed;
    }

    public void setLoginFailed(boolean loginFailed) {
        this.loginFailed = loginFailed;
    }

    public boolean isLoginOnOtherClient() {
        return loginOnOtherClient;
    }

    public void setLoginOnOtherClient(boolean loginOnOtherClient) {
        this.loginOnOtherClient = loginOnOtherClient;
    }

    public HUserModel getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(HUserModel loginUser) {
        this.loginUser = loginUser;
    }

    public String getCurLoginToken() {
        return curLoginToken;
    }

    public void setCurLoginToken(String curLoginToken) {
        this.curLoginToken = curLoginToken;
    }

    public String getCurImei() {
        return curImei;
    }

    public void setCurImei(String curImei) {
        this.curImei = curImei;
    }

    public Map<String, String> getSysCfgMap() {
        return sysCfgMap;
    }

    public void setSysCfgMap(Map<String, String> sysCfgMap) {
        this.sysCfgMap = sysCfgMap;
    }
}
