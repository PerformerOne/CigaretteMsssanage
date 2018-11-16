package com.hupo.cigarette.dao.helper;

import com.hupo.cigarette.app.App;
import com.hupo.cigarette.dao.db.OrderModelInfo;
import com.hupo.cigarette.dao.greendao.OrderModelInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

public class OrderDetailInfoHelper {

    private OrderModelInfoDao dao;
    private static OrderDetailInfoHelper helper;

    public static synchronized OrderDetailInfoHelper getInstance() {
        if (helper == null) {
            helper = new OrderDetailInfoHelper();
        }
        return helper;
    }

    private OrderDetailInfoHelper() {
        dao=App.getDaoSession().getOrderModelInfoDao();
    }

    public List<OrderModelInfo> getDetail(String drawId){
        dao=App.getDaoSession().getOrderModelInfoDao();
        return dao.queryBuilder().where(OrderModelInfoDao.Properties.DrawInvId.eq(drawId),OrderModelInfoDao.Properties.Status.notEq(5)).orderAsc(OrderModelInfoDao.Properties.LineNo).build().list();
    }

    public List<OrderModelInfo> getDetailInfo(String orderId){
        dao=App.getDaoSession().getOrderModelInfoDao();
        return dao.queryBuilder().where(OrderModelInfoDao.Properties.CustId.eq(orderId)).build().list();
    }

    //根据订单号或者商户名称查找商户列表
    public List<OrderModelInfo> getDetailByOrderIdOrName(String drawId, String orderId, String manager){
        dao=App.getDaoSession().getOrderModelInfoDao();
        QueryBuilder<OrderModelInfo> b = dao.queryBuilder();
        WhereCondition or = b.or(OrderModelInfoDao.Properties.Id.like("%"+orderId+"%"), OrderModelInfoDao.Properties.Manager.like("%"+manager+"%"));
        return dao.queryBuilder().where(OrderModelInfoDao.Properties.DrawInvId.eq(drawId),or).build().list();
    }


    public void updateMerchant(String orderId,String drawId){
        String[] split = orderId.split(",");
        for (String s : split) {
            OrderModelInfo info = dao.queryBuilder().where(OrderModelInfoDao.Properties.Id.eq(s)).unique();
            if (info!=null){
                info.setDrawInvId(drawId);
                dao.update(info);
            }
        }
    }

    public List<OrderModelInfo> getNormalOrder(){
        return dao.queryBuilder().where(OrderModelInfoDao.Properties.Status.eq(4)).build().list();
    }

    public List<OrderModelInfo> getExpOrder(){
        return dao.queryBuilder().where(OrderModelInfoDao.Properties.Status.eq(5)).build().list();
    }

    public void getErrorOrder(String orderId,String selectReason,String customReason){
        OrderModelInfo modelInfo=dao.queryBuilder().where(OrderModelInfoDao.Properties.CustId.eq(orderId)).unique();
        if (modelInfo != null){
            modelInfo.setStatus(5);
            modelInfo.setSelectReason(selectReason);
            modelInfo.setCustomReason(customReason);
            dao.update(modelInfo);
        }
    }

    public void getOnCarOfExp(String sendNo, String orderIds){
        String[] split = orderIds.split(",");
        for (String s : split) {
            OrderModelInfo info = dao.queryBuilder().where(OrderModelInfoDao.Properties.Id.eq(s)).unique();
            if (info!=null){
                info.setSendNo(sendNo);
                dao.update(info);
            }
        }
    }

}
