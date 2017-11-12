/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <pre>
 * 调用用户模块验证token服务
 * </pre>
 * @author WangGuangYuan
 * @version $Id: UserTokenValidateServiceClient.java, v 0.1 2017年5月18日 上午8:58:12 Administrator Exp $
 */

@FeignClient(name = "slns-business")
public interface UserTokenValidateServiceClient {
    @RequestMapping(value = "/user/login/token/validate")
    boolean validate(@RequestParam("token") String token);
}
