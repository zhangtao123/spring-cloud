/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anji.allways.business.entity.WarehouseLinkCustomerEntity;
import com.anji.allways.business.mapper.WarehouseLinkCustomerEntityMapper;
import com.anji.allways.business.service.WarehouseLinkCustomerService;

/**
 * @author xuyuyang
 * @version $Id: DictService.java, v 0.1 2017年8月22日 下午2:40:39 xuyuyang Exp $
 */
@Service
@Transactional
public class WarehouseLinkCustomerServiceImpl implements WarehouseLinkCustomerService {

    @Autowired
    private WarehouseLinkCustomerEntityMapper mapper;

    /**
     * @Title: insert @Description: 插入对象
     * @param t
     *            插入的对象
     * @return 返回插入记录后的ID
     */
    @Override
    public Integer insert(WarehouseLinkCustomerEntity record) {
        mapper.insert(record);
        return record.getId();
    }

}
