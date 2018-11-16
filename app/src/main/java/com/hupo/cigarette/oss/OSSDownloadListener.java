package com.hupo.cigarette.oss;

import android.graphics.drawable.Drawable;

import java.io.File;

public interface OSSDownloadListener {
    void onProgress(int i, long currentSize, long totalSize);
    void onSuccess(int i, Drawable result);
    void onFailure(int i, String errMsg);
    void onSuccess(int i, File file);
}
