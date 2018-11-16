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
import com.hupo.cigarette.dao.db.DrawInvsDetailInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gemini on 2018/10/26.
 *
 * 上车-领货单适配器
 */
public class AboardReceiveAdapter extends RecyclerView.Adapter<AboardReceiveAdapter.MyViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<DrawInvsDetailInfo> list = new ArrayList<>();
    private LayoutInflater inflater = null;

    private CheckBoxInterface checkBoxInterface;//按钮回调函数

    private int type; // 0 显示 1 隐藏
    private Map<Integer, Boolean> map = new HashMap<>();

    public AboardReceiveAdapter(Context context,int type){
        this.mContext = context;
        this.type = type;
        // 布局装载器对象
        inflater = LayoutInflater.from(context);
    }

    public void onCheckedChanged(CheckBoxInterface checkBoxInterface){
        this.checkBoxInterface=checkBoxInterface;
    }

    public interface CheckBoxInterface{

        public void onCheckedChanged(Map<Integer, Boolean> map,int position);
    }

    public void setData(List<DrawInvsDetailInfo> lists){
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

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_receive, viewGroup, false);
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
        DrawInvsDetailInfo bean = list.get(position);
        holder.receiveNoTv.setText("单号/线路:"+bean.getNo()+"/"+bean.getExternalLineId());
        holder.receiveRoadTv.setText("线路:"+bean.getExternalLineId());
        holder.receiveNumTv.setText(bean.getCustomerCount()+"户/"+bean.getGoodsCount()+"条/"+bean.getBagCount()+"包");
        holder.receiveAreaTv.setText("区域:"+bean.getExternalAreaName());
        holder.receiveDateTv.setText("业务日期:"+bean.getBornDate());
        holder.receiveSelectCk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
            holder.receiveSelectCk.setChecked(true);
        } else {
            holder.receiveSelectCk.setChecked(false);
        }

        if (type == 0){
            holder.receiveSelectCk.setVisibility(View.VISIBLE);
        }else {
            holder.receiveSelectCk.setVisibility(View.GONE);
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

        @BindView(R.id.ck_receive_select) CheckBox receiveSelectCk;
        @BindView(R.id.tv_receive_no) TextView receiveNoTv;//单号/线路
        @BindView(R.id.tv_receive_road) TextView receiveRoadTv;//线路
        @BindView(R.id.tv_receive_num) TextView receiveNumTv;//数量
        @BindView(R.id.tv_receive_area) TextView receiveAreaTv;//区域
        @BindView(R.id.tv_receive_date) TextView receiveDateTv;//业务日期

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
