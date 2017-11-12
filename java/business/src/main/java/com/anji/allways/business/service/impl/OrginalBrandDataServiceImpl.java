/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anji.allways.business.entity.OriginalBrandEntity;
import com.anji.allways.business.mapper.OriginalBrandEntityMapper;
import com.anji.allways.business.service.OrginalBrandDataService;

/**
 * <pre>
 *
 * </pre>
 * @author wangyanjun
 * @version $Id: OrginalBrandDataServiceImpl.java, v 0.1 2017年9月18日 下午2:17:17 wangyanjun Exp $
 */
@Service
@Transactional
public class OrginalBrandDataServiceImpl implements OrginalBrandDataService {

    @Autowired
    private OriginalBrandEntityMapper originalBrandEntityMapper;

    /**
     * @param entity
     * @return
     * @see com.anji.allways.business.service.OrginalBrandDataService#addBrand(com.anji.allways.business.entity.OriginalBrandEntity)
     */
    @Override
    public Integer addBrand(OriginalBrandEntity entity) {
        return originalBrandEntityMapper.insert(entity);
    }

}
