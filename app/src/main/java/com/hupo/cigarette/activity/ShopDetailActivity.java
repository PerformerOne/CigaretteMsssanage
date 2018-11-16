package com.hupo.cigarette.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hupo.cigarette.R;
import com.hupo.cigarette.adapter.ShopDetailPhotoAdapter;
import com.hupo.cigarette.base.BaseActivity;
import com.hupo.cigarette.bus.RxBus;
import com.hupo.cigarette.net.Constants;
import com.hupo.cigarette.oss.OSSDownSomeListener;
import com.hupo.cigarette.oss.OSSFileController;
import com.hupo.cigarette.utils.LogUtils;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.widget.dialog.CenterAlertDialog;
import com.hupo.cigarette.widget.img.FileUtil;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HCustModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.kareluo.imaging.IMGEditActivity;

public class ShopDetailActivity extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_shopDetail_num)
    TextView tvShopDetailNum;
    @BindView(R.id.tv_shopDetail_name)
    TextView tvShopDetailName;
    @BindView(R.id.tv_shopDetail_code)
    TextView tvShopDetailCode;
    @BindView(R.id.tv_shopDetail_phone)
    TextView tvShopDetailPhone;
    @BindView(R.id.tv_shopDetail_manager)
    TextView tvShopDetailManager;
    @BindView(R.id.tv_shopDetail_addr)
    TextView tvShopDetailAddr;
    @BindView(R.id.recycler_shopDetail_photo)
    RecyclerView recyclerShopDetailPhoto;
    @BindView(R.id.lay_back)
    LinearLayout layBack;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.tv_shopDetail_deletePhoto)
    TextView tvShopDetailDeletePhoto;
    @BindView(R.id.tv_shopDetail_commit)
    TextView tvShopDetailCommit;

    private HCustModel shopDetail;
    private int position;
    private ShopDetailPhotoAdapter adapter;
    private List<File> files = new ArrayList<>();
    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 110;
    private static final int REQ_IMAGE_EDIT = 1001;
    private File mTmpFile;
    private String fileName = "";

    @Override
    public int intiLayout() {
        position = getIntent().getIntExtra("Position", 0);
        shopDetail=new Gson().fromJson(getIntent().getStringExtra("data"),HCustModel.class);
        return R.layout.activity_shopdetail;
    }
    @Override
    public void initView() {
        tvShopDetailNum.setText(position > 8 ? "" + (position + 1) : "0" + (position + 1));
        tvShopDetailName.setText(shopDetail.getName());
        tvShopDetailCode.setText("商户编号：" + shopDetail.getId());
        tvShopDetailPhone.setText("电话：" + shopDetail.getManagerTel());
        tvShopDetailManager.setText("负责人：" + shopDetail.getManager());
        tvShopDetailAddr.setText(shopDetail.getAddr());

        OSSFileController.getController().getImageByProfix(1, Constants.OSS_PROFIX +shopDetail.getId(), new OSSDownSomeListener() {
            @Override
            public void onProgress(int i, long currentSize, long totalSize) {

            }

            @Override
            public void onSuccess(int i, Map<String, File> map) {
                List<File> f = new ArrayList<>();
                for (String s : map.keySet()) {
                    if (s.indexOf("_cut") != -1) {
                        f.add(map.get(s));
                    } else {
                        files.add(map.get(s));
                    }
                }
                adapter.setData(f);
                if (f.size() == 0) {
                    tvShopDetailDeletePhoto.setVisibility(View.INVISIBLE);
                } else {
                    tvShopDetailDeletePhoto.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(int i, String errMsg) {

            }
        });
    }

    @Override
    public void initData() {
        tvTitle.setText(R.string.title_shopDetail);
        layBack.setVisibility(View.VISIBLE);
        adapter = new ShopDetailPhotoAdapter(this, recyclerShopDetailPhoto);

    }

    @Override
    public void initListener() {

        adapter.setOnItemClickListener((adapter, holder, view, position) -> {
            if (this.adapter.getDataSize() == 0) {
                showCameraAction();
            } else {
                ArrayList<String> s = new ArrayList<>();
                s.add(this.adapter.getItem(position).getPath() + this.adapter.getItem(position).getName());
                Intent intent = new Intent();
                intent.setClass(this, PhotoAty.class);
                intent.putExtra("list", s);
                intent.putExtra("pos", position);
                startActivity(intent);
            }

        });
    }

    @OnClick({R.id.tv_shopDetail_deletePhoto, R.id.tv_shopDetail_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_shopDetail_deletePhoto:
                new CenterAlertDialog(this, "清除图片将删除原来的所有图片，确定要清除图片？", "确定", "取消", new CenterAlertDialog.Listener() {
                    @Override
                    public void onLeftClick() {
                        adapter.clear();
                    }

                    @Override
                    public void onRightClick() {

                    }
                });
                break;
            case R.id.tv_shopDetail_commit:
                break;
        }
    }

    private void showCameraAction() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(ShopDetailActivity.this.getPackageManager()) != null) {
                try {
                    mTmpFile = FileUtil.createTmpFile(ShopDetailActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mTmpFile != null && mTmpFile.exists()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(ShopDetailActivity.this, "com.union.huayu.fileprovider", mTmpFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                    } else {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
                    }
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    Toast.makeText(ShopDetailActivity.this, R.string.error_image_not_exist, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ShopDetailActivity.this, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    editPhoto();
                }
                break;
            case REQ_IMAGE_EDIT:
                adapter.addData(new File(fileName));
                break;
        }
    }

    private void editPhoto() {
        fileName=OSSFileController.IMAGE_TEMP+System.currentTimeMillis()+".png";
        LogUtils.e(fileName);

        Intent intent = new Intent(this, IMGEditActivity.class);
        intent.putExtra(IMGEditActivity.EXTRA_IMAGE_URI, Uri.fromFile(mTmpFile));
        intent.putExtra(IMGEditActivity.EXTRA_IMAGE_SAVE_PATH, fileName);
        startActivityForResult(intent, REQ_IMAGE_EDIT);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_WRITE_ACCESS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCameraAction();
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().removeAllStickyEvents();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(ShopDetailActivity.this, 0,null);
    }
}
