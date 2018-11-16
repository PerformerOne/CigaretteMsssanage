package com.hupo.cigarette.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hupo.cigarette.R;
import com.hupo.cigarette.net.Constants;
import com.hupo.cigarette.oss.OSSDownSomeListener;
import com.hupo.cigarette.oss.OSSFileController;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HCustModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShoperListAdapter extends BaseRecyclerViewAdapter {

    private Context context;
    private List<HCustModel> data=new ArrayList<>();

    public ShoperListAdapter(Context context, RecyclerView view) {
        this.context = context;
        view.setLayoutManager(new LinearLayoutManager(this.context));
        view.setAdapter(this);
    }

    public void setData(List<HCustModel> data) {
        this.data.clear();
        if (data!=null){
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public int bindView(int viewtype) {
        return R.layout.item_shoper_details;
    }

    public void getPosition(int position){

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ImageView head=viewHolder.obtainView(R.id.iv_item_shoper_head);
        TextView name=viewHolder.obtainView(R.id.tv_item_shoper_name);
        TextView num=viewHolder.obtainView(R.id.tv_item_shoper_num);
        TextView code=viewHolder.obtainView(R.id.tv_item_shoper_code);
        TextView phone=viewHolder.obtainView(R.id.tv_item_shoper_phone);
        TextView manager=viewHolder.obtainView(R.id.tv_item_shoper_manager);
        TextView address=viewHolder.obtainView(R.id.tv_item_shoper_address);

        num.setText(i>8?""+(i+1):"0"+(i+1));
        name.setText(data.get(i).getName());
        code.setText("商户编号："+data.get(i).getId());
        phone.setText("电话："+data.get(i).getManagerTel());
        manager.setText("负责人："+data.get(i).getManager());
        address.setText(data.get(i).getAddr());
//        OSSFileController.getController().downloadFiles(i, Constants.OSS_PROFIX + data.get(i).getId(), new OSSDownSomeListener() {
//            @Override
//            public void onProgress(int i, long currentSize, long totalSize) {
//
//            }
//
//            @Override
//            public void onSuccess(int i, Map<String, Drawable> map) {
//                for (String s : map.keySet()) {
//                    head.setImageDrawable(map.get(s));
//                }
//            }
//
//            @Override
//            public void onFailure(int i, String errMsg) {
//                LogUtils.e("1111111111111"+errMsg);
//            }
//        });

        OSSFileController.getController().getImageByProfix(i, Constants.OSS_PROFIX+data.get(i).getId(), new OSSDownSomeListener() {
            @Override
            public void onProgress(int i, long currentSize, long totalSize) {

            }

            @Override
            public void onSuccess(int i, Map<String, File> map) {
                if (map.keySet().size()>0) {
                    Glide.with(context).load(map.get(map.keySet().iterator().next())).placeholder(Constants.PLACEHOLD_RES).into(head);
                }
            }

            @Override
            public void onFailure(int i, String errMsg) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public HCustModel getItem(int position) {
        return data.get(position);
    }
}
