/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anji.allways.business.common.constant.SlnsConstants;
import com.anji.allways.business.entity.CustomerPrivateKeyEntity;
import com.anji.allways.business.mapper.CustomerPrivateKeyEntityMapper;
import com.anji.allways.business.service.CustomerPrivateKeyService;
import com.anji.allways.common.util.MD5Util;

/**
 * <pre>
 * </pre>
 * @author wangyanjun
 * @version $Id: CustomerPrivateKeyServiceImpl.java, v 0.1 2017年8月31日 下午1:27:01 wangyanjun Exp $
 */
@Service
@Transactional
public class CustomerPrivateKeyServiceImpl implements CustomerPrivateKeyService {

    @Autowired
    private CustomerPrivateKeyEntityMapper customerPrivateKeyEntityMapper;

    /**
     * @param id
     * @return
     * @see com.anji.allways.business.service.CustomerPrivateKeyService#queryKeyById(java.lang.String)
     */
    @Override
    public CustomerPrivateKeyEntity queryKeyById(String customerId) {
        String encryptId = MD5Util.encrypt(SlnsConstants.CUSTOMER_PERFIX + customerId);
        return customerPrivateKeyEntityMapper.selectByCustomerId(encryptId);
    }

    /**
     * @param customerId
     * @return
     * @see com.anji.allways.business.service.CustomerPrivateKeyService#addKey(java.lang.String)
     */
    @Override
    public Integer addKey(String customerId) {
        String encryptId = MD5Util.encrypt(SlnsConstants.CUSTOMER_PERFIX + customerId);
        String key = MD5Util.encrypt(SlnsConstants.CUSTOMER_KEY_SALT + customerId);
        CustomerPrivateKeyEntity customerPrivateKeyEntity = new CustomerPrivateKeyEntity();
        customerPrivateKeyEntity.setCustomerId(encryptId);
        customerPrivateKeyEntity.setPrivateKey(key);
        customerPrivateKeyEntity.setCreatorName("");
        customerPrivateKeyEntity.setCreateTime(new Date());
        return customerPrivateKeyEntityMapper.insert(customerPrivateKeyEntity);
    }

    /**
     * @param customerId
     * @return
     * @see com.anji.allways.business.service.CustomerPrivateKeyService#deleteKey(java.lang.String)
     */
    @Override
    public Integer deleteKey(String customerId) {
        // TODO Auto-generated method stub
        return null;
    }

}
