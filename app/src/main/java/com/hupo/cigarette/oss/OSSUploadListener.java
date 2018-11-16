package com.hupo.cigarette.oss;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

public interface OSSUploadListener {
    void onProgress(int i, long currentSize, long totalSize);
    void onSuccess(int i, PutObjectResult result);
    void onFailure(int i, String errMsg);
}
