/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.hystrix;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anji.allways.feign.UserAuthValidateServiceClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * <pre>
 * 熔断处理权限校验接口实现
 * </pre>
 * @author WangGuangYuan
 * @version $Id: HystrixWrappedUserTokenValidateServiceClient.java, v 0.1 2017年5月18日 上午8:59:29 Administrator Exp $
 */
@Service
public class HystrixWrappedUserAuthValidateServiceClient implements UserAuthValidateServiceClient {
    private Logger                        logger = LogManager.getLogger(HystrixWrappedUserAuthValidateServiceClient.class);

    @Autowired
    private UserAuthValidateServiceClient userAuthValidateServiceClient;

    @Override
    @HystrixCommand(groupKey = "authGroup", fallbackMethod = "fallBackCall")
    public boolean validateByUrl(String userId, String authUrl) {
        boolean isValid = userAuthValidateServiceClient.validateByUrl(userId, authUrl);
        logger.info("调用验证权限返回结果：" + isValid);
        return isValid;
    }

    public boolean fallBackCall(String userId, String authUrl) {
        logger.info("调用权限验证失败");
        return false;
    }

}
