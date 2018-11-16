package com.hupo.cigarette.oss;

public interface OSSDeleteListener {
    void onSuccess(int i);
    void onFailure(int i, String errMsg);
}
