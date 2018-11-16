package com.hupo.cigarette.dao.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HOrderBagModel;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

public class HOrderBagModelConverter  implements PropertyConverter<List<HOrderBagModel>, String> {
    @Override
    public List<HOrderBagModel> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        List<HOrderBagModel> list_transport = new Gson().fromJson(databaseValue, new TypeToken<List<HOrderBagModel>>() {
        }.getType());
        return list_transport;
    }

    @Override
    public String convertToDatabaseValue(List<HOrderBagModel> arrays) {
        if (arrays == null) {
            return null;
        } else {
            return new Gson().toJson(arrays);
        }
    }
}