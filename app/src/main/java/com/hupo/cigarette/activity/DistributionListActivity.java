package com.hupo.cigarette.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.adapter.DistributionListAdapter;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.net.DefaultObserver;
import com.hupo.cigarette.net.IdeaApi;
import com.hupo.cigarette.utils.HttpParamsUtil;
import com.hupo.cigarette.utils.IntentUtils;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.utils.ToastUtils;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.SendNoAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HSendNoModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gemini on 2018/11/5.
 *
 * 配送单
 */
public class DistributionListActivity extends BaseActivity {

    @BindView(R.id.tv_title) TextView titleTv;//标题
    @BindView(R.id.lay_back) LinearLayout backLay;//返回
    @BindView(R.id.rv_merchant_info) RecyclerView distributionListRv;//配送单列表
    private DistributionListAdapter distributionListAdapter;//配送单列表适配
    private List<HSendNoModel> sendNos = new ArrayList<>();//配送单列表
    @BindView(R.id.refreshLayout) RefreshLayout mRefreshLayout;//下拉刷新

    @Override
    public int intiLayout() {
        return R.layout.activity_merchant_info;
    }

    @Override
    public void initView() {
        titleTv.setText(getString(R.string.title_distribution));//设置标题
        backLay.setVisibility(View.VISIBLE);
        //初始化RecyclerView
        distributionListRv.setLayoutManager(new LinearLayoutManager(this));
        distributionListRv.setAdapter(distributionListAdapter = new DistributionListAdapter(this));

    }

    @Override
    public void initData() {
        Map<String,Object> map = new HashMap<>();
        map.put("searchData",new HSendNoModel());
        IdeaApi.getApiService()
                .getSendNos(HttpParamsUtil.setParams(map))
                .compose(this.<SendNoAdapter>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<SendNoAdapter>(this, SendNoAdapter.class) {
                    @Override
                    public void onSuccess(SendNoAdapter jsonAdapter) {
                        sendNos.clear();

                        if (jsonAdapter.getSendNoList()!=null&&jsonAdapter.getSendNoList().size()>0){
                            sendNos.addAll(jsonAdapter.getSendNoList());
                        }
                        distributionListAdapter.setData(sendNos);
                    }

                    @Override
                    public void onFail(SendNoAdapter jsonAdapter) {
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

        distributionListAdapter.setOnItemClickListener(new DistributionListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                IntentUtils.startAtyWithSingleParam(DistributionListActivity.this,BillGoodsActivity.class,"sendNo",sendNos.get(position).getNo());
            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(DistributionListActivity.this, 0,null);
    }
}
