package com.hupo.cigarette.dao.helper;

import com.google.gson.Gson;
import com.hupo.cigarette.app.App;
import com.hupo.cigarette.dao.db.CarInfo;
import com.hupo.cigarette.dao.greendao.CarInfoDao;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.CarAdapter;

public class CarInfoHelper {
    private CarInfoDao dao;
    private static CarInfoHelper helper;

    public static synchronized CarInfoHelper getInstance() {
        if (helper == null) {
            helper = new CarInfoHelper();
        }
        return helper;
    }

    private CarInfoHelper() {
        dao=App.getDaoSession().getCarInfoDao();
    }


    public void insertCar(CarAdapter car){
        dao.deleteAll();
        CarInfo i=new Gson().fromJson(new Gson().toJson(car),CarInfo.class);
        dao.insert(i);
    }

    public void deleteCar(){
        dao.deleteAll();
    }

    public CarInfo getCar(){
        return dao.queryBuilder().unique();
    }
}
