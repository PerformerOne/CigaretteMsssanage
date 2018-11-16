package com.hupo.cigarette.oss;

import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.ListObjectsRequest;
import com.alibaba.sdk.android.oss.model.ListObjectsResult;
import com.alibaba.sdk.android.oss.model.OSSObjectSummary;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekRunnable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hupo.cigarette.app.App;
import com.hupo.cigarette.net.Constants;
import com.hupo.cigarette.utils.BitmapUtil;
import com.hupo.cigarette.utils.HttpParamsUtil;
import com.hupo.cigarette.utils.LogUtils;
import com.huposoft.commons.json.JsonAdapter;
import com.huposoft.softs.chuanchuan.platform.app.api.OSSKeyTokenAdapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OSSFileController {

    private static OSSFileController controller;
    private OSS oss;
    private static final String BUCKET_NAME = "tobacco-images";
    private static final String IMAGE_PATH_HEAD = "/sdcard/hupo_images/shopHead/";
    private static final String IMAGE_PATH_GOODS = "/sdcard/hupo_images/goodsEnsure/";
    private static final String IMAGE_PATH_EXP = "/sdcard/hupo_images/orderExp/";
    public static final String IMAGE_TEMP = "/sdcard/hupo_images/temp/";

    private String filePath;

    public synchronized static OSSFileController getController() {
        if (controller == null) {
            controller = new OSSFileController();
        }
        return controller;
    }


    private OSSFileController() {
        initOss();
    }

    private void initOss() {
        String endpoint = "http://oss-cn-zhangjiakou.aliyuncs.com";
        OSSCredentialProvider credentialProvider = new OSSFederationCredentialProvider() {
            @Override
            public OSSFederationToken getFederationToken() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    FormBody formBody = new FormBody
                            .Builder()
                            .add("val", HttpParamsUtil.setParams(new HashMap<>()))
                            .build();
                    Request request = new Request.
                            Builder().
                            url(Constants.HOST + "platform/app/getOssAccessKeyToken.htm").
                            post(formBody).
                            build();
                    Response response = client.newCall(request).execute();
                    JsonAdapter<OSSKeyTokenAdapter> adapter = new Gson().fromJson(response.body().string(), new TypeToken<JsonAdapter<OSSKeyTokenAdapter>>() {
                    }.getType());
                    LogUtils.e(HttpParamsUtil.decryptString(adapter.getVal()));
                    if (!TextUtils.isEmpty(HttpParamsUtil.decryptString(adapter.getVal()))) {
                        OSSKeyTokenAdapter oss = new Gson().fromJson(HttpParamsUtil.decryptString(adapter.getVal()), OSSKeyTokenAdapter.class);
                        return new OSSFederationToken(oss.getAccessKeyId(), oss.getAccessKeySecret(), oss.getToken(), "");
                    }
                } catch (IOException e) {
                }
                return new OSSFederationToken("", "", "", "鉴权错误");
            }
        };
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(10 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(App.getInstance(), endpoint, credentialProvider);
    }

    public void getImageFile(int i, IMAGE_TYPE type, String key, String fileName, OSSDownloadListener listener) {

        String fileKey = Constants.OSS_PROFIX + key;
        switch (type) {
            case ORDER_EXP:
                filePath = IMAGE_PATH_EXP;
                break;
            case SHOP_HEAD:
                filePath = IMAGE_PATH_HEAD;
                break;
            case GOOD_ENSURE:
                filePath = IMAGE_PATH_GOODS;
                break;
        }
        File f = new File(filePath + fileKey);
        if (!f.exists()) {
            f.mkdirs();
        }
        f = new File(filePath + fileKey + fileName);
        if (!f.exists()) {
            downloadFile(i, filePath + fileKey + fileName, fileKey + fileName, listener);
        } else {
            listener.onSuccess(i, f);
        }
    }

    public void getImageByProfix(int i, String prefix, OSSDownSomeListener listener) {

        GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.HIGH) {
            @Override
            public void run() {
                File f=new File(IMAGE_PATH_HEAD+prefix);
                if (f.exists()){
                    File[] files = f.listFiles();
                    Map<String,File> fi=new HashMap<>();
                    for (File file : files) {
                        String s = Uri.fromFile(file).toString();
                        if (s.indexOf(prefix)!=-1){
                            fi.put(s,file);
                        }
                    }
                    if (fi.size()>0){
                        listener.onSuccess(i,fi);
                        return;
                    }
                }
                if (oss == null) {
                    initOss();
                }
                ListObjectsRequest listGet = new ListObjectsRequest(BUCKET_NAME);
                listGet.setCRC64(OSSRequest.CRC64Config.YES);
                listGet.setPrefix(prefix);
                oss.asyncListObjects(listGet, new OSSCompletedCallback<ListObjectsRequest, ListObjectsResult>() {
                    @Override
                    public void onSuccess(ListObjectsRequest request, ListObjectsResult result) {
                        Map<String, File> map = new HashMap<>();
                        for (OSSObjectSummary summary : result.getObjectSummaries()) {
                            try {
                                String key = summary.getKey();
                                File f = new File(IMAGE_PATH_HEAD + key);
                                if (!f.exists()) {
                                    String[] split = key.split("/");
                                    key=key.replace(split[split.length - 1], "");
                                    f = new File(IMAGE_PATH_HEAD + key);
                                    f.mkdirs();
                                    f = new File(IMAGE_PATH_HEAD + summary.getKey());
                                    f = getFile(f, summary.getKey());
                                }
                                if (f != null)
                                    map.put(summary.getKey(), f);
                            } catch (ClientException e) {
                                listener.onFailure(i, "客户端连接失败");
                            } catch (ServiceException e) {
                                listener.onFailure(i, "服务端连接异常");
                            }
                        }
                        listener.onSuccess(i, map);
                    }

                    @Override
                    public void onFailure(ListObjectsRequest request, ClientException clientExcepion, ServiceException serviceException) {
                        // 请求异常
                        if (clientExcepion != null) {
                            listener.onFailure(i, clientExcepion.getMessage());
                        } else if (serviceException != null) {
                            listener.onFailure(i, serviceException.getMessage());
                        } else {
                            listener.onFailure(i, "");
                        }
                    }
                });
            }
        }, ThreadType.NORMAL_THREAD);


    }

    private void downloadFile(final int i, String filePath, String objectKey, OSSDownloadListener listener) {
        GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.HIGH) {
            @Override
            public void run() {
                if (oss == null) {
                    initOss();
                }
                GetObjectRequest get = new GetObjectRequest(BUCKET_NAME, objectKey);
                get.setCRC64(OSSRequest.CRC64Config.YES);
                get.setProgressListener((request, currentSize, totalSize) -> listener.onProgress(i, currentSize, totalSize));
                oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
                    @Override
                    public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                        // 请求成功
                        File f = new File(filePath);
                        listener.onSuccess(i, BitmapUtil.inputToFile(result.getObjectContent(), f));
                    }

                    @Override
                    public void onFailure(GetObjectRequest request, ClientException
                            clientExcepion, ServiceException serviceException) {
                        // 请求异常
                        if (clientExcepion != null) {
                            listener.onFailure(i, clientExcepion.getMessage());
                        } else if (serviceException != null) {
                            listener.onFailure(i, serviceException.getMessage());
                        } else {
                            listener.onFailure(i, "");
                        }
                    }
                });
            }
        }, ThreadType.NORMAL_THREAD);

    }

    public void uploadFile(Map<String, String> keyAndPath, final OSSUploadListener listener) {
        int a = 0;
        for (String s : keyAndPath.keySet()) {
            uploadFile(a, s, keyAndPath.get(s), new OSSUploadListener() {
                @Override
                public void onProgress(int i, long currentSize, long totalSize) {
                }

                @Override
                public void onSuccess(int i, PutObjectResult result) {
                    listener.onSuccess(i, result);
                }

                @Override
                public void onFailure(int i, String errMsg) {
                    listener.onFailure(i, errMsg);
                }
            });
            a++;
        }
    }

    public void uploadFile(final int i, String objectKey, String uploadFilePath,
                           final OSSUploadListener listener) {
        GeekThreadManager.getInstance().execute(new GeekRunnable(ThreadPriority.HIGH) {
            @Override
            public void run() {
                if (oss == null) {
                    initOss();
                }
                PutObjectRequest put = new PutObjectRequest(BUCKET_NAME, objectKey, uploadFilePath);
                put.setCRC64(OSSRequest.CRC64Config.YES);
                put.setProgressCallback((request, currentSize, totalSize) -> listener.onProgress(i, currentSize, totalSize));
                oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        listener.onSuccess(i, result);
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                        if (clientExcepion != null) {
                            listener.onFailure(i, clientExcepion.getMessage());
                        } else if (serviceException != null) {
                            listener.onFailure(i, serviceException.getMessage());
                        } else {
                            listener.onFailure(i, "");
                        }
                    }
                });
            }
        }, ThreadType.NORMAL_THREAD);


    }


//    private void downloadFiles(int i, String prefix, OSSDownSomeListener listener) {
//        ListObjectsRequest listGet = new ListObjectsRequest(BUCKET_NAME);
//        listGet.setCRC64(OSSRequest.CRC64Config.YES);
//        listGet.setPrefix(prefix);
//        oss.asyncListObjects(listGet, new OSSCompletedCallback<ListObjectsRequest, ListObjectsResult>() {
//            @Override
//            public void onSuccess(ListObjectsRequest request, ListObjectsResult result) {
//                Map<String, Drawable> map = new HashMap<>();
//                for (OSSObjectSummary summary : result.getObjectSummaries()) {
//                    try {
//                        Drawable file = getFile(summary.getKey());
//                        map.put(summary.getKey(), file);
//                    } catch (IOException e) {
//                        listener.onFailure(i, "解析失败");
//                    } catch (ClientException e) {
//                        listener.onFailure(i, "客户端连接失败");
//                    } catch (ServiceException e) {
//                        listener.onFailure(i, "服务端连接异常");
//                    }
//                }
//                listener.onSuccess(i, map);
//            }
//
//            @Override
//            public void onFailure(ListObjectsRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                // 请求异常
//                if (clientExcepion != null) {
//                    listener.onFailure(i, clientExcepion.getMessage());
//                } else if (serviceException != null) {
//                    listener.onFailure(i, serviceException.getMessage());
//                } else {
//                    listener.onFailure(i, "");
//                }
//            }
//        });
//    }

    private File getFile(File file, String key) throws ClientException, ServiceException {
        GetObjectRequest get = new GetObjectRequest(BUCKET_NAME, key);
        GetObjectResult getResult = oss.getObject(get);
        // 获取文件输入流
        InputStream inputStream = getResult.getObjectContent();
        return BitmapUtil.inputToFile(inputStream, file);
    }

    public void delete(int i, List<String> keys, OSSDeleteListener listener) {
        final int[] a = {0};
        for (String key : keys) {
            DeleteObjectRequest delete = new DeleteObjectRequest(BUCKET_NAME, key);
            oss.asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
                @Override
                public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {
                    a[0]++;
                    if (a[0] == keys.size()) {
                        listener.onSuccess(i);
                    }
                }

                @Override
                public void onFailure(DeleteObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                    // 请求异常
                    if (clientExcepion != null) {
                        // 本地异常如网络异常等
                        listener.onFailure(i, "网络异常");
                    }
                    if (serviceException != null) {
                        // 服务异常
                        listener.onFailure(i, serviceException.getRawMessage());
                    }
                }

            });
        }
    }


}
