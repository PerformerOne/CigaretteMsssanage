package com.hupo.cigarette.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hupo.cigarette.R;
import com.hupo.cigarette.activity.MapActivity;
import com.hupo.cigarette.activity.ReceiveMerchantInfoActivity;
import com.hupo.cigarette.adapter.SendReceiveAdapter;
import com.hupo.cigarette.base.BaseFragment;
import com.hupo.cigarette.bean.MerchantInfo;
import com.hupo.cigarette.dao.db.DrawInvsDetailInfo;
import com.hupo.cigarette.dao.helper.DrawInvsDetailHelper;
import com.hupo.cigarette.utils.IntentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Gemini on 2018/10/29.
 *
 * 配送-领货单
 */
public class SendReceiveFragment extends BaseFragment {

    @BindView(R.id.rv_order_receive) RecyclerView orderReceiveRv;//领货单列表
    private SendReceiveAdapter sendReceiveAdapter;
    private List<DrawInvsDetailInfo> detailInfos = new ArrayList<>();

    @Override
    public int intiLayout() {
        return R.layout.fragment_receive;
    }

    @Override
    public void initView() {
        //初始化RecyclerView
        orderReceiveRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderReceiveRv.setAdapter(sendReceiveAdapter = new SendReceiveAdapter(getActivity()));

    }

    @Override
    public void initData() {
        if (DrawInvsDetailHelper.getInstance().getPrepareSendDrawInvs().size() > 0){
            detailInfos.clear();
            detailInfos.addAll(DrawInvsDetailHelper.getInstance().getPrepareSendDrawInvs());
            sendReceiveAdapter.setData(detailInfos);
        }
    }

    @Override
    public void initListener() {
        //地图回调函数
        sendReceiveAdapter.mapOnclick(new SendReceiveAdapter.MapBtnInterface() {
            @Override
            public void onclick(View view, int position) {
                MerchantInfo bean = new MerchantInfo();
                bean.setSendNo(detailInfos.get(position).getNo());
                bean.setUid(detailInfos.get(position).getUid());
                IntentUtils.startAtyWithSerialObj(getActivity(),MapActivity.class,"info",bean);
            }
        });

        //详情回调函数
        sendReceiveAdapter.detailOnclick(new SendReceiveAdapter.DetailBtnInterface() {
            @Override
            public void onclick(View view, int position) {
                MerchantInfo bean = new MerchantInfo();
                bean.setType(1);
                bean.setSendNo(detailInfos.get(position).getNo());
                bean.setUid(detailInfos.get(position).getUid());
                IntentUtils.startAtyWithSerialObj(getActivity(),ReceiveMerchantInfoActivity.class,"info",bean);
            }
        });
    }
}
