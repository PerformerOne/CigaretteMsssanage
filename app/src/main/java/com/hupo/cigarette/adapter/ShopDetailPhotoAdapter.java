package com.hupo.cigarette.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hupo.cigarette.R;
import com.hupo.cigarette.net.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShopDetailPhotoAdapter extends BaseRecyclerViewAdapter{
    private List<File> shops =new ArrayList<>();
    private Context context;

    public ShopDetailPhotoAdapter(Context context, RecyclerView view) {
        this.context = context;
        view.setLayoutManager(new GridLayoutManager(context,3));
        view.setAdapter(this);
    }

    public void setData(List<File> files){
        shops.clear();
        if (files!=null){
            shops.addAll(files);
        }
        notifyDataSetChanged();
    }

    @Override
    public int bindView(int viewtype) {
        return R.layout.item_shopdetail_photo;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ImageView view = viewHolder.obtainView(R.id.iv);
        if (i==shops.size()){
            view.setImageResource(R.mipmap.ic_addphoto);
        }else{
            Glide.with(context).load(shops.get(i)).placeholder(Constants.PLACEHOLD_RES).into(view);
        }
    }

    @Override
    public int getItemCount() {
        return shops.size()+1;
    }

    @Override
    public File getItem(int position) {
        return shops.get(position);
    }

    public int getDataSize() {
        return shops.size();
    }

    public void addData(File file) {
        shops.add(file);
        notifyDataSetChanged();
    }

    public void clear() {
        shops.clear();
        notifyDataSetChanged();
    }
}
