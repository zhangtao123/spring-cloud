package com.anji.allways.business.vo;

import java.util.Date;
import java.util.List;

public class DestinationVO {
    private int     id;
    private String  destination;
    private String  carSum;
    private String  name;
    private String  mobile;
    private Date    deliveryTime;
    private int     keyIsTaken;
    private String  despatchConfirmPicturePath;
    private String  acceptComment;
    private Date    receiveTime;
    private String  shipNo;
    private Integer isPrinted;

    /**
     * @return the isPrinted
     */
    public Integer getIsPrinted() {
        return isPrinted;
    }

    /**
     * @param isPrinted
     *            the isPrinted to set
     */
    public void setIsPrinted(Integer isPrinted) {
        this.isPrinted = isPrinted;
    }

    /**
     * 运单表列表
     */
    private List<OrderVO> orderList;

    /**
     * 发运交接现场照片路径
     */
    private List<String>  despatchConfirmPicturePathList;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the shipNo
     */
    public String getShipNo() {
        return shipNo;
    }

    /**
     * @param shipNo
     *            the shipNo to set
     */
    public void setShipNo(String shipNo) {
        this.shipNo = shipNo;
    }

    /**
     * @return the despatchConfirmPicturePathList
     */
    public List<String> getDespatchConfirmPicturePathList() {
        return despatchConfirmPicturePathList;
    }

    /**
     * @param despatchConfirmPicturePathList
     *            the despatchConfirmPicturePathList to set
     */
    public void setDespatchConfirmPicturePathList(List<String> despatchConfirmPicturePathList) {
        this.despatchConfirmPicturePathList = despatchConfirmPicturePathList;
    }

    /**
     * @return the despatchConfirmPicturePath
     */
    public String getDespatchConfirmPicturePath() {
        return despatchConfirmPicturePath;
    }

    /**
     * @param despatchConfirmPicturePath
     *            the despatchConfirmPicturePath to set
     */
    public void setDespatchConfirmPicturePath(String despatchConfirmPicturePath) {
        this.despatchConfirmPicturePath = despatchConfirmPicturePath;
    }

    /**
     * @return the acceptComment
     */
    public String getAcceptComment() {
        return acceptComment;
    }

    /**
     * @param acceptComment
     *            the acceptComment to set
     */
    public void setAcceptComment(String acceptComment) {
        this.acceptComment = acceptComment;
    }

    /**
     * @return the deliveryTime
     */
    public Date getDeliveryTime() {
        if (null == deliveryTime) {
            return null;
        }
        return (Date) deliveryTime.clone();
    }

    /**
     * @param deliveryTime
     *            the deliveryTime to set
     */
    public void setDeliveryTime(Date deliveryTime) {
        if (null == deliveryTime) {
            this.deliveryTime = null;
        } else {
            this.deliveryTime = (Date) deliveryTime.clone();
        }
    }

    /**
     * @return the receiveTime
     */
    public Date getReceiveTime() {
        if (null == receiveTime) {
            return null;
        }
        return (Date) receiveTime.clone();
    }

    /**
     * @param receiveTime
     *            the receiveTime to set
     */
    public void setReceiveTime(Date receiveTime) {
        if (null == receiveTime) {
            this.receiveTime = null;
        } else {
            this.receiveTime = (Date) receiveTime.clone();
        }
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination
     *            the destination to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @return the carSum
     */
    public String getCarSum() {
        return carSum;
    }

    /**
     * @param carSum
     *            the carSum to set
     */
    public void setCarSum(String carSum) {
        this.carSum = carSum;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the keyIsTaken
     */
    public int getKeyIsTaken() {
        return keyIsTaken;
    }

    /**
     * @param keyIsTaken
     *            the keyIsTaken to set
     */
    public void setKeyIsTaken(int keyIsTaken) {
        this.keyIsTaken = keyIsTaken;
    }

    /**
     * @return the orderList
     */
    public List<OrderVO> getOrderList() {
        return orderList;
    }

    /**
     * @param orderList
     *            the orderList to set
     */
    public void setOrderList(List<OrderVO> orderList) {
        this.orderList = orderList;
    }

}
