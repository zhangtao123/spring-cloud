/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.alibaba.fastjson.JSON;
import com.anji.allways.common.constants.RespCode;

/**
 * <pre>
 * 应答对象模型
 * </pre>
 * @author WangGuangYuan
 * @version $Id: ZuulResponseModel.java, v 0.1 2017年5月8日 上午10:15:12 Administrator Exp $
 */
public class ZuulResponseModel<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String            repCode;

    private String            repMsg;

    private T                 repData;

    public ZuulResponseModel() {
        this.repCode = RespCode.SUCCESS; // 默认成功
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }

    /**
     * Getter method for property <tt>repCode</tt>.
     * @return property value of repCode
     */
    public String getRepCode() {
        return repCode;
    }

    /**
     * Setter method for property <tt>repCode</tt>.
     * @param repCode
     *            value to be assigned to property repCode
     */
    public void setRepCode(String repCode) {
        this.repCode = repCode;
    }

    /**
     * Getter method for property <tt>repMsg</tt>.
     * @return property value of repMsg
     */
    public String getRepMsg() {
        return repMsg;
    }

    /**
     * Setter method for property <tt>repMsg</tt>.
     * @param repMsg
     *            value to be assigned to property repMsg
     */
    public void setRepMsg(String repMsg) {
        this.repMsg = repMsg;
    }

    /**
     * Getter method for property <tt>repData</tt>.
     * @return property value of repData
     */
    public T getRepData() {
        return repData;
    }

    /**
     * Setter method for property <tt>repData</tt>.
     * @param repData
     *            value to be assigned to property repData
     */
    public void setRepData(T repData) {
        this.repData = repData;
    }

}
