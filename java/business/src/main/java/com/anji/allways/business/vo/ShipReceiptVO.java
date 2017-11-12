package com.anji.allways.business.vo;

import java.util.Date;

public class ShipReceiptVO {

    private Integer id;
    // 质损单编号
    private String  deliveryPlanNo;
    // 机计划出库时间
    private Date    departTime;
    private Integer truckDriverId;
    private String  customer;
    private String  receivingAddress;
    private Integer vehicleId;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the deliveryPlanNo
     */
    public String getDeliveryPlanNo() {
        return deliveryPlanNo;
    }

    /**
     * @param deliveryPlanNo
     *            the deliveryPlanNo to set
     */
    public void setDeliveryPlanNo(String deliveryPlanNo) {
        this.deliveryPlanNo = deliveryPlanNo;
    }

    /**
     * @return the departTime
     */
    public Date getDepartTime() {
        if (null == departTime) {
            return null;
        }
        return (Date) departTime.clone();
    }

    /**
     * @param departTime
     *            the departTime to set
     */
    public void setDepartTime(Date departTime) {
        if (null == departTime) {
            this.departTime = null;
        } else {
            this.departTime = (Date) departTime.clone();
        }
    }

    /**
     * @return the truckDriverId
     */
    public Integer getTruckDriverId() {
        return truckDriverId;
    }

    /**
     * @param truckDriverId
     *            the truckDriverId to set
     */
    public void setTruckDriverId(Integer truckDriverId) {
        this.truckDriverId = truckDriverId;
    }

    /**
     * @return the customer
     */
    public String getCustomer() {
        return customer;
    }

    /**
     * @param customer
     *            the customer to set
     */
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    /**
     * @return the receivingAddress
     */
    public String getReceivingAddress() {
        return receivingAddress;
    }

    /**
     * @param receivingAddress
     *            the receivingAddress to set
     */
    public void setReceivingAddress(String receivingAddress) {
        this.receivingAddress = receivingAddress;
    }

    /**
     * @return the vehicleId
     */
    public Integer getVehicleId() {
        return vehicleId;
    }

    /**
     * @param vehicleId
     *            the vehicleId to set
     */
    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

}
