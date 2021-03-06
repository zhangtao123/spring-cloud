package com.anji.allways.business.entity;

public class StorageVehicleEntity {
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.vin
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  vin;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.brand
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  brand;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.series
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  series;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.model
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  model;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.color
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  color;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.location
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  location;

    private String  vehicleComment;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.license_number
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  licenseNumber;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.registration_number
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  registrationNumber;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.vehicle_type
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private int     vehicleType;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.storage_plan_id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer storagePlanId;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.delivery_plan_no
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  deliveryPlanNo;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_storage_vehicle.reserved
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  reserved;

    /**
     * 是否已加入计划单 0：未知 1：是 2：否
     */
    private Integer isStorage;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_storage_vehicle.id
     * @return the value of tb_storage_vehicle.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_storage_vehicle.id
     * @param id
     *            the value for tb_storage_vehicle.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getVehicleComment() {
        return vehicleComment;
    }

    public void setVehicleComment(String vehicleComment) {
        this.vehicleComment = vehicleComment == null ? null : vehicleComment.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_storage_vehicle.license_number
     * @return the value of tb_storage_vehicle.license_number
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getLicenseNumber() {
        return licenseNumber;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_storage_vehicle.license_number
     * @param licenseNumber
     *            the value for tb_storage_vehicle.license_number
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber == null ? null : licenseNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_storage_vehicle.registration_number
     * @return the value of tb_storage_vehicle.registration_number
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_storage_vehicle.registration_number
     * @param registrationNumber
     *            the value for tb_storage_vehicle.registration_number
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber == null ? null : registrationNumber.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_storage_vehicle.vehicle_type
     * @return the value of tb_storage_vehicle.vehicle_type
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public int getVehicleType() {
        return vehicleType;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_storage_vehicle.vehicle_type
     * @param vehicleType
     *            the value for tb_storage_vehicle.vehicle_type
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setVehicleType(int vehicleType) {
        this.vehicleType = vehicleType;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_storage_vehicle.storage_plan_id
     * @return the value of tb_storage_vehicle.storage_plan_id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getStoragePlanId() {
        return storagePlanId;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_storage_vehicle.storage_plan_id
     * @param storagePlanId
     *            the value for tb_storage_vehicle.storage_plan_id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setStoragePlanId(Integer storagePlanId) {
        this.storagePlanId = storagePlanId;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_storage_vehicle.delivery_plan_no
     * @return the value of tb_storage_vehicle.delivery_plan_no
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getDeliveryPlanNo() {
        return deliveryPlanNo;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_storage_vehicle.delivery_plan_no
     * @param deliveryPlanNo
     *            the value for tb_storage_vehicle.delivery_plan_no
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setDeliveryPlanNo(String deliveryPlanNo) {
        this.deliveryPlanNo = deliveryPlanNo == null ? null : deliveryPlanNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_storage_vehicle.reserved
     * @return the value of tb_storage_vehicle.reserved
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getReserved() {
        return reserved;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_storage_vehicle.reserved
     * @param reserved
     *            the value for tb_storage_vehicle.reserved
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setReserved(String reserved) {
        this.reserved = reserved == null ? null : reserved.trim();
    }

    /**
     * Getter method for property <tt>isStorage</tt>.
     * @return property value of isStorage
     */
    public Integer getIsStorage() {
        return isStorage;
    }

    /**
     * Setter method for property <tt>isStorage</tt>.
     * @param isStorage
     *            value to be assigned to property isStorage
     */
    public void setIsStorage(Integer isStorage) {
        this.isStorage = isStorage;
    }
}
