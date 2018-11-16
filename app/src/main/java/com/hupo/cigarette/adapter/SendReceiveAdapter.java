package com.hupo.cigarette.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.dao.db.DrawInvsDetailInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gemini on 2018/10/26.
 *
 * 配送-领货单适配器
 */
public class SendReceiveAdapter extends RecyclerView.Adapter<SendReceiveAdapter.MyViewHolder>{

    private Context mContext;
    private List<DrawInvsDetailInfo> list = new ArrayList<>();
    private LayoutInflater inflater = null;

    private MapBtnInterface mapBtnInterface;//地图按钮回调函数
    private DetailBtnInterface detailBtnInterface;//详情按钮回调函数

    public SendReceiveAdapter(Context context){
        this.mContext = context;
        // 布局装载器对象
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<DrawInvsDetailInfo> lists){
        list.clear();
        list.addAll(lists);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_send_receive, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;

    }

    //地图
    public void mapOnclick(MapBtnInterface mapBtnInterface){
        this.mapBtnInterface=mapBtnInterface;
    }

    public interface MapBtnInterface{
        public void onclick(View view, int position);
    }


    //详情
    public void detailOnclick(DetailBtnInterface detailBtnInterface){
        this.detailBtnInterface=detailBtnInterface;
    }

    public interface DetailBtnInterface{
        public void onclick(View view, int position);
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
        //地图
        holder.receiveMapTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapBtnInterface != null){
                    mapBtnInterface.onclick(v,position);
                }
            }
        });

        //详情
        holder.receiveDetailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailBtnInterface != null){
                    detailBtnInterface.onclick(v,position);
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

        @BindView(R.id.tv_send_rno) TextView receiveNoTv;//单号
        @BindView(R.id.tv_send_road) TextView receiveRoadTv;//线路
        @BindView(R.id.tv_send_rnum) TextView receiveNumTv;//数量
        @BindView(R.id.tv_send_rarea) TextView receiveAreaTv;//区域
        @BindView(R.id.tv_send_rtime) TextView receiveDateTv;//业务日期
        @BindView(R.id.tv_receive_map) TextView receiveMapTv;//地图
        @BindView(R.id.tv_receive_detail) TextView receiveDetailTv;//详情

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
