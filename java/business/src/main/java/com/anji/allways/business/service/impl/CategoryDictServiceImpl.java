/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anji.allways.business.entity.CategoryDictEntity;
import com.anji.allways.business.mapper.CategoryDictEntityMapper;
import com.anji.allways.business.service.CategoryDictService;

/**
 * <pre>
 * </pre>
 * @author xuyuyang
 * @version $Id: CategoryDictServiceImpl.java, v 0.1 2017年8月22日 下午2:40:39 xuyuyang Exp $
 */
@Service
@Transactional
public class CategoryDictServiceImpl implements CategoryDictService {

    @Autowired
    private CategoryDictEntityMapper categoryDictEntityMapper;

    /**
     * @Title: insert @Description: 插入对象
     * @param t
     *            插入的对象
     * @return 返回插入记录后的ID
     */
    @Override
    public Integer insert(CategoryDictEntity record) {
        categoryDictEntityMapper.insert(record);
        return record.getId();
    }
}
