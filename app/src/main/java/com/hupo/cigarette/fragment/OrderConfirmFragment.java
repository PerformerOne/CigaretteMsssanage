package com.hupo.cigarette.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.activity.SendErrorActivity;
import com.hupo.cigarette.adapter.UploadPhotoAdapter;
import com.hupo.cigarette.base.BaseFragment;
import com.hupo.cigarette.bean.PicItem;
import com.hupo.cigarette.utils.ToastUtils;
import com.hupo.cigarette.widget.MyGridView;
import com.hupo.cigarette.widget.img.MultiImageSelectorActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Gemini on 2018/10/30.
 *
 * 收货
 */
public class OrderConfirmFragment extends RxFragment {

    private Unbinder unbinder;

    @BindView(R.id.gd_confirm_picture) GridView confirmPictureGd;//确认收货图片

    private List<PicItem> mUploadPhotoList;
    private UploadPhotoAdapter adapter;
    private static final int REQUEST_IMAGE = 2;//选照片
    private ArrayList<String> resultList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_order_confirm, container, false);
        //返回一个Unbinder值（进行解绑）
        unbinder = ButterKnife.bind(this, mView);

        mUploadPhotoList = new ArrayList<>();
        adapter = new UploadPhotoAdapter(getActivity(), mUploadPhotoList, 6);
        adapter.setOnClickLitener(new UploadPhotoAdapter.OnClickLitener() {
            @Override
            public void onItemClick(View view) {
                Intent intent = new Intent(getActivity(), MultiImageSelectorActivity.class);
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
        confirmPictureGd.setAdapter(adapter);
        return mView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
