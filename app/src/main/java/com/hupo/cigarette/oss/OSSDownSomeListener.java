package com.hupo.cigarette.oss;

import java.io.File;
import java.util.Map;

public interface OSSDownSomeListener {
    void onProgress(int i, long currentSize, long totalSize);
    void onSuccess(int i, Map<String, File> map);
    void onFailure(int i, String errMsg);
}
