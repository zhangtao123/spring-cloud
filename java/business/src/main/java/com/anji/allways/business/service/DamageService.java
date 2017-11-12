/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import com.anji.allways.business.entity.DamageEntity;

/**
 * <pre>
 *
 * </pre>
 * @author wangyanjun
 * @version $Id: DamageService.java, v 0.1 2017年9月14日 下午11:55:57 wangyanjun Exp $
 */
public interface DamageService {

    // 添加质损单
    Integer addDamageInfo(DamageEntity entity);

    // 修改质损单
    Integer updateDamageInfo(DamageEntity entity);
}
