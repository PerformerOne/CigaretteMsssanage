package com.hupo.cigarette.dao.helper;

import com.google.gson.Gson;
import com.hupo.cigarette.app.App;
import com.hupo.cigarette.dao.db.DrawInvsDetailInfo;
import com.hupo.cigarette.dao.greendao.DrawInvsDetailInfoDao;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HDrawInvModel;

import java.util.List;

public class DrawInvsDetailHelper {
    private DrawInvsDetailInfoDao dao;
    private static DrawInvsDetailHelper helper;

    public static synchronized DrawInvsDetailHelper getInstance() {
        if (helper == null) {
            helper = new DrawInvsDetailHelper();
        }
        return helper;
    }

    private DrawInvsDetailHelper() {
        dao=App.getDaoSession().getDrawInvsDetailInfoDao();
    }

    public DrawInvsDetailInfo getDetail(String no){
        return dao.queryBuilder().where(DrawInvsDetailInfoDao.Properties.Uid.eq(no)).unique();
    }

    public List<DrawInvsDetailInfo> getDetails(){
        return dao.queryBuilder().build().list();
    }

    public void getOnCar(String sendNo, String car, String drawIds){
        String[] split = drawIds.split(",");
        for (String s : split) {
            DrawInvsDetailInfo in=dao.queryBuilder().where(DrawInvsDetailInfoDao.Properties.Uid.eq(s)).unique();
            if (in==null){
                continue;
            }
            in.setSendNo(sendNo);
            in.setCarId(car);
            in.setStatus(2);
            dao.update(in);
        }
    }

    public void getPrepareSend(String sendNo, String car){
        List<DrawInvsDetailInfo> beans=dao.queryBuilder().where(DrawInvsDetailInfoDao.Properties.SendNo.eq(sendNo)).build().list();
        for (DrawInvsDetailInfo in : beans) {
            in.setCarId(car);
            in.setStatus(3);
            dao.update(in);
        }
    }


    public void getPrepareStart(String uid){
        List<DrawInvsDetailInfo> beans=dao.queryBuilder().where(DrawInvsDetailInfoDao.Properties.Uid.eq(uid)).build().list();
        for (DrawInvsDetailInfo in : beans) {
            in.setStatus(4);
            dao.update(in);
        }
    }

    public void cancelGetOnCar(String sendNo){
        List<DrawInvsDetailInfo> detailInfos=dao.queryBuilder().where(DrawInvsDetailInfoDao.Properties.SendNo.eq(sendNo)).build().list();
        for (DrawInvsDetailInfo detailInfo : detailInfos) {
            detailInfo.setStatus(1);
            dao.update(detailInfo);
        }

    }

    public void cancelGetPrepareSend(String sendNo){
        List<DrawInvsDetailInfo> detailInfos=dao.queryBuilder().where(DrawInvsDetailInfoDao.Properties.SendNo.eq(sendNo)).build().list();
        for (DrawInvsDetailInfo detailInfo : detailInfos) {
            detailInfo.setStatus(2);
            dao.update(detailInfo);
        }
    }


    public List<DrawInvsDetailInfo> getOnCarDrawInvs(){
        return dao.queryBuilder().where(DrawInvsDetailInfoDao.Properties.Status.eq(2)).build().list();
    }

    public List<DrawInvsDetailInfo> getPrepareSendDrawInvs(){
        return dao.queryBuilder().where(DrawInvsDetailInfoDao.Properties.Status.eq(3)).build().list();
    }

    public void changeSplit(String uid,HDrawInvModel bean){
        DrawInvsDetailInfo b=new Gson().fromJson(new Gson().toJson(bean),DrawInvsDetailInfo.class);
        DrawInvsDetailInfo unique = dao.queryBuilder().where(DrawInvsDetailInfoDao.Properties.Uid.eq(uid)).build().unique();
        b.setId(unique.getId());
        dao.update(b);
    }

    public void insertSplit(HDrawInvModel bean){
        DrawInvsDetailInfo b=new Gson().fromJson(new Gson().toJson(bean),DrawInvsDetailInfo.class);
        dao.insert(b);
    }
}
