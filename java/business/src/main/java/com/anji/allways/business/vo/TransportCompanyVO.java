package com.anji.allways.business.vo;

import java.io.Serializable;

import com.anji.allways.business.entity.TransportCompanyEntity;

public class TransportCompanyVO extends TransportCompanyEntity implements Serializable {

    /**
     * <pre>
     * 页面回显vo
     * </pre>
     */
    private static final long serialVersionUID = -4320492336976846661L;

    /**
     * 运输公司状态类型名称
     */
    private String            statusName;

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
