/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.feign;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <pre>
 *
 * </pre>
 * @author zhanglele
 * @version $Id: DictServiceClient.java, v 0.1 2017年7月20日 上午8:52:44 zhanglele Exp $
 */
@FeignClient(name = "slns-auth")
public interface DictServiceClient {

    @RequestMapping("/dict/inner/query/list")
    Map<String, String> queryAllByCodeInner(@RequestParam("typeCode") String typeCode);
}
