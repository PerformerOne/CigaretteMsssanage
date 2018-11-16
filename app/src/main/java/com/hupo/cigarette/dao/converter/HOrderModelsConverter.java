package com.hupo.cigarette.dao.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HOrderModel;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

public class HOrderModelsConverter implements PropertyConverter<List<HOrderModel>, String> {
    @Override
    public List<HOrderModel> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }

        List<HOrderModel> list_transport = new Gson().fromJson(databaseValue,new TypeToken<List<HOrderModel>>(){}.getType());
        return list_transport;
    }

    @Override
    public String convertToDatabaseValue(List<HOrderModel> arrays) {
        if (arrays == null) {
            return null;
        } else {
            return new Gson().toJson(arrays);
        }

    }
}