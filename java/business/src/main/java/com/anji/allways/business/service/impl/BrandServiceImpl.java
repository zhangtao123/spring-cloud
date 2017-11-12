/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anji.allways.business.entity.BrandEntity;
import com.anji.allways.business.mapper.BrandEntityMapper;
import com.anji.allways.business.service.BrandService;

/**
 * <pre>
 *
 * </pre>
 * @author wangyanjun
 * @version $Id: BrandServiceImpl.java, v 0.1 2017年9月18日 下午7:36:03 wangyanjun Exp $
 */
@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandEntityMapper brandEntityMapper;

    /**
     * @param entity
     * @return
     * @see com.anji.allways.business.service.BrandService#addBrand(com.anji.allways.business.entity.BrandEntity)
     */
    @Override
    public Integer addBrand(BrandEntity entity) {
        return brandEntityMapper.insert(entity);
    }

}
