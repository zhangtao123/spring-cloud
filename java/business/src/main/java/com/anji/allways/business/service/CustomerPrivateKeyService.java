/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import com.anji.allways.business.entity.CustomerPrivateKeyEntity;

/**
 * <pre>
 *
 * </pre>
 * @author wangyanjun
 * @version $Id: CustomerPrivateKeyService.java, v 0.1 2017年8月31日 下午1:24:52 wangyanjun Exp $
 */
public interface CustomerPrivateKeyService {

    CustomerPrivateKeyEntity queryKeyById(String id);

    Integer addKey(String customerId);

    Integer deleteKey(String customerId);

}
