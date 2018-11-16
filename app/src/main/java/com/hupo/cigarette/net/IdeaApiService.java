package com.hupo.cigarette.net;

import com.huposoft.commons.json.JsonAdapter;
import com.huposoft.softs.chuanchuan.platform.app.api.LoginResultAdapter;
import com.huposoft.softs.chuanchuan.platform.app.api.UserAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.CarAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.CustAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.DrawInvAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.DrawInvCustPhotoAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.OnCarResultAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.OrderAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.SendAreaAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.SendLineAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.SendNoAdapter;
import com.huposoft.softs.chuanchuan.vmspda.commons.api.SplitDrawInvResultAdapter;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Gemini on 2018/2/7.
 */

public interface IdeaApiService {

    /**
     * 登录
     * @param val
     * @return
     */
    @FormUrlEncoded
    @POST("platform/app/login.htm")
    Observable<LoginResultAdapter> login(@Field("val") String val);

    /**
     * 登出
     * @param val
     * @return
     */
    @FormUrlEncoded
    @POST("platform/app/logout.htm")
    Observable<JsonAdapter> logout(@Field("val") String val);

    /**
     * 修改密码
     * @param val
     * @return
     */
    @FormUrlEncoded
    @POST("platform/app/editPwd.htm")
    Observable<JsonAdapter> editPwd(@Field("val") String val);

    /**
     * 获取联系人列表
     * @param val
     * @return
     */
    @FormUrlEncoded
    @POST("platform/app/getPersonInfos.htm")
    Observable<UserAdapter> getPersonInfos(@Field("val") String val);


    /**
     * 获取配送单列表
     * @param val
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/sendNo/getSendNos.htm")
    Observable<SendNoAdapter> getSendNos(@Field("val") String val);

    /**
     * 获取配送单下的领货单
     * @param val
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/drawInv/findBySendNo.htm")
    Observable<DrawInvAdapter> findBySendNo(@Field("val") String val);

    /**
     * 获取领货单下的订单
     * @param val
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/order/findByDrawInv.htm")
    Observable<OrderAdapter> findByDrawInv(@Field("val") String val);

    /**
     * 获取当前登录用户名下的异常订单
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/order/getExpOrders.htm")
    Observable<OrderAdapter> getExpOrders(@Field("val") String val);

    /**
     * 绑定车辆
     * @param val
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/car/bindCar.htm")
    Observable<CarAdapter> bindCar(@Field("val") String val);

    /**
     * 取消绑定车辆
     * @param val
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/car/unbindCar.htm")
    Observable<CarAdapter> unbindCar(@Field("val") String val);

    /**
     * 获取当前登录用户的所有领货单单号
     * @param val
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/drawInv/getDrawInvNos.htm")
    Observable<DrawInvAdapter> getDrawInvNos(@Field("val") String val);


    /**
     * 获取领货单详情
     * @param val
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/drawInv/getDrawInvs.htm")
    Observable<DrawInvAdapter> getDrawInvs(@Field("val") String val);

    /**
     * 获取配送区域信息
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/sendLine/getSendAreas.htm")
    Observable<SendAreaAdapter> getAreaInfo(@Field("val") String val);


    /**
     * 获取配送线路信息
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/sendLine/getSendLines.htm")
    Observable<SendLineAdapter> getLineInfo(@Field("val") String val);

    /**
     * 获取指定配送线路的商户列表信息
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/cust/getSendLineCusts.htm")
    Observable<CustAdapter> getShopList(@Field("val") String val);

    /**
     * 获取正在配送的配送单信息
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/sendNo/getCurSendNo.htm")
    Observable<SendNoAdapter> getCurSendNos(@Field("val") String val);

    /**
     * 上车
     * @param val
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/drawInv/bindCar.htm")
    Observable<OnCarResultAdapter> onTheCar(@Field("val") String val);

    /**
     * 取消上车
     * @param val
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/drawInv/unBindCar.htm")
    Observable<JsonAdapter> cancelOnTheCar(@Field("val") String val);

    /**
     * 领货单拆单
     * @param val
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/drawInv/splitDrawInv.htm")
    Observable<SplitDrawInvResultAdapter> splitDrawInv(@Field("val") String val);

    /**
     * 开始配送
     * @param val
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/sendNo/affirmOnline.htm")
    Observable<SendNoAdapter> affirmOnline(@Field("val") String val);

    /**
     * 获取当前绑定的车辆
     * @param val
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/car/getBindCar.htm")
    Observable<CarAdapter> getBindCar(@Field("val") String val);


    /**
     * 获取指定商户的门头缩略图
     * @param val
     * @return
     */
    @FormUrlEncoded
    @POST("vmspda/app/cust/getCustShortcutPhotos.htm")
    Observable<DrawInvCustPhotoAdapter> getCustShortcutPhotos(@Field("val") String val);
}
