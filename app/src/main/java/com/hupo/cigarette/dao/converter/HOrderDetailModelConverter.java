package com.hupo.cigarette.dao.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HOrderDetailModel;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

public class HOrderDetailModelConverter implements PropertyConverter<List<HOrderDetailModel>, String> {
    @Override
    public List<HOrderDetailModel> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        List<HOrderDetailModel> list_transport = new Gson().fromJson(databaseValue, new TypeToken<List<HOrderDetailModel>>() {
        }.getType());
        return list_transport;
    }

    @Override
    public String convertToDatabaseValue(List<HOrderDetailModel> arrays) {
        if (arrays == null) {
            return null;
        } else {
            return new Gson().toJson(arrays);
        }
    }
}