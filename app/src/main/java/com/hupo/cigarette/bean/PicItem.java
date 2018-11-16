package com.hupo.cigarette.bean;

/**
 * Created by Administrator on 2016/10/12.
 */
public class PicItem {
    public PicItem(long index, String url, String bitmap) {
        mIndex = index;
        mUrl = url;
        mBitmap = bitmap;
    }

    private String mBitmap;
    private String mUrl;
    private long mIndex;

    public String getBitmap() {
        return mBitmap;
    }

    public void setBitmap(String bitmap) {
        mBitmap = bitmap;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public long getIndex() {
        return mIndex;
    }

    public void setIndex(long index) {
        mIndex = index;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PicItem) {
            PicItem u = (PicItem) o;
            return this.mIndex == u.getIndex() && this.mUrl.equals(u.getUrl());
        }
        return super.equals(o);
    }
}
