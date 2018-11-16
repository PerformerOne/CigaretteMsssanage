package com.hupo.cigarette.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hupo.cigarette.R;
import com.hupo.cigarette.adapter.ShoperListAdapter;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.net.DefaultObserver;
import com.hupo.cigarette.net.IdeaApi;
import com.hupo.cigarette.utils.HttpParamsUtil;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.utils.ToastUtils;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.CustAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HCustModel;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HSendLineModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ShoperActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView titleTv;//标题
    @BindView(R.id.lay_back)
    LinearLayout backLay;//返回
    @BindView(R.id.recycler_shoper_details)
    RecyclerView recyclerView;
    @BindView(R.id.tv_shopers_name)
    TextView nameTv;
    @BindView(R.id.tv_shopers_shops)
    TextView shopTv;
    @BindView(R.id.tv_shopers_locshops)
    TextView locShopTv;
    @BindView(R.id.et_shopers_search)
    EditText searchEd;

    public static final String LINE_DATA ="LINE_DATA";

    private HSendLineModel lineData;

    private ShoperListAdapter adapter;
    private List<HCustModel> data=new ArrayList<>();

    @Override
    public int intiLayout() {
        return R.layout.activity_shopers;
    }

    @Override
    public void initView() {
        titleTv.setText(getString(R.string.title_shoper));//设置标题
        backLay.setVisibility(View.VISIBLE);
        adapter=new ShoperListAdapter(this,recyclerView);
    }

    @Override
    public void initData() {
        lineData= (HSendLineModel) getIntent().getSerializableExtra(LINE_DATA);
        nameTv.setText(lineData.getName());
        Map<String,String> para=new HashMap<>();
        para.put("lineId",lineData.getUid());
        IdeaApi.getApiService()
                .getShopList(HttpParamsUtil.setParam(para))
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<CustAdapter>(this, CustAdapter.class) {
                    @Override
                    public void onSuccess(CustAdapter jsonAdapter) {
                        data.addAll(jsonAdapter.getCusts());
                        adapter.setData(jsonAdapter.getCusts());
                    }

                    @Override
                    public void onFail(CustAdapter jsonAdapter) {
                        super.onFail(jsonAdapter);
                        ToastUtils.show(jsonAdapter.getMsg());
                    }
                });
    }

    @Override
    public void initListener() {
        searchEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)){
                    adapter.setData(data);
                    return;
                }
                List<HCustModel> d=new ArrayList<>();
                for (HCustModel datum : data) {
                    if (datum.getName().indexOf(s.toString())!=-1) {
                        d.add(datum);
                    }
                }
                adapter.setData(d);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        adapter.setOnItemClickListener((adapter, holder, view, position) -> {
            Intent i=new Intent(this,ShopDetailActivity.class);
            i.putExtra("Position",position);
            i.putExtra("data",new Gson().toJson(this.adapter.getItem(position)));
            startActivity(i);
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(ShoperActivity.this, 0,null);
    }
}
