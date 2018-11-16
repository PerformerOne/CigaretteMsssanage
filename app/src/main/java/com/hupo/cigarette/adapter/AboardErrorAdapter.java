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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gemini on 2018/10/26.
 *
 * 上车 - 异常订单适配器
 */
public class AboardErrorAdapter extends RecyclerView.Adapter<AboardErrorAdapter.MyViewHolder> {

    private Context mContext;
    private List<OrderModelInfo> list = new ArrayList<>();
    private LayoutInflater inflater = null;

    private CheckBoxInterface checkBoxInterface;//按钮回调函数

    public AboardErrorAdapter(Context context){
        this.mContext = context;
        // 布局装载器对象
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<OrderModelInfo> lists){
        list.clear();
        list.addAll(lists);
        notifyItemChanged(0,lists.size()-1);
    }



    public void onCheckedChanged(CheckBoxInterface checkBoxInterface){
        this.checkBoxInterface=checkBoxInterface;
    }

    public interface CheckBoxInterface{

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked,int position);
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
        holder.errorAreaTv.setText(bean.getAreaName());
        holder.errorSelectCk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBoxInterface != null){
                    checkBoxInterface.onCheckedChanged(buttonView,isChecked, position);
                }
            }
        });
    }


    @Override
    public int getItemCount()
    {
        return list.size()>0 ? list.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_error_id) TextView errorIdTv;//序号
        @BindView(R.id.ck_error_select) CheckBox errorSelectCk;
        @BindView(R.id.tv_error_shoper) TextView errorShoperTv;
        @BindView(R.id.tv_error_no) TextView errorNoTv;
        @BindView(R.id.tv_error_name) TextView errorNameTv;
        @BindView(R.id.tv_error_phone) TextView errorPhoneTv;
        @BindView(R.id.tv_error_num) TextView errorNumTv;
        @BindView(R.id.tv_error_area) TextView errorAreaTv;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
