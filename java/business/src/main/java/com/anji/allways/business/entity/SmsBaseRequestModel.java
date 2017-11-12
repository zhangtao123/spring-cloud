/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * <pre>
 * 基础请求对象
 * </pre>
 * @author WangGuangYuan
 * @version $Id: BaseRequestModel.java, v 0.1 2017年5月16日 上午10:03:26 Administrator Exp $
 */
public class SmsBaseRequestModel implements Serializable {

    /**
     * <pre>
     * 序列化
     * </pre>
     */
    private static final long serialVersionUID = 1499905544067396720L;

    private String            sign;

    private String            time;

    private String            sysCode;

    private JSONObject        reqData;

    /**
     * Getter method for property <tt>sign</tt>.
     * @return property value of sign
     */
    public String getSign() {
        return sign;
    }

    /**
     * Setter method for property <tt>sign</tt>.
     * @param sign
     *            value to be assigned to property sign
     */
    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * Getter method for property <tt>time</tt>.
     * @return property value of time
     */
    public String getTime() {
        return time;
    }

    /**
     * Setter method for property <tt>time</tt>.
     * @param time
     *            value to be assigned to property time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Getter method for property <tt>sysCode</tt>.
     * @return property value of sysCode
     */
    public String getSysCode() {
        return sysCode;
    }

    /**
     * Setter method for property <tt>sysCode</tt>.
     * @param sysCode
     *            value to be assigned to property sysCode
     */
    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    /**
     * Getter method for property <tt>reqData</tt>.
     * @return property value of reqData
     */
    public JSONObject getReqData() {
        return reqData;
    }

    /**
     * Setter method for property <tt>reqData</tt>.
     * @param reqData
     *            value to be assigned to property reqData
     */
    public void setReqData(JSONObject reqData) {
        this.reqData = reqData;
    }

}
