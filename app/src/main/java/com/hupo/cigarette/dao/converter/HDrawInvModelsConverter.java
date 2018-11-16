package com.hupo.cigarette.dao.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HDrawInvModel;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

public class HDrawInvModelsConverter implements PropertyConverter<List<HDrawInvModel>, String> {
    @Override
    public List<HDrawInvModel> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        List<HDrawInvModel> list_transport = new Gson().fromJson(databaseValue,new TypeToken<List<HDrawInvModel>>(){}.getType());
        return list_transport;
    }

    @Override
    public String convertToDatabaseValue(List<HDrawInvModel> arrays) {
        if (arrays == null) {
            return null;
        } else {
            return new Gson().toJson(arrays);
        }

    }
}
