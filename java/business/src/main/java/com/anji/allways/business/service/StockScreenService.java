/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import java.util.Map;

/**
 * <pre>
 * </pre>
 * @author 李军
 * @version $Id: StockScreenService.java, v 0.1 2017年8月28日 下午6:55:57 李军 Exp $
 */
public interface StockScreenService {
    // 获取筛选信息
    Map<String, Object> getScreen(Integer userId);

    // 获取筛选信息
    Map<String, Object> getScreenBrand(Integer userId);
}
