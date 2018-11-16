package com.hupo.cigarette.fragment;


import android.view.View;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.activity.AboardActivity;
import com.hupo.cigarette.activity.AreasActivity;
import com.hupo.cigarette.activity.BoundVehiclesActivity;
import com.hupo.cigarette.activity.DistributionListActivity;
import com.hupo.cigarette.activity.OrderErrorActivity;
import com.hupo.cigarette.activity.PrepareSendActivity;
import com.hupo.cigarette.activity.PrepareStartActivity;
import com.hupo.cigarette.activity.ReturnJourneyActivity;
import com.hupo.cigarette.base.BaseFragment;
import com.hupo.cigarette.bean.AreaBean;
import com.hupo.cigarette.dao.helper.CarInfoHelper;
import com.hupo.cigarette.dao.helper.DrawInvsDetailHelper;
import com.hupo.cigarette.dao.helper.SendNoInfoHelper;
import com.hupo.cigarette.utils.DataUtil;
import com.hupo.cigarette.utils.IntentUtils;

import butterknife.BindView;
import butterknife.OnClick;


public class WorkFragment extends BaseFragment {

    @BindView(R.id.tv_work_progress)
    TextView progressTv;
    @BindView(R.id.tv_work_photo)
    TextView photoTv;
    @BindView(R.id.tv_work_exp)
    TextView expTv;
    @BindView(R.id.tv_work_smoke)
    TextView smokeCounTv;
    @BindView(R.id.tv_work_shop)
    TextView shopCountTv;

    @Override
    public int intiLayout() {
        return R.layout.fragment_work;
    }

    @Override
    public void initView() {
        showStatisData();

    }

    @Override
    public void initData() {


    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.lay_work_send,R.id.lay_work_receive,R.id.lay_work_error,R.id.lay_work_map,R.id.lay_work_shop})
    public void OnClick(View view){
        switch (view.getId()){
            //配送单
            case R.id.lay_work_send:
                IntentUtils.startAty(getActivity(),DistributionListActivity.class);
                break;
            //领货单
            case R.id.lay_work_receive:
                if (SendNoInfoHelper.getInstance().getDetailCount() == 0){
                    if (CarInfoHelper.getInstance().getCar() != null){
                        //无配送单信息 有车辆信息 -->上车
                        IntentUtils.startAty(getActivity(),AboardActivity.class);
                    }else {
                        //无配送单信息 无车辆信息 -->绑定
                        IntentUtils.startAty(getActivity(),BoundVehiclesActivity.class);
                    }
                }else {
                    if (DrawInvsDetailHelper.getInstance().getOnCarDrawInvs().size() > 0){
                        //有配送单信息 领货单状态为2 -->准备配送
                        IntentUtils.startAty(getActivity(),PrepareSendActivity.class);
                    }else if (DrawInvsDetailHelper.getInstance().getPrepareSendDrawInvs().size() > 0){
                        //有配送单信息 领货单状态为3 -->开始配送
                        IntentUtils.startAty(getActivity(),PrepareStartActivity.class);
                    }
                }
                break;

            //异常订单
            case R.id.lay_work_error:
                IntentUtils.startAty(getActivity(),OrderErrorActivity.class);
                break;

            //地图
            case R.id.lay_work_map:
                AreaBean areaBean = new AreaBean();
                areaBean.setLatitude(35.18);
                areaBean.setLongitude(113.52);
                IntentUtils.startAtyWithSerialObj(getActivity(),ReturnJourneyActivity.class,"areaBean",areaBean);
                break;
            //商户
            case R.id.lay_work_shop:
                IntentUtils.startAty(getActivity(),AreasActivity.class);
                break;

        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            showStatisData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showStatisData();
    }

    private void showStatisData(){
        DataUtil.getInstance().synData();
        progressTv.setText(DataUtil.getInstance().getProgress()+"%");
        photoTv.setText(DataUtil.getInstance().getCurrentPhoto()+"/"+DataUtil.getInstance().getTotalPhoto());
        expTv.setText(DataUtil.getInstance().getExpOrder()+"");
        smokeCounTv.setText(DataUtil.getInstance().getSmokeCount()+"");
        shopCountTv.setText(DataUtil.getInstance().getShopCount()+"");
    }
}
