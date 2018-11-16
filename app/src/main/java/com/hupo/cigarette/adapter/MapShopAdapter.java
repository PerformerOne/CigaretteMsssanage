package com.hupo.cigarette.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hupo.cigarette.R;
import com.hupo.cigarette.dao.db.OrderModelInfo;
import com.hupo.cigarette.net.Constants;
import com.hupo.cigarette.oss.IMAGE_TYPE;
import com.hupo.cigarette.oss.OSSDownloadListener;
import com.hupo.cigarette.oss.OSSFileController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gemini on 2018/11/5.
 *
 * 地图商户详情
 */
public class MapShopAdapter extends BaseAdapter {

    private OnClickInterface onClickInterface;//单击事件回调函数
    private Context mContext;
    private List<OrderModelInfo> list = new ArrayList<>();
    private String sendNo;//领货单单号

    public MapShopAdapter(Context context,String sendNo) {
        this.mContext = context;
        this.sendNo = sendNo;
    }

    public void setData(List<OrderModelInfo> lists){
        list.clear();
        list.addAll(lists);
        notifyDataSetChanged();
    }

    //单击
    public void Onclick(OnClickInterface onClickInterface){
        this.onClickInterface=onClickInterface;
    }

    public interface OnClickInterface{
        public void onclick(View view, int position);
    }

    @Override
    public int getCount() {
        return list.size() > 0 ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder;

        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.item_map_shop, null);
            viewholder = new ViewHolder(convertView);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        if (position < 10){
            viewholder.shopIdTv.setText("0" + (position+1));
        }else {
            viewholder.shopIdTv.setText("" + (position+1));
        }

        OrderModelInfo bean = list.get(position);

        viewholder.shopNameTv.setText(bean.getCustName());
        if (TextUtils.isEmpty(sendNo)){
            viewholder.shopNoTv.setText("暂无数据");
        }else {
            viewholder.shopNoTv.setText(sendNo);
        }
        viewholder.shopSnoTv.setText(bean.getCustId());
        viewholder.shopManagerTv.setText(bean.getManager());
        viewholder.shopPhoneTv.setText(bean.getManagerTel());
        viewholder.shopNumTv.setText(bean.getCount()+"条/"+bean.getBagCount()+"包");
        viewholder.shopAreaTv.setText(bean.getAddr());

        OSSDownloadListener listener = new OSSDownloadListener() {
            @Override
            public void onProgress(int i, long currentSize, long totalSize) {

            }

            @Override
            public void onSuccess(int i, Drawable result) {

            }

            @Override
            public void onFailure(int i, String errMsg) {

            }

            @Override
            public void onSuccess(int i, File file) {
                Glide.with(mContext).load(file).placeholder(Constants.PLACEHOLD_RES).into(viewholder.shopImgIv);

            }
        };

        OSSFileController.getController().getImageFile(position, IMAGE_TYPE.SHOP_HEAD,  "140402100046/","8D564D396C1E4269A3FDCE66A7C640AA.png", listener);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickInterface != null){
                    onClickInterface.onclick(v,position);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView shopImgIv;
        TextView shopIdTv,shopNameTv,shopNoTv,shopSnoTv,shopManagerTv,shopPhoneTv,shopNumTv,shopAreaTv;

        public ViewHolder(View view) {
            shopImgIv = view.findViewById(R.id.tv_shop_img);
            shopIdTv = view.findViewById(R.id.tv_shop_id);
            shopNameTv = view.findViewById(R.id.tv_shop_name);
            shopNoTv = view.findViewById(R.id.tv_shop_no);
            shopSnoTv = view.findViewById(R.id.tv_shop_sno);
            shopManagerTv = view.findViewById(R.id.tv_shop_manager);
            shopPhoneTv = view.findViewById(R.id.tv_shop_phone);
            shopNumTv = view.findViewById(R.id.tv_shop_num);
            shopAreaTv = view.findViewById(R.id.tv_shop_area);
        }
    }
}
