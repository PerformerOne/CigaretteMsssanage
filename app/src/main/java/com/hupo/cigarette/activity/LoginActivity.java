package com.hupo.cigarette.activity;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.hupo.cigarette.MainActivity;
import com.hupo.cigarette.R;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.bean.User;
import com.hupo.cigarette.net.Constants;
import com.hupo.cigarette.net.DefaultObserver;
import com.hupo.cigarette.net.IdeaApi;
import com.hupo.cigarette.utils.HttpParamsUtil;
import com.hupo.cigarette.utils.IntentUtils;
import com.hupo.cigarette.utils.LogUtils;
import com.hupo.cigarette.utils.PreferUtils;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.utils.ToastUtils;
import com.hupo.cigarette.utils.Utils;
import com.huposoft.commons.utils.MD5;
import com.huposoft.softs.chuanchuan.platform.app.api.LoginResultAdapter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gemini on 2018/10/26.
 *
 * 登录
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_login_username) EditText loginUserNameEt;//用户名
    @BindView(R.id.et_login_password) EditText loginPasswordEt;//密码

    @Override
    public int intiLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        boolean isLogin = PreferUtils.getBoolean("isLogin",false);
        if (isLogin){
            IntentUtils.startAty(LoginActivity.this,MainActivity.class);
            finish();
        }

        loginUserNameEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    loginUserNameEt.setCursorVisible(true);// 再次点击显示光标
                }
                return false;
            }
        });

        loginPasswordEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    loginPasswordEt.setCursorVisible(true);// 再次点击显示光标
                }
                return false;
            }
        });

    }

    @OnClick({R.id.btn_login})
    public void OnClick(View view){
        switch (view.getId()){
            //登录
            case R.id.btn_login:
                login();
                break;
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    //登录
    private void login() {
        String userName = loginUserNameEt.getText().toString();
        final String password = loginPasswordEt.getText().toString();

        Map<String,String> map = new HashMap<>();
        map.put(HttpParamsUtil.KEY_ACCOUNT,userName);
        map.put(HttpParamsUtil.KEY_PWD,MD5.GetMD5Code(password));
        map.put(HttpParamsUtil.KEY_DEVICETYPE,Constants.deviceType);
        map.put(HttpParamsUtil.KEY_DEVICE_OS_VERSION,Utils.getDeviceVersion());
        String s = HttpParamsUtil.setParam(map);
        LogUtils.e(HttpParamsUtil.decryptString(s));
        IdeaApi.getApiService()
                .login(s)
                .compose(this.<LoginResultAdapter>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<LoginResultAdapter>(this, LoginResultAdapter.class) {
                    @Override
                    public void onSuccess(LoginResultAdapter jsonAdapter) {
                        User user = new User();
                        user.setCurImei(jsonAdapter.getCurImei());
                        user.setCurLoginToken(jsonAdapter.getCurLoginToken());
                        user.setLoginUser(jsonAdapter.getLoginUser());
                        user.setSysCfgMap(jsonAdapter.getSysCfgMap());
                        PreferUtils.saveBean2Sp("user", user);
                        PreferUtils.put("isLogin",true);
                        PreferUtils.put("pwd",password);
                        IntentUtils.startAty(LoginActivity.this,MainActivity.class);
                    }

                    @Override
                    public void onFail(LoginResultAdapter jsonAdapter) {
                        super.onFail(jsonAdapter);
                        ToastUtils.show(jsonAdapter.getMsg());
                    }
                });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(LoginActivity.this, 0,null);
    }
}
