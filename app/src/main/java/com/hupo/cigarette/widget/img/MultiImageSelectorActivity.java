package com.hupo.cigarette.widget.img;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.activity.PrepareSendActivity;
import com.hupo.cigarette.utils.StatusBarUtil;
import com.hupo.cigarette.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Multi image selector
 * Created by Nereo on 2015/4/7.
 * Updated by nereo on 2016/1/19.
 * Updated by nereo on 2016/5/18.
 */
public class MultiImageSelectorActivity extends FragmentActivity
		implements MultiImageSelectorFragment.Callback {

	/**
	 * 最大图片选择次数，int类型，默认9
	 */
	public static final String EXTRA_SELECT_COUNT = "max_select_count";
	/**
	 * 图片选择模式，默认多选
	 */
	public static final String EXTRA_SELECT_MODE = "select_count_mode";
	/**
	 * 是否显示相机，默认显示
	 */
	public static final String EXTRA_SHOW_CAMERA = "show_camera";
	/**
	 * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
	 */
	public static final String EXTRA_RESULT = "select_result";
	/**
	 * 默认选择集
	 */
	public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

	/**
	 * 单选
	 */
	public static final int MODE_SINGLE = 0;
	/**
	 * 多选
	 */
	public static final int MODE_MULTI = 1;
	private int mode;
	private static final int PERMISSON_REQUESTCODE = 0;
	private static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 110;

	private ArrayList<String> resultList = new ArrayList<>();
	private TextView mSubmitButton;
	private int mDefaultCount;
	private boolean isShow;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_default);

		Intent intent = getIntent();
		mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 6);
		 mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
		isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
		if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
			resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
		}
		requstPermission();


//		 返回按钮
		findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});

		// 完成按钮
		mSubmitButton = (TextView) findViewById(R.id.commit);
		if(mode==0){
			mSubmitButton.setVisibility(View.GONE);
		}else {
			mSubmitButton.setVisibility(View.VISIBLE);
		}
		if (resultList == null || resultList.size() <= 0) {
			mSubmitButton.setText("完成");
			mSubmitButton.setEnabled(false);
		} else {
			mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
			mSubmitButton.setEnabled(true);
		}
		mSubmitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (resultList != null && resultList.size() > 0) {
					// 返回已选择的图片数据
					Intent data = new Intent();
					Bundle bundle = new Bundle();
					bundle.putStringArrayList(EXTRA_RESULT, resultList);
					data.putExtras(bundle);
					setResult(RESULT_OK, data);
					finish();
				} else {
					ToastUtils.show("请选择图像");
				}
			}
		});

		setStatusBar();
	}

	private void requstPermission() {
		try {
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||  ActivityCompat.checkSelfPermission((this), Manifest.permission.WRITE_EXTERNAL_STORAGE)
					!= PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission((this), Manifest.permission.READ_EXTERNAL_STORAGE)
					!= PackageManager.PERMISSION_GRANTED ) {
				ActivityCompat.requestPermissions((this), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
						REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
			}else {
				goMutiImageFragt();
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
	}


	protected void setStatusBar() {
		StatusBarUtil.setColor(this, getResources().getColor(R.color.tF87E3D));
		StatusBarUtil.setTranslucentForImageViewInFragment(MultiImageSelectorActivity.this, 0,null);
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode){
			case REQUEST_STORAGE_WRITE_ACCESS_PERMISSION:
				if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
						if(grantResults[1]== PackageManager.PERMISSION_GRANTED){
								if(grantResults[2]== PackageManager.PERMISSION_GRANTED){
										if(grantResults[3]== PackageManager.PERMISSION_GRANTED){
											goMutiImageFragt();
										}else {
											ToastUtils.show("请您打开读取手机状态的权限");
											finish();
										}
								}else {
									ToastUtils.show("请您打开写存储卡的权限");
									finish();
								}
						}else {
							ToastUtils.show("请您打开读存储卡的权限");
							finish();
						}
				}else {
					ToastUtils.show("请您打开相机权限");
					finish();
				}
				break;
		}

	}

	private void goMutiImageFragt() {
		final Bundle bundle = new Bundle();
		bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
		bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
		bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
		bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);

		getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
                .commit();
	}

	@Override
	public void onSingleImageSelected(String path) {
		Intent data = new Intent();
		Bundle bundle = new Bundle();
		resultList.add(path);
		bundle.putStringArrayList(EXTRA_RESULT, resultList);
		data.putExtras(bundle);
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public void onImageSelected(String path) {
		if (!resultList.contains(path)) {
			resultList.add(path);
		}
		// 有图片之后，改变按钮状态
		if (resultList.size() > 0) {
			mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
			if (!mSubmitButton.isEnabled()) {
				mSubmitButton.setEnabled(true);
			}
		}
	}

	@Override
	public void onImageUnselected(String path) {
		if (resultList.contains(path)) {
			resultList.remove(path);
			mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
		} else {
			mSubmitButton.setText("完成(" + resultList.size() + "/" + mDefaultCount + ")");
		}
		// 当为选择图片时候的状态
		if (resultList.size() == 0) {
			mSubmitButton.setText("完成");
			/*mSubmitButton.setEnabled(false);*/
		}
	}

	@Override
	public void onCameraShot(File imageFile) {
		if (imageFile != null) {
			Intent data = new Intent();
			Bundle bundle = new Bundle();
			resultList.add(imageFile.getAbsolutePath());
			bundle.putStringArrayList(EXTRA_RESULT, resultList);
			data.putExtras(bundle);
			setResult(RESULT_OK, data);
			finish();
		}
	}
}
