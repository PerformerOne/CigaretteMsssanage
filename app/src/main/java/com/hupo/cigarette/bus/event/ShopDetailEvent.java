package com.hupo.cigarette.bus.event;

import com.huposoft.softs.chuanchuan.vmspda.commons.api.model.HCustModel;

public class ShopDetailEvent {
    private HCustModel detail;


    public ShopDetailEvent(HCustModel detail) {
        this.detail = detail;
    }

    public HCustModel getDetail() {
        return detail;
    }

    public void setDetail(HCustModel detail) {
        this.detail = detail;
    }
}
