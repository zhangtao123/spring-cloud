/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.hystrix;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anji.allways.business.feign.DictServiceClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * <pre>
 *
 * </pre>
 * @author zhanglele
 * @version $Id: HystrixWrappedRowDictServiceClient.java, v 0.1 2017年7月20日 上午8:53:51 zhanglele Exp $
 */
@Service
public class HystrixWrappedRowDictServiceClient implements DictServiceClient {
    @Autowired
    private DictServiceClient dictServiceClient;

    /**
     * @param typeCode
     * @return
     * @see com.anji.allways.business.row.feign.DictServiceClient#queryAllByCodeInner(java.lang.String)
     */
    @Override
    @HystrixCommand(groupKey = "dictGroup", fallbackMethod = "queryAllByCodeInnerFallBackCall")
    public Map<String, String> queryAllByCodeInner(String typeCode) {
        return dictServiceClient.queryAllByCodeInner(typeCode);
    }

    public Map<String, String> queryAllByCodeInnerFallBackCall(String typeCode) {
        return new HashMap<String, String>();
    }
}
