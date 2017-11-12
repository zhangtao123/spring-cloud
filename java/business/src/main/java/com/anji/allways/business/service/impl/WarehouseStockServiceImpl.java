/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anji.allways.business.entity.VehicleEntity;
import com.anji.allways.business.enums.RowDictCodeE;
import com.anji.allways.business.mapper.VehicleEntityMapper;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.service.WarehouseStockService;
import com.anji.allways.business.vo.VehicleVO;

/**
 * @author wangyanjun
 * @version $Id: WarehouseStockServiceImpl.java, v 0.1 2017年9月13日 下午3:28:34 wangyanjun Exp $
 */
@Service
@Transactional
public class WarehouseStockServiceImpl implements WarehouseStockService {

    @Autowired
    private VehicleEntityMapper vehicleEntityMapper;

    // 获取数据字典Service
    @Autowired
    private DictService         dictService;

    /**
     * @param entity
     * @param pageNum
     * @param pageRows
     * @return
     * @see com.anji.allways.business.service.WarehouseStockService#queryStockVehicles(com.anji.allways.business.entity.VehicleEntity, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public Map<String, Object> queryStockVehicles(VehicleEntity entity, Integer pageNum, Integer pageRows) {
        Map<String, Object> map = new HashMap<String, Object>();

        // 查询总数
        int num = vehicleEntityMapper.queryStockVehicleCount(entity);
        if (num > 0) {

            entity.setPageNumber(pageRows);
            entity.setTotalNumber(num);
            entity.setCurrentPage(pageNum);

            // 查询分页信息
            List<VehicleVO> storageVehicleList = vehicleEntityMapper.queryStockVehicles(entity);

            // 获取车辆状态数据字典
            Map<String, String> retValue = dictService.queryAllByCode(RowDictCodeE.STORAGE_CAR_STATUS.getValue());
            // 获取质量状态数据字典
            Map<String, String> retValue1 = dictService.queryAllByCode(RowDictCodeE.QUALITY_STATUS.getValue());
            // 获取锁定状态数据字典
            Map<String, String> retValue2 = dictService.queryAllByCode(RowDictCodeE.LOCK_STATUS.getValue());
            for (VehicleVO vehicleVO : storageVehicleList) {
                // 车辆状态为0：已入库 1：待调度 2：待自提 3：待出库 6：已取消 是在库状态
                String vehicleStatus = "";
                // 设置车辆状态名称
                if (vehicleVO.getVehicleStatus() == 0 || vehicleVO.getVehicleStatus() == 1 || vehicleVO.getVehicleStatus() == 2 || vehicleVO.getVehicleStatus() == 3
                    || vehicleVO.getVehicleStatus() == 6) {
                    vehicleStatus = "0";
                } else {
                    // 4：待收货 5：已完成 7:确认交接 为出库状态
                    vehicleStatus = "1";
                }
                String vehicleStatusName = retValue.get(vehicleStatus);
                vehicleVO.setVehicleStatusName(vehicleStatusName);

                // 设置质量状态名称
                String qualityStatusName = retValue1.get(String.valueOf(vehicleVO.getQualityStatus()));
                vehicleVO.setQualityStatusName(qualityStatusName);

                // 设置锁定状态名称
                String lockStatusName = retValue2.get(String.valueOf(vehicleVO.getLockStatus()));
                vehicleVO.setLockStatusName(lockStatusName);
            }
            map.put("total", num);
            map.put("rows", storageVehicleList);
        }

        return map;
    }

    /**
     * <pre>
     * 查询导出库存记录
     * </pre>
     *
     * @param entity
     * @param pageNum
     * @param pageRows
     * @return
     */
    public List<Map<String, Object>> queryStorageRecordsForExportData(Map<String, Object> map) {

        return vehicleEntityMapper.queryStorageRecordsForExportData(map);
    }

}
