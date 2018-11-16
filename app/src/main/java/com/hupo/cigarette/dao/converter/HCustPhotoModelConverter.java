package com.hupo.cigarette.dao.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HCustPhotoModel;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

public class HCustPhotoModelConverter implements PropertyConverter<List<HCustPhotoModel>, String> {
    @Override
    public List<HCustPhotoModel> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        List<HCustPhotoModel> list_transport = new Gson().fromJson(databaseValue, new TypeToken<List<HCustPhotoModel>>() {
        }.getType());
        return list_transport;
    }

    @Override
    public String convertToDatabaseValue(List<HCustPhotoModel> arrays) {
        if (arrays == null) {
            return null;
        } else {
            return new Gson().toJson(arrays);
        }
    }
}