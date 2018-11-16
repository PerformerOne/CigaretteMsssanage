package com.hupo.cigarette.activity;

import android.content.Intent;
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
import com.hupo.cigarette.fragment.OrderConfirmFragment;
import com.hupo.cigarette.fragment.OrderPackageFragment;
import com.hupo.cigarette.fragment.OrderTotalFragment;
import com.hupo.cigarette.utils.StatusBarUtil;

import butterknife.BindView;

/**
 * Created by Gemini on 2018/10/30.
 *
 * 订单详情
 */
public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title) TextView titleTv;//标题
    @BindView(R.id.lay_back) LinearLayout backLay;//返回

    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.toolbar_tab) TabLayout mTabLayout;
    OrderTotalFragment orderTotalFragment;//汇总
    OrderPackageFragment orderPackageFragment;//包明细
    OrderConfirmFragment orderConfirmFragment;//收货
    PagerAdapter mPagerAdapter;

    private String orderId,//订单id
    sendNo;//领货单号

    @Override
    public int intiLayout() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView() {

        titleTv.setText(getString(R.string.title_order_detail));//设置标题
        backLay.setVisibility(View.VISIBLE);

        mViewPager.setOffscreenPageLimit(3);
        orderTotalFragment = new OrderTotalFragment();
        orderPackageFragment = new OrderPackageFragment();
        orderConfirmFragment = new OrderConfirmFragment();

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        Intent intent = getIntent();
        MerchantInfo info = (MerchantInfo) intent.getSerializableExtra("info");
        if (info != null){
            orderId = info.getUid();
            sendNo = info.getSendNo();
        }

        int fragment = intent.getIntExtra("fragment",0);
        if (fragment == 2){
            mViewPager.setCurrentItem(2);
        }
    }

    @Override
    public void initData() {

        orderTotalFragment.setOrderId(orderId,sendNo);
    }

    @Override
    public void initListener() {

    }

    public class PagerAdapter extends FragmentPagerAdapter {
        private Fragment mCurrentFragment;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return orderTotalFragment;
            } else if (position == 1) {
                return orderPackageFragment;
            } else if (position == 2){
                return orderConfirmFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(OrderDetailActivity.this, 0,null);
    }
}
