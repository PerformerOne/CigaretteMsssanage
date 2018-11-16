package com.hupo.cigarette.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.bean.User;
import com.hupo.cigarette.net.Constants;
import com.hupo.cigarette.net.DefaultObserver;
import com.hupo.cigarette.net.IdeaApi;
import com.hupo.cigarette.utils.HttpParamsUtil;
import com.hupo.cigarette.utils.IntentUtils;
import com.hupo.cigarette.utils.PreferUtils;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.utils.ToastUtils;
import com.huposoft.commons.json.JsonAdapter;
import com.huposoft.commons.utils.AES;
import com.huposoft.commons.utils.MD5;
import com.huposoft.commons.utils.TimeUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gemini on 2018/11/2.
 *
 * 个人设置
 */
public class UserSettingActivity extends BaseActivity {

    @BindView(R.id.tv_title) TextView titleTv;//标题
    @BindView(R.id.lay_back) LinearLayout backLay;//返回
    @BindView(R.id.tv_confirm) TextView confirmTv;//完成
    @BindView(R.id.et_pwd_old) EditText pwdOldEt;//原始密码
    @BindView(R.id.et_pwd_new) EditText pwdNewEt;//新密码
    @BindView(R.id.et_pwd_again) EditText pwdAgainEt;//确认密码

    private String oldPwd,//原始密码
                   newPwd,//新密码
                   againPwd;//确认密码

    private String pwd;

    @Override
    public int intiLayout() {
        return R.layout.activity_user_setting;
    }

    @Override
    public void initView() {
        titleTv.setText(getString(R.string.title_user_setting));//设置标题
        backLay.setVisibility(View.VISIBLE);
        confirmTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        pwd = PreferUtils.getString("pwd","");
    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.tv_confirm})
    public void OnClick(View view){
        switch (view.getId()){
            //确认
            case R.id.tv_confirm:
                changePwd();
                break;
        }
    }

    private void changePwd() {
        oldPwd = pwdOldEt.getText().toString();
        newPwd = pwdNewEt.getText().toString();
        againPwd = pwdAgainEt.getText().toString();

        if (TextUtils.isEmpty(oldPwd)){
            ToastUtils.show("请输入原始密码");
            return;
        }

        if (!oldPwd.equals(pwd)){
            ToastUtils.show("请输入正确的原始密码");
            return;
        }

        if (TextUtils.isEmpty(newPwd)){
            ToastUtils.show("请输入新密码");
            return;
        }
        if (TextUtils.isEmpty(againPwd)){
            ToastUtils.show("请输入确认密码");
            return;
        }

        if (!newPwd.equals(againPwd)){
            ToastUtils.show("两次输入的密码不一致");
            return;
        }

        Map<String,String> map = new HashMap<>();
        map.put("newPwd",MD5.GetMD5Code(newPwd));

        IdeaApi.getApiService()
                .editPwd(HttpParamsUtil.setParam(map))
                .compose(this.<JsonAdapter>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<JsonAdapter>(this, JsonAdapter.class) {
                    @Override
                    public void onSuccess(JsonAdapter jsonAdapter) {
                        ToastUtils.show("修改成功");
                        PreferUtils.clear();
                        IntentUtils.startAty(UserSettingActivity.this,LoginActivity.class);
                    }

                    @Override
                    public void onFail(JsonAdapter jsonAdapter) {
                        super.onFail(jsonAdapter);
                        ToastUtils.show(jsonAdapter.getMsg());
                    }
                });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(UserSettingActivity.this, 0,null);
    }

}
