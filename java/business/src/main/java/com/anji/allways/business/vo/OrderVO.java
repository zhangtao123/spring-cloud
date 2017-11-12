package com.anji.allways.business.vo;

import com.anji.allways.business.entity.OrderEntity;

public class OrderVO extends OrderEntity {
    private int    orderNum;

    // 收货人名称
    // private String consigneeName;

    // 收货人电话
    private String consigneeMobile;

    // 自提时间
    private String pickupSelfTimeS;

    /**
     * @return the pickupSelfTimeS
     */
    public String getPickupSelfTimeS() {
        return pickupSelfTimeS;
    }

    /**
     * @param pickupSelfTimeS
     *            the pickupSelfTimeS to set
     */
    public void setPickupSelfTimeS(String pickupSelfTimeS) {
        this.pickupSelfTimeS = pickupSelfTimeS;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * @return the consigneeMobile
     */
    public String getConsigneeMobile() {
        return consigneeMobile;
    }

    /**
     * @param consigneeMobile
     *            the consigneeMobile to set
     */
    public void setConsigneeMobile(String consigneeMobile) {
        this.consigneeMobile = consigneeMobile;
    }
}
