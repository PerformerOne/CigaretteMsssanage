package com.hupo.cigarette.net;

import com.hupo.cigarette.R;

/**
 * Created by Gemini on 2018/10/26.
 */

public interface Constants {

    //网络请求超时时间毫秒
    int DEFAULT_TIMEOUT = 20000;
    int UDP_PORT = 27788;
    int PORT = 53222;

    //新乡
//    String IP="47.92.49.240";
//    String OSS_PROFIX="xinxiang/cpi/";
//    String cityId = "7FA78A3202964CE08E6BC82BE12609EE";//新乡


    //长治
//    String IP = "47.92.98.15";
//    String OSS_PROFIX = "changzhi/cpi/";
    String cityId = "18C5C0E1108048A58EB8D073382034E0";//长治

//    String IP = "43.242.156.166";//192.168.10.163
    String IP="//192.168.10.163";
    String OSS_PROFIX="changzhi/cpi/";

    //西安
//    String IP="47.92.101.52";
//    String OSS_PROFIX="xian/cpi/";
//    String cityId = "484246EF1C904831831645BF23C9E595";//西安

    String HOST = "http://"+IP+":"+PORT+"/";
    String deviceType = "Android";
    String deviceOSVersion = "";

    String AES_PWD = "hupo!@#123111111";

    String MSG_TYPE_SEND = "send";
    String MSG_TYPE_RECEIVE = "return";

    /**
     * 消息信息与消息内容分隔符
     */
    String MSG_CONTENT_SPLIT = ",,:,,";


    /**
     * 消息分割符
     */
    String MSG_SPLIT = ",";


    String UDP_UID = "c46c643e-fe4d-4038-ba10-8d1738bd29ea";

    int PLACEHOLD_RES = R.mipmap.smokeshop;

    String OSS_ERROR="changzhi/aoi-tmp/";
}
