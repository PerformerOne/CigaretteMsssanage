package com.hupo.cigarette.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.hupo.cigarette.app.App;
import com.hupo.cigarette.dao.helper.BillOfGoodsInfoHelper;
import com.hupo.cigarette.dao.helper.DrawInvsDetailHelper;
import com.hupo.cigarette.dao.helper.OrderDetailInfoHelper;
import com.hupo.cigarette.oss.OSSFileController;

public class InitService extends IntentService {
    public InitService() {
        super(App.getInstance().getPackageName() + "initService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        new Thread(()->{
            BillOfGoodsInfoHelper.getInstance();
            DrawInvsDetailHelper.getInstance();
            OrderDetailInfoHelper.getInstance();
            OSSFileController.getController();
            UDPMsgService.startUDPService(this);
        }).start();
    }

    public static void startIntentService(Context mContext) {
        Intent intent = new Intent(mContext, InitService.class);
        mContext.startService(intent);
    }
}
