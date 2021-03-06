package com.anji.allways.business.entity;

import java.util.Date;

import com.anji.allways.common.util.PageUtil;

public class TruckDriverEntity extends PageUtil {
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_truck_driver.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_truck_driver.mobile
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  mobile;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_truck_driver.name
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  name;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_truck_driver.id_card_no
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  idCardNo;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_truck_driver.id_card_front_picture
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  idCardFrontPicture;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_truck_driver.id_card_back_picture
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  idCardBackPicture;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_truck_driver.account_name
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  accountName;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_truck_driver.transport_company_name
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  transportCompanyName;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_truck_driver.transport_company_id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer transportCompanyId;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_truck_driver.truck_number
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  truckNumber;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_truck_driver.status
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private int     status;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_truck_driver.create_user
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer createUser;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_truck_driver.create_time
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Date    createTime;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_truck_driver.update_user
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer updateUser;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_truck_driver.update_time
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Date    updateTime;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_truck_driver.reserved
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  reserved;

    /**
     * 创建开始时间
     */
    private String  createStartTime;

    /**
     * 创建结束时间
     */
    private String  createEndTime;

    /**
     * 仓库状态查询使用
     */
    private String  searchStatus;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_truck_driver.id
     * @return the value of tb_truck_driver.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_truck_driver.id
     * @param id
     *            the value for tb_truck_driver.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_truck_driver.mobile
     * @return the value of tb_truck_driver.mobile
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_truck_driver.mobile
     * @param mobile
     *            the value for tb_truck_driver.mobile
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_truck_driver.name
     * @return the value of tb_truck_driver.name
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_truck_driver.name
     * @param name
     *            the value for tb_truck_driver.name
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_truck_driver.id_card_no
     * @return the value of tb_truck_driver.id_card_no
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getIdCardNo() {
        return idCardNo;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_truck_driver.id_card_no
     * @param idCardNo
     *            the value for tb_truck_driver.id_card_no
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo == null ? null : idCardNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_truck_driver.id_card_front_picture
     * @return the value of tb_truck_driver.id_card_front_picture
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getIdCardFrontPicture() {
        return idCardFrontPicture;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_truck_driver.id_card_front_picture
     * @param idCardFrontPicture
     *            the value for tb_truck_driver.id_card_front_picture
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setIdCardFrontPicture(String idCardFrontPicture) {
        this.idCardFrontPicture = idCardFrontPicture == null ? null : idCardFrontPicture.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_truck_driver.id_card_back_picture
     * @return the value of tb_truck_driver.id_card_back_picture
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getIdCardBackPicture() {
        return idCardBackPicture;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_truck_driver.id_card_back_picture
     * @param idCardBackPicture
     *            the value for tb_truck_driver.id_card_back_picture
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setIdCardBackPicture(String idCardBackPicture) {
        this.idCardBackPicture = idCardBackPicture == null ? null : idCardBackPicture.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_truck_driver.account_name
     * @return the value of tb_truck_driver.account_name
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_truck_driver.account_name
     * @param accountName
     *            the value for tb_truck_driver.account_name
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_truck_driver.transport_company_name
     * @return the value of tb_truck_driver.transport_company_name
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getTransportCompanyName() {
        return transportCompanyName;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_truck_driver.transport_company_name
     * @param transportCompanyName
     *            the value for tb_truck_driver.transport_company_name
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setTransportCompanyName(String transportCompanyName) {
        this.transportCompanyName = transportCompanyName == null ? null : transportCompanyName.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_truck_driver.transport_company_id
     * @return the value of tb_truck_driver.transport_company_id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getTransportCompanyId() {
        return transportCompanyId;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_truck_driver.transport_company_id
     * @param transportCompanyId
     *            the value for tb_truck_driver.transport_company_id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setTransportCompanyId(Integer transportCompanyId) {
        this.transportCompanyId = transportCompanyId;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_truck_driver.truck_number
     * @return the value of tb_truck_driver.truck_number
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getTruckNumber() {
        return truckNumber;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_truck_driver.truck_number
     * @param truckNumber
     *            the value for tb_truck_driver.truck_number
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber == null ? null : truckNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_truck_driver.status
     * @return the value of tb_truck_driver.status
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public int getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_truck_driver.status
     * @param status
     *            the value for tb_truck_driver.status
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_truck_driver.create_user
     * @return the value of tb_truck_driver.create_user
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getCreateUser() {
        return createUser;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_truck_driver.create_user
     * @param createUser
     *            the value for tb_truck_driver.create_user
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_truck_driver.create_time
     * @return the value of tb_truck_driver.create_time
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Date getCreateTime() {
        if (createTime == null) {
            return null;
        }
        return (Date) createTime.clone();
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_truck_driver.create_time
     * @param createTime
     *            the value for tb_truck_driver.create_time
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
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_truck_driver.update_user
     * @return the value of tb_truck_driver.update_user
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getUpdateUser() {
        return updateUser;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_truck_driver.update_user
     * @param updateUser
     *            the value for tb_truck_driver.update_user
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_truck_driver.update_time
     * @return the value of tb_truck_driver.update_time
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Date getUpdateTime() {
        if (updateTime == null) {
            return null;
        }
        return (Date) updateTime.clone();
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_truck_driver.update_time
     * @param updateTime
     *            the value for tb_truck_driver.update_time
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setUpdateTime(Date updateTime) {
        if (updateTime == null) {
            this.updateTime = null;
        } else {
            this.updateTime = (Date) updateTime.clone();
        }

    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_truck_driver.reserved
     * @return the value of tb_truck_driver.reserved
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getReserved() {
        return reserved;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_truck_driver.reserved
     * @param reserved
     *            the value for tb_truck_driver.reserved
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setReserved(String reserved) {
        this.reserved = reserved == null ? null : reserved.trim();
    }

    /**
     * Getter method for property <tt>createStartTime</tt>.
     * @return property value of createStartTime
     */
    public String getCreateStartTime() {
        return createStartTime;
    }

    /**
     * Setter method for property <tt>createStartTime</tt>.
     * @param createStartTime
     *            value to be assigned to property createStartTime
     */
    public void setCreateStartTime(String createStartTime) {
        this.createStartTime = createStartTime;
    }

    /**
     * Getter method for property <tt>createEndTime</tt>.
     * @return property value of createEndTime
     */
    public String getCreateEndTime() {
        return createEndTime;
    }

    /**
     * Setter method for property <tt>createEndTime</tt>.
     * @param createEndTime
     *            value to be assigned to property createEndTime
     */
    public void setCreateEndTime(String createEndTime) {
        this.createEndTime = createEndTime;
    }

    /**
     * Getter method for property <tt>searchStatus</tt>.
     * @return property value of searchStatus
     */
    public String getSearchStatus() {
        return searchStatus;
    }

    /**
     * Setter method for property <tt>searchStatus</tt>.
     * @param searchStatus
     *            value to be assigned to property searchStatus
     */
    public void setSearchStatus(String searchStatus) {
        this.searchStatus = searchStatus;
    }

}
