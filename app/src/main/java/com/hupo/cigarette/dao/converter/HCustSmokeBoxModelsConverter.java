package com.hupo.cigarette.dao.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HCustSmokeBoxModel;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

public class HCustSmokeBoxModelsConverter implements PropertyConverter<List<HCustSmokeBoxModel>, String> {
    @Override
    public List<HCustSmokeBoxModel> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        List<HCustSmokeBoxModel> list_transport = new Gson().fromJson(databaseValue,new TypeToken<List<HCustSmokeBoxModel>>(){}.getType());
        return list_transport;
    }

    @Override
    public String convertToDatabaseValue(List<HCustSmokeBoxModel> arrays) {
        if (arrays == null) {
            return null;
        } else {
            return new Gson().toJson(arrays);
        }
    }
}
