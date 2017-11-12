/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anji.allways.feign.UserTokenValidateServiceClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * <pre>
 * 熔断处理token校验接口实现
 * </pre>
 * @author WangGuangYuan
 * @version $Id: HystrixWrappedUserTokenValidateServiceClient.java, v 0.1 2017年5月18日 上午8:59:29 Administrator Exp $
 */
@Service
public class HystrixWrappedUserTokenValidateServiceClient implements UserTokenValidateServiceClient {
    @Autowired
    private UserTokenValidateServiceClient userTokenValidateServiceClient;

    /**
     * @param token
     * @return
     * @see com.anji.allways.feign.UserTokenValidateServiceClient#validate(java.lang.String)
     */
    @Override
    @HystrixCommand(groupKey = "userGroup", fallbackMethod = "fallBackCall")
    public boolean validate(String token) {
        return userTokenValidateServiceClient.validate(token);
    }

    public boolean fallBackCall(String token) {
        return false;
    }
}
