package com.hupo.cigarette.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.activity.MessageActivity;
import com.hupo.cigarette.adapter.UserListAdapter;
import com.hupo.cigarette.base.BaseFragment;
import com.hupo.cigarette.net.Constants;
import com.hupo.cigarette.net.DefaultObserver;
import com.hupo.cigarette.net.IdeaApi;
import com.hupo.cigarette.test.TestData;
import com.hupo.cigarette.utils.HttpParamsUtil;
import com.hupo.cigarette.utils.IntentUtils;
import com.hupo.cigarette.utils.PreferUtils;
import com.hupo.cigarette.utils.ToastUtils;
import com.huposoft.commons.utils.MD5;
import com.huposoft.softs.chuanchuan.platform.app.api.LoginResultAdapter;
import com.huposoft.softs.chuanchuan.platform.app.api.UserAdapter;
import com.huposoft.softs.chuanchuan.platform.app.api.models.HUserModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class UserFragment extends BaseFragment {

    @BindView(R.id.tv_title) TextView titleTv;//标题
    @BindView(R.id.et_user_search) EditText userSearchEt;//联系人搜索
    @BindView(R.id.rv_user_list) RecyclerView userListRv;//联系人列表
    @BindView(R.id.refreshLayout) RefreshLayout mRefreshLayout;
    private UserListAdapter userListAdapter;//联系人适配
    private List<HUserModel> users = new ArrayList<>();

    @Override
    public int intiLayout() {
        return R.layout.fragment_user;
    }

    @Override
    public void initView() {
        //初始化RecyclerView
        userListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        userListRv.setAdapter(userListAdapter = new UserListAdapter(getActivity()));
        titleTv.setText(getString(R.string.title_user));//设置标题
    }

    @Override
    public void initData() {
        IdeaApi.getApiService()
                .getPersonInfos(HttpParamsUtil.setParam(new HashMap<>()))
                .compose(this.<UserAdapter>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<UserAdapter>(getActivity(), UserAdapter.class) {
                    @Override
                    public void onSuccess(UserAdapter jsonAdapter) {
                        users.clear();
                        users.addAll(jsonAdapter.getUsers());
                        userListAdapter.setData(users);
                    }

                    @Override
                    public void onFail(UserAdapter jsonAdapter) {
                        super.onFail(jsonAdapter);
                        ToastUtils.show(jsonAdapter.getMsg());
                    }
                });
    }

    @Override
    public void initListener() {

        //刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData();
                refreshlayout.finishRefresh();
            }
        });

        userListAdapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                IntentUtils.startAtyWithSingleParam(getActivity(),MessageActivity.class,"userName",users.get(position).getRealName());
            }
        });
    }

}
