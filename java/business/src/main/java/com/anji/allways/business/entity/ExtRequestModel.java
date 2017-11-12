/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * <pre>
 *
 * </pre>
 * @author wangyanjun
 * @version $Id: ExtRequestModel.java, v 0.1 2017年8月31日 上午9:27:00 wangyanjun Exp $
 */
public class ExtRequestModel implements Serializable {

    private static final long serialVersionUID = 2885783320770351263L;

    private String            uid;

    private String            sign;

    private String            warehouseId;

    private JSONObject        reqData;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public JSONObject getReqData() {
        return reqData;
    }

    public void setReqData(JSONObject reqData) {
        this.reqData = reqData;
    }
}
