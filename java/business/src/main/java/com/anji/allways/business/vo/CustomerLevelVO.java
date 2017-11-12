package com.anji.allways.business.vo;

import java.util.List;
import java.util.Map;

public class CustomerLevelVO {

    /**
     * 客户ID
     */
    private String                 id;

    /**
     * 客户名称
     */
    private String                 text;

    /**
     * 客户名简称
     */
    private String                 shortName;

    /**
     * 上级客户ID
     */
    private String                 parentId;

    /**
     * 递归查询（子客户）
     */
    private List<CustomerLevelVO> children;

    /**
     * 状态VO
     */
    private StateVO                state;

    /**
     * 父客户ID保持
     */
    private Map<String, String>    attributes;

    /**
     * 是否退出递归
     */
    private String                 type;

    /**
     * Getter method for property <tt>code</tt>.
     * @return property value of code
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method for property <tt>code</tt>.
     * @param code
     *            value to be assigned to property code
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter method for property <tt>name</tt>.
     * @return property value of name
     */
    public String getText() {
        return text;
    }

    /**
     * Setter method for property <tt>name</tt>.
     * @param name
     *            value to be assigned to property name
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Getter method for property <tt>shortName</tt>.
     * @return property value of shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Setter method for property <tt>shortName</tt>.
     * @param shortName
     *            value to be assigned to property shortName
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * Getter method for property <tt>childList</tt>.
     * @return property value of childList
     */
    public List<CustomerLevelVO> getChildren() {
        return children;
    }

    /**
     * Setter method for property <tt>childList</tt>.
     * @param childList
     *            value to be assigned to property childList
     */
    public void setChildren(List<CustomerLevelVO> children) {
        this.children = children;
    }

    /**
     * Getter method for property <tt>parentId</tt>.
     * @return property value of parentId
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * Setter method for property <tt>parentId</tt>.
     * @param parentId
     *            value to be assigned to property parentId
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * Getter method for property <tt>state</tt>.
     * @return property value of state
     */
    public StateVO getState() {
        return state;
    }

    /**
     * Setter method for property <tt>state</tt>.
     * @param state
     *            value to be assigned to property state
     */
    public void setState(StateVO state) {
        this.state = state;
    }

    /**
     * Getter method for property <tt>attributes</tt>.
     * @return property value of attributes
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Setter method for property <tt>attributes</tt>.
     * @param attributes
     *            value to be assigned to property attributes
     */
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    /**
     * Getter method for property <tt>type</tt>.
     * @return property value of type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter method for property <tt>type</tt>.
     * @param type
     *            value to be assigned to property type
     */
    public void setType(String type) {
        this.type = type;
    }

}
