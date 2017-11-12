/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anji.allways.business.entity.DamageEntity;
import com.anji.allways.business.mapper.DamageEntityMapper;
import com.anji.allways.business.service.DamageService;

/**
 * <pre>
 *
 * </pre>
 * @author wangyanjun
 * @version $Id: DamageServiceImpl.java, v 0.1 2017年9月14日 下午11:59:05 wangyanjun Exp $
 */
@Service
@Transactional
public class DamageServiceImpl implements DamageService {

    @Autowired
    private DamageEntityMapper damageEntityMapper;

    /**
     * @param entity
     * @return
     * @see com.anji.allways.business.service.DamageService#addDamageInfo(com.anji.allways.business.entity.DamageEntity)
     */
    @Override
    public Integer addDamageInfo(DamageEntity entity) {
        damageEntityMapper.insert(entity);
        return entity.getId();
    }

    /**
     * @param entity
     * @return
     * @see com.anji.allways.business.service.DamageService#updateDamageInfo(com.anji.allways.business.entity.DamageEntity)
     */
    @Override
    public Integer updateDamageInfo(DamageEntity entity) {
        return damageEntityMapper.updateByPrimaryKey(entity);
    }

}
