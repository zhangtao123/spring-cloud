package com.anji.allways.business.vo;

public class HomePageVO {
    // 待调度
    private int dispatch;

    // 待出库
    private int outbound;

    // 待调度
    private int pickUp;

    // 在库车辆
    private int inbound;

    /**
     * @return the dispatch
     */
    public int getDispatch() {
        return dispatch;
    }

    /**
     * @param dispatch
     *            the dispatch to set
     */
    public void setDispatch(int dispatch) {
        this.dispatch = dispatch;
    }

    /**
     * @return the outbound
     */
    public int getOutbound() {
        return outbound;
    }

    /**
     * @param outbound
     *            the outbound to set
     */
    public void setOutbound(int outbound) {
        this.outbound = outbound;
    }

    /**
     * @return the pickUp
     */
    public int getPickUp() {
        return pickUp;
    }

    /**
     * @param pickUp
     *            the pickUp to set
     */
    public void setPickUp(int pickUp) {
        this.pickUp = pickUp;
    }

    /**
     * @return the inbound
     */
    public int getInbound() {
        return inbound;
    }

    /**
     * @param inbound
     *            the inbound to set
     */
    public void setInbound(int inbound) {
        this.inbound = inbound;
    }

}
