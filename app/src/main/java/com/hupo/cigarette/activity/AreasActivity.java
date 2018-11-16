package com.hupo.cigarette.activity;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.adapter.AreaListAdapter;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.net.DefaultObserver;
import com.hupo.cigarette.net.IdeaApi;
import com.hupo.cigarette.utils.HttpParamsUtil;
import com.hupo.cigarette.utils.IntentUtils;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.utils.ToastUtils;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.SendAreaAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HSendAreaModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.hupo.cigarette.activity.LineActivity.LINE_AREA;

/**
 * 区域
 *
 */
public class AreasActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView titleTv;//标题
    @BindView(R.id.lay_back)
    LinearLayout backLay;//返回
    @BindView(R.id.recycler_area_data)
    RecyclerView recyclerView;
    @BindView(R.id.et_areas_search)
    EditText searchEd;

    //details适配器
    private AreaListAdapter adapter;
    private List<HSendAreaModel> data=new ArrayList<>();

    @Override
    public int intiLayout() {
        return R.layout.activity_areas;
    }

    @Override
    public void initView() {
        titleTv.setText(getString(R.string.title_area));//设置标题
        backLay.setVisibility(View.VISIBLE);
        adapter=new AreaListAdapter(this,recyclerView);
    }

    @Override
    public void initData() {
        IdeaApi.getApiService()
                .getAreaInfo(HttpParamsUtil.setParams(new HashMap<>()))
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<SendAreaAdapter>(this, SendAreaAdapter.class) {
                    @Override
                    public void onSuccess(SendAreaAdapter jsonAdapter) {
                        data.addAll(jsonAdapter.getSendAreas());
                        adapter.setData(jsonAdapter.getSendAreas());
                    }

                    @Override
                    public void onFail(SendAreaAdapter jsonAdapter) {
                        super.onFail(jsonAdapter);
                        ToastUtils.show(jsonAdapter.getMsg());
                    }
                });
    }

    //事件监听方法
    @Override
    public void initListener() {
        adapter.setOnItemClickListener((adapter1,holder,view,position)->{
            IntentUtils.startAtyWithSerialObj(this,LineActivity.class,LINE_AREA,AreasActivity.this.adapter.getItem(position));
        });
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
                List<HSendAreaModel> d=new ArrayList<>();
                for (HSendAreaModel datum : data) {
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
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(AreasActivity.this, 0,null);
    }

}
