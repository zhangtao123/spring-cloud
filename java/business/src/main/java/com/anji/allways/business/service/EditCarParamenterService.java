/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import java.util.List;
import java.util.Map;

import com.anji.allways.business.entity.VehicleEntity;

/**
 * <pre>
 * </pre>
 * @author 李军
 * @version $Id: EditCarParamenterService.java, v 0.1 2017年8月26日 下午3:58:36 李军 Exp $
 */
public interface EditCarParamenterService {
    // 查询车辆信息
    Map<String, Object> getEditParamenter(int vehicleId);

    // 编辑车辆信息
    int keepEditParamenter(VehicleEntity vehicleEntity);

    // 修改车辆锁定状态
    int updateLockStruts(Map<String, Object> mapVue);

    // 检查年份是否存在
    int checkYearExist(Map<String, Object> mapVue);

    // 检查年份是否存在
    List<String> selectByBrandToYear(Map<String, Object> mapVue);

    VehicleEntity selectByPrimaryKey(int vehicleId);
}
