package com.hupo.cigarette.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.net.DefaultObserver;
import com.hupo.cigarette.net.IdeaApi;
import com.hupo.cigarette.utils.HttpParamsUtil;
import com.hupo.cigarette.utils.IntentUtils;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.utils.ToastUtils;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.CarAdapter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gemini on 2018/10/26.
 *
 * 车辆绑定
 */
public class BoundVehiclesActivity extends BaseActivity {

    @BindView(R.id.tv_title) TextView titleTv;//标题
    @BindView(R.id.lay_back) LinearLayout backLay;//返回
    @BindView(R.id.et_bound_search) EditText boundSearchEt;//车牌搜索框


    @Override
    public int intiLayout() {
        return R.layout.activity_bound_vehicles;
    }

    @Override
    public void initView() {
        titleTv.setText(getString(R.string.title_bound_vehicles));//设置标题
        backLay.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(BoundVehiclesActivity.this, 0,null);
    }

    @OnClick({R.id.iv_bound_search})
    public void OnClick(View view){
        switch (view.getId()){
            //搜索
            case R.id.iv_bound_search:
                search();
                break;
        }
    }

    private void search() {
        Map<String, String> map = new HashMap<>();
        map.put("carNum",boundSearchEt.getText().toString());
        IdeaApi.getApiService()
                .bindCar(HttpParamsUtil.setParam(map))
                .compose(this.<CarAdapter>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<CarAdapter>(this, CarAdapter.class) {
                    @Override
                    public void onSuccess(CarAdapter jsonAdapter) {
                        IntentUtils.startAty(BoundVehiclesActivity.this,AboardActivity.class);
                        finish();
                    }

                    @Override
                    public void onFail(CarAdapter jsonAdapter) {
                        super.onFail(jsonAdapter);
                        ToastUtils.show(jsonAdapter.getMsg());
                    }
                });
    }

}
