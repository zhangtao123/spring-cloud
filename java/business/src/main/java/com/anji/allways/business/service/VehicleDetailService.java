/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import java.util.List;
import java.util.Map;

import com.anji.allways.business.entity.VehicleEntity;

/**
 * @author 李军
 * @version $Id: VehicleDetailService.java, v 0.1 2017年8月29日 上午9:24:21 李军 Exp $
 */
public interface VehicleDetailService {
    // 获取车辆详细信息
    Map<String, Object> carDetail(int vehicleId);

    // 获取库存
    Map<String, Object> getStockDetail(Map<String, Object> map);

    // 根据vin码获取车辆信息
    Map<String, Object> getByVinVehicle(String vin, int userId);

    // 经销商更新车辆图片
    Map<String, Object> updateVehiclePath(String vehicleId, String path);

    // 经销商更新车辆图片
    boolean deleteVehiclePath(Integer pathId, String path);

    Integer addBatchVehicles(List<VehicleEntity> vehicleList);

    // 根据仓库id和客户id查询在库车辆数
    Integer countInVehicle(Integer wareHouseId, Integer customerId);
}
