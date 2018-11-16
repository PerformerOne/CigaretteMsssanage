package com.hupo.cigarette.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.adapter.LineListAdapter;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.net.DefaultObserver;
import com.hupo.cigarette.net.IdeaApi;
import com.hupo.cigarette.utils.HttpParamsUtil;
import com.hupo.cigarette.utils.IntentUtils;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.utils.ToastUtils;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.SendLineAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HSendAreaModel;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.hupo.cigarette.activity.ShoperActivity.LINE_DATA;

public class LineActivity extends BaseActivity {


    public static final String LINE_AREA="LINE_AREA";

    @BindView(R.id.tv_title)
    TextView titleTv;//标题
    @BindView(R.id.lay_back)
    LinearLayout backLay;//返回
    @BindView(R.id.recycler_line_data)
    RecyclerView recyclerView;
    @BindView(R.id.tv_line_lines)
    TextView nameTv;
    @BindView(R.id.tv_line_shops)
    TextView shopTv;
    @BindView(R.id.tv_line_locShop)
    TextView locShopTv;
    @BindView(R.id.tv_line_name)
    TextView areaNameTv;


    private LineListAdapter adapter;

    private HSendAreaModel areaData;

    @Override
    public int intiLayout() {

        return R.layout.activity_line;
    }

    @Override
    public void initView() {

        titleTv.setText(getString(R.string.title_line));//设置标题
        backLay.setVisibility(View.VISIBLE);
        adapter=new LineListAdapter(this,recyclerView);
    }


    @Override
    public void initData() {
        areaData= (HSendAreaModel) getIntent().getSerializableExtra(LINE_AREA);
        areaNameTv.setText(areaData.getName());
        Map<String, String> map = new HashMap<>();
        map.put("areaId",areaData.getUid());
        IdeaApi.getApiService()
                .getLineInfo(HttpParamsUtil.setParam(map))
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<SendLineAdapter>(this, SendLineAdapter.class) {
                    @Override
                    public void onSuccess(SendLineAdapter jsonAdapter) {
                        adapter.setData(jsonAdapter.getSendLines());
                    }

                    @Override
                    public void onFail(SendLineAdapter jsonAdapter) {
                        super.onFail(jsonAdapter);
                        ToastUtils.show(jsonAdapter.getMsg());
                    }
                });

    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener((adapter, holder, view, position) -> {
            IntentUtils.startAtyWithSerialObj(this,ShoperActivity.class, LINE_DATA,LineActivity.this.adapter.getItem(position));
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(LineActivity.this, 0,null);
    }
}
