package com.hupo.cigarette.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Gemini on 2018/11/12.
 */
public abstract class BaseFragmentC extends RxFragment {

    private Unbinder unbinder;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(intiLayout(), container, false);
        //返回一个Unbinder值（进行解绑）
        unbinder = ButterKnife.bind(this, mView);
        //初始化控件
        initView();

        return mView;
    }
    /**
     * onDestroyView中进行解绑操作
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        //设置数据
        initData();
        initListener();
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
     * 监听事件
     */
    public abstract void initListener();
}
