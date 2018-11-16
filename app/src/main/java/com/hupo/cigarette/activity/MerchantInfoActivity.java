package com.hupo.cigarette.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.adapter.MerchantInfoAdapter;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.bean.MerchantInfo;
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
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gemini on 2018/10/29.
 *
 * 商户列表
 */
public class MerchantInfoActivity extends BaseActivity {

    @BindView(R.id.tv_title) TextView titleTv;//标题
    @BindView(R.id.lay_back) LinearLayout backLay;//返回
    @BindView(R.id.rv_merchant_info) RecyclerView merchantInfoRv;//商户列表界面
    @BindView(R.id.refreshLayout) RefreshLayout mRefreshLayout;//下拉刷新
    private MerchantInfoAdapter merchantInfoAdapter;

    private String uid,//领货单id
            sendNo;//领货单单号

    private List<HOrderModel> orders = new ArrayList<>();//订单列表

    @Override
    public int intiLayout() {
        return R.layout.activity_merchant_info;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        MerchantInfo merchantInfo = (MerchantInfo) intent.getSerializableExtra("info");
        uid = merchantInfo.getUid();
        sendNo = merchantInfo.getSendNo();

        titleTv.setText(getString(R.string.title_merchant_split));//设置标题
        backLay.setVisibility(View.VISIBLE);

        merchantInfoRv.setLayoutManager(new LinearLayoutManager(this));
        merchantInfoRv.setAdapter(merchantInfoAdapter = new MerchantInfoAdapter(this,sendNo));
    }

    @Override
    public void initData() {
        Map<String,Object> map = new HashMap<>();
        HOrderModel bean = new HOrderModel();
        bean.setDrawInvId(uid);
        map.put("searchData",bean);
        IdeaApi.getApiService()
                .findByDrawInv(HttpParamsUtil.setParams(map))
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
        StatusBarUtil.setTranslucentForImageViewInFragment(MerchantInfoActivity.this, 0,null);
    }
}
