package com.hupo.cigarette.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.hupo.cigarette.R;
import com.hupo.cigarette.activity.ShopDetailActivity;
import com.hupo.cigarette.adapter.OrderDetailAdapter;
import com.hupo.cigarette.base.BaseFragment;
import com.hupo.cigarette.dao.db.OrderModelInfo;
import com.hupo.cigarette.dao.helper.OrderDetailInfoHelper;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HCustModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Gemini on 2018/10/30.
 *
 * 汇总
 */
public class OrderTotalFragment extends BaseFragment {

    @BindView(R.id.rv_order_receive) RecyclerView orderReceiveRv;//汇总列表
    private OrderDetailAdapter orderDetailAdapter;//汇总适配器
    private List<OrderModelInfo> modelInfos = new ArrayList<>();

    private String orderId,//订单id
    sendNo;//领货单单号

    @Override
    public int intiLayout() {
        return R.layout.fragment_receive;
    }

    @Override
    public void initView() {
        //初始化RecyclerView
        orderReceiveRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderReceiveRv.setAdapter(orderDetailAdapter = new OrderDetailAdapter(getActivity(),sendNo));

    }

    @Override
    public void initData() {
        if (OrderDetailInfoHelper.getInstance().getDetailInfo(orderId).size() > 0){
            modelInfos.clear();
            modelInfos.addAll(OrderDetailInfoHelper.getInstance().getDetailInfo(orderId));
            orderDetailAdapter.setData(modelInfos);
        }

    }

    //获取activity传的值
    public void setOrderId(String orderId, String sendNo){
        this.orderId = orderId;
        this.sendNo = sendNo;
    }

    @Override
    public void initListener() {
        //修改图片
        orderDetailAdapter.changePicOnclick(new OrderDetailAdapter.ChangePicInterface() {
            @Override
            public void onclick(View view, int position) {
                OrderModelInfo bean = modelInfos.get(position);
                HCustModel detail = new HCustModel();
                detail.setId(bean.getCustId());
                detail.setName(bean.getCustName());
                detail.setAddr(bean.getAddr());
                detail.setManager(bean.getManager());
                detail.setManagerTel(bean.getManagerTel());
                detail.setLongitude(bean.getCustLongitude());
                detail.setLatitude(bean.getCustLatitude());
                Intent i=new Intent(getActivity(),ShopDetailActivity.class);
                i.putExtra("Position",position);
                i.putExtra("data",new Gson().toJson(detail));
                getActivity().startActivity(i);
            }
        });
    }
}
