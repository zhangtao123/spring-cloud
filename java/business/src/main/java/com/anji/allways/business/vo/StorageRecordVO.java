/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.vo;

import java.util.Date;

/**
 * @author wangyanjun
 * @version $Id: StorageRecordVO.java, v 0.1 2017年9月11日 下午11:45:07 wangyanjun Exp $
 */
public class StorageRecordVO {

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.vin
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String vin;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.brand
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String brand;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.series
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String series;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.model
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String model;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.color
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String color;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.location
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String location;

    /**
     * 入库时间
     */
    private Date   storageTime;

    /**
     * 客户名
     */
    private String customer;

    /**
     * 仓库名
     */
    private String warehouseName;

    /**
     * 操作人
     */
    private String operator;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_storage_vehicle.vin
     * @return the value of tb_storage_vehicle.vin
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getVin() {
        return vin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_storage_vehicle.vin
     * @param vin
     *            the value for tb_storage_vehicle.vin
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setVin(String vin) {
        this.vin = vin == null ? null : vin.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_storage_vehicle.brand
     * @return the value of tb_storage_vehicle.brand
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getBrand() {
        return brand;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_storage_vehicle.brand
     * @param brand
     *            the value for tb_storage_vehicle.brand
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setBrand(String brand) {
        this.brand = brand == null ? null : brand.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_storage_vehicle.series
     * @return the value of tb_storage_vehicle.series
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getSeries() {
        return series;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_storage_vehicle.series
     * @param series
     *            the value for tb_storage_vehicle.series
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setSeries(String series) {
        this.series = series == null ? null : series.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_storage_vehicle.model
     * @return the value of tb_storage_vehicle.model
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getModel() {
        return model;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_storage_vehicle.model
     * @param model
     *            the value for tb_storage_vehicle.model
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_storage_vehicle.color
     * @return the value of tb_storage_vehicle.color
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getColor() {
        return color;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_storage_vehicle.color
     * @param color
     *            the value for tb_storage_vehicle.color
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_storage_vehicle.location
     * @return the value of tb_storage_vehicle.location
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getLocation() {
        return location;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_storage_vehicle.location
     * @param location
     *            the value for tb_storage_vehicle.location
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_storage_plan.storage_time
     * @return the value of tb_storage_plan.storage_time
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Date getStorageTime() {
        if (null == storageTime) {
            return null;
        }
        return (Date) storageTime.clone();
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_storage_plan.storage_time
     * @param createTime
     *            the value for tb_storage_plan.storage_time
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setStorageTime(Date storageTime) {
        if (null == storageTime) {
            this.storageTime = null;
        } else {
            this.storageTime = (Date) storageTime.clone();
        }
    }

    /**
     * Getter method for property <tt>customer</tt>.
     * @return property value of customer
     */
    public String getCustomer() {
        return customer;
    }

    /**
     * Setter method for property <tt>customer</tt>.
     * @param customer
     *            value to be assigned to property customer
     */
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    /**
     * Getter method for property <tt>warehouseName</tt>.
     * @return property value of warehouseName
     */
    public String getWarehouseName() {
        return warehouseName;
    }

    /**
     * Setter method for property <tt>warehouseName</tt>.
     * @param warehouseName
     *            value to be assigned to property warehouseName
     */
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    /**
     * Getter method for property <tt>operator</tt>.
     * @return property value of operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Setter method for property <tt>operator</tt>.
     * @param operator
     *            value to be assigned to property operator
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }
}
