package com.hupo.cigarette.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.adapter.MessageDetailAdapter;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.test.TestData;
import com.hupo.cigarette.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Gemini on 2018/11/2.
 *
 * 消息详情列表
 */
public class MessageActivity extends BaseActivity {


    @BindView(R.id.tv_title) TextView titleTv;//标题
    @BindView(R.id.lay_back) LinearLayout backLay;//返回
    @BindView(R.id.rv_message_detail) RecyclerView messageDetailRv;//消息详情列表
    @BindView(R.id.iv_message_add) ImageView messageAddIv;//添加按钮
    @BindView(R.id.lay_message_slip) LinearLayout messageSlipLay;//转单布局
    private MessageDetailAdapter messageDetailAdapter;//消息列表详情适配器

    private String userName;//选择的用户名

    @Override
    public int intiLayout() {
        return R.layout.activity_message_detail;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        titleTv.setText(userName);
        backLay.setVisibility(View.VISIBLE);

        //初始化RecyclerView
        messageDetailRv.setLayoutManager(new LinearLayoutManager(this));
        messageDetailRv.setAdapter(messageDetailAdapter = new MessageDetailAdapter(this,TestData.getTestData(1)));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.iv_message_add,R.id.lay_message})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.lay_message:
                messageAddIv.setImageDrawable(getDrawable(R.mipmap.icon_message_add_g));
                messageSlipLay.setVisibility(View.GONE);
                break;
            //添加按钮 显示布局
            case R.id.iv_message_add:
                messageAddIv.setImageDrawable(getDrawable(R.mipmap.icon_message_add_o));
                messageSlipLay.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(MessageActivity.this, 0,null);
    }
}
