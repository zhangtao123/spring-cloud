/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.enums;

import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author liukangsong
 * @version $Id: DictCodeE.java, v 0.1 2017年5月10日 下午2:05:31 liukangsong Exp $
 */
public enum RowDictCodeE {
    COMPANY_STATUS("运输公司状态", "COMSTA000001"),
    DRIVER_STATUS("运输司机状态", "DRIVERSTA000001"),
    STANDARD_COLOR("标准色", "COLOR000001"),
    ORDER_STATE("订单状态", "STATE000001"),
    WAREHOUSE_STATUS("仓库状态", "WAREHOUSESTA000001"),
    BUSSIONTIME_TYPE("营业时间类型", "BUSSIONTIME000001"),
    WARNING_TYPE("使用告警类型", "WARNINGTYPE000001"),
    STORAGE_PLAN_STATUS("入库计划单状态", "STORAGEPLAN000001"),
    ORDER_STATUS("运输订单状态", "ORDERSTATUS000001"),
    CAR_STATUS("车辆状态", "STATE000001"),
    QUALITY_STATUS("质量状态", "QUALITYSTATUS000001"),
    LOCK_STATUS("锁定状态", "LOCKSTATUS000001"),
    CUSTOMER_STATUS("客户状态", "CUSTOMERSTA000001"),
    STORAGE_CAR_STATUS("库存车辆状态", "STORAGECARSTA000001");

    private String name;

    private String value;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        if (StringUtils.isBlank(name)) {
            name = "";
        }
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        if (StringUtils.isBlank(value)) {
            value = "";
        }
        this.value = value;
    }

    RowDictCodeE(String name, String value) {
        this.name = name;
        this.value = value;
    }

    private static List<RowDictCodeE> allStatus = EnumUtils.getEnumList(RowDictCodeE.class);

    /**
     * @since 1.0
     * @param status
     *            状态
     * @return <br>
     */
    public static RowDictCodeE value(String status) {
        for (RowDictCodeE eachStatus : allStatus) {
            if (eachStatus.getValue().equals(status)) {
                return eachStatus;
            }
        }
        return null;
    }

    /**
     * @since 1.0
     * @return <br>
     */
    public static JSONArray toJSONArray() {
        JSONArray data = new JSONArray();
        for (RowDictCodeE orderStatus : allStatus) {
            JSONObject json = new JSONObject();
            json.put("name", orderStatus.getName());
            json.put("value", orderStatus.getValue());
            data.add(json);
        }
        return data;
    }
}
