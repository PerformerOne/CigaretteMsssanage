package com.hupo.cigarette.bus.event;

import com.hupo.cigarette.msg.UDPSendListener;

public class SendUDPMsgEvent {
    private String msg;
    private UDPSendListener listener;

    public SendUDPMsgEvent(String msg, UDPSendListener listener) {
        this.msg = msg;
        this.listener = listener;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UDPSendListener getListener() {
        return listener;
    }

    public void setListener(UDPSendListener listener) {
        this.listener = listener;
    }
}
