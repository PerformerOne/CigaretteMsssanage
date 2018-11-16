package com.hupo.cigarette.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HSendLineModel;

import java.util.ArrayList;
import java.util.List;

public class LineListAdapter extends BaseRecyclerViewAdapter{

    private List<HSendLineModel> data=new ArrayList<>();
    private Context context;

    public LineListAdapter(Context context, RecyclerView view){
        this.context=context;
        view.setLayoutManager(new LinearLayoutManager(this.context));
        view.setAdapter(this);
    }

    public void setData(List<HSendLineModel> data){
        this.data.clear();
        if (data!=null){
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public int bindView(int viewtype) {
        return R.layout.item_line_details;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TextView name=viewHolder.obtainView(R.id.tv_item_line_name);
        TextView shop=viewHolder.obtainView(R.id.tv_item_line_shops);
        TextView locShop=viewHolder.obtainView(R.id.tv_item_line_locshops);

        name.setText(data.get(i).getName());
        shop.setText("商户数："+(TextUtils.isEmpty(data.get(i).getCustCount())?0:data.get(i).getCustCount()));
        locShop.setText("有坐标商户数："+(TextUtils.isEmpty(data.get(i).getHpCustCount())?0:data.get(i).getHpCustCount()));


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public HSendLineModel getItem(int position) {
        return data.get(position);
    }
}
