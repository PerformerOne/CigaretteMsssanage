package com.hupo.cigarette.bus.listener;

public interface RefreshOfflineListener {

    void progress(int cur,int total,String msg);

    /**
     *
     * @param i 1,获取当前登录用户的所有领货单单号  2,获取配送单列表 3,获取当前登录用户名下的异常订单
     */
    void success(int i);
    void error(int i,int errorCode,String errMsg);
    void finish();

}
