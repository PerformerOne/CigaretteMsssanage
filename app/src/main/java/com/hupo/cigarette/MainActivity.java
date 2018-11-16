package com.hupo.cigarette;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.hupo.cigarette.adapter.ViewPagerAdapter;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.bus.RxBus;
import com.hupo.cigarette.bus.event.RefreshOfflineEvent;
import com.hupo.cigarette.bus.listener.RefreshOfflineListener;
import com.hupo.cigarette.fragment.MessFragment;
import com.hupo.cigarette.fragment.MineFragment;
import com.hupo.cigarette.fragment.UserFragment;
import com.hupo.cigarette.fragment.WorkFragment;
import com.hupo.cigarette.service.InitService;
import com.hupo.cigarette.utils.BottomNavigationViewHelper;
import com.hupo.cigarette.utils.LogUtils;
import com.hupo.cigarette.utils.PreferUtils;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.main_bottom) BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;
    @BindView(R.id.main_viewpager) ViewPager viewPager;
    private MenuItem menuItem;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        bindPermission();
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setItemIconTintList(null);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        List<Fragment> list = new ArrayList<>();
        list.add(new WorkFragment());
        list.add(new MessFragment());
        list.add(new UserFragment());
        list.add(new MineFragment());
        viewPagerAdapter.setList(list);

    }

    private void bindPermission() {
        final int[] i = {0};
        rxPermissions.requestEach(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(permission -> {
                    i[0]++;
                    if (i[0]==2){
                        InitService.startIntentService(MainActivity.this);
                    }
                });
    }

    @Override
    public void initData() {
        if (!PreferUtils.getString("refreshTime","").equals(new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis()))){
            RxBus.getInstance().postSticky(new RefreshOfflineEvent(new RefreshOfflineListener() {
                @Override
                public void progress(int cur, int total, String msg) {

                }

                @Override
                public void success(int i) {

                }

                @Override
                public void error(int i, int errorCode, String errMsg) {
                    LogUtils.e("&&&&&&&&&&&&&&&&&&&&"+i);
                }

                @Override
                public void finish() {
                    PreferUtils.put("refreshTime",new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis()));
                }
            }));
        }



    }

    @Override
    public void initListener() {

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.navigation_work:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_mess:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_user:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_mine:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this, 0,null);
    }

    /**
     * 双击返回退出程序
     */
    private boolean isExit = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (!isExit) {
                    ToastUtils.show(getString(R.string.sys_exit));
                    isExit = true;
                    new CountDownTimer(3000, 1000) {
                        @Override
                        public void onFinish() {
                            isExit = false;
                        }

                        @Override
                        public void onTick(long arg0) {
                        }
                    }.start();
                } else {
                    finish();
                }
                break;
            default:
                return false;
        }

        return true;
    }
}
