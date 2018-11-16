package com.hupo.cigarette.dao.helper;

import android.text.TextUtils;

import com.geek.thread.GeekThreadManager;
import com.geek.thread.ThreadPriority;
import com.geek.thread.ThreadType;
import com.geek.thread.task.GeekThread;
import com.google.gson.Gson;
import com.hupo.cigarette.app.App;
import com.hupo.cigarette.bus.listener.RefreshOfflineListener;
import com.hupo.cigarette.dao.db.BillOfGoodsInfo;
import com.hupo.cigarette.dao.db.DrawInvsDetailInfo;
import com.hupo.cigarette.dao.db.OrderModelInfo;
import com.hupo.cigarette.dao.db.SendNoAdapterInfo;
import com.hupo.cigarette.dao.greendao.BillOfGoodsInfoDao;
import com.hupo.cigarette.dao.greendao.DrawInvsDetailInfoDao;
import com.hupo.cigarette.dao.greendao.OrderModelInfoDao;
import com.hupo.cigarette.dao.greendao.SendNoAdapterInfoDao;
import com.hupo.cigarette.msg.ErrorCode;
import com.hupo.cigarette.net.DefaultObserver;
import com.hupo.cigarette.net.IdeaApi;
import com.hupo.cigarette.utils.HttpParamsUtil;
import com.hupo.cigarette.utils.LogUtils;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.CarAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.DrawInvAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.OrderAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.SendNoAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HDrawInvModel;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HOrderModel;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HSendNoModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RefreshHelper {



    private boolean isGoing;

    private BillOfGoodsInfoDao billOfGoodsInfoDao;
    private SendNoAdapterInfoDao sendNoAdapterInfoDao;
    private DrawInvsDetailInfoDao detailInfoDao;
    private OrderModelInfoDao orderModelInfoDao;

    private static RefreshHelper helper;

    public static synchronized RefreshHelper getInstance() {
        if (helper == null) {
            helper = new RefreshHelper();
        }
        return helper;
    }

    private RefreshHelper() {
        billOfGoodsInfoDao = App.getDaoSession().getBillOfGoodsInfoDao();
        sendNoAdapterInfoDao = App.getDaoSession().getSendNoAdapterInfoDao();
        detailInfoDao = App.getDaoSession().getDrawInvsDetailInfoDao();
        orderModelInfoDao = App.getDaoSession().getOrderModelInfoDao();
    }

    public void refresh(RefreshOfflineListener listener) {
        if (isGoing) {
            listener.error(0, ErrorCode.ERROR_GOING, "");
            return;
        }
        isGoing = true;
        GeekThreadManager.getInstance().execute(new GeekThread(ThreadPriority.NORMAL) {
            @Override
            public void run() {
                super.run();
                IdeaApi.getApiService()
                        .getBindCar(HttpParamsUtil.setParams(new HashMap<>()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultObserver<CarAdapter>(null, CarAdapter.class) {

                            @Override
                            public void onSuccess(CarAdapter response) {
                                CarInfoHelper.getInstance().insertCar(response);
                            }

                            @Override
                            public void onFail(CarAdapter response) {
                                super.onFail(response);
                            }
                        });
                downloadData(1, listener);
            }
        },ThreadType.NORMAL_THREAD);
    }

    private void downloadData(int i, RefreshOfflineListener listener) {
        switch (i) {
            case 1:
                listener.progress(i, 6, "正在下载领货单单号");
                IdeaApi.getApiService()
                        .getDrawInvNos(HttpParamsUtil.setParams(new HashMap<>()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultObserver<DrawInvAdapter>(null, DrawInvAdapter.class) {
                            @Override
                            public void onSuccess(DrawInvAdapter jsonAdapter) {
                                for (BillOfGoodsInfo bill : formatBill(jsonAdapter.getNos())) {
                                    List<BillOfGoodsInfo> list = billOfGoodsInfoDao.queryBuilder().where(BillOfGoodsInfoDao.Properties.Uuid.eq(bill.getUuid())).build().list();
                                    if (bill.getStatus() != 4) {
                                        if (list == null || list.size() == 0) {
                                            billOfGoodsInfoDao.insert(bill);
                                        } else if (bill.getStatus() < 4 && list.get(0).getStatus() < bill.getStatus()) {
                                            billOfGoodsInfoDao.delete(list.get(0));
                                            billOfGoodsInfoDao.insert(bill);
                                        }
                                    } else {
                                        if (list != null && list.size() > 0 && list.get(0).getStatus() < 4) {
                                            billOfGoodsInfoDao.delete(list.get(0));
                                        }
                                    }
                                }
                                listener.success(1);
                                downloadData(2, listener);
                                LogUtils.e("1111111111111111111");
                            }

                            @Override
                            public void onFail(DrawInvAdapter jsonAdapter) {
                                super.onFail(jsonAdapter);
                                LogUtils.e("222222",new Gson().toJson(jsonAdapter));
                                isGoing = false;
                                listener.error(1, jsonAdapter.getErrCode(), jsonAdapter.getMsg());
                            }
                        });
                break;
            case 2:
                listener.progress(2, 6, "正在下载领货单详情");
                List<BillOfGoodsInfo> list = billOfGoodsInfoDao.queryBuilder().list();
                if (list != null) {
                    StringBuffer params = new StringBuffer();
                    for (int a = 0; a < list.size(); a++) {
                        if (a != list.size() - 1) {
                            params.append(list.get(a).getUuid() + ",");
                        } else {
                            params.append(list.get(a).getUuid());
                        }
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("ids", params.toString());
                    map.put("detail", true);
                    IdeaApi.getApiService()
                            .getDrawInvs(HttpParamsUtil.setParams(map))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DefaultObserver<DrawInvAdapter>(null, DrawInvAdapter.class) {

                                @Override
                                public void onSuccess(DrawInvAdapter response) {
                                    for (HDrawInvModel m : response.getDrawInvs()) {
                                        List<DrawInvsDetailInfo> l = detailInfoDao.queryBuilder().where(DrawInvsDetailInfoDao.Properties.No.eq(m.getNo())).list();
                                        String beforeTime = "";
                                        if (l != null && l.size() > 0) {
                                            beforeTime = l.get(0).getLastUpdateTime();
                                            detailInfoDao.delete(l.get(0));
                                        }
                                        DrawInvsDetailInfo detailInfo = new DrawInvsDetailInfo(
                                                m.getUid(),
                                                m.getNo(),
                                                m.getExternalNo(),
                                                m.getExternalAreaId(),
                                                m.getExternalAreaName(),
                                                m.getExternalLineId(),
                                                m.getExternalLineName(),
                                                m.getCages(),
                                                m.getTotalPrice(),
                                                m.getBagCount(),
                                                m.getTotalCount(),
                                                m.getCarId(),
                                                m.getCarNo(),
                                                m.getCarNum(),
                                                m.isCarOnline(),
                                                m.getDeliveryId(),
                                                m.getDeliveryName(),
                                                m.getDriverId(),
                                                m.getDriverName(),
                                                m.getOrders(),
                                                m.getStatus(),
                                                m.getBornDate(),
                                                m.getSendDate(),
                                                m.getGoodsCount(),
                                                m.getSendNo(),
                                                m.getLineId(),
                                                m.getLineName(),
                                                m.getAreaId(),
                                                m.getAreaName(),
                                                m.getCustomerCount(),
                                                m.getAbnormalCount(),
                                                m.getLastUpdateTime(),
                                                m.getPackingType(),
                                                beforeTime);
                                        detailInfoDao.insert(detailInfo);
                                    }
                                    listener.success(2);
                                    downloadData(3, listener);
                                }

                                @Override
                                public void onFail(DrawInvAdapter jsonAdapter) {
                                    super.onFail(jsonAdapter);
                                    isGoing = false;
                                    listener.error(2, jsonAdapter.getErrCode(), jsonAdapter.getMsg());
                                }
                            });

                } else {
                    downloadData(3, listener);
                }
                break;
            case 3:
                listener.progress(i, 6, "正在下载配送单");
                IdeaApi.getApiService()
                        .getCurSendNos(HttpParamsUtil.setParams(new HashMap<>()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultObserver<SendNoAdapter>(null, SendNoAdapter.class) {
                            @Override
                            public void onSuccess(SendNoAdapter jsonAdapter) {
                                HSendNoModel model = jsonAdapter.getCurSendNo();
                                if (!TextUtils.isEmpty(model.getNo())) {
                                    SendNoAdapterInfo info = new SendNoAdapterInfo(
                                            model.getNo(),
                                            model.getSendCarNo(),
                                            model.getCarId(),
                                            model.getCarNo(),
                                            model.getCarNum(),
                                            model.getDeliveryid(),
                                            model.getDeliveryName(),
                                            model.getDriverId(),
                                            model.getDriverName(),
                                            model.getSendAreaId(),
                                            model.getSendAreaName(),
                                            model.getOrderCount(),
                                            model.getBagCount(),
                                            model.getAffirmCount(),
                                            model.getGoodsCount(),
                                            model.getTotalPrice(),
                                            model.getCustCount(),
                                            model.getSendDate(),
                                            model.getAbnormalCount(),
                                            model.getStatus(),
                                            model.getBatchId());
                                    List<SendNoAdapterInfo> unique = sendNoAdapterInfoDao.queryBuilder().list();
                                    if (unique != null && unique.size() > 0) {
                                        sendNoAdapterInfoDao.deleteAll();
                                    }
                                    sendNoAdapterInfoDao.insert(info);

                                    listener.success(3);
                                    downloadData(4, listener);
                                } else {
                                    listener.success(3);
                                    downloadData(5, listener);
                                }
                            }

                            @Override
                            public void onFail(SendNoAdapter jsonAdapter) {
                                super.onFail(jsonAdapter);
                                isGoing = false;
                                listener.error(4, jsonAdapter.getErrCode(), jsonAdapter.getMsg());
                            }
                        });

                break;
            case 4:
                listener.progress(i, 6, "正在获取配送单下的领货单");
                SendNoAdapterInfo unique = sendNoAdapterInfoDao.queryBuilder().unique();

                HDrawInvModel m = new HDrawInvModel();
                m.setSendNo(unique.getNo());

                Map<String, Object> map = new HashMap<>();
                map.put("searchData", m);
                IdeaApi.getApiService()
                        .findBySendNo(HttpParamsUtil.setParams(map))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultObserver<DrawInvAdapter>(null, DrawInvAdapter.class) {
                            @Override
                            public void onSuccess(DrawInvAdapter response) {
                                for (HDrawInvModel model : response.getDrawInvs()) {
                                    List<DrawInvsDetailInfo> l = detailInfoDao.queryBuilder().where(DrawInvsDetailInfoDao.Properties.No.eq(model.getNo())).list();
                                    if (l == null || l.size() == 0) {
                                        DrawInvsDetailInfo detailInfo = new DrawInvsDetailInfo(
                                                model.getUid(),
                                                model.getNo(),
                                                model.getExternalNo(),
                                                model.getExternalAreaId(),
                                                model.getExternalAreaName(),
                                                model.getExternalLineId(),
                                                model.getExternalLineName(),
                                                model.getCages(),
                                                model.getTotalPrice(),
                                                model.getBagCount(),
                                                model.getTotalCount(),
                                                model.getCarId(),
                                                model.getCarNo(),
                                                model.getCarNum(),
                                                model.isCarOnline(),
                                                model.getDeliveryId(),
                                                model.getDeliveryName(),
                                                model.getDriverId(),
                                                model.getDriverName(),
                                                model.getOrders(),
                                                model.getStatus(),
                                                model.getBornDate(),
                                                model.getSendDate(),
                                                model.getGoodsCount(),
                                                model.getSendNo(),
                                                model.getLineId(),
                                                model.getLineName(),
                                                model.getAreaId(),
                                                model.getAreaName(),
                                                model.getCustomerCount(),
                                                model.getAbnormalCount(),
                                                model.getLastUpdateTime(),
                                                model.getPackingType(),
                                                "");
                                        detailInfoDao.insert(detailInfo);
                                    }
                                }
                                listener.success(4);
                                downloadData(5, listener);
                            }

                            @Override
                            public void onFail(DrawInvAdapter jsonAdapter) {
                                super.onFail(jsonAdapter);
                                isGoing = false;
                                listener.error(4, jsonAdapter.getErrCode(), jsonAdapter.getMsg());
                            }
                        });

                break;
            case 5:
                listener.progress(i, 6, "获取领货单下的订单");
                List<DrawInvsDetailInfo> l = detailInfoDao.queryBuilder().where(DrawInvsDetailInfoDao.Properties.LastUpdateTime.notEq(DrawInvsDetailInfoDao.Properties.BeforeUpdateTime)).build().list();
                if (l != null && l.size() > 0) {
                    int[] c = {0};
                    for (int b = 0; b < l.size(); b++) {
                        HOrderModel model = new HOrderModel();
                        model.setDrawInvId(l.get(b).getUid());
                        Map<String, Object> params = new HashMap<>();
                        params.put("searchData", model);
                        IdeaApi.getApiService()
                                .findByDrawInv(HttpParamsUtil.setParams(params))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultObserver<OrderAdapter>(null, OrderAdapter.class) {

                                    @Override
                                    public void onSuccess(OrderAdapter response) {


                                        for (HOrderModel o : response.getOrders()) {
                                            OrderModelInfo inf = new OrderModelInfo(
                                                    o.getId(),
                                                    o.getCustId(),
                                                    o.getCustName(),
                                                    o.getManager(),
                                                    o.getManagerTel(),
                                                    o.getPwd(),
                                                    o.getNfcCardId(),
                                                    o.getAddr(),
                                                    o.getCustPhones(),
                                                    o.getCustLongitude(),
                                                    o.getCustLatitude(),
                                                    o.getCount(),
                                                    o.getPrice(),
                                                    o.getStatus(),
                                                    o.getBagCount(),
                                                    o.getExternalDrawInvNo(),
                                                    o.getExternalAreaId(),
                                                    o.getExternalAreaName(),
                                                    o.getExternalLineId(),
                                                    o.getExternalLineName(),
                                                    o.getExternalLineNo(),
                                                    o.getAreaId(),
                                                    o.getAreaName(),
                                                    o.getLineId(),
                                                    o.getLineName(),
                                                    o.getLineNo(),
                                                    o.getPmtStatus(),
                                                    o.getAbnormalCount(),
                                                    o.getSmokeBoxLendCount(),
                                                    o.getCustPhotoUpdateTime(),
                                                    o.getCustPhotoCount(),
                                                    o.getBagCodes(),
                                                    o.getOrderDetails(),
                                                    o.getCustPhotos(),
                                                    model.getDrawInvId(),
                                                    "");

                                            OrderModelInfo modelInfo = orderModelInfoDao.queryBuilder().where(OrderModelInfoDao.Properties.Id.eq(o.getId())).build().unique();

                                            if (o.getStatus() != 4) {
                                                if (modelInfo == null ) {
                                                    orderModelInfoDao.insert(inf);
                                                } else if (modelInfo.getStatus() < 4 && modelInfo.getStatus() < o.getStatus()) {
                                                    orderModelInfoDao.delete(modelInfo);
                                                    orderModelInfoDao.insert(inf);
                                                }
                                            } else {
                                                if (modelInfo != null  && modelInfo.getStatus() < 4) {
                                                    orderModelInfoDao.delete(modelInfo);
                                                }
                                            }
                                        }
                                        c[0]++;
                                        if (c[0] == l.size()) {
                                            listener.success(5);
                                            downloadData(6, listener);
                                        }
                                    }

                                    @Override
                                    public void onFail(OrderAdapter jsonAdapter) {
                                        super.onFail(jsonAdapter);
                                        c[0]++;
                                        isGoing = false;
                                        listener.error(5, jsonAdapter.getErrCode(), jsonAdapter.getMsg());
                                    }
                                });
                    }

                } else {
                    listener.success(5);
                    downloadData(6, listener);
                }
                break;
            case 6:
                listener.progress(i, 6, "获取异常订单");
                IdeaApi.getApiService()
                        .getExpOrders(HttpParamsUtil.setParams(new HashMap<>()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DefaultObserver<OrderAdapter>(null, OrderAdapter.class) {

                            @Override
                            public void onSuccess(OrderAdapter response) {
                                for (HOrderModel model : response.getOrders()) {
                                    OrderModelInfo inf = new OrderModelInfo(
                                            model.getId(),
                                            model.getCustId(),
                                            model.getCustName(),
                                            model.getManager(),
                                            model.getManagerTel(),
                                            model.getPwd(),
                                            model.getNfcCardId(),
                                            model.getAddr(),
                                            model.getCustPhones(),
                                            model.getCustLongitude(),
                                            model.getCustLatitude(),
                                            model.getCount(),
                                            model.getPrice(),
                                            model.getStatus(),
                                            model.getBagCount(),
                                            model.getExternalDrawInvNo(),
                                            model.getExternalAreaId(),
                                            model.getExternalAreaName(),
                                            model.getExternalLineId(),
                                            model.getExternalLineName(),
                                            model.getExternalLineNo(),
                                            model.getAreaId(),
                                            model.getAreaName(),
                                            model.getLineId(),
                                            model.getLineName(),
                                            model.getLineNo(),
                                            model.getPmtStatus(),
                                            model.getAbnormalCount(),
                                            model.getSmokeBoxLendCount(),
                                            model.getCustPhotoUpdateTime(),
                                            model.getCustPhotoCount(),
                                            model.getBagCodes(),
                                            model.getOrderDetails(),
                                            model.getCustPhotos(),
                                            model.getDrawInvId(),
                                            "");
                                    OrderModelInfo un = orderModelInfoDao.queryBuilder().where(OrderModelInfoDao.Properties.Id.eq(model.getId())).build().unique();
                                    if (model.getStatus() != 5) {
                                        if (un == null ) {
                                            orderModelInfoDao.insert(inf);
                                        } else if (model.getStatus() < 5 && un.getStatus() < model.getStatus()) {
                                            orderModelInfoDao.delete(un);
                                            orderModelInfoDao.insert(inf);
                                        }
                                    } else {
                                        if (un != null  && un.getStatus() < 5) {
                                            orderModelInfoDao.delete(un);
                                        }
                                    }
                                }
                                isGoing = false;
                                listener.success(6);
                                listener.finish();
                            }

                            @Override
                            public void onFail(OrderAdapter jsonAdapter) {
                                super.onFail(jsonAdapter);
                                isGoing = false;
                                listener.error(5, jsonAdapter.getErrCode(), jsonAdapter.getMsg());
                            }
                        });
                break;
        }
    }


    private List<BillOfGoodsInfo> formatBill(List<String> nos) {
        List<BillOfGoodsInfo> billNos = new ArrayList<>();
        for (int i = 0; i < nos.size(); i++) {
            String billNo = nos.get(i);
            String[] split = billNo.split(",");
            BillOfGoodsInfo billOfGoodsInfo = new BillOfGoodsInfo();
            billOfGoodsInfo.setUuid(split[0]);
            billOfGoodsInfo.setBillNo(split[1]);
            billOfGoodsInfo.setStatus(Integer.valueOf(split[2]));
            billOfGoodsInfo.setLastDate(split[3]);
            billNos.add(billOfGoodsInfo);
        }

        return billNos;
    }
}
