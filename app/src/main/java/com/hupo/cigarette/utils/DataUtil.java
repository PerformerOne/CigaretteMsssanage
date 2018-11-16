package com.hupo.cigarette.utils;

import com.hupo.cigarette.app.App;
import com.hupo.cigarette.dao.db.DrawInvsDetailInfo;
import com.hupo.cigarette.dao.db.OrderModelInfo;
import com.hupo.cigarette.dao.db.SendNoAdapterInfo;
import com.hupo.cigarette.dao.greendao.DrawInvsDetailInfoDao;
import com.hupo.cigarette.dao.greendao.OrderModelInfoDao;
import com.hupo.cigarette.dao.greendao.SendNoAdapterInfoDao;
import com.hupo.cigarette.dao.helper.OrderDetailInfoHelper;

import java.util.List;

public class DataUtil {
    private static DataUtil u;

    private double totalP = 0;
    private double currentP = 0;
    private int expOrder = 0;
    private int smokeCount = 0;
    private int shopCount = 0;
    private int progress = 0;
    private int currentPhoto = 0;
    private int totalPhoto = 0;

    public synchronized static DataUtil getInstance() {
        if (u == null) {
            u = new DataUtil();
        }
        return u;
    }

    public DataUtil synData() {
        totalP = 0;
        currentP = 0;
        expOrder = 0;
        smokeCount = 0;
        shopCount = 0;
        progress = 0;
        currentPhoto = 0;
        totalPhoto = 0;

        SendNoAdapterInfoDao sendDao = App.getDaoSession().getSendNoAdapterInfoDao();
        DrawInvsDetailInfoDao drawDao = App.getDaoSession().getDrawInvsDetailInfoDao();
        OrderModelInfoDao orderDao = App.getDaoSession().getOrderModelInfoDao();


        List<SendNoAdapterInfo> sends = sendDao.queryBuilder().list();
        if (sends != null && sends.size() > 0) {
            SendNoAdapterInfo send = sends.get(0);
            List<DrawInvsDetailInfo> list = drawDao.queryBuilder().where(DrawInvsDetailInfoDao.Properties.SendNo.eq(send.getNo())).build().list();
            if (list != null) {
                for (DrawInvsDetailInfo d : list) {
                    List<OrderModelInfo> orders = orderDao.queryBuilder().where(OrderModelInfoDao.Properties.DrawInvId.eq(d.getNo())).build().list();
                    if (orders != null) {
                        for (OrderModelInfo o : orders) {
                            smokeCount += o.getCount();
                        }
                    } else {
                        totalP += 0;
                    }
                }
                List<OrderModelInfo> exp = OrderDetailInfoHelper.getInstance().getExpOrder();
                List<OrderModelInfo> i = OrderDetailInfoHelper.getInstance().getNormalOrder();
                expOrder = exp.size();
                totalP += expOrder;
                currentP = i == null ? 0 : i.size();
                progress = (int) (totalP * 100 / currentP);
            }
        }
        return u;
    }

    public int getExpOrder() {
        return expOrder;
    }

    public int getSmokeCount() {
        return smokeCount;
    }

    public int getShopCount() {
        return shopCount;
    }

    public int getProgress() {
        return progress;
    }

    public int getCurrentPhoto() {
        return currentPhoto;
    }

    public int getTotalPhoto() {
        return totalPhoto;
    }
}
