package com.hupo.cigarette.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HSendAreaModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 区域details适配器
 */
public class AreaListAdapter extends BaseRecyclerViewAdapter{

    private Context context;
    private List<HSendAreaModel> data=new ArrayList<>();

    public AreaListAdapter(Context context, RecyclerView view) {
        this.context = context;
        view.setLayoutManager(new LinearLayoutManager(this.context));
        view.setAdapter(this);
    }


    public void setData(List<HSendAreaModel> data){
        this.data.clear();
        if (data!=null){
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public int bindView(int viewtype) {
        return R.layout.item_area_details;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TextView name=viewHolder.obtainView(R.id.tv_item_area_name);
        TextView lines=viewHolder.obtainView(R.id.tv_item_area_lines);
        TextView shops=viewHolder.obtainView(R.id.tv_item_area_shops);
        TextView locShops=viewHolder.obtainView(R.id.tv_item_area_locShop);

        name.setText(data.get(i).getName());
        lines.setText("线路数："+data.get(i).getLineCount());
        shops.setText("商户数："+(TextUtils.isEmpty(data.get(i).getCustCount())?0:data.get(i).getCustCount()));
        locShops.setText("有坐标商户数："+(TextUtils.isEmpty(data.get(i).getHpCustCount())?0:data.get(i).getHpCustCount()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public HSendAreaModel getItem(int position) {
        return data.get(position);
    }
}
