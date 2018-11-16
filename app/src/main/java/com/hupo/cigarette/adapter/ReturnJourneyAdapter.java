package com.hupo.cigarette.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.navi.model.AMapNaviPath;
import com.hupo.cigarette.R;
import com.hupo.cigarette.utils.SecondsUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gemini on 2018/10/26.
 *
 * 导航路线适配器
 */
public class ReturnJourneyAdapter extends RecyclerView.Adapter<ReturnJourneyAdapter.MyViewHolder> implements View.OnClickListener{

    private Context mContext;
    HashMap<Integer, AMapNaviPath> paths = new HashMap<>();
    private LayoutInflater inflater = null;
    int[] ints;//路线id 12 13 14
    private int routeID = 0;

    public ReturnJourneyAdapter(Context context){
        this.mContext = context;
        // 布局装载器对象
        inflater = LayoutInflater.from(context);
    }

    public void setData(int[] ints,HashMap<Integer, AMapNaviPath> path){
        this.ints = ints;
        paths.clear();
        paths.putAll(path);
        notifyDataSetChanged();
    }


    public void setRouteID(int routeID){
        this.routeID = routeID;
        notifyDataSetChanged();
    }

    private OnItemClickListener mOnItemClickListener = null;

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_return_journey, viewGroup, false);
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
        AMapNaviPath path = paths.get(ints[position]);
        if (path != null) {
            String time = SecondsUtils.secondToTime(path.getAllTime());
            holder.returnDetailTv.setText(path.getLabels());
            holder.returnTimeTv.setText(time);
            holder.returnDistanceTv.setText(Math.round((path.getAllLength()/100d)/10d) + "公里");
        }
        if (position == routeID){
            holder.returnDetailTv.setTextColor(mContext.getResources().getColor(R.color.tF87E3D));
            holder.returnTimeTv.setTextColor(mContext.getResources().getColor(R.color.tF87E3D));
            holder.returnDistanceTv.setTextColor(mContext.getResources().getColor(R.color.tF87E3D));
        }else {
            holder.returnDetailTv.setTextColor(mContext.getResources().getColor(R.color.t413D3B));
            holder.returnTimeTv.setTextColor(mContext.getResources().getColor(R.color.t413D3B));
            holder.returnDistanceTv.setTextColor(mContext.getResources().getColor(R.color.t413D3B));
        }
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount()
    {
        return paths.size()>0 ? paths.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_return_detail) TextView returnDetailTv;
        @BindView(R.id.tv_return_time) TextView returnTimeTv;
        @BindView(R.id.tv_return_distance) TextView returnDistanceTv;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
