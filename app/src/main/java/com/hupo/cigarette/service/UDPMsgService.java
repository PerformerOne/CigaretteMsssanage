package com.hupo.cigarette.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.hupo.cigarette.app.App;
import com.hupo.cigarette.bus.RxBus;
import com.hupo.cigarette.bus.event.RefreshOfflineEvent;
import com.hupo.cigarette.bus.event.SendUDPMsgEvent;
import com.hupo.cigarette.dao.helper.RefreshHelper;
import com.hupo.cigarette.location.LocationManager;
import com.hupo.cigarette.msg.UDPClient;
import com.hupo.cigarette.msg.UDPMsgEntity;
import com.hupo.cigarette.msg.UDPMsgListener;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UDPMsgService extends Service {

    private UDPClient client;
    private LocationManager locationManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RefreshHelper.getInstance();
        client = new UDPClient.Builder().listener(new UDPMsgListener() {
            @Override
            public void receiveMsg(UDPMsgEntity msg) {
                switch (msg.getMsgType()){

                }
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onError(int err, String msg) {

            }
        }).build();

        locationManager=new LocationManager(location -> {
            //定位回调
            if (location != null&&location.getErrorCode() == 0) {
                App.getInstance().setLocation(location);
                RxBus.getInstance().post(location);
            }
        });
        locationManager.startLocation();
        initRxBus();
    }

    @SuppressLint("CheckResult")
    private void initRxBus() {
        //发送UDP
        RxBus.getInstance()
                .toObservable(SendUDPMsgEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(eventMsg -> {
                    client.sendMsg(eventMsg);
                });

        //刷新数据
        RxBus.getInstance()
                .toObservableSticky(RefreshOfflineEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    RxBus.getInstance().removeStickyEvent(RefreshOfflineEvent.class);
                    refreshData(event);
                });

    }

    private void refreshData(RefreshOfflineEvent event) {
        RefreshHelper.getInstance().refresh(event.getListener());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        client.finish();
        locationManager.destroy();
    }

    public static void startUDPService(Context mContext) {
        Intent intent = new Intent(mContext, UDPMsgService.class);
        mContext.startService(intent);
    }
}
