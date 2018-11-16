package com.hupo.cigarette.dao.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HCustPhoneModel;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

public class HCustPhoneModelConverter implements PropertyConverter<List<HCustPhoneModel>, String> {
    @Override
    public List<HCustPhoneModel> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        List<HCustPhoneModel> list_transport = new Gson().fromJson(databaseValue, new TypeToken<List<HCustPhoneModel>>() {
        }.getType());
        return list_transport;
    }

    @Override
    public String convertToDatabaseValue(List<HCustPhoneModel> arrays) {
        if (arrays == null) {
            return null;
        } else {
            return new Gson().toJson(arrays);
        }
    }
}