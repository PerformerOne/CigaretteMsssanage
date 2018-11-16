package com.hupo.cigarette.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.activity.LoginActivity;
import com.hupo.cigarette.activity.UserSettingActivity;
import com.hupo.cigarette.app.App;
import com.hupo.cigarette.base.BaseFragment;
import com.hupo.cigarette.bean.User;
import com.hupo.cigarette.service.UDPMsgService;
import com.hupo.cigarette.utils.IntentUtils;
import com.hupo.cigarette.utils.PreferUtils;
import com.hupo.cigarette.widget.dialog.CenterAlertDialog;

import butterknife.BindView;
import butterknife.OnClick;


public class MineFragment extends BaseFragment {

    @BindView(R.id.tv_mine_name) TextView mineNameTv;//当前用户名
    @BindView(R.id.tv_mine_phone) TextView minePhoneTv;//当前用户手机号码

    private User user;
    @Override
    public int intiLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView() {
        user = PreferUtils.getBeanFromSp( "user");
        if (user!=null){
            mineNameTv.setText(user.getLoginUser().getRealName());
            minePhoneTv.setText(user.getLoginUser().getTel());
        }

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.lay_mine_setting,R.id.lay_mine_logout})
    public void OnClick(View view){
        switch (view.getId()){
            //修改密码
            case R.id.lay_mine_setting:
                IntentUtils.startAty(getActivity(),UserSettingActivity.class);
                break;
            //注销
            case R.id.lay_mine_logout:
                new CenterAlertDialog(getActivity(), "确定注销吗？" , "确定", "取消", new CenterAlertDialog.Listener() {
                    @Override
                    public void onLeftClick() {
                        PreferUtils.clear();
                        getActivity().stopService(new Intent(getActivity(),UDPMsgService.class));
                        App.getInstance().setUser(null);
                        App.getInstance().deleteAllDB();
                        IntentUtils.startAty(getActivity(),LoginActivity.class);
//                        next();
                    }

                    @Override
                    public void onRightClick() {
                    }
                }).show();
                break;
        }
    }

//    //跳转下一级界面
//    private void next(){
//        LevelInfo bean = App.getDaoSession().getLevelInfoDao().queryBuilder().where(LevelInfoDao.Properties.UserName.eq(user.getLoginUser().getUsername())).build().unique();
//        bean.setLevel(0);
//        if (bean != null){
//            App.getDaoSession().getLevelInfoDao().update(bean);
//        }else {
//            App.getDaoSession().getLevelInfoDao().insert(bean);
//        }
//    }
}
