package com.hupo.cigarette.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.adapter.ReceiveMerchantInfoAdapter;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.bean.AreaBean;
import com.hupo.cigarette.bean.MerchantInfo;
import com.hupo.cigarette.dao.db.OrderModelInfo;
import com.hupo.cigarette.dao.helper.OrderDetailInfoHelper;
import com.hupo.cigarette.utils.IntentUtils;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.utils.ToastUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Gemini on 2018/10/29.
 *
 * 商户列表
 */
public class ReceiveMerchantInfoActivity extends BaseActivity {

    @BindView(R.id.tv_title) TextView titleTv;//标题
    @BindView(R.id.lay_back) LinearLayout backLay;//返回
    @BindView(R.id.rv_merchant_info) RecyclerView merchantInfoRv;//商户列表界面
    @BindView(R.id.refreshLayout) RefreshLayout mRefreshLayout;//下拉刷新
    private ReceiveMerchantInfoAdapter merchantInfoAdapter;

    private int type = 0;
    private String uid,//领货单id
            sendNo;//领货单单号

    private List<OrderModelInfo> orders = new ArrayList<>();//订单列表


    @Override
    public int intiLayout() {
        return R.layout.activity_merchant_info;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        MerchantInfo merchantInfo = (MerchantInfo) intent.getSerializableExtra("info");
        type = merchantInfo.getType();
        uid = merchantInfo.getUid();
        sendNo = merchantInfo.getSendNo();

        titleTv.setText(getString(R.string.title_merchant_split));//设置标题
        backLay.setVisibility(View.VISIBLE);

        merchantInfoRv.setLayoutManager(new LinearLayoutManager(this));
        merchantInfoRv.setAdapter(merchantInfoAdapter = new ReceiveMerchantInfoAdapter(this,type,sendNo));
    }

    @Override
    public void initData() {
        List<OrderModelInfo> d = OrderDetailInfoHelper.getInstance().getDetail(uid);
        if (d.size() > 0){
            orders.clear();
            orders.addAll(d);
            merchantInfoAdapter.setData(orders);
        }
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


        //异常
        merchantInfoAdapter.errorOnclick(new ReceiveMerchantInfoAdapter.ErrorBtnInterface() {
            @Override
            public void onclick(View view, int position) {
                MerchantInfo info = new MerchantInfo();
                info.setUid(uid);
                info.setSendNo(orders.get(position).getCustId());
                IntentUtils.startAtyWithSerialObj(ReceiveMerchantInfoActivity.this,SendErrorActivity.class,"info",info);
            }
        });

        //详情
        merchantInfoAdapter.detailOnclick(new ReceiveMerchantInfoAdapter.DetailBtnInterface() {
            @Override
            public void onclick(View view, int position) {

                MerchantInfo info = new MerchantInfo();
                info.setUid(orders.get(position).getCustId());
                info.setSendNo(sendNo);
                IntentUtils.startAtyWithSerialObj(ReceiveMerchantInfoActivity.this,OrderDetailActivity.class,"info",info);
            }
        });

        //电话
        merchantInfoAdapter.phoneOnclick(new ReceiveMerchantInfoAdapter.PhoneBtnInterface() {
            @Override
            public void onclick(View view, int position) {
                ToastUtils.show("电话");
            }
        });

        //导航
        merchantInfoAdapter.navigationOnclick(new ReceiveMerchantInfoAdapter.NavigationBtnInterface() {
            @Override
            public void onclick(View view, int position) {
                AreaBean areaBean = new AreaBean();
                areaBean.setLatitude(35.18);
                areaBean.setLongitude(113.52);
                IntentUtils.startAtyWithSerialObj(ReceiveMerchantInfoActivity.this,ReturnJourneyActivity.class,"areaBean",areaBean);
            }
        });

        //确认收货
        merchantInfoAdapter.confirmOnclick(new ReceiveMerchantInfoAdapter.ConfirmBtnInterface() {
            @Override
            public void onclick(View view, int position) {
                IntentUtils.startAtyWithSingleParam(ReceiveMerchantInfoActivity.this,OrderDetailActivity.class,"fragment",2);
            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(ReceiveMerchantInfoActivity.this, 0,null);
    }
}
