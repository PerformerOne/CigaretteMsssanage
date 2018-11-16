package com.hupo.cigarette.msg;

public interface UDPSendListener {
    void success(UDPMsgEntity entity);
    void faild();
}
