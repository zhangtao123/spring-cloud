/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.entity;

import java.util.List;

import com.anji.allways.common.util.PageUtil;

/**
 * @author wangyanjun
 * @version $Id: StorageRecordEntity.java, v 0.1 2017年9月11日 下午11:25:18 wangyanjun Exp $
 */
public class StorageRecordEntity extends PageUtil {

    private String        vin;

    /**
     * 入库开始时间
     */
    private String        storageStartTime;

    /**
     * 入库结束时间
     */
    private String        storageEndTime;

    /**
     * 客户名
     */
    private String        customer;

    /**
     * 仓库名
     */
    private String        warehouseName;

    /**
     * 入库计划单状态
     */
    private int           status;

    /**
     * 所有层级ID
     */
    private List<Integer> idList;

    /**
     * 创建时间
     */
    private Integer       createUser;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin == null ? null : vin.trim();
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer == null ? null : customer.trim();
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName == null ? null : warehouseName.trim();
    }

    public String getStorageStartTime() {
        return storageStartTime;
    }

    public void setStorageStartTime(String storageStartTime) {
        this.storageStartTime = storageStartTime;
    }

    public String getStorageEndTime() {
        return storageEndTime;
    }

    public void setStorageEndTime(String storageEndTime) {
        this.storageEndTime = storageEndTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Getter method for property <tt>idList</tt>.
     * @return property value of idList
     */
    public List<Integer> getIdList() {
        return idList;
    }

    /**
     * Setter method for property <tt>idList</tt>.
     * @param idList
     *            value to be assigned to property idList
     */
    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }

    /**
     * Getter method for property <tt>createUser</tt>.
     * @return property value of createUser
     */
    public Integer getCreateUser() {
        return createUser;
    }

    /**
     * Setter method for property <tt>createUser</tt>.
     * @param createUser
     *            value to be assigned to property createUser
     */
    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }
}
