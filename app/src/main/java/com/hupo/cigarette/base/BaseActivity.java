package com.hupo.cigarette.base;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.hupo.cigarette.R;
import com.hupo.cigarette.utils.CommonDialogUtils;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends RxAppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int REQUEST_READ_PHONE_STATE = 1;
    protected RxPermissions rxPermissions;
    protected CommonDialogUtils dialogUtils;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialogUtils=new CommonDialogUtils();
        rxPermissions=new RxPermissions(this);
        //设置布局
        setContentView(intiLayout());
        setStatusBar();
        ButterKnife.bind(this);
        //初始化控件
        initView();
        //设置数据
        initData();
        initListener();
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
        }

    }



    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.tF87E3D));
    }

    /**
     * 后退按钮响应事件
     */
    public void onBackClick(View v) {
        finish();
    }

    /**
     * 设置布局
     *
     * @return
     */
    public abstract int intiLayout();

    /**
     * 初始化布局
     */
    public abstract void initView();

    /**
     * 设置数据
     */
    public abstract void initData();

    /**
     * 事件监听
     */
    public abstract void initListener();

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;

            default:
                break;
        }
    }

}
