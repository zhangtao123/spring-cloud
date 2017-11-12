package com.anji.allways.business.dto;

import java.util.List;

import com.anji.allways.business.entity.DeliveryPlanEntity;

public class DeliveryPlanDTO extends DeliveryPlanEntity {
    private String        createStartTime;

    private String        createEndTime;

    private String        deliveryTimeStart;

    private String        deliveryTimeEnd;

    private String        pickupPlanTimeStart;

    private String        pickupPlanTimeEnd;

    private String        pickupPlanTimeBak;

    private String        createTimeShow;

    private String        carsNum;

    private String        truckNumber;

    private String        statusName;

    private String        doType;

    private String        mobile;

    private String        outNum;

    /**
     * 所有层级ID
     */
    private List<Integer> idList;

    /**
     * @return the createStartTime
     */
    public String getCreateStartTime() {
        return createStartTime;
    }

    /**
     * @return the idList
     */
    public List<Integer> getIdList() {
        return idList;
    }

    /**
     * @param idList
     *            the idList to set
     */
    public void setIdList(List<Integer> idList) {
        this.idList = idList;
    }

    /**
     * @param createStartTime
     *            the createStartTime to set
     */
    public void setCreateStartTime(String createStartTime) {
        this.createStartTime = createStartTime;
    }

    /**
     * @return the createEndTime
     */
    public String getCreateEndTime() {
        return createEndTime;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     *            the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @param createEndTime
     *            the createEndTime to set
     */
    public void setCreateEndTime(String createEndTime) {
        this.createEndTime = createEndTime;
    }

    /**
     * @return the deliveryTimeStart
     */
    public String getDeliveryTimeStart() {
        return deliveryTimeStart;
    }

    /**
     * @param deliveryTimeStart
     *            the deliveryTimeStart to set
     */
    public void setDeliveryTimeStart(String deliveryTimeStart) {
        this.deliveryTimeStart = deliveryTimeStart;
    }

    /**
     * @return the deliveryTimeEnd
     */
    public String getDeliveryTimeEnd() {
        return deliveryTimeEnd;
    }

    /**
     * @param deliveryTimeEnd
     *            the deliveryTimeEnd to set
     */
    public void setDeliveryTimeEnd(String deliveryTimeEnd) {
        this.deliveryTimeEnd = deliveryTimeEnd;
    }

    /**
     * @return the pickupPlanTimeStart
     */
    public String getPickupPlanTimeStart() {
        return pickupPlanTimeStart;
    }

    /**
     * @param pickupPlanTimeStart
     *            the pickupPlanTimeStart to set
     */
    public void setPickupPlanTimeStart(String pickupPlanTimeStart) {
        this.pickupPlanTimeStart = pickupPlanTimeStart;
    }

    /**
     * @return the pickupPlanTimeEnd
     */
    public String getPickupPlanTimeEnd() {
        return pickupPlanTimeEnd;
    }

    /**
     * @param pickupPlanTimeEnd
     *            the pickupPlanTimeEnd to set
     */
    public void setPickupPlanTimeEnd(String pickupPlanTimeEnd) {
        this.pickupPlanTimeEnd = pickupPlanTimeEnd;
    }

    /**
     * @return the pickupPlanTimeBak
     */
    public String getPickupPlanTimeBak() {
        return pickupPlanTimeBak;
    }

    /**
     * @param pickupPlanTimeBak
     *            the pickupPlanTimeBak to set
     */
    public void setPickupPlanTimeBak(String pickupPlanTimeBak) {
        this.pickupPlanTimeBak = pickupPlanTimeBak;
    }

    /**
     * @return the createTimeShow
     */
    public String getCreateTimeShow() {
        return createTimeShow;
    }

    /**
     * @param createTimeShow
     *            the createTimeShow to set
     */
    public void setCreateTimeShow(String createTimeShow) {
        this.createTimeShow = createTimeShow;
    }

    /**
     * @return the carsNum
     */
    public String getCarsNum() {
        return carsNum;
    }

    /**
     * @param carsNum
     *            the carsNum to set
     */
    public void setCarsNum(String carsNum) {
        this.carsNum = carsNum;
    }

    /**
     * @return the truckNumber
     */
    public String getTruckNumber() {
        return truckNumber;
    }

    /**
     * @param truckNumber
     *            the truckNumber to set
     */
    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    /**
     * @return the statusName
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * @param statusName
     *            the statusName to set
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * @return the doType
     */
    public String getDoType() {
        return doType;
    }

    /**
     * @param doType
     *            the doType to set
     */
    public void setDoType(String doType) {
        this.doType = doType;
    }

    /**
     * Getter method for property <tt>outNum</tt>.
     * @return property value of outNum
     */
    public String getOutNum() {
        return outNum;
    }

    /**
     * Setter method for property <tt>outNum</tt>.
     * @param outNum
     *            value to be assigned to property outNum
     */
    public void setOutNum(String outNum) {
        this.outNum = outNum;
    }

}
