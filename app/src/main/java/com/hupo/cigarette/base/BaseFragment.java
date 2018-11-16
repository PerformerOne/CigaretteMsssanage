package com.hupo.cigarette.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Gemini on 2018/10/25.
 *
 * Fragment基类
 */
public abstract class BaseFragment extends RxFragment {

    private Unbinder unbinder;

    /**
     * 视图是否已经初初始化
     */
    protected boolean isInit = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(intiLayout(), container, false);
        //返回一个Unbinder值（进行解绑）
        unbinder = ButterKnife.bind(this, mView);
        isInit = true;

        return mView;
    }
    /**
     * onDestroyView中进行解绑操作
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        isInit = false;
    }


    @Override
    public void onResume() {
        super.onResume();
        /**初始化的时候去加载数据**/
        isCanLoadData();
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }
        if (getUserVisibleHint()) {
            //初始化控件
            initView();
            //设置数据
            initData();
            initListener();
        }
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
