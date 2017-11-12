/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 统计库存信息请求Vo
 * @author Administrator
 * @version $Id: StatisticsEntityResquestVo.java, v 0.1 2017年9月29日 下午3:33:07 Administrator Exp $
 */
public class StatisticsEntityResquestVo implements Serializable {

    /**
     * <pre>
     * 序列化
     * </pre>
     */
    private static final long serialVersionUID = 7367423809838684761L;
    // 时间
    private String            requestData;
    // 经销商ID
    private Integer           dealerId;
    // 仓库ID
    private List<Integer>     warehouseIds     = new ArrayList<Integer>();

    /**
     * Getter method for property <tt>requestData</tt>.
     * @return property value of requestData
     */
    public String getRequestData() {
        return requestData;
    }

    /**
     * Setter method for property <tt>requestData</tt>.
     * @param requestData
     *            value to be assigned to property requestData
     */
    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    /**
     * Getter method for property <tt>warehouseIds</tt>.
     * @return property value of warehouseIds
     */
    public List<Integer> getWarehouseIds() {
        return warehouseIds;
    }

    /**
     * Setter method for property <tt>warehouseIds</tt>.
     * @param warehouseIds
     *            value to be assigned to property warehouseIds
     */
    public void setWarehouseIds(List<Integer> warehouseIds) {
        this.warehouseIds = warehouseIds;
    }

    /**
     * Getter method for property <tt>dealerId</tt>.
     * @return property value of dealerId
     */
    public Integer getDealerId() {
        return dealerId;
    }

    /**
     * Setter method for property <tt>dealerId</tt>.
     * @param dealerId
     *            value to be assigned to property dealerId
     */
    public void setDealerId(Integer dealerId) {
        this.dealerId = dealerId;
    }

}
