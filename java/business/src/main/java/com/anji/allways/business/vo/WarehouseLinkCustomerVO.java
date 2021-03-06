package com.anji.allways.business.vo;

import java.util.Date;

public class WarehouseLinkCustomerVO {
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_warehouse_link_customer.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_warehouse_link_customer.customer_id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer customerId;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_warehouse_link_customer.warehouse_id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer warehouseId;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_warehouse_link_customer.space_amount
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer spaceAmount;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_warehouse_link_customer.create_user
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer createUser;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_warehouse_link_customer.create_time
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Date    createTime;

    /**
     * 客户名
     */
    private String  customerName;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_warehouse_link_customer.id
     * @return the value of tb_warehouse_link_customer.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_warehouse_link_customer.id
     * @param id
     *            the value for tb_warehouse_link_customer.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_warehouse_link_customer.customer_id
     * @return the value of tb_warehouse_link_customer.customer_id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_warehouse_link_customer.customer_id
     * @param customerId
     *            the value for tb_warehouse_link_customer.customer_id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_warehouse_link_customer.warehouse_id
     * @return the value of tb_warehouse_link_customer.warehouse_id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getWarehouseId() {
        return warehouseId;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_warehouse_link_customer.warehouse_id
     * @param warehouseId
     *            the value for tb_warehouse_link_customer.warehouse_id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_warehouse_link_customer.space_amount
     * @return the value of tb_warehouse_link_customer.space_amount
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getSpaceAmount() {
        return spaceAmount;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_warehouse_link_customer.space_amount
     * @param spaceAmount
     *            the value for tb_warehouse_link_customer.space_amount
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setSpaceAmount(Integer spaceAmount) {
        this.spaceAmount = spaceAmount;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_warehouse_link_customer.create_user
     * @return the value of tb_warehouse_link_customer.create_user
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getCreateUser() {
        return createUser;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_warehouse_link_customer.create_user
     * @param createUser
     *            the value for tb_warehouse_link_customer.create_user
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_warehouse_link_customer.create_time
     * @return the value of tb_warehouse_link_customer.create_time
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Date getCreateTime() {
        if (createTime == null) {
            return null;
        } else {
            return (Date) createTime.clone();
        }
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_warehouse_link_customer.create_time
     * @param createTime
     *            the value for tb_warehouse_link_customer.create_time
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setCreateTime(Date createTime) {
        if (createTime == null) {
            this.createTime = null;
        } else {
            this.createTime = (Date) createTime.clone();
        }
    }

    /**
     * Getter method for property <tt>customerName</tt>.
     * @return property value of customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Setter method for property <tt>customerName</tt>.
     * @param customerName
     *            value to be assigned to property customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
