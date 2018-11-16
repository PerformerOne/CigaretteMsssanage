package com.hupo.cigarette.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.hupo.cigarette.R;
import com.hupo.cigarette.activity.PhotoAty;
import com.hupo.cigarette.app.App;
import com.hupo.cigarette.bean.PicItem;
import com.hupo.cigarette.utils.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;



/**
 * Created by Gemini on 2018/10/30.
 *
 * 上传图片数据适配器
 */
public class UploadPhotoAdapter extends BaseAdapter {
   private LayoutInflater mInflater;
    private List<PicItem> mDatas;
    private MyImageLoadingListener mMyImageLoadingListener;// 自定义异步加载事件
    private ArrayList<Bitmap> mBitmapList;
    private Context contexts;
    private int number;

    public ArrayList<Bitmap> getBitmapList() {
        return mBitmapList;
    }

    /**
     * 设置数据
     *
     * @param mDatas
     */
    public void setDatas(List<PicItem> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public List<PicItem> getDatas() {
        return mDatas;
    }

    /**
     * ItemClick的回调接口
     *
     * @author zhy
     */
    public interface OnClickLitener {
        void onItemClick(View view);
    }

    public interface OnRefreshView {
        void onRefresh();
    }

    public interface OnItemAction {
        void onRemove(String picUrl, ArrayList<String> resultList);
    }


    private OnClickLitener mOnClickLitener;
    private OnRefreshView mOnRefreshView;
    private OnItemAction mOnItemAction;

    public void setOnClickLitener(OnClickLitener onClickLitener) {
        mOnClickLitener = onClickLitener;
    }

    public void setOnRefreshView(OnRefreshView onRefreshView) {
        mOnRefreshView = onRefreshView;
    }

    public void setOnItemAction(OnItemAction onItemAction) {
        mOnItemAction = onItemAction;
    }

    public UploadPhotoAdapter(Context context, List<PicItem> datats, int num) {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
        contexts=context;
        number=num;
        mBitmapList = new ArrayList<>();
        mMyImageLoadingListener = new MyImageLoadingListener();
    }


    @Override
    public int getCount() {
        if (mDatas == null) {
            return 0;
        }
        if (mDatas.size() == number) {
            return mDatas.size();
        } else {
            return mDatas.size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {


        if (position == getCount() - 1 && mDatas.size() != number) {
            convertView = mInflater.inflate(R.layout.item_pic_add, null);
            convertView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnClickLitener != null) {
                                mOnClickLitener.onItemClick(v);
                            }
                        }
                    });

        } else {
            convertView = mInflater.inflate(R.layout.item_pic, null);
            ImageView pic = (ImageView) convertView.findViewById(R.id.pic);
            ImageButton del = (ImageButton) convertView.findViewById(R.id.delete);
            final PicItem picItem = (PicItem) getItem(position);
            String bitmap = picItem.getBitmap();
            if (bitmap != null) {
                ImageLoader.getInstance().displayImage("file:///"+bitmap,pic, App.getSimpleOptions(R.mipmap.nodata_new,R.mipmap.nodata_new));
            } else {
                ImageLoader.getInstance().displayImage(picItem.getUrl(),
                        pic, App.getSimpleOptions(
                                R.mipmap.nodata_new, R.mipmap.nodata_new), mMyImageLoadingListener);
            }
            pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> imgs = new ArrayList<String>();
                    for (int i = 0;i < mDatas.size();i++){
                        imgs.add(mDatas.get(i).getBitmap());
                    }
                    Intent intent = new Intent();
                    intent.setClass(contexts, PhotoAty.class);
                    intent.putExtra("list", imgs);
                    intent.putExtra("pos", position);
                    contexts.startActivity(intent);
                }
            });
            del.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatas.remove(position);
                            if (mOnRefreshView != null) {
                                mOnRefreshView.onRefresh();
                            }
                            if (mOnItemAction != null) {
                                ArrayList<String> resultList = new ArrayList<>();
                                for (int i=0;i<mDatas.size();i++){
                                    resultList.add(mDatas.get(i).getUrl());
                                }

                                mOnItemAction.onRemove(picItem.getUrl(),resultList);
                            }
                            notifyDataSetChanged();
                        }
                    });
        }
        return convertView;
    }
    /**
     * 自定义监听图片异步加载的事件类
    */
    private class MyImageLoadingListener extends SimpleImageLoadingListener {

        // 保存图片加载后的Url，优化图片加载过程中的闪跳问题
        public final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
            if (bitmap == null)
                return;
            ImageView imageView = (ImageView) view;
            boolean isFirstDisplay = !displayedImages.contains(imageUri);
            if (isFirstDisplay) {
                mBitmapList.add(bitmap);
                FadeInBitmapDisplayer.animate(imageView, 100);
                displayedImages.add(imageUri);
            }
        }
    }
}
