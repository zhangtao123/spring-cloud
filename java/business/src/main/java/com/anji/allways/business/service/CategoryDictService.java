/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import com.anji.allways.business.entity.CategoryDictEntity;

/**
 * <pre>
 * </pre>
 * @author xuyuyang
 * @version $Id: CategoryDictService.java, v 0.1 2017年8月22日 下午2:40:39 xuyuyang Exp $
 */
public interface CategoryDictService {

    /**
     * @Title: insert @Description: 插入对象
     * @param t
     *            插入的对象
     * @return 返回插入记录后的ID
     */
    Integer insert(CategoryDictEntity entity);
}
