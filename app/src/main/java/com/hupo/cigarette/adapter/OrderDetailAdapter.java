package com.hupo.cigarette.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.dao.db.OrderModelInfo;
import com.hupo.cigarette.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gemini on 2018/10/26.
 *
 * 订单详情汇总适配器
 */
public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder>{

    private Context mContext;
    private List<OrderModelInfo> list = new ArrayList<>();
    private LayoutInflater inflater = null;

    private ChangePicInterface changePicInterface;//修改照片回调函数

    private String sendNo;

    public OrderDetailAdapter(Context context, String sendNo){
        this.mContext = context;
        this.sendNo = sendNo;
        // 布局装载器对象
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<OrderModelInfo> lists){
        list.clear();
        list.addAll(lists);
        notifyDataSetChanged();
    }

    //修改照片
    public void changePicOnclick(ChangePicInterface changePicInterface){
        this.changePicInterface=changePicInterface;
    }

    public interface ChangePicInterface{
        public void onclick(View view, int position);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_detail, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        if (position<9){
            holder.errorIdTv.setText("0"+(position+1));
        }else {
            holder.errorIdTv.setText((position+1)+"");
        }

        OrderModelInfo bean = list.get(position);
        holder.orderNameTv.setText(bean.getCustName());
        if (TextUtils.isEmpty(sendNo)){
            holder.orderSnoTv.setText("暂无数据");
        }else {
            holder.orderSnoTv.setText(sendNo);
        }
        holder.orderNoTv.setText(bean.getCustId());
        holder.orderManagerTv.setText(bean.getManager());
        holder.orderPhoneTv.setText(bean.getManagerTel());
        LogUtils.e("@@@@@@@@@@@"+bean.getOrderDetails().size());
        int count = 0;
        for (int i = 0; i < bean.getOrderDetails().size(); i++ ){
            count += bean.getOrderDetails().get(i).getCount();
            LogUtils.e("@@@@@@@@@@@"+bean.getOrderDetails().get(i).getCount());
        }
        holder.orderNumTv.setText(count+"条/"+bean.getOrderDetails().size()+"包");
        holder.orderAreaTv.setText(bean.getAddr());

        holder.changePicTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changePicInterface != null){
                    changePicInterface.onclick(v,position);
                }
            }
        });
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
        @BindView(R.id.tv_change_picture) TextView changePicTv;//更改照片
        @BindView(R.id.tv_order_name) TextView orderNameTv;//商户名
        @BindView(R.id.tv_order_sno) TextView orderSnoTv;//领货单号
        @BindView(R.id.tv_order_no) TextView orderNoTv;//订单号
        @BindView(R.id.tv_order_manager) TextView orderManagerTv;//姓名
        @BindView(R.id.tv_order_phone) TextView orderPhoneTv;//电话
        @BindView(R.id.tv_order_num) TextView orderNumTv;//数量
        @BindView(R.id.tv_order_area) TextView orderAreaTv;//位置

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
