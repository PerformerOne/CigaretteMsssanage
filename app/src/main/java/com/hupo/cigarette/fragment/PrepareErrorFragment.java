package com.hupo.cigarette.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.adapter.OrderErrorAdapter;
import com.hupo.cigarette.base.BaseFragment;
import com.hupo.cigarette.test.TestData;

import butterknife.BindView;

/**
 * Created by Gemini on 2018/10/29.
 *
 * 准备配送-异常订单
 */
public class PrepareErrorFragment extends BaseFragment {

    @BindView(R.id.rv_order_error) RecyclerView orderErrorRv;//异常订单列表
    private OrderErrorAdapter orderErrorAdapter;

    @Override
    public int intiLayout() {
        return R.layout.fragment_error;
    }

    @Override
    public void initView() {
        //初始化RecyclerView
        orderErrorRv.setLayoutManager(new LinearLayoutManager(getActivity()));
//        orderErrorRv.setAdapter(orderErrorAdapter = new OrderErrorAdapter(getActivity(),TestData.getTestData(11),1));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
