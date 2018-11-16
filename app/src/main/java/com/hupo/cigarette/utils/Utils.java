package com.hupo.cigarette.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.hupo.cigarette.app.App;
import com.hupo.cigarette.msg.UDPMsgEntity;

import static android.content.Context.TELEPHONY_SERVICE;
import static com.hupo.cigarette.net.Constants.MSG_CONTENT_SPLIT;
import static com.hupo.cigarette.net.Constants.MSG_SPLIT;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 16/12/08
 *     desc  : Utils初始化相关
 * </pre>
 */
public class Utils {

    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceImei() {
        TelephonyManager TelephonyMgr = (TelephonyManager) App.getCtx().getSystemService(TELEPHONY_SERVICE);
        return TelephonyMgr.getDeviceId();
    }

    /**
     * 获取versionName
     * @return
     */
    public static String getDeviceVersion() {
        try {
            PackageInfo packageInfo = App.getInstance().getPackageManager()
                    .getPackageInfo(App.getInstance().getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * UDP消息格式化
     */
    public static UDPMsgEntity udpMsgFormat(String msg){
        if (TextUtils.isEmpty(msg)){
            return null;
        }

        String[] msgInfo = msg.split(MSG_CONTENT_SPLIT);
        if (msgInfo.length != 2) {
            return null;
        }
        String[] msgDescribeInfos = msgInfo[0].split(MSG_SPLIT);
        if (msgDescribeInfos.length < 12) {
            return null;
        }



        String[] describeInfos = msgInfo[0].split(",");
        if (describeInfos.length < 12) {
            return null;
        }

        String[] addr=describeInfos[0].split(":");

        String content = msgInfo[1];
        if (content.indexOf(",:,")!=-1){
            content=content.split(",:,")[1];
        }


        UDPMsgEntity entity=new UDPMsgEntity();
        entity.setApVersion(msgDescribeInfos[1]);
        entity.setTime(msgDescribeInfos[2]);
        entity.setContentType(msgDescribeInfos[3]);
        entity.setSendUid(msgDescribeInfos[7]);
        entity.setReceiverUid(msgDescribeInfos[8]);
        entity.setMsgUid(msgDescribeInfos[9]);
        entity.setMsgType(msgDescribeInfos[10]);
        entity.setContentType(msgDescribeInfos[11]);
        entity.setMsg(content);
        entity.setIp(addr[0]);
        entity.setPort(Integer.valueOf(addr[1]));
        return entity;
    }
}