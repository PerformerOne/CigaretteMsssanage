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
import com.hupo.cigarette.dao.db.OrderModelInfo;
import com.hupo.cigarette.net.Constants;
import com.hupo.cigarette.oss.IMAGE_TYPE;
import com.hupo.cigarette.oss.OSSDownloadListener;
import com.hupo.cigarette.oss.OSSFileController;

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
public class ReceiveMerchantInfoAdapter extends RecyclerView.Adapter<ReceiveMerchantInfoAdapter.MyViewHolder>{

    private Context mContext;
    private List<OrderModelInfo> list = new ArrayList<>();
    private LayoutInflater inflater = null;
    private int type;//0 隐藏底部布局 1显示
    private String sendNo;//领货单单号

    private ErrorBtnInterface errorBtnInterface;//异常按钮回调函数
    private DetailBtnInterface detailBtnInterface;//详情按钮回调函数
    private PhoneBtnInterface phoneBtnInterface;//电话按钮回调函数
    private NavigationBtnInterface navigationBtnInterface;//导航按钮回调函数
    private ConfirmBtnInterface confirmBtnInterface;//确认收货按钮回调函数

    public ReceiveMerchantInfoAdapter(Context context, int type, String sendNo){
        this.mContext = context;
        this.type = type;
        this.sendNo = sendNo;
        // 布局装载器对象
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<OrderModelInfo> lists){
        list.clear();
        list.addAll(lists);
        notifyDataSetChanged();
    }

    //异常
    public void errorOnclick(ErrorBtnInterface errorBtnInterface){
        this.errorBtnInterface=errorBtnInterface;
    }

    public interface ErrorBtnInterface{
        public void onclick(View view, int position);
    }

    //详情
    public void detailOnclick(DetailBtnInterface detailBtnInterface){
        this.detailBtnInterface=detailBtnInterface;
    }

    public interface DetailBtnInterface{
        public void onclick(View view, int position);
    }

    //电话
    public void phoneOnclick(PhoneBtnInterface phoneBtnInterface){
        this.phoneBtnInterface=phoneBtnInterface;
    }

    public interface PhoneBtnInterface{
        public void onclick(View view, int position);
    }

    //导航
    public void navigationOnclick(NavigationBtnInterface navigationBtnInterface){
        this.navigationBtnInterface=navigationBtnInterface;
    }

    public interface NavigationBtnInterface{
        public void onclick(View view, int position);
    }

    //确认收货
    public void confirmOnclick(ConfirmBtnInterface confirmBtnInterface){
        this.confirmBtnInterface=confirmBtnInterface;
    }

    public interface ConfirmBtnInterface{
        public void onclick(View view, int position);
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
        OrderModelInfo bean = list.get(position);

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

        if (type == 0){
            holder.startInfoLay.setVisibility(View.GONE);
        }else {
            holder.startInfoLay.setVisibility(View.VISIBLE);
        }


        OSSFileController.getController().getImageFile(position, IMAGE_TYPE.SHOP_HEAD, "140402100046/", "8D564D396C1E4269A3FDCE66A7C640AA.png", new OSSDownloadListener() {
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
        });

        //异常
        holder.sendErrorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (errorBtnInterface != null){
                    errorBtnInterface.onclick(v,position);
                }
            }
        });

        //详情
        holder.sendDetailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailBtnInterface != null){
                    detailBtnInterface.onclick(v,position);
                }
            }
        });

        //电话
        holder.sendPhoneTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneBtnInterface != null){
                    phoneBtnInterface.onclick(v,position);
                }
            }
        });

        //导航
        holder.sendNavigationTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navigationBtnInterface != null){
                    navigationBtnInterface.onclick(v,position);
                }
            }
        });

        //确认收货
        holder.sendConfirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmBtnInterface != null){
                    confirmBtnInterface.onclick(v,position);
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
        @BindView(R.id.tv_send_error) TextView sendErrorTv;//异常
        @BindView(R.id.tv_send_detail) TextView sendDetailTv;//详情
        @BindView(R.id.tv_send_phone) TextView sendPhoneTv;//电话
        @BindView(R.id.tv_send_navigation) TextView sendNavigationTv;//导航
        @BindView(R.id.tv_send_confirm) TextView sendConfirmTv;//到货确认

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
