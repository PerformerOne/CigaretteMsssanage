package com.hupo.cigarette.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import com.hupo.cigarette.bean.MerchantInfo;
import com.hupo.cigarette.dao.helper.CarInfoHelper;
import com.hupo.cigarette.dao.helper.DrawInvsDetailHelper;
import com.hupo.cigarette.dao.helper.SendNoInfoHelper;
import com.hupo.cigarette.fragment.ErrorFragment;
import com.hupo.cigarette.fragment.ReceiveFragment;
import com.hupo.cigarette.net.DefaultObserver;
import com.hupo.cigarette.net.IdeaApi;
import com.hupo.cigarette.utils.HttpParamsUtil;
import com.hupo.cigarette.utils.IntentUtils;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.utils.ToastUtils;
import com.hupo.cigarette.widget.dialog.CenterAlertDialog;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.CarAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.OnCarResultAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Gemini on 2018/10/29.
 *
 * 上车
 */
public class AboardActivity extends BaseActivity {

    @BindView(R.id.tv_title) TextView titleTv;//标题
    @BindView(R.id.lay_back) LinearLayout backLay;//返回
    @BindView(R.id.view_pager) public ViewPager mViewPager;
    @BindView(R.id.toolbar_tab) public TabLayout mTabLayout;
    ReceiveFragment receiveFragment;//领货单
    ErrorFragment errorFragment;//异常订单
    PagerAdapter mPagerAdapter;

    @BindView(R.id.lay_aboard_receive) LinearLayout aboardReceiveLay;//领货单底部布局
    @BindView(R.id.lay_aboard_error) LinearLayout aboardErrorLay;//异常订单底部布局

    @BindView(R.id.tv_aboard_num) TextView aboardNumTv;//车牌
    private String carNum;//绑定的车牌

    private int receiveNum ,//领货单数量
    errorNum,//异常订单数量
    allNum;//选择的上车数量

    private List<String> drawInvIds = new ArrayList<>();//领货单编号
    private List<String> expOrderIds = new ArrayList<>();//异常订单编号

    private String carId;//车辆id

    private String drawInvId,//拼接的领货单单号
            expOrderId;//拼接的异常订单编号
    private String sendNo;//配送单号

    @Override
    public int intiLayout() {
        return R.layout.activity_aboard;
    }

    @Override
    public void initView() {
        carNum = CarInfoHelper.getInstance().getCar().getNum();
        carId =  CarInfoHelper.getInstance().getCar().getCarId();
        aboardNumTv.setText(carNum+"正在上车");

        titleTv.setText(getString(R.string.title_aboard));//设置标题
        backLay.setVisibility(View.VISIBLE);

        mViewPager.setOffscreenPageLimit(2);
        receiveFragment = new ReceiveFragment();
        errorFragment = new ErrorFragment();

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
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0){
                    aboardReceiveLay.setVisibility(View.VISIBLE);
                    aboardErrorLay.setVisibility(View.GONE);
                }else if (i == 1){
                    aboardReceiveLay.setVisibility(View.GONE);
                    aboardErrorLay.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        receiveFragment.onCheckedChanged(new ReceiveFragment.CheckBoxInterface() {
            @Override
            public void onCheckedChanged(List<String> nos, int num) {
                drawInvIds.clear();
                drawInvIds.addAll(nos);
                receiveNum = num;
            }
        });

        errorFragment.onCheckedChanged(new ErrorFragment.CheckBoxInterface() {
            @Override
            public void onCheckedChanged(List<String> nos, int num) {
                expOrderIds.clear();
                expOrderIds.addAll(nos);
                errorNum = num;
            }
        });


    }


    @OnClick({R.id.tv_aboard_split,R.id.tv_aboard_car,R.id.tv_aboard_unbound,R.id.tv_aboard_ecar})
    public void OnClick(View view){
        switch (view.getId()){
            //取消绑定
            case R.id.tv_aboard_unbound:
                new CenterAlertDialog(AboardActivity.this, "您将取消"+carNum+"的绑定？" , "确定", "取消", new CenterAlertDialog.Listener() {
                    @Override
                    public void onLeftClick() {
                        unbound();
                    }

                    @Override
                    public void onRightClick() {
                    }
                }).show();

                break;
            //领货单上车
            case R.id.tv_aboard_car:
            //异常订单上车
            case R.id.tv_aboard_ecar:
                allNum = receiveNum + errorNum;

                if (allNum < 1){
                    ToastUtils.show("至少选择一个单子");
                    return;
                }
                new CenterAlertDialog(AboardActivity.this, "您确定将"+allNum+"个领货单进行上车？" , "确定", "取消", new CenterAlertDialog.Listener() {
                    @TargetApi(Build.VERSION_CODES.O)
                    @Override
                    public void onLeftClick() {
                        aboardCar();
                    }

                    @Override
                    public void onRightClick() {
                    }
                }).show();
                break;
            //拆分
            case R.id.tv_aboard_split:
                if (receiveNum < 1){
                    ToastUtils.show("至少选择一个单子");
                    return;
                }

                if (receiveNum > 1){
                    ToastUtils.show("只能选择一个领货单");
                    return;
                }
                MerchantInfo bean = new MerchantInfo();
                bean.setType(0);
                bean.setUid(drawInvIds.get(0));
                IntentUtils.startAtyWithSerialObj(AboardActivity.this,MerchantSplitActivity.class,"info",bean);
                break;
        }
    }

    //上车
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void aboardCar() {
        StringBuilder sbd = new StringBuilder();
        for (int i = 0; i < drawInvIds.size(); i++) {
            sbd.append(drawInvIds.get(i));
            sbd.append(",");
        }
        drawInvId = sbd.length() > 0 ? sbd.toString().substring(0,sbd.length()-1) : "";

        StringBuilder sbe = new StringBuilder();
        for (int i = 0; i < expOrderIds.size(); i++) {
            sbe.append(expOrderIds.get(i));
            sbe.append(",");
        }
        expOrderId = sbe.length() > 0 ? sbe.toString().substring(0,sbe.length()-1) : "";

        Map<String, Object> map = new HashMap<>();
        map.put("carId",carId);
        map.put("drawInvIds",drawInvId);
        map.put("expOrderIds",expOrderId);
        IdeaApi.getApiService()
                .onTheCar(HttpParamsUtil.setParams(map))
                .compose(this.<OnCarResultAdapter>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<OnCarResultAdapter>(this, OnCarResultAdapter.class) {
                    @Override
                    public void onSuccess(OnCarResultAdapter jsonAdapter) {
                        sendNo = jsonAdapter.getSendNo();
                        //修改领货单上车状态
                        DrawInvsDetailHelper.getInstance().getOnCar(sendNo,carId,drawInvId);
                        //保存配送单信息
                        SendNoInfoHelper.getInstance().insertSend(sendNo,carId,carNum);
                        IntentUtils.startAty(AboardActivity.this,PrepareSendActivity.class);
                        finish();
                    }

                    @Override
                    public void onFail(OnCarResultAdapter jsonAdapter) {
                        super.onFail(jsonAdapter);
                        ToastUtils.show(jsonAdapter.getMsg());
                    }
                });
    }

    //取消绑定
    private void unbound() {
        Map<String, String> map = new HashMap<>();
        map.put("carNum",carNum);
        IdeaApi.getApiService()
                .unbindCar(HttpParamsUtil.setParam(map))
                .compose(this.<CarAdapter>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<CarAdapter>(this, CarAdapter.class) {
                    @Override
                    public void onSuccess(CarAdapter jsonAdapter) {
                        IntentUtils.startAty(AboardActivity.this,BoundVehiclesActivity.class);
                        finish();
                    }

                    @Override
                    public void onFail(CarAdapter jsonAdapter) {
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
                return receiveFragment;
            } else if (position == 1) {
                return errorFragment;
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
        StatusBarUtil.setTranslucentForImageViewInFragment(AboardActivity.this, 0,null);
    }
}
