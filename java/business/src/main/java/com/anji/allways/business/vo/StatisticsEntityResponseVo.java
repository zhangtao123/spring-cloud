/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 统计库存信息返回Vo
 * @author Administrator
 * @version $Id: StatisticsEntityResponseVo.java, v 0.1 2017年9月29日 下午3:34:33 Administrator Exp $
 */
public class StatisticsEntityResponseVo implements Serializable {

    /**
     * <pre>
     * 序列化
     * </pre>
     */
    private static final long serialVersionUID = -4108826719986750008L;
    // 客户ID
    private Integer           customerId;
    // 统计数
    private Integer           count;
    // 仓库ID
    private Integer           warehouseId;
    // 经销商ID
    private Integer           dealerId;
    // 入库时间
    private Date              storageTime;
    // 出库时间
    private Date              deliveryTime;

    /**
     * Getter method for property <tt>customerId</tt>.
     * @return property value of customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * Setter method for property <tt>customerId</tt>.
     * @param customerId
     *            value to be assigned to property customerId
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * Getter method for property <tt>count</tt>.
     * @return property value of count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Setter method for property <tt>count</tt>.
     * @param count
     *            value to be assigned to property count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * Getter method for property <tt>warehouseId</tt>.
     * @return property value of warehouseId
     */
    public Integer getWarehouseId() {
        return warehouseId;
    }

    /**
     * Setter method for property <tt>warehouseId</tt>.
     * @param warehouseId
     *            value to be assigned to property warehouseId
     */
    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
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

    /**
     * Getter method for property <tt>storageTime</tt>.
     * @return property value of storageTime
     */
    public Date getStorageTime() {
        if (null == storageTime) {
            return null;
        }
        return (Date) storageTime.clone();
    }

    /**
     * Setter method for property <tt>storageTime</tt>.
     * @param storageTime
     *            value to be assigned to property storageTime
     */
    public void setStorageTime(Date storageTime) {
        if (null == storageTime) {
            this.storageTime = null;
        } else {
            this.storageTime = (Date) storageTime.clone();
        }
    }

    /**
     * Getter method for property <tt>deliveryTime</tt>.
     * @return property value of deliveryTime
     */
    public Date getDeliveryTime() {
        if (null == deliveryTime) {
            return null;
        }
        return (Date) deliveryTime.clone();
    }

    /**
     * Setter method for property <tt>deliveryTime</tt>.
     * @param deliveryTime
     *            value to be assigned to property deliveryTime
     */
    public void setDeliveryTime(Date deliveryTime) {
        if (null == deliveryTime) {
            this.deliveryTime = null;
        } else {
            this.deliveryTime = (Date) deliveryTime.clone();
        }
    }

}
