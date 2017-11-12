/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.anji.allways.base.entity.BaseRequestModel;
import com.anji.allways.base.entity.BaseResponseModel;
import com.anji.allways.business.entity.StoragePlanEntity;
import com.anji.allways.business.entity.StorageRecordEntity;
import com.anji.allways.business.entity.StorageVehicleEntity;
import com.anji.allways.business.entity.TempVehicleEntity;

/**
 * @author wangyanjun
 * @version $Id: StorageService.java, v 0.1 2017年8月31日 上午10:59:49 wangyanjun Exp $
 */
public interface StorageService {

    // 创建入库计划
    Integer addPlan(StoragePlanEntity entity);

    // 预约入库商品车
    Integer addPreVehicle(StorageVehicleEntity entity);

    // 查询入库计划单详细信息
    StoragePlanEntity queryStoragePlanDetailById(Integer id);

    // 查询单次入库计划商品车信息
    List<StorageVehicleEntity> queryVehiclesByPlanId(Integer id);

    // 确定入库
    Integer confirmStorage(List<StorageVehicleEntity> vehicleInfos);

    // 分页查询入库计划单
    Map<String, Object> queryStoragePlans(StoragePlanEntity entity, Integer pageNum, Integer pageRows);

    // 分页查询入库记录
    Map<String, Object> queryStorageRecords(StorageRecordEntity entity, Integer pageNum, Integer pageRows);

    /**
     * 查询导出入库记录
     * @param entity
     * @param pageNum
     * @param pageRows
     * @return
     */
    List<Map<String, Object>> queryStorageRecordsForExportData(Map<String, Object> map);

    /**
     * 取消入库计划单
     * @param id
     *            入库计划单ID
     * @param userId
     *            操作用户ID
     * @return 影响行数
     */
    int cancelStoragePlan(Integer id, String userId);

    /**
     * 修改入库计划单操作
     * @param id
     *            入库计划单ID
     * @param userId
     *            操作用户ID
     * @param customerName
     *            客户名称
     * @param customerId
     *            客户ID
     * @param cars
     *            入库车辆信息
     * @return 影响行数
     */
    int updateStoragePlan(Integer id, String userId, String customerName, Integer customerId, JSONArray cars);

    /**
     * 确定入库操作
     */
    BaseResponseModel<Object> confirmStoragePlans(BaseRequestModel request) throws Exception;

    /**
     * 新建入库计划操作
     * @param customerId
     *            客户ID
     * @param customerName
     *            客户名称
     * @param warehouseName
     *            仓库名
     * @param warehouseId
     *            仓库ID
     * @param userId
     *            登录用户ID
     * @param cars
     *            车辆信息集合
     * @return
     */
    BaseResponseModel<Object> addStoragePlan(Integer customerId, String customerName, String warehouseName, Integer warehouseId, Integer userId, JSONArray cars);

    /**
     * 查询导出入库计划单
     * @param entity
     * @param pageNum
     * @param pageRows
     * @return
     */
    List<Map<String, Object>> exportStoragePlanByExcel(Map<String, Object> map);

    /**
     * 入库计划导入操作
     * @param list
     * @param userId
     *            用户ID
     */
    void importOperate(List<Map<String, String>> list, String userId);

    /**
     * 查询导入入库计划
     * @param list
     * @param userId
     *            用户ID
     */
    List<TempVehicleEntity> searchTemplateVehicle(String userId);

    /**
     * 新建页面初始化时删除用户导入临时表中的数据
     * @param list
     * @param userId
     *            用户ID
     */
    void deleteTemplateVehicle(String userId);

    /**
     * 创建入库计划及确定入库
     * @param request
     * @return
     */
    BaseResponseModel<Object> addInnerStoragePlanAndConfirm(BaseRequestModel request);

}
