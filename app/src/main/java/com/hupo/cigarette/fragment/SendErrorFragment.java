package com.hupo.cigarette.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hupo.cigarette.R;
import com.hupo.cigarette.activity.MapActivity;
import com.hupo.cigarette.activity.MerchantInfoActivity;
import com.hupo.cigarette.activity.OrderDetailActivity;
import com.hupo.cigarette.activity.SendErrorActivity;
import com.hupo.cigarette.adapter.OrderErrorAdapter;
import com.hupo.cigarette.adapter.SendErrorAdapter;
import com.hupo.cigarette.base.BaseFragment;
import com.hupo.cigarette.test.TestData;
import com.hupo.cigarette.utils.IntentUtils;
import com.hupo.cigarette.utils.ToastUtils;

import butterknife.BindView;

/**
 * Created by Gemini on 2018/10/29.
 *
 * 配送-异常订单
 */
public class SendErrorFragment extends BaseFragment {

    @BindView(R.id.rv_order_error) RecyclerView orderErrorRv;//异常订单列表
    private SendErrorAdapter sendErrorAdapter;

    @Override
    public int intiLayout() {
        return R.layout.fragment_error;
    }

    @Override
    public void initView() {
        //初始化RecyclerView
        orderErrorRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderErrorRv.setAdapter(sendErrorAdapter = new SendErrorAdapter(getActivity(),TestData.getTestData(11)));

        //异常
        sendErrorAdapter.errorOnclick(new SendErrorAdapter.ErrorBtnInterface() {
            @Override
            public void onclick(View view, int position) {
                IntentUtils.startAty(getActivity(),SendErrorActivity.class);
            }
        });

        //详情
        sendErrorAdapter.detailOnclick(new SendErrorAdapter.DetailBtnInterface() {
            @Override
            public void onclick(View view, int position) {
                IntentUtils.startAty(getActivity(),OrderDetailActivity.class);
            }
        });

        //电话
        sendErrorAdapter.phoneOnclick(new SendErrorAdapter.PhoneBtnInterface() {
            @Override
            public void onclick(View view, int position) {
                ToastUtils.show("电话");
            }
        });

        //导航
        sendErrorAdapter.navigationOnclick(new SendErrorAdapter.NavigationBtnInterface() {
            @Override
            public void onclick(View view, int position) {
                IntentUtils.startAty(getActivity(),MapActivity.class);
            }
        });

        //确认收货
        sendErrorAdapter.confirmOnclick(new SendErrorAdapter.ConfirmBtnInterface() {
            @Override
            public void onclick(View view, int position) {
                IntentUtils.startAtyWithSingleParam(getActivity(),OrderDetailActivity.class,"fragment",2);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
