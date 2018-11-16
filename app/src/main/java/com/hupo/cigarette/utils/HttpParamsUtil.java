package com.hupo.cigarette.utils;

import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hupo.cigarette.app.App;
import com.hupo.cigarette.bean.User;
import com.hupo.cigarette.net.Constants;
import com.huposoft.commons.utils.AES;
import com.huposoft.commons.utils.MD5;

import java.text.SimpleDateFormat;
import java.util.Map;

public class HttpParamsUtil {

    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_IMEI = "imei";
    public static final String KEY_USERUID = "userUid";
    public static final String KEY_PWD = "pwd";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_ACCOUNT = "account";
    public static final String KEY_DEVICETYPE = "deviceType";
    public static final String KEY_DEVICE_OS_VERSION = "deviceOSVersion";
    public static final String KEY_CITYID = "cityId";


    public static String setParam(Map<String, String> p) {
        User user = App.getInstance().getUser();
        Uri.Builder builder = Uri.parse("").buildUpon();

        for (String s : p.keySet()) {
            builder.appendQueryParameter(s, p.get(s));
        }
        builder.appendQueryParameter(KEY_TIMESTAMP, new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        builder.appendQueryParameter(KEY_IMEI, Utils.getDeviceImei());
        builder.appendQueryParameter(KEY_CITYID, Constants.cityId);
        if (user == null) {
            return encryptString(builder.toString().substring(1, builder.toString().length()));
        }
        builder.appendQueryParameter(KEY_USERUID, user.getLoginUser().getUuid());
        builder.appendQueryParameter(KEY_TOKEN, user.getCurLoginToken());
        builder.appendQueryParameter(KEY_PWD, MD5.GetMD5Code(PreferUtils.getString("pwd", "")));
        return encryptString(builder.toString().substring(1, builder.toString().length()));
    }

    public static String setParams(Map<String, Object> p) {
        User user = App.getInstance().getUser();
        Uri.Builder builder = Uri.parse("").buildUpon();
        builder.appendQueryParameter(KEY_TIMESTAMP, new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        builder.appendQueryParameter(KEY_IMEI, Utils.getDeviceImei());
        builder.appendQueryParameter(KEY_CITYID, Constants.cityId);
        String a = builder.toString().substring(1, builder.toString().length());
        String c = "";
        for (String s : p.keySet()) {
            if (p.get(s) instanceof String){
                c += "&" + s + "=" +  p.get(s);
            }else{
                c += "&" + s + "=" + new Gson().toJson(p.get(s));

            }

        }
        if (user == null) {
            return encryptString(a + c);
        }
        builder.appendQueryParameter(KEY_USERUID, user.getLoginUser().getUuid());
        builder.appendQueryParameter(KEY_TOKEN, user.getCurLoginToken());
        if (!p.containsKey(KEY_PWD)) {
            builder.appendQueryParameter(KEY_PWD, MD5.GetMD5Code(PreferUtils.getString("pwd", "")));
        }
        String b = builder.toString();
        String d = b + c;
        return encryptString(d.substring(1, d.length()));
    }

    public static String decryptString(String p) {
        if (TextUtils.isEmpty(p))
            return null;
        return AES.decrypt(Constants.AES_PWD, p);
    }

    public static String encryptString(String p) {
        return AES.encrypt(Constants.AES_PWD, p);
    }

}
