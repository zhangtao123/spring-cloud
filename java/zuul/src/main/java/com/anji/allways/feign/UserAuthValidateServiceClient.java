package com.anji.allways.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <pre>
 * 调用slns_auth模块验证权限
 * </pre>
 * @author WangGuangYuan
 * @version $Id: UserAuthValidateServiceClient.java, v 0.1 2017年5月23日 上午10:06:56 Administrator Exp $
 */
@FeignClient(name = "slns-auth")
public interface UserAuthValidateServiceClient {
    @RequestMapping(value = "/auth/validateByUrl")
    boolean validateByUrl(@RequestParam("userId") String userId, @RequestParam("url") String authUrl);
}
