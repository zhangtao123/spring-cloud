package com.anji.allways.business.vo;

import java.util.List;

public class DeliveryPlanChildVO {

    /**
     * 子出库计划单ID
     */
    private Integer         id;

    /**
     * 子出库计划单号
     */
    private String          no;

    /**
     * 子出库计划单状态(2:待自提 3:待出库 4:待收货 5:已完成 6:已取消)
     */
    private int             status;

    /**
     * 行程状态(0:默认 1:出库交接 2:确认送达 3:身份确认 4:收车交接)
     */
    private String          routingStatus;

    /**
     * 客户
     */
    private String          customer;

    /**
     * 收货人
     */
    private String          consigneeName;

    /**
     * 收货人地址
     */
    private String          consigneeAddress;

    /**
     * 收货人电话
     */
    private String          consigneeMobile;

    /**
     * 送达车辆数
     */
    private Integer         carNum;

    /**
     * 收货人用户ID
     */
    private Integer         consigneeUserId;

    /**
     * 车辆信息列表
     */
    private List<VehicleVO> carList;

    /**
     * Getter method for property <tt>id</tt>.
     * @return property value of id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Setter method for property <tt>id</tt>.
     * @param id
     *            value to be assigned to property id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter method for property <tt>no</tt>.
     * @return property value of no
     */
    public String getNo() {
        return no;
    }

    /**
     * Setter method for property <tt>no</tt>.
     * @param no
     *            value to be assigned to property no
     */
    public void setNo(String no) {
        this.no = no;
    }

    /**
     * Getter method for property <tt>status</tt>.
     * @return property value of status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Setter method for property <tt>status</tt>.
     * @param status
     *            value to be assigned to property status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Getter method for property <tt>routingStatus</tt>.
     * @return property value of routingStatus
     */
    public String getRoutingStatus() {
        return routingStatus;
    }

    /**
     * Setter method for property <tt>routingStatus</tt>.
     * @param routingStatus
     *            value to be assigned to property routingStatus
     */
    public void setRoutingStatus(String routingStatus) {
        this.routingStatus = routingStatus;
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
     * Getter method for property <tt>consigneeName</tt>.
     * @return property value of consigneeName
     */
    public String getConsigneeName() {
        return consigneeName;
    }

    /**
     * Setter method for property <tt>consigneeName</tt>.
     * @param consigneeName
     *            value to be assigned to property consigneeName
     */
    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    /**
     * Getter method for property <tt>consigneeAddress</tt>.
     * @return property value of consigneeAddress
     */
    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    /**
     * Setter method for property <tt>consigneeAddress</tt>.
     * @param consigneeAddress
     *            value to be assigned to property consigneeAddress
     */
    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    /**
     * Getter method for property <tt>consigneeMobile</tt>.
     * @return property value of consigneeMobile
     */
    public String getConsigneeMobile() {
        return consigneeMobile;
    }

    /**
     * Setter method for property <tt>consigneeMobile</tt>.
     * @param consigneeMobile
     *            value to be assigned to property consigneeMobile
     */
    public void setConsigneeMobile(String consigneeMobile) {
        this.consigneeMobile = consigneeMobile;
    }

    /**
     * Getter method for property <tt>carList</tt>.
     * @return property value of carList
     */
    public List<VehicleVO> getCarList() {
        return carList;
    }

    /**
     * Setter method for property <tt>carList</tt>.
     * @param carList
     *            value to be assigned to property carList
     */
    public void setCarList(List<VehicleVO> carList) {
        this.carList = carList;
    }

    /**
     * Getter method for property <tt>carNum</tt>.
     * @return property value of carNum
     */
    public Integer getCarNum() {
        return carNum;
    }

    /**
     * Setter method for property <tt>carNum</tt>.
     * @param carNum
     *            value to be assigned to property carNum
     */
    public void setCarNum(Integer carNum) {
        this.carNum = carNum;
    }

    /**
     * Getter method for property <tt>consigneeUserId</tt>.
     * @return property value of consigneeUserId
     */
    public Integer getConsigneeUserId() {
        return consigneeUserId;
    }

    /**
     * Setter method for property <tt>consigneeUserId</tt>.
     * @param consigneeUserId
     *            value to be assigned to property consigneeUserId
     */
    public void setConsigneeUserId(Integer consigneeUserId) {
        this.consigneeUserId = consigneeUserId;
    }

}
