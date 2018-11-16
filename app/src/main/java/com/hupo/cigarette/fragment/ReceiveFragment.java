package com.hupo.cigarette.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hupo.cigarette.R;
import com.hupo.cigarette.activity.ReceiveMerchantInfoActivity;
import com.hupo.cigarette.adapter.AboardReceiveAdapter;
import com.hupo.cigarette.base.BaseFragmentC;
import com.hupo.cigarette.bean.MerchantInfo;
import com.hupo.cigarette.dao.db.DrawInvsDetailInfo;
import com.hupo.cigarette.dao.helper.DrawInvsDetailHelper;
import com.hupo.cigarette.utils.IntentUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Gemini on 2018/10/29.
 *
 * 上车-领货单
 */
public class ReceiveFragment extends BaseFragmentC {


    @BindView(R.id.rv_order_receive) RecyclerView orderReceiveRv;//领货单列表
    private AboardReceiveAdapter aboardReceiveAdapter;
    private List<DrawInvsDetailInfo> detailInfos = new ArrayList<>();

    private CheckBoxInterface checkBoxInterface;//按钮回调函数
    private List<String> nos = new ArrayList<>();//领货单编号

    private int receiveNum ;//选择的领货单数量

    @Override
    public int intiLayout() {
        return R.layout.fragment_receive;
    }

    @Override
    public void initView() {
        //初始化RecyclerView
        orderReceiveRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderReceiveRv.setAdapter(aboardReceiveAdapter = new AboardReceiveAdapter(getActivity(),0));
    }

    @Override
    public void initData() {
        if (DrawInvsDetailHelper.getInstance().getDetails().size() > 0){
            detailInfos.clear();
            detailInfos.addAll(DrawInvsDetailHelper.getInstance().getDetails());
            aboardReceiveAdapter.setData(detailInfos);
        }
    }
    @Override
    public void initListener() {
        aboardReceiveAdapter.setOnItemClickListener(new AboardReceiveAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MerchantInfo bean = new MerchantInfo();
                bean.setType(0);
                bean.setUid(detailInfos.get(position).getUid());
                bean.setSendNo(detailInfos.get(position).getNo());
                IntentUtils.startAtyWithSerialObj(getActivity(),ReceiveMerchantInfoActivity.class,"info",bean);
            }
        });

        aboardReceiveAdapter.onCheckedChanged(new AboardReceiveAdapter.CheckBoxInterface() {
            @Override
            public void onCheckedChanged(Map<Integer, Boolean> map, int position) {
                if (checkBoxInterface != null){
                    String no = detailInfos.get(position).getUid();
                    if (map != null && map.containsKey(position)) {
                        nos.add(no);
                        receiveNum += 1;
                    } else {
                        nos.remove(no);
                        receiveNum -= 1;
                    }
                    checkBoxInterface.onCheckedChanged(nos, receiveNum);
                }
            }
        });
    }


    public void onCheckedChanged(CheckBoxInterface checkBoxInterface){
        this.checkBoxInterface=checkBoxInterface;
    }

    public interface CheckBoxInterface{

        public void onCheckedChanged(List<String> nos, int receiveNum);
    }
}
