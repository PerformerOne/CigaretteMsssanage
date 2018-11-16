package com.hupo.cigarette.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.adapter.MerchantSplitAdapter;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.bean.MerchantInfo;
import com.hupo.cigarette.dao.db.OrderModelInfo;
import com.hupo.cigarette.dao.helper.DrawInvsDetailHelper;
import com.hupo.cigarette.dao.helper.OrderDetailInfoHelper;
import com.hupo.cigarette.net.DefaultObserver;
import com.hupo.cigarette.net.IdeaApi;
import com.hupo.cigarette.utils.HttpParamsUtil;
import com.hupo.cigarette.utils.IntentUtils;
import com.hupo.cigarette.utils.SecondsUtils;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.utils.ToastUtils;
import com.hupo.cigarette.widget.dialog.CenterAlertDialog;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.SplitDrawInvResultAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gemini on 2018/10/29.
 *
 * 商户拆分
 */
public class MerchantSplitActivity extends BaseActivity {

    @BindView(R.id.tv_title) TextView titleTv;//标题
    @BindView(R.id.lay_back) LinearLayout backLay;//返回

    @BindView(R.id.refreshLayout) RefreshLayout mRefreshLayout;//下拉刷新
    @BindView(R.id.rv_merchant_split) RecyclerView merchantSplitRv;//拆分商户列表
    private MerchantSplitAdapter splitAdapter;
    private List<OrderModelInfo> orders = new ArrayList<>();//订单列表
    private List<String> orderIds = new ArrayList<>();//需要进行拆分为新领货单的订单

    private String drawInvId,//需要进行拆单的领货单编号
                   orderId,//需要进行拆分为新领货单的订单
                   splitOperTime;//拆分时间

    private int type = 0;

    @Override
    public int intiLayout() {
        return R.layout.activity_merchant_split;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        MerchantInfo merchantInfo = (MerchantInfo) intent.getSerializableExtra("info");
        type = merchantInfo.getType();
        drawInvId = merchantInfo.getUid();

        titleTv.setText(getString(R.string.title_merchant_split));//设置标题
        backLay.setVisibility(View.VISIBLE);

        //初始化RecyclerView
        merchantSplitRv.setLayoutManager(new LinearLayoutManager(this));
        merchantSplitRv.setAdapter(splitAdapter = new MerchantSplitAdapter(this,type));
        merchantSplitRv.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                return false;
            }
        });
        //解决数据加载不完的问题
        merchantSplitRv.setNestedScrollingEnabled(false);
        merchantSplitRv.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
        merchantSplitRv.setFocusable(false);
    }

    @Override
    public void initData() {
        if (OrderDetailInfoHelper.getInstance().getDetail(drawInvId).size() > 0){
            orders.clear();
            orders.addAll(OrderDetailInfoHelper.getInstance().getDetail(drawInvId));
            splitAdapter.setData(orders);
        }

        splitOperTime = SecondsUtils.getDateForAll();
    }

    @Override
    public void initListener() {

        splitAdapter.onCheckedChanged(new MerchantSplitAdapter.CheckBoxInterface() {
            @Override
            public void onCheckedChanged(Map<Integer, Boolean> map, int position) {
                String id = orders.get(position).getId();
                if (map != null && map.containsKey(position)) {
                    orderIds.add(id);
                } else {
                    orderIds.remove(id);
                }
            }
        });

        //刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData();
                refreshlayout.finishRefresh();
            }
        });
    }

    @OnClick({R.id.tv_merchant_confirm})
    public void OnClick(View view){
        switch (view.getId()){
            //完成拆分
            case R.id.tv_merchant_confirm:
                if (orders.size() > 1){
                    new CenterAlertDialog(MerchantSplitActivity.this, "您确定将"+orderIds+"个订单进行拆分？" , "确定", "取消", new CenterAlertDialog.Listener() {
                        @Override
                        public void onLeftClick() {
                            merchantConfirm();
                        }

                        @Override
                        public void onRightClick() {
                        }
                    }).show();
                }else {
                    ToastUtils.show("不可拆分");
                }
                break;
        }
    }

    private void merchantConfirm() {
        StringBuilder sbd = new StringBuilder();
        for (int i = 0; i < orderIds.size(); i++) {
            sbd.append(orderIds.get(i));
            sbd.append(",");
        }
        orderId = sbd.length() > 0 ? sbd.toString().substring(0,sbd.length()-1) : "";

        Map<String, Object> map = new HashMap<>();
        map.put("drawInvId", drawInvId);
        map.put("orderId", orderId);
        map.put("splitOperTime", splitOperTime);
        IdeaApi.getApiService()
                .splitDrawInv(HttpParamsUtil.setParams(map))
                .compose(this.<SplitDrawInvResultAdapter>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<SplitDrawInvResultAdapter>(this, SplitDrawInvResultAdapter.class) {
                    @Override
                    public void onSuccess(SplitDrawInvResultAdapter jsonAdapter) {
                        IntentUtils.startAty(MerchantSplitActivity.this,AboardActivity.class);
                        DrawInvsDetailHelper.getInstance().changeSplit(drawInvId,jsonAdapter.getOldDrawInv());
                        DrawInvsDetailHelper.getInstance().insertSplit(jsonAdapter.getNewDrawInv());
                        OrderDetailInfoHelper.getInstance().updateMerchant(orderId,jsonAdapter.getNewDrawInv().getUid());
                    }

                    @Override
                    public void onFail(SplitDrawInvResultAdapter jsonAdapter) {
                        super.onFail(jsonAdapter);
                        ToastUtils.show(jsonAdapter.getMsg());
                    }
                });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(MerchantSplitActivity.this, 0,null);
    }
}
