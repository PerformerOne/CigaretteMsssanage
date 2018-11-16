package com.hupo.cigarette.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.app.App;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.widget.HackyViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by Gemini on 2018/10/30.
 *
 * 查看大图
 */
public class PhotoAty extends BaseActivity {

    private static final String ISLOCKED_ARG = "isLocked";

    private HackyViewPager mViewPager;
    private MenuItem menuLockItem;

    private ArrayList<String> imgs;
    private int pos = 0;
    @BindView(R.id.commit) TextView mNum;


    class SamplePagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return imgs.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().displayImage("file:///"+imgs.get(position), photoView, App.getSimpleOptions(R.mipmap.nodata_new, R.mipmap.nodata_new));

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(PhotoAty.this, 0,null);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_photo;
    }

    @Override
    public void initView() {
        imgs = getIntent().getStringArrayListExtra("list");
        pos = getIntent().getIntExtra("pos",0);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new SamplePagerAdapter());
    }

    @Override
    public void initData() {
        mViewPager.setCurrentItem(pos);
    }

    @Override
    public void initListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pos=position;
                mNum.setText(pos+1+""+"/"+imgs.size()+"");
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
