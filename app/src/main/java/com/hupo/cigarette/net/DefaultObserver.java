package com.hupo.cigarette.net;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.hupo.cigarette.R;
import com.hupo.cigarette.utils.CommonDialogUtils;
import com.hupo.cigarette.utils.LogUtils;
import com.hupo.cigarette.utils.ToastUtils;
import com.huposoft.commons.json.JsonAdapter;
import com.huposoft.commons.utils.AES;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import org.json.JSONException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.hupo.cigarette.net.DefaultObserver.ExceptionReason.CONNECT_ERROR;
import static com.hupo.cigarette.net.DefaultObserver.ExceptionReason.CONNECT_TIMEOUT;
import static com.hupo.cigarette.net.DefaultObserver.ExceptionReason.PARSE_ERROR;
import static com.hupo.cigarette.net.DefaultObserver.ExceptionReason.UNKNOWN_ERROR;


/**
 * Created by Gemini on 2018/10/25.
 */

public abstract class DefaultObserver<T extends JsonAdapter> implements Observer<T> {

    private CommonDialogUtils dialogUtils;
    private T resolvedJsonAdapter;
    private Class<T> jsonAdapterClass;

    public DefaultObserver(Activity acti, Class<T> jsonAdapterClass) {
        this.jsonAdapterClass = jsonAdapterClass;
        if (acti!=null){
            dialogUtils=new CommonDialogUtils();
            dialogUtils.showProgress(acti);
        }
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    public T getResolvedJsonAdapter() {
        return resolvedJsonAdapter;
    }

    @Override
    public void onNext(T jsonAdapter) {
        String val = jsonAdapter.getVal();
        String decryptVal = AES.decrypt(Constants.AES_PWD, val);
        LogUtils.e("Retrofit", decryptVal+"");
        if (null == decryptVal || "".equals(decryptVal)) {
            try {
                //转换失败
                Object tmpObj = jsonAdapterClass.newInstance();
                T jsonAdapter1 = (T)tmpObj;
                jsonAdapter1.setMsg("", false);
                onFail(jsonAdapter1);
                dismissProgress();
                return;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        Gson gson = new Gson();
        resolvedJsonAdapter = gson.fromJson(decryptVal, jsonAdapterClass);
        if (resolvedJsonAdapter.isSuccess()) {
            onSuccess(resolvedJsonAdapter);
        } else {
            onFail(resolvedJsonAdapter);
        }
        dismissProgress();
    }

    private void dismissProgress(){
        if(dialogUtils!=null){
            dialogUtils.dismissProgress();
        }
    }

    @Override
    public void onError(Throwable e) {
        LogUtils.e("Retrofit", AES.decrypt(Constants.AES_PWD, e.toString())+"");
        dismissProgress();
        if (e instanceof HttpException) {     //   HTTP错误
            onException(ExceptionReason.BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {   //   连接错误
            onException(CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {   //  连接超时
            onException(CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {   //  解析错误
            onException(PARSE_ERROR);
        } else {
            onException(UNKNOWN_ERROR);
        }
    }

    @Override
    public void onComplete() {
    }

    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    abstract public void onSuccess(T response);

    /**
     * 服务器返回数据，但响应码不为200
     *
     * @param response 服务器返回的数据
     */
    public void onFail(T response) {
        String message = response.getMsg();
        if (TextUtils.isEmpty(message)) {
            ToastUtils.show(R.string.response_return_error);
        } else {
            ToastUtils.show(message);
        }
    }

    /**
     * 请求异常
     *
     * @param reason
     */
    public void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                ToastUtils.show(R.string.connect_error, Toast.LENGTH_SHORT);
                break;

            case CONNECT_TIMEOUT:
                ToastUtils.show(R.string.connect_timeout, Toast.LENGTH_SHORT);
                break;

            case BAD_NETWORK:
                ToastUtils.show(R.string.bad_network, Toast.LENGTH_SHORT);
                break;

            case PARSE_ERROR:
                ToastUtils.show(R.string.parse_error, Toast.LENGTH_SHORT);
                break;

            case UNKNOWN_ERROR:
            default:
                ToastUtils.show(R.string.unknown_error, Toast.LENGTH_SHORT);
                break;
        }
    }

    /**
     * 请求网络失败原因
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
    }
}
