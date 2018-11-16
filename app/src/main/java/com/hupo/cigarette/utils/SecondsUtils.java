package com.hupo.cigarette.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gemini on 2018/11/2.
 *
 * 将秒转换为时间
 */
public class SecondsUtils {

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /**
     * 返回日时分秒
     * @param second
     * @return
     */
    public static String secondToTime(long second) {
        long days = second / 86400;//转换天数
        second = second % 86400;//剩余秒数
        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        if (0 < days){
            if (second > 0){
                return days + "天"+hours+"时"+(minutes+1)+"分";
            }
            return days + "天"+hours+"时"+minutes+"分";
        }else {
            if (second > 0){
                return hours+"时"+(minutes+1)+"分";
            }
            return hours+"时"+minutes+"分";
        }
    }

    public static String getDate() {
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);

    }

    public static String getDateForAll() {
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return sdf.format(date);

    }
}
