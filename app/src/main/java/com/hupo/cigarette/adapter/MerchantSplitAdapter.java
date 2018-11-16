package com.hupo.cigarette.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.dao.db.OrderModelInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gemini on 2018/10/26.
 *
 * 拆分-商户列表适配器
 */
public class MerchantSplitAdapter extends RecyclerView.Adapter<MerchantSplitAdapter.MyViewHolder> {

    private Context mContext;
    private List<OrderModelInfo> list = new ArrayList<>();
    private LayoutInflater inflater = null;
    private int type;//0 显示按钮 1 隐藏
    private CheckBoxInterface checkBoxInterface;//按钮回调函数

    private Map<Integer, Boolean> map = new HashMap<>();

    public MerchantSplitAdapter(Context context , int type){
        this.mContext = context;
        this.type = type;
        // 布局装载器对象
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<OrderModelInfo> lists){
        list.clear();
        list.addAll(lists);
        notifyDataSetChanged();
    }

    public void onCheckedChanged(CheckBoxInterface checkBoxInterface){
        this.checkBoxInterface=checkBoxInterface;
    }

    public interface CheckBoxInterface{

        public void onCheckedChanged(Map<Integer, Boolean> map, int position);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_error, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        OrderModelInfo bean = list.get(position);
        if (position<9){
            holder.errorIdTv.setText("0"+(position+1));
        }else {
            holder.errorIdTv.setText((position+1)+"");
        }
        holder.errorShoperTv.setText(bean.getCustName());
        holder.errorNoTv.setText(bean.getCustId());
        holder.errorNameTv.setText(bean.getManager());
        holder.errorPhoneTv.setText(bean.getManagerTel());
        holder.errorNumTv.setText(bean.getCount()+"条/"+bean.getBagCount()+"包");
        holder.errorAreaTv.setText(bean.getAddr());

        if (type == 0){
            holder.errorSelectCk.setVisibility(View.VISIBLE);
        }else {
            holder.errorSelectCk.setVisibility(View.GONE);
        }

        holder.errorSelectCk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    map.put(position, true);
                } else {
                    map.remove(position);
                }
                if (checkBoxInterface != null){
                    checkBoxInterface.onCheckedChanged(map, position);
                }
            }
        });

        if (map != null && map.containsKey(position)) {
            holder.errorSelectCk.setChecked(true);
        } else {
            holder.errorSelectCk.setChecked(false);
        }
    }


    @Override
    public int getItemCount()
    {
        return list.size()>0 ? list.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_error_id) TextView errorIdTv;//序号
        @BindView(R.id.ck_error_select) CheckBox errorSelectCk;
        @BindView(R.id.tv_error_shoper) TextView errorShoperTv;//商家名
        @BindView(R.id.tv_error_no) TextView errorNoTv;//商户编号
        @BindView(R.id.tv_error_name) TextView errorNameTv;//负责人
        @BindView(R.id.tv_error_phone) TextView errorPhoneTv;//电话
        @BindView(R.id.tv_error_num) TextView errorNumTv;//数量
        @BindView(R.id.tv_error_area) TextView errorAreaTv;//地址

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
