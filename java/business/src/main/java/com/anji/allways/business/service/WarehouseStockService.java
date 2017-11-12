/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import java.util.List;
import java.util.Map;

import com.anji.allways.business.entity.VehicleEntity;

/**
 * @author wangyanjun
 * @version $Id: WarehouseStockService.java, v 0.1 2017年9月13日 下午3:26:25 wangyanjun Exp $
 */
public interface WarehouseStockService {
    // 查询运输订单
    Map<String, Object> queryStockVehicles(VehicleEntity entity, Integer pageNum, Integer pageRows);

    /**
     * 查询导出库存记录
     * @param entity
     * @param pageNum
     * @param pageRows
     * @return
     */
    List<Map<String, Object>> queryStorageRecordsForExportData(Map<String, Object> map);
}
