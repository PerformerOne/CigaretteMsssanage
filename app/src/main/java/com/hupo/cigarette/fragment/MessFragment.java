package com.hupo.cigarette.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.activity.MessageActivity;
import com.hupo.cigarette.adapter.MessageListAdapter;
import com.hupo.cigarette.adapter.UserListAdapter;
import com.hupo.cigarette.base.BaseFragment;
import com.hupo.cigarette.test.TestData;
import com.hupo.cigarette.utils.IntentUtils;

import butterknife.BindView;

public class MessFragment extends BaseFragment {

    @BindView(R.id.tv_title) TextView titleTv;//标题
    @BindView(R.id.rv_message_list) RecyclerView messListRv;//消息列表
    private MessageListAdapter messageListAdapter;//消息适配

    @Override
    public int intiLayout() {
        return R.layout.fragment_mess;
    }

    @Override
    public void initView() {

        titleTv.setText(getString(R.string.title_message));//设置标题
        //初始化RecyclerView
        messListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        messListRv.setAdapter(messageListAdapter = new MessageListAdapter(getActivity(),TestData.getTestData(8)));
        messageListAdapter.setOnItemClickListener(new MessageListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                IntentUtils.startAtyWithSingleParam(getActivity(),MessageActivity.class,"userName",TestData.getTestData(8).get(position));
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
