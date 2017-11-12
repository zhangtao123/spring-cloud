/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.anji.allways.business.dto.UserDTO;
import com.anji.allways.business.entity.UserEntity;

/**
 * @author wangyanjun
 * @version $Id: UserCenter.java, v 0.1 2017年8月22日 下午1:39:12 wangyanjun Exp $
 */
public final class UserCenter {
    private static Map<String, UserDTO> sessionMap = new HashMap<>();
    private static UserCenter           instance   = null;

    private UserCenter() {
    }

    public static synchronized UserCenter getInstance() {
        if (instance == null) {
            instance = new UserCenter();
        }
        return instance;
    }

    public synchronized void putUserDTO(String session, UserDTO user) {
        if (StringUtils.isEmpty(session) || null == user) {
            return;
        }
        sessionMap.put(session, user);
    }

    public UserEntity getUserDTOBySession(String session) {
        if (StringUtils.isEmpty(session)) {
            return null;
        }
        return sessionMap.get(session);
    }

    public synchronized void removeUserDTO(String session) {
        if (StringUtils.isEmpty(session)) {
            return;
        }
        sessionMap.remove(session);
    }
}
