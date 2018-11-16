package com.hupo.cigarette.dao.converter;

import com.google.gson.Gson;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HSendNoModel;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HSendNoModelConverter implements PropertyConverter<HSendNoModel, String> {
    @Override
    public HSendNoModel convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        return new Gson().fromJson(databaseValue, HSendNoModel.class);
    }

    @Override
    public String convertToDatabaseValue(HSendNoModel entityProperty) {
        if (entityProperty == null) {
            return null;
        }
        return new Gson().toJson(entityProperty);
    }
}