package com.hupo.cigarette.dao.helper;

import com.hupo.cigarette.app.App;
import com.hupo.cigarette.dao.db.SendNoAdapterInfo;
import com.hupo.cigarette.dao.greendao.SendNoAdapterInfoDao;

public class SendNoInfoHelper {

    private SendNoAdapterInfoDao dao;
    private static SendNoInfoHelper helper;

    public static synchronized SendNoInfoHelper getInstance() {
        if (helper == null) {
            helper = new SendNoInfoHelper();
        }
        return helper;
    }

    private SendNoInfoHelper() {
        dao=App.getDaoSession().getSendNoAdapterInfoDao();
    }

    public SendNoAdapterInfo getDetail(){
        return dao.queryBuilder().build().unique();
    }

    public long getDetailCount(){
        return dao.queryBuilder().count();
    }


    public void insertSend(String sendNo,String carId,String carNum){
        SendNoAdapterInfo i=new SendNoAdapterInfo();
        i.setCarId(carId);
        i.setCarNum(carNum);
        i.setNo(sendNo);
        dao.insert(i);
    }

    public void deleteSend(String sendNo){
        SendNoAdapterInfo unique = dao.queryBuilder().where(SendNoAdapterInfoDao.Properties.No.eq(sendNo)).unique();
        if (unique!=null){
            dao.delete(unique);
        }
    }
}
