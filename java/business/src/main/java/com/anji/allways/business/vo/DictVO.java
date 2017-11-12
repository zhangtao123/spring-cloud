/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.vo;

import java.io.Serializable;

public class DictVO implements Serializable {

    private static final long serialVersionUID = -5319145400965692084L;
    private String            code;
    private String            name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
