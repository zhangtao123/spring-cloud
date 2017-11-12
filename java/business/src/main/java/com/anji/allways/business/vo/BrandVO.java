/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.vo;

import java.util.List;

/**
 * <pre>
 *
 * </pre>
 * @author 李军
 * @version $Id: BrandVo.java, v 0.1 2017年9月2日 下午3:39:32 李军 Exp $
 */
public class BrandVO {
    private String        id;

    private String        url;

    private String        name;

    private List<BrandVO> list;

    /**
     * Getter method for property <tt>id</tt>.
     * @return property value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method for property <tt>id</tt>.
     * @param id
     *            value to be assigned to property id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter method for property <tt>url</tt>.
     * @return property value of url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter method for property <tt>url</tt>.
     * @param url
     *            value to be assigned to property url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Getter method for property <tt>name</tt>.
     * @return property value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for property <tt>name</tt>.
     * @param name
     *            value to be assigned to property name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for property <tt>list</tt>.
     * @return property value of list
     */
    public List<BrandVO> getList() {
        return list;
    }

    /**
     * Setter method for property <tt>list</tt>.
     * @param list
     *            value to be assigned to property list
     */
    public void setList(List<BrandVO> list) {
        this.list = list;
    }
}
