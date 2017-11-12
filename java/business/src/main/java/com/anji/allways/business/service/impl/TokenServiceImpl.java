/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.anji.allways.base.redis.RedisUtils;
import com.anji.allways.business.service.TokenService;

/**
 * <pre>
 *
 * </pre>
 * @author wangyanjun
 * @version $Id: TokenServiceImpl.java, v 0.1 2017年9月6日 下午3:09:32 wangyanjun Exp $
 */
@Service
public class TokenServiceImpl implements TokenService {

    // private static Map<String, UserEntity> tokenMap = new HashMap<>();

    @Autowired
    private RedisUtils redis;

    /**
     * @return
     * @see com.anji.allways.business.service.TokenService#genSessionId()
     */
    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * @param token
     * @param userId
     * @see com.anji.allways.business.service.TokenService#putSession(java.lang.String, com.anji.allways.business.entity.UserEntity)
     */
    @Override
    public synchronized void putToken(String token, String userId) {
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(userId)) {
            return;
        }
        redis.set(token, userId, (long) 30 * 60);
    }

    /**
     * @param token
     * @return
     * @see com.anji.allways.business.service.TokenService#getToken(java.lang.Integer)
     */
    @Override
    public String getUserId(String token) {
        if (null == token || !redis.exists(token)) {
            return null;
        }
        return redis.get(token);
    }

    /**
     * @param userId
     * @see com.anji.allways.business.service.TokenService#removeToken(java.lang.String)
     */
    @Override
    public synchronized void removeToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return;
        }
        redis.del(token);
    }

}
