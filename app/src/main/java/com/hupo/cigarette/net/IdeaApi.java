package com.hupo.cigarette.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hupo.cigarette.utils.LogUtils;
import com.hupo.cigarette.utils.NetworkUtils;
import com.hupo.cigarette.utils.Utils;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Gemini on 2018/2/7.
 */

public class IdeaApi {
    private Retrofit retrofit;
    private IdeaApiService service;
//
//    private static String ip;
//    private static String address;


    private IdeaApi() {
//        private IdeaApi(Context context) {
//        mContext = context;
       /* HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);*/

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                try {
                    LogUtils.e("OKHttp-----", URLDecoder.decode(message, "utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.e("OKHttp-----", message);
                }
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        File cacheFile = new File(Utils.getContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(new HttpHeaderInterceptor())
                .addNetworkInterceptor(new HttpCacheInterceptor())
               // .sslSocketFactory(SslContextFactory.getSSLSocketFactoryForTwoWay())   https认证 如果要使用https且为自定义证书 可以去掉这两行注释，并自行配制证书。
               // .hostnameVerifier(new SafeHostnameVerifier())
                .cache(cache)
                .build();

        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .serializeNulls()
                .create();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .baseUrl("http://"+ip+":"+address+"/BaseFrame/")
                .baseUrl(Constants.HOST)
                .build();
        service = retrofit.create(IdeaApiService.class);
    }


    //  添加请求头的拦截器
    private class HttpHeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            //  配置请求头
            String accessToken = "token";
            String tokenType = "tokenType";
            Request request = chain.request().newBuilder()
                    .header("app_key","appId")
                    .header("Authorization", tokenType + " " + accessToken)
                    .header("Content-Type", "application/json")
                    .addHeader("Connection", "close")
                    .build();
            return chain.proceed(request);
        }
    }

    //  创建单例
    private static class SingletonHolder {
        private static final IdeaApi INSTANCE =new IdeaApi();

    }
    public static IdeaApiService getApiService() {
//        SharedPreferences preferences= App.getCtx().getSharedPreferences("base", Context.MODE_PRIVATE);
//        ip=preferences.getString("ip","defaultIp");
//        address=preferences.getString("address","defaultAddress");
//        return SingletonHolder.INSTANCE.service;
        return new IdeaApi().service;
    }

    class HttpCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            Response originalResponse = chain.proceed(request);

            if (NetworkUtils.isConnected()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }

    private class SafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            if (Constants.IP.equals(hostname)) {//校验hostname是否正确，如果正确则建立连接
                return true;
            }
//            if (ip.equals(hostname)) {//校验hostname是否正确，如果正确则建立连接
//                return true;
//            }
            return false;
        }
    }
}
