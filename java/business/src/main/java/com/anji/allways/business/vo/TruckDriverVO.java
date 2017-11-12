package com.anji.allways.business.vo;

import com.anji.allways.business.entity.TruckDriverEntity;

public class TruckDriverVO extends TruckDriverEntity {

    /**
     * 司机状态名称
     */
    private String statusName;

    /**
     * Getter method for property <tt>statusName</tt>.
     * @return property value of statusName
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * Setter method for property <tt>statusName</tt>.
     * @param statusName
     *            value to be assigned to property statusName
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
