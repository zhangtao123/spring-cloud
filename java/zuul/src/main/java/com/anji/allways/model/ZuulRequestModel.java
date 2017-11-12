/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.model;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * <pre>
 * 请求对象模型
 * </pre>
 * @author WangGuangYuan
 * @version $Id: ZuulRequestModel.java, v 0.1 2017年5月16日 上午10:03:26 Administrator Exp $
 */
public class ZuulRequestModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String            url;

    private String            token;

    private String            userId;

    private String            sign;

    private String            time;

    private JSONObject        reqData;

    private String            jsonStr;

    private String            authUrl;

    public boolean isVaildateRequest() { // 校验自身参数合法性
        boolean isValid = false;
        if (null == time || null == sign || "".equals(time) || "".equals(sign)) {
            return isValid;
        }
        isValid = true;
        return isValid;
    }

    /**
     * Getter method for property <tt>url</tt>.
     * @return property value of url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter method for property <tt>url</tt>.
     * @param url
     *            value to be assigned to property url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Getter method for property <tt>token</tt>.
     * @return property value of token
     */
    public String getToken() {
        return token;
    }

    /**
     * Setter method for property <tt>token</tt>.
     * @param token
     *            value to be assigned to property token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Getter method for property <tt>userId</tt>.
     * @return property value of userId
     */
    public String getUserId() {
        if (null != this.userId) {
            return userId;
        }
        if (null == this.token || 0 == this.token.length() || !this.token.contains("_")) {
            this.token = "111_xxxxx";
        }
        String[] tokenInfo = this.token.split("_");
        if (tokenInfo.length != 2) {
            return null;
        }
        this.userId = tokenInfo[0];
        return userId;
    }

    /**
     * Setter method for property <tt>userId</tt>.
     * @param userId
     *            value to be assigned to property userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    /**
     * Getter method for property <tt>jsonStr</tt>.
     * @return property value of jsonStr
     */
    public String getJsonStr() {
        return jsonStr;
    }

    /**
     * Setter method for property <tt>jsonStr</tt>.
     * @param jsonStr
     *            value to be assigned to property jsonStr
     */
    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    /**
     * Getter method for property <tt>authUrl</tt>.
     * @return property value of authUrl
     */
    public String getAuthUrl() {
        return authUrl;
    }

    /**
     * Setter method for property <tt>authUrl</tt>.
     * @param authUrl
     *            value to be assigned to property authUrl
     */
    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

}
