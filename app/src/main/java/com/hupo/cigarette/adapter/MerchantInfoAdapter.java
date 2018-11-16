package com.hupo.cigarette.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hupo.cigarette.R;
import com.hupo.cigarette.net.Constants;
import com.hupo.cigarette.oss.IMAGE_TYPE;
import com.hupo.cigarette.oss.OSSDownloadListener;
import com.hupo.cigarette.oss.OSSFileController;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HOrderModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gemini on 2018/10/26.
 *
 * 商户列表适配器
 */
public class MerchantInfoAdapter extends RecyclerView.Adapter<MerchantInfoAdapter.MyViewHolder>{

    private Context mContext;
    private List<HOrderModel> list = new ArrayList<>();
    private LayoutInflater inflater = null;
    private String sendNo;//领货单单号

    public MerchantInfoAdapter(Context context,String sendNo){
        this.mContext = context;
        this.sendNo = sendNo;
        // 布局装载器对象
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<HOrderModel> lists){
        list.clear();
        list.addAll(lists);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_merchant_info, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        HOrderModel bean = list.get(position);

        holder.merchantSname.setText(bean.getCustName());
        if (TextUtils.isEmpty(sendNo)){
            holder.merchantNo.setText("暂无数据");
        }else {
            holder.merchantNo.setText(sendNo);
        }
        holder.merchantSno.setText(bean.getCustId());
        holder.merchantName.setText(bean.getManager());
        holder.merchantPhone.setText(bean.getManagerTel());
        holder.merchantNum.setText(bean.getCount()+"条/"+bean.getBagCount()+"包");
        holder.merchantArea.setText(bean.getAddr());

        if (position<9){
            holder.merchantIdTv.setText("0"+(position+1));
        }else {
            holder.merchantIdTv.setText((position+1)+"");
        }

        holder.startInfoLay.setVisibility(View.GONE);

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
                Glide.with(mContext).load(file).placeholder(Constants.PLACEHOLD_RES).into(holder.merchantImg);

            }
        };

        OSSFileController.getController().getImageFile(position, IMAGE_TYPE.SHOP_HEAD,  "140402100046/","8D564D396C1E4269A3FDCE66A7C640AA.png", listener);
    }

    @Override
    public int getItemCount()
    {
        return list.size()>0 ? list.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_merchant_img) ImageView merchantImg;//图片
        @BindView(R.id.tv_merchant_sname) TextView merchantSname;//商户名称
        @BindView(R.id.tv_merchant_no) TextView merchantNo;//领货单编号
        @BindView(R.id.tv_merchant_sno) TextView merchantSno;//商户编号
        @BindView(R.id.tv_merchant_name) TextView merchantName;//负责人
        @BindView(R.id.tv_merchant_phone) TextView merchantPhone;//电话
        @BindView(R.id.tv_merchant_num) TextView merchantNum;//数量
        @BindView(R.id.tv_merchant_area) TextView merchantArea;//地址

        @BindView(R.id.tv_merchant_id) TextView merchantIdTv;
        @BindView(R.id.lay_start_info) LinearLayout startInfoLay;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
