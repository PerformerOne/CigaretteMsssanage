package com.hupo.cigarette.msg;

public interface UDPMsgListener {
    void receiveMsg(UDPMsgEntity msg);
    void onFinish();
    void onError(int err,String msg);
}
