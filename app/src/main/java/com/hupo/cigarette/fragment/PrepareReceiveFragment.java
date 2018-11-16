package com.hupo.cigarette.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hupo.cigarette.R;
import com.hupo.cigarette.activity.ReceiveMerchantInfoActivity;
import com.hupo.cigarette.adapter.AboardReceiveAdapter;
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
 * 准备配送-领货单
 */
public class PrepareReceiveFragment extends BaseFragment {

    @BindView(R.id.rv_order_receive) RecyclerView orderReceiveRv;//领货单列表
    private AboardReceiveAdapter aboardReceiveAdapter;
    private List<DrawInvsDetailInfo> detailInfos = new ArrayList<>();

    @Override
    public int intiLayout() {
        return R.layout.fragment_receive;
    }

    @Override
    public void initView() {
        //初始化RecyclerView
        orderReceiveRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderReceiveRv.setAdapter(aboardReceiveAdapter = new AboardReceiveAdapter(getActivity(),1));
        if (DrawInvsDetailHelper.getInstance().getOnCarDrawInvs().size() > 0){
            detailInfos.clear();
            detailInfos.addAll(DrawInvsDetailHelper.getInstance().getOnCarDrawInvs());
            aboardReceiveAdapter.setData(detailInfos);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        aboardReceiveAdapter.setOnItemClickListener(new AboardReceiveAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MerchantInfo bean = new MerchantInfo();
                bean.setType(0);
                bean.setSendNo(detailInfos.get(position).getNo());
                bean.setUid(detailInfos.get(position).getUid());
                IntentUtils.startAtyWithSerialObj(getActivity(),ReceiveMerchantInfoActivity.class,"info",bean);
            }
        });
    }
}
