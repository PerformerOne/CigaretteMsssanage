package com.hupo.cigarette.dao.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HSendNoModel;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

public class HSendNoModelsConverter implements PropertyConverter<List<HSendNoModel>, String> {
    @Override
    public List<HSendNoModel> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        List<HSendNoModel> list_transport =new Gson().fromJson(databaseValue,new TypeToken<List<HSendNoModel>>(){}.getType());
        return list_transport;
    }

    @Override
    public String convertToDatabaseValue(List<HSendNoModel> arrays) {
        if (arrays == null) {
            return null;
        } else {
            return new Gson().toJson(arrays);
        }

    }
}