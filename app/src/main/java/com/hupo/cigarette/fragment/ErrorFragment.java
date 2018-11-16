package com.hupo.cigarette.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;

import com.hupo.cigarette.R;
import com.hupo.cigarette.adapter.AboardErrorAdapter;
import com.hupo.cigarette.base.BaseFragment;
import com.hupo.cigarette.dao.db.OrderModelInfo;
import com.hupo.cigarette.dao.helper.OrderDetailInfoHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Gemini on 2018/10/29.
 *
 * 上车-异常订单
 */
public class ErrorFragment extends BaseFragment {

    @BindView(R.id.rv_order_error) RecyclerView orderErrorRv;//异常订单列表
    private AboardErrorAdapter aboardErrorAdapter;

    private List<OrderModelInfo> orders = new ArrayList<>();//异常订单列表

    private CheckBoxInterface checkBoxInterface;//按钮回调函数
    private List<String> nos = new ArrayList<>();//异常订单编号

    private int errorNum ;//选择的异常订单数量

    @Override
    public int intiLayout() {
        return R.layout.fragment_error;
    }

    @Override
    public void initView() {
        //初始化RecyclerView
        orderErrorRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderErrorRv.setAdapter(aboardErrorAdapter = new AboardErrorAdapter(getActivity()));
    }

    @Override
    public void initData() {

        if (OrderDetailInfoHelper.getInstance().getExpOrder().size() > 0){
            orders.clear();
            orders.addAll(OrderDetailInfoHelper.getInstance().getExpOrder());
            aboardErrorAdapter.setData(orders);
        }
    }

    @Override
    public void initListener() {
        aboardErrorAdapter.onCheckedChanged(new AboardErrorAdapter.CheckBoxInterface() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked, int position) {
                if (checkBoxInterface != null){
                    String no = orders.get(position).getId();
                    if (isChecked){
                        nos.add(no);
                        errorNum += 1;
                    }else {
                        nos.remove(no);
                        errorNum -= 1;
                    }
                    checkBoxInterface.onCheckedChanged( nos, errorNum);
                }
            }
        });
    }

    public void onCheckedChanged(CheckBoxInterface checkBoxInterface){
        this.checkBoxInterface=checkBoxInterface;
    }

    public interface CheckBoxInterface{

        public void onCheckedChanged(List<String> nos, int errorNum);
    }
}
