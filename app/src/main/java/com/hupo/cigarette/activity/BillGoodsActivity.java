package com.hupo.cigarette.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.adapter.OrderReceiveAdapter;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.bean.MerchantInfo;
import com.hupo.cigarette.net.DefaultObserver;
import com.hupo.cigarette.net.IdeaApi;
import com.hupo.cigarette.utils.HttpParamsUtil;
import com.hupo.cigarette.utils.IntentUtils;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.utils.ToastUtils;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.DrawInvAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HDrawInvModel;
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
 * Created by Gemini on 2018/11/5.
 *
 * 配送单-领货单
 */
public class BillGoodsActivity extends BaseActivity {

    @BindView(R.id.tv_title) TextView titleTv;//标题
    @BindView(R.id.lay_back) LinearLayout backLay;//返回
    @BindView(R.id.rv_merchant_info) RecyclerView billGoodsRv;//领货单列表
    @BindView(R.id.refreshLayout) RefreshLayout mRefreshLayout;//下拉刷新
    private OrderReceiveAdapter orderReceiveAdapter;//领货单适配

    private String sendNo;//配送单单号

    private List<HDrawInvModel> billGoods = new ArrayList<>();//领货单集合

    @Override
    public int intiLayout() {
        return R.layout.activity_merchant_info;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        sendNo = intent.getStringExtra("sendNo");

        titleTv.setText(getString(R.string.title_bill_goods));//设置标题
        backLay.setVisibility(View.VISIBLE);

        //初始化RecyclerView
        billGoodsRv.setLayoutManager(new LinearLayoutManager(this));
        billGoodsRv.setAdapter(orderReceiveAdapter = new OrderReceiveAdapter(this,1));
    }

    @Override
    public void initData() {
        Map<String,Object> map = new HashMap<>();
        HDrawInvModel bean = new HDrawInvModel();
        bean.setSendNo(sendNo);
        map.put("searchData",bean);
        IdeaApi.getApiService()
                .findBySendNo(HttpParamsUtil.setParams(map))
                .compose(this.<DrawInvAdapter>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<DrawInvAdapter>(this, DrawInvAdapter.class) {
                    @Override
                    public void onSuccess(DrawInvAdapter jsonAdapter) {
                        billGoods.clear();
                        billGoods.addAll(jsonAdapter.getDrawInvs());
                        orderReceiveAdapter.setData(billGoods);
                    }

                    @Override
                    public void onFail(DrawInvAdapter jsonAdapter) {
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

        orderReceiveAdapter.setOnItemClickListener(new OrderReceiveAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MerchantInfo bean = new MerchantInfo();
                bean.setUid(billGoods.get(position).getUid());
                bean.setSendNo(billGoods.get(position).getNo());
                IntentUtils.startAtyWithSerialObj(BillGoodsActivity.this,MerchantInfoActivity.class,"info",bean);
            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(BillGoodsActivity.this, 0,null);
    }
}
