package com.anji.allways.business.entity;

import java.util.Date;

public class DamageEntity {
    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_damage.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_damage.no
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  no;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_damage.vehicle_id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Integer vehicleId;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_damage.register
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  register;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_damage.register_time
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Date    registerTime;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_damage.confirmed_status
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private int     confirmedStatus;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_damage.confirmer
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  confirmer;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_damage.duty_officer
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  dutyOfficer;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_damage.damage_position
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  damagePosition;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_damage.damage_detail
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  damageDetail;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_damage.damage_picture_path
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  damagePicturePath;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_damage.damage_picture_number
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private int     damagePictureNumber;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_damage.reserved
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  reserved;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_damage.creator_name
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private String  creatorName;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_damage.create_time
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private Date    createTime;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to the database column tb_damage.create_time
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    private int     damageType;

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_damage.id
     * @return the value of tb_damage.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_damage.id
     * @param id
     *            the value for tb_damage.id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_damage.no
     * @return the value of tb_damage.no
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getNo() {
        return no;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_damage.no
     * @param no
     *            the value for tb_damage.no
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setNo(String no) {
        this.no = no == null ? null : no.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_damage.vehicle_id
     * @return the value of tb_damage.vehicle_id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Integer getVehicleId() {
        return vehicleId;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_damage.vehicle_id
     * @param vehicleId
     *            the value for tb_damage.vehicle_id
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_damage.register
     * @return the value of tb_damage.register
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getRegister() {
        return register;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_damage.register
     * @param register
     *            the value for tb_damage.register
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setRegister(String register) {
        this.register = register == null ? null : register.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_damage.register_time
     * @return the value of tb_damage.register_time
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public Date getRegisterTime() {
        if (registerTime == null) {
            return null;
        } else {
            return (Date) registerTime.clone();
        }
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_damage.register_time
     * @param registerTime
     *            the value for tb_damage.register_time
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setRegisterTime(Date registerTime) {
        if (registerTime == null) {
            this.registerTime = null;
        } else {
            this.registerTime = (Date) registerTime.clone();
        }
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_damage.confirmed_status
     * @return the value of tb_damage.confirmed_status
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public int getConfirmedStatus() {
        return confirmedStatus;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_damage.confirmed_status
     * @param confirmedStatus
     *            the value for tb_damage.confirmed_status
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setConfirmedStatus(int confirmedStatus) {
        this.confirmedStatus = confirmedStatus;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_damage.confirmer
     * @return the value of tb_damage.confirmer
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getConfirmer() {
        return confirmer;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_damage.confirmer
     * @param confirmer
     *            the value for tb_damage.confirmer
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setConfirmer(String confirmer) {
        this.confirmer = confirmer == null ? null : confirmer.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_damage.duty_officer
     * @return the value of tb_damage.duty_officer
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getDutyOfficer() {
        return dutyOfficer;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_damage.duty_officer
     * @param dutyOfficer
     *            the value for tb_damage.duty_officer
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setDutyOfficer(String dutyOfficer) {
        this.dutyOfficer = dutyOfficer == null ? null : dutyOfficer.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_damage.damage_position
     * @return the value of tb_damage.damage_position
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getDamagePosition() {
        return damagePosition;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_damage.damage_position
     * @param damagePosition
     *            the value for tb_damage.damage_position
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setDamagePosition(String damagePosition) {
        this.damagePosition = damagePosition == null ? null : damagePosition.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_damage.damage_detail
     * @return the value of tb_damage.damage_detail
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getDamageDetail() {
        return damageDetail;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_damage.damage_detail
     * @param damageDetail
     *            the value for tb_damage.damage_detail
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setDamageDetail(String damageDetail) {
        this.damageDetail = damageDetail == null ? null : damageDetail.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_damage.damage_picture_path
     * @return the value of tb_damage.damage_picture_path
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getDamagePicturePath() {
        return damagePicturePath;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_damage.damage_picture_path
     * @param damagePicturePath
     *            the value for tb_damage.damage_picture_path
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setDamagePicturePath(String damagePicturePath) {
        this.damagePicturePath = damagePicturePath == null ? null : damagePicturePath.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_damage.damage_picture_number
     * @return the value of tb_damage.damage_picture_number
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public int getDamagePictureNumber() {
        return damagePictureNumber;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_damage.damage_picture_number
     * @param damagePictureNumber
     *            the value for tb_damage.damage_picture_number
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setDamagePictureNumber(int damagePictureNumber) {
        this.damagePictureNumber = damagePictureNumber;
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_damage.reserved
     * @return the value of tb_damage.reserved
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getReserved() {
        return reserved;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_damage.reserved
     * @param reserved
     *            the value for tb_damage.reserved
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setReserved(String reserved) {
        this.reserved = reserved == null ? null : reserved.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_damage.creator_name
     * @return the value of tb_damage.creator_name
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_damage.creator_name
     * @param creatorName
     *            the value for tb_damage.creator_name
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName == null ? null : creatorName.trim();
    }

    /**
     * This method was generated by MyBatis Generator. This method returns the value of the database column tb_damage.create_time
     * @return the value of tb_damage.create_time
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
     * This method was generated by MyBatis Generator. This method sets the value of the database column tb_damage.create_time
     * @param createTime
     *            the value for tb_damage.create_time
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
     * Getter method for property <tt>damageType</tt>.
     * @return property value of damageType
     */
    public int getDamageType() {
        return damageType;
    }

    /**
     * Setter method for property <tt>damageType</tt>.
     * @param damageType
     *            value to be assigned to property damageType
     */
    public void setDamageType(int damageType) {
        this.damageType = damageType;
    }
}
