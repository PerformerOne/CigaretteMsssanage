package com.hupo.cigarette.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HOrderModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gemini on 2018/10/26.
 *
 * 异常订单适配器
 */
public class OrderErrorAdapter extends RecyclerView.Adapter<OrderErrorAdapter.MyViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<HOrderModel> list = new ArrayList<>();
    private LayoutInflater inflater = null;
    private int type;

    public OrderErrorAdapter(Context context ,int type){
        this.mContext = context;
        this.type = type;
        // 布局装载器对象
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<HOrderModel> lists){
        list.clear();
        list.addAll(lists);
        notifyDataSetChanged();
    }

    private OnItemClickListener mOnItemClickListener = null;

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_error, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;

    }

    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        if (position<9){
            holder.errorIdTv.setText("0"+(position+1));
        }else {
            holder.errorIdTv.setText((position+1)+"");
        }

        if (type == 0){
            holder.errorSelectCk.setVisibility(View.VISIBLE);
        }else {
            holder.errorSelectCk.setVisibility(View.GONE);
        }

        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }


    @Override
    public int getItemCount()
    {
        return list.size()>0 ? list.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_error_id) TextView errorIdTv;//序号
        @BindView(R.id.ck_error_select) CheckBox errorSelectCk;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
