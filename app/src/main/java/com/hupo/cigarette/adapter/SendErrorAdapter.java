package com.hupo.cigarette.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hupo.cigarette.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gemini on 2018/10/26.
 *
 * 配送-异常订单适配器
 */
public class SendErrorAdapter extends RecyclerView.Adapter<SendErrorAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> list;
    private LayoutInflater inflater = null;

    private ErrorBtnInterface errorBtnInterface;//异常按钮回调函数
    private DetailBtnInterface detailBtnInterface;//详情按钮回调函数
    private PhoneBtnInterface phoneBtnInterface;//电话按钮回调函数
    private NavigationBtnInterface navigationBtnInterface;//导航按钮回调函数
    private ConfirmBtnInterface confirmBtnInterface;//确认收货按钮回调函数

    public SendErrorAdapter(Context context, List<String> lists){
        this.mContext = context;
        this.list = lists;
        // 布局装载器对象
        inflater = LayoutInflater.from(context);
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

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_send_error, viewGroup, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        if (position<9){
            holder.errorIdTv.setText("0"+(position+1));
        }else {
            holder.errorIdTv.setText((position+1)+"");
        }

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

        @BindView(R.id.tv_error_id) TextView errorIdTv;//序号
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
