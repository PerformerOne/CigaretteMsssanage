package com.hupo.cigarette.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.adapter.MerchantInfoAdapter;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.net.DefaultObserver;
import com.hupo.cigarette.net.IdeaApi;
import com.hupo.cigarette.utils.HttpParamsUtil;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.utils.ToastUtils;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.OrderAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HOrderModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gemini on 2018/11/5.
 *
 * 异常订单
 */
public class OrderErrorActivity extends BaseActivity {

    @BindView(R.id.tv_title) TextView titleTv;//标题
    @BindView(R.id.lay_back) LinearLayout backLay;//返回
    @BindView(R.id.rv_order_error) RecyclerView orderErrorRv;//异常订单列表
    private MerchantInfoAdapter merchantInfoAdapter;
    @BindView(R.id.refreshLayout) RefreshLayout mRefreshLayout;//下拉刷新

    private List<HOrderModel> orders = new ArrayList<>();//异常订单列表

    @Override
    public int intiLayout() {
        return R.layout.activity_order_error;
    }

    @Override
    public void initView() {
        titleTv.setText(getString(R.string.title_order_error));//设置标题
        backLay.setVisibility(View.VISIBLE);
        orderErrorRv.setLayoutManager(new LinearLayoutManager(this));
        orderErrorRv.setAdapter(merchantInfoAdapter = new MerchantInfoAdapter(this,""));
    }

    @Override
    public void initData() {

        IdeaApi.getApiService()
                .getExpOrders(HttpParamsUtil.setParam(new HashMap<>()))
                .compose(this.<OrderAdapter>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<OrderAdapter>(this, OrderAdapter.class) {
                    @Override
                    public void onSuccess(OrderAdapter jsonAdapter) {
                        orders.clear();
                        orders.addAll(jsonAdapter.getOrders());
                        merchantInfoAdapter.setData(orders);
                    }

                    @Override
                    public void onFail(OrderAdapter jsonAdapter) {
                        super.onFail(jsonAdapter);
                        ToastUtils.show(jsonAdapter.getMsg());
                    }
                });
    }

    @Override
    public void initListener() {
        //刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData();
                refreshlayout.finishRefresh();
            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(OrderErrorActivity.this, 0,null);
    }
}
