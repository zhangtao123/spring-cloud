package com.anji.allways.business.vo;

public class StateVO {

    /**
     * 是否展开
     */
    private boolean opened;

    /**
     * 是否可用
     */
    private boolean disabled;

    /**
     * 是否选中
     */
    private boolean selected;

    /**
     * Getter method for property <tt>opened</tt>.
     * @return property value of opened
     */
    public boolean isOpened() {
        return opened;
    }

    /**
     * Setter method for property <tt>opened</tt>.
     * @param opened
     *            value to be assigned to property opened
     */
    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    /**
     * Getter method for property <tt>disabled</tt>.
     * @return property value of disabled
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * Setter method for property <tt>disabled</tt>.
     * @param disabled
     *            value to be assigned to property disabled
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * Getter method for property <tt>selected</tt>.
     * @return property value of selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Setter method for property <tt>selected</tt>.
     * @param selected
     *            value to be assigned to property selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
