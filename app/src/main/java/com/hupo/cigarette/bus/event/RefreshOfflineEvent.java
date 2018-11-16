package com.hupo.cigarette.bus.event;

import com.hupo.cigarette.bus.listener.RefreshOfflineListener;

public class RefreshOfflineEvent {
    private RefreshOfflineListener listener;

    public RefreshOfflineEvent(RefreshOfflineListener listener) {
        this.listener = listener;
    }

    public RefreshOfflineListener getListener() {
        return listener;
    }

    public void setListener(RefreshOfflineListener listener) {
        this.listener = listener;
    }
}
