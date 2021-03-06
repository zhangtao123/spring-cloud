package com.anji.allways.business.entity;

import java.util.Date;

public class TempVehicleEntity {
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_temp_vehicle.id
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_temp_vehicle.no
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    private String  no;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_temp_vehicle.vin
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    private String  vin;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_temp_vehicle.brand
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    private String  brand;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_temp_vehicle.series
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    private String  series;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_temp_vehicle.model
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    private String  model;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_temp_vehicle.color
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    private String  color;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_temp_vehicle.license_number
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    private String  licenseNumber;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_temp_vehicle.registration_number
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    private String  registrationNumber;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_temp_vehicle.rownum
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    private String  rownum;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_temp_vehicle.comment
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    private String  comment;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_temp_vehicle.create_user
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    private Integer createUser;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_temp_vehicle.create_time
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    private Date    createTime;

    /**
     * 库位号
     */
    private String  location;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_temp_vehicle.id
     * @return the value of tb_temp_vehicle.id
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_temp_vehicle.id
     * @param id
     *            the value for tb_temp_vehicle.id
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_temp_vehicle.no
     * @return the value of tb_temp_vehicle.no
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public String getNo() {
        return no;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_temp_vehicle.no
     * @param no
     *            the value for tb_temp_vehicle.no
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public void setNo(String no) {
        this.no = no == null ? null : no.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_temp_vehicle.vin
     * @return the value of tb_temp_vehicle.vin
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public String getVin() {
        return vin;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_temp_vehicle.vin
     * @param vin
     *            the value for tb_temp_vehicle.vin
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public void setVin(String vin) {
        this.vin = vin == null ? null : vin.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_temp_vehicle.brand
     * @return the value of tb_temp_vehicle.brand
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public String getBrand() {
        return brand;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_temp_vehicle.brand
     * @param brand
     *            the value for tb_temp_vehicle.brand
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public void setBrand(String brand) {
        this.brand = brand == null ? null : brand.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_temp_vehicle.series
     * @return the value of tb_temp_vehicle.series
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public String getSeries() {
        return series;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_temp_vehicle.series
     * @param series
     *            the value for tb_temp_vehicle.series
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public void setSeries(String series) {
        this.series = series == null ? null : series.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_temp_vehicle.model
     * @return the value of tb_temp_vehicle.model
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public String getModel() {
        return model;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_temp_vehicle.model
     * @param model
     *            the value for tb_temp_vehicle.model
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_temp_vehicle.color
     * @return the value of tb_temp_vehicle.color
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public String getColor() {
        return color;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_temp_vehicle.color
     * @param color
     *            the value for tb_temp_vehicle.color
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public void setColor(String color) {
        this.color = color == null ? null : color.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_temp_vehicle.license_number
     * @return the value of tb_temp_vehicle.license_number
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public String getLicenseNumber() {
        return licenseNumber;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_temp_vehicle.license_number
     * @param licenseNumber
     *            the value for tb_temp_vehicle.license_number
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber == null ? null : licenseNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_temp_vehicle.registration_number
     * @return the value of tb_temp_vehicle.registration_number
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_temp_vehicle.registration_number
     * @param registrationNumber
     *            the value for tb_temp_vehicle.registration_number
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber == null ? null : registrationNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_temp_vehicle.rownum
     * @return the value of tb_temp_vehicle.rownum
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public String getRownum() {
        return rownum;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_temp_vehicle.rownum
     * @param rownum
     *            the value for tb_temp_vehicle.rownum
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public void setRownum(String rownum) {
        this.rownum = rownum == null ? null : rownum.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_temp_vehicle.comment
     * @return the value of tb_temp_vehicle.comment
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public String getComment() {
        return comment;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_temp_vehicle.comment
     * @param comment
     *            the value for tb_temp_vehicle.comment
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_temp_vehicle.create_user
     * @return the value of tb_temp_vehicle.create_user
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public Integer getCreateUser() {
        return createUser;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_temp_vehicle.create_user
     * @param createUser
     *            the value for tb_temp_vehicle.create_user
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_temp_vehicle.create_time
     * @return the value of tb_temp_vehicle.create_time
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public Date getCreateTime() {
        if (null == createTime) {
            return null;
        }
        return (Date) createTime.clone();
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_temp_vehicle.create_time
     * @param createTime
     *            the value for tb_temp_vehicle.create_time
     * @mbggenerated Fri Sep 29 21:06:31 CST 2017
     */
    public void setCreateTime(Date createTime) {
        if (null == createTime) {
            this.createTime = null;
        } else {
            this.createTime = (Date) createTime.clone();
        }
    }

    /**
     * Getter method for property <tt>location</tt>.
     *
     * @return property value of location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter method for property <tt>location</tt>.
     *
     * @param location value to be assigned to property location
     */
    public void setLocation(String location) {
        this.location = location;
    }
}
