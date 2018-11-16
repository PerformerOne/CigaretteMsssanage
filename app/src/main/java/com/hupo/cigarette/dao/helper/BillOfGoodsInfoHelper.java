package com.hupo.cigarette.dao.helper;

import com.hupo.cigarette.app.App;
import com.hupo.cigarette.dao.db.BillOfGoodsInfo;
import com.hupo.cigarette.dao.greendao.BillOfGoodsInfoDao;

import java.util.List;

public class BillOfGoodsInfoHelper {

    private BillOfGoodsInfoDao dao;
    private static BillOfGoodsInfoHelper helper;

    public static synchronized BillOfGoodsInfoHelper getInstance() {
        if (helper == null) {
            helper = new BillOfGoodsInfoHelper();
        }
        return helper;
    }

    private BillOfGoodsInfoHelper() {
        dao=App.getDaoSession().getBillOfGoodsInfoDao();
    }

    public List<BillOfGoodsInfo> getBill(){
        return dao.queryBuilder().build().list();
    }

}
