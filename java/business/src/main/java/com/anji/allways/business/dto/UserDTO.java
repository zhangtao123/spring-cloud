/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.dto;

import com.alibaba.fastjson.JSONObject;
import com.anji.allways.business.entity.UserEntity;
import com.anji.allways.common.util.BeanUtil;

/**
 * <pre>
 *
 * </pre>
 * @author wangyanjun
 * @version $Id: UserDTO.java, v 0.1 2017年8月22日 下午2:00:24 wangyanjun Exp $
 */
public class UserDTO extends UserEntity {
    private JSONObject permissionData;

    public UserDTO(JSONObject permissionData) {
        BeanUtil.copyBeanProperties(permissionData, this);
        this.permissionData = permissionData;
    }

    public JSONObject getPermissionData() {
        return permissionData;
    }

    public void setPermissionData(JSONObject permissionData) {
        this.permissionData = permissionData;
    }
}
