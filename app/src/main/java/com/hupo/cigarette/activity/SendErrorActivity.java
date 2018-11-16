package com.hupo.cigarette.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.hupo.cigarette.R;
import com.hupo.cigarette.adapter.ErrorChooseAdapter;
import com.hupo.cigarette.adapter.UploadPhotoAdapter;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.bean.MerchantInfo;
import com.hupo.cigarette.bean.PicItem;
import com.hupo.cigarette.dao.helper.DrawInvsDetailHelper;
import com.hupo.cigarette.dao.helper.OrderDetailInfoHelper;
import com.hupo.cigarette.net.Constants;
import com.hupo.cigarette.oss.OSSFileController;
import com.hupo.cigarette.oss.OSSUploadListener;
import com.hupo.cigarette.utils.IntentUtils;
import com.hupo.cigarette.utils.LogUtils;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.widget.MyGridView;
import com.hupo.cigarette.widget.flowtagView.FlowTagView;
import com.hupo.cigarette.widget.img.MultiImageSelectorActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Gemini on 2018/10/30.
 *
 * 配送-异常订单
 */
public class SendErrorActivity extends BaseActivity {

    @BindView(R.id.tv_title) TextView titleTv;//标题
    @BindView(R.id.lay_back) LinearLayout backLay;//返回
    @BindView(R.id.fl_error_choose) FlowTagView errorChooseFl;//异常原因
    @BindView(R.id.et_custom_resaon) EditText customReasonEt;//其他原因
    private ErrorChooseAdapter errorChooseAdapter;
    private List<String> strs = new ArrayList<>();

    @BindView(R.id.gd_error_picture) MyGridView errorPicture;//异常图片
    private List<PicItem> mUploadPhotoList;
    private UploadPhotoAdapter adapter;
    private static final int REQUEST_IMAGE = 2;//选照片
    private ArrayList<String> resultList = new ArrayList<>();

    private String orderId;//订单号

    private String selectReason,//选择原因
                   customReason;//输入原因

    private String uid;//订单id
    @Override
    public int intiLayout() {
        return R.layout.activity_send_error;
    }

    @Override
    public void initView() {

        Intent intent = getIntent();
        MerchantInfo info = (MerchantInfo) intent.getSerializableExtra("info");
        orderId = info.getSendNo();
        uid = info.getUid();

        titleTv.setText(getString(R.string.title_error));//设置标题
        backLay.setVisibility(View.VISIBLE);

        strs.add("配送缺烟");
        strs.add("零售户搬迁");
        strs.add("零售户未开门");
        strs.add("零售户不符合配送条件");

        selectReason = strs.get(0);

        errorChooseAdapter = new ErrorChooseAdapter(this,strs);
        errorChooseFl.setAdapter(errorChooseAdapter);
        errorChooseFl.setItemClickListener(new FlowTagView.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                errorChooseAdapter.ClickSelected(position);
                selectReason = strs.get(position);
            }
        });

        mUploadPhotoList = new ArrayList<>();
        adapter = new UploadPhotoAdapter(SendErrorActivity.this, mUploadPhotoList, 6);
        adapter.setOnClickLitener(new UploadPhotoAdapter.OnClickLitener() {
            @Override
            public void onItemClick(View view) {
                Intent intent = new Intent(SendErrorActivity.this, MultiImageSelectorActivity.class);
                // 是否显示调用相机拍照
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大图片选择数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 6);
                intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, resultList);
                // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
        adapter.setOnItemAction(new UploadPhotoAdapter.OnItemAction() {
            @Override
            public void onRemove(String picUrl, ArrayList<String> img) {
                resultList = img;
                resultList.remove(picUrl);
            }
        });
        errorPicture.setAdapter(adapter);
    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.tv_error_confirm})
    public void OnClick(View view){
        switch (view.getId()){
            //提交
            case R.id.tv_error_confirm:
                errorConfirm();
                break;
        }
    }

    private void errorConfirm() {
        customReason = customReasonEt.getText().toString().trim();
        OrderDetailInfoHelper.getInstance().getErrorOrder(orderId,selectReason,customReason);
        if (OrderDetailInfoHelper.getInstance().getDetail(uid).size() == 0){
            DrawInvsDetailHelper.getInstance().getPrepareStart(uid);
        }
        Map<String,String> map = new HashMap<>();
        for (int i = 0; i < resultList.size(); i++){
            map.put(Constants.OSS_ERROR+orderId+"/"+(i+1)+".png",resultList.get(i));
        }

        OSSUploadListener listener = new OSSUploadListener() {
            @Override
            public void onProgress(int i, long currentSize, long totalSize) {

            }

            @Override
            public void onSuccess(int i, PutObjectResult result) {
                IntentUtils.startAty(SendErrorActivity.this,PrepareStartActivity.class);
            }

            @Override
            public void onFailure(int i, String errMsg) {
                LogUtils.e("第"+i+"张图片上传失败");
            }
        };
        OSSFileController.getController().uploadFile(map,listener);
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Bundle bundle = data.getExtras();
            if (requestCode == REQUEST_IMAGE) {
                mUploadPhotoList.clear();
                resultList = bundle.getStringArrayList(MultiImageSelectorActivity.EXTRA_RESULT);
                for (String picPath : resultList) {
                    if (picPath == null) continue;
                    PicItem picItem = new PicItem(System.currentTimeMillis(), picPath, picPath);
                    if (mUploadPhotoList.size() < 6) {
                        mUploadPhotoList.add(0, picItem);
                    }
                }
                adapter.setDatas(mUploadPhotoList);
                adapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(SendErrorActivity.this, 0,null);
    }
}
