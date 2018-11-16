package com.hupo.cigarette.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.dao.db.SendNoAdapterInfo;
import com.hupo.cigarette.dao.helper.DrawInvsDetailHelper;
import com.hupo.cigarette.dao.helper.SendNoInfoHelper;
import com.hupo.cigarette.fragment.PrepareErrorFragment;
import com.hupo.cigarette.fragment.PrepareReceiveFragment;
import com.hupo.cigarette.net.DefaultObserver;
import com.hupo.cigarette.net.IdeaApi;
import com.hupo.cigarette.utils.HttpParamsUtil;
import com.hupo.cigarette.utils.IntentUtils;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.utils.ToastUtils;
import com.hupo.cigarette.widget.dialog.CenterAlertDialog;
import com.huposoft.commons.json.JsonAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.SendNoAdapter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gemini on 2018/10/30.
 *
 * 准备配送
 */
public class PrepareSendActivity extends BaseActivity {

    @BindView(R.id.tv_title) TextView titleTv;//标题
    @BindView(R.id.lay_back) LinearLayout backLay;//返回
    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.toolbar_tab) TabLayout mTabLayout;
    PrepareReceiveFragment prepareReceiveFragment;//领货单
    PrepareErrorFragment prepareErrorFragment;//异常订单
    PagerAdapter mPagerAdapter;


    private String carId,
                   sendNo;


    @Override
    public int intiLayout() {
        return R.layout.activity_prepare_send;
    }

    @Override
    public void initView() {
        SendNoAdapterInfo sendNoAdapterInfo = SendNoInfoHelper.getInstance().getDetail();
        carId = sendNoAdapterInfo.getCarId();
        sendNo = sendNoAdapterInfo.getNo();

        titleTv.setText(getString(R.string.title_prepare));//设置标题
        backLay.setVisibility(View.VISIBLE);

        mViewPager.setOffscreenPageLimit(2);
        prepareReceiveFragment = new PrepareReceiveFragment();
        prepareErrorFragment = new PrepareErrorFragment();

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
    }

    @OnClick({R.id.tv_prepare_start,R.id.tv_aboard_cancel})
    public void OnClick(View view){
        switch (view.getId()){
            //取消上车
            case R.id.tv_aboard_cancel:
                cancelGetOnCar();

                break;
            //开始配送
            case R.id.tv_prepare_start:
                new CenterAlertDialog(PrepareSendActivity.this, "开始配送吗？" , "确定", "取消", new CenterAlertDialog.Listener() {
                    @Override
                    public void onLeftClick() {
                        startSend();

                    }

                    @Override
                    public void onRightClick() {
                    }
                }).show();
                break;
        }
    }

    //开始配送
    private void startSend() {
        Map<String, Object> map = new HashMap<>();
        map.put("carId",carId);
        map.put("sendNo",sendNo);
        IdeaApi.getApiService()
                .affirmOnline(HttpParamsUtil.setParams(map))
                .compose(this.<SendNoAdapter>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<SendNoAdapter>(this, SendNoAdapter.class) {
                    @Override
                    public void onSuccess(SendNoAdapter jsonAdapter) {
                        //修改领货单开始配送状态
                        DrawInvsDetailHelper.getInstance().getPrepareSend(sendNo,carId);
                        IntentUtils.startAty(PrepareSendActivity.this,PrepareStartActivity.class);
                        finish();
                    }

                    @Override
                    public void onFail(SendNoAdapter jsonAdapter) {
                        super.onFail(jsonAdapter);
                        ToastUtils.show(jsonAdapter.getMsg());
                    }
                });
    }

    //取消上车
    private void cancelGetOnCar() {
        Map<String, Object> map = new HashMap<>();
        map.put("carId",carId);
        map.put("sendNo",sendNo);
        IdeaApi.getApiService()
                .cancelOnTheCar(HttpParamsUtil.setParams(map))
                .compose(this.<JsonAdapter>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<JsonAdapter>(this, JsonAdapter.class) {
                    @Override
                    public void onSuccess(JsonAdapter jsonAdapter) {
                        SendNoInfoHelper.getInstance().deleteSend(sendNo);
                        DrawInvsDetailHelper.getInstance().cancelGetOnCar(sendNo);
                        IntentUtils.startAty(PrepareSendActivity.this,AboardActivity.class);
                        finish();
                    }

                    @Override
                    public void onFail(JsonAdapter jsonAdapter) {
                        super.onFail(jsonAdapter);
                        ToastUtils.show(jsonAdapter.getMsg());
                    }
                });
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        private Fragment mCurrentFragment;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return prepareReceiveFragment;
            } else if (position == 1) {
                return prepareErrorFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(PrepareSendActivity.this, 0,null);
    }
}
