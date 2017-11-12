/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

/**
 * <pre>
 *
 * </pre>
 * @author wangyanjun
 * @version $Id: TokenService.java, v 0.1 2017年9月6日 下午2:54:40 wangyanjun Exp $
 */
public interface TokenService {

    String generateToken();

    String getUserId(String token);

    void putToken(String token, String userId);

    void removeToken(String token);
}
