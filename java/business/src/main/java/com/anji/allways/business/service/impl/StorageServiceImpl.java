/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anji.allways.base.entity.BaseRequestModel;
import com.anji.allways.base.entity.BaseResponseModel;
import com.anji.allways.business.common.constant.SlnsConstants;
import com.anji.allways.business.entity.StoragePlanEntity;
import com.anji.allways.business.entity.StorageRecordEntity;
import com.anji.allways.business.entity.StorageVehicleEntity;
import com.anji.allways.business.entity.TempVehicleEntity;
import com.anji.allways.business.entity.UserEntity;
import com.anji.allways.business.entity.VehicleEntity;
import com.anji.allways.business.entity.WarehouseEntity;
import com.anji.allways.business.entity.WarehouseLinkCustomerEntity;
import com.anji.allways.business.enums.RowDictCodeE;
import com.anji.allways.business.mapper.StoragePlanEntityMapper;
import com.anji.allways.business.mapper.StorageVehicleEntityMapper;
import com.anji.allways.business.mapper.TempVehicleEntityMapper;
import com.anji.allways.business.mapper.VehicleEntityMapper;
import com.anji.allways.business.mapper.WarehouseEntityMapper;
import com.anji.allways.business.mapper.WarehouseLinkCustomerEntityMapper;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.service.SerialNumberBuildService;
import com.anji.allways.business.service.StorageService;
import com.anji.allways.business.service.UserService;
import com.anji.allways.business.vo.StorageRecordVO;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.StringUtil;

/**
 * @author wangyanjun
 * @version $Id: StorageServiceImpl.java, v 0.1 2017年8月31日 上午11:16:49 wangyanjun Exp $
 */
@Service
@Transactional
public class StorageServiceImpl implements StorageService {

    @Autowired
    private StoragePlanEntityMapper           storagePlanEntityMapper;

    @Autowired
    private StorageVehicleEntityMapper        storageVehicleEntityMapper;

    // 获取数据字典Service
    @Autowired
    private DictService                       dictService;

    @Autowired
    private SerialNumberBuildService          serialNumberBuildService;

    @Autowired
    private UserService                       userService;

    @Autowired
    private TempVehicleEntityMapper           tempVehicleEntityMapper;

    @Autowired
    private WarehouseEntityMapper             warehouseEntityMapper;

    @Autowired
    private VehicleEntityMapper               vehicleEntityMapper;

    @Autowired
    private WarehouseLinkCustomerEntityMapper warehouseLinkCustomerEntityMapper;

    /**
     * @param entity
     * @return
     * @see com.anji.allways.business.service.StorageService#addPlan(com.anji.allways.business.entity.DeliveryPlanEntity)
     */
    @Override
    public Integer addPlan(StoragePlanEntity entity) {

        storagePlanEntityMapper.insert(entity);
        return entity.getId();
    }

    /**
     * @param entity
     * @return
     * @see com.anji.allways.business.service.StorageService#addPreVehicle(com.anji.allways.business.entity.StorageVehicleEntity)
     */
    @Override
    public Integer addPreVehicle(StorageVehicleEntity entity) {
        return storageVehicleEntityMapper.insert(entity);
    }

    /**
     * @param no
     * @return
     * @see com.anji.allways.business.service.StorageService#queryStoragePlanDetailByNo(java.lang.String)
     */
    @Override
    public StoragePlanEntity queryStoragePlanDetailById(Integer id) {
        return storagePlanEntityMapper.selectByPrimaryKey(id);
    }

    /**
     * @param no
     * @return
     * @see com.anji.allways.business.service.StorageService#queryVehiclesByPlanNo(java.lang.String)
     */
    @Override
    public List<StorageVehicleEntity> queryVehiclesByPlanId(Integer id) {
        return storageVehicleEntityMapper.selectByPlanId(id);
    }

    /**
     * @param vehicleInfos
     * @return
     * @see com.anji.allways.business.service.StorageService#confirmStorage(java.util.List)
     */
    @Override
    public Integer confirmStorage(List<StorageVehicleEntity> vehicleInfos) {
        for (StorageVehicleEntity entity : vehicleInfos) {
            storageVehicleEntityMapper.updateByVinAndId(entity);
        }
        return vehicleInfos.size();
    }

    /**
     * 入库计划分页查询
     * @param entity
     * @param pageNum
     * @param pageRows
     * @return
     * @see com.anji.allways.business.service.StorageService#queryStoragePlans(com.anji.allways.business.entity.StoragePlanEntity, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public Map<String, Object> queryStoragePlans(StoragePlanEntity entity, Integer pageNum, Integer pageRows) {
        Map<String, Object> map = new HashMap<String, Object>();

        // 查询总数
        int num = storagePlanEntityMapper.queryStoragePlanCount(entity);
        if (num > 0) {

            entity.setPageNumber(pageRows);
            entity.setTotalNumber(num);
            entity.setCurrentPage(pageNum);

            // 查询分页信息
            List<StoragePlanEntity> storagePlanList = storagePlanEntityMapper.queryStoragePlans(entity);

            // 获取入库计划单状态数据字典
            Map<String, String> retValue = dictService.queryAllByCode(RowDictCodeE.STORAGE_PLAN_STATUS.getValue());
            if (null != storagePlanList && storagePlanList.size() > 0) {

                String statusName = "";
                for (StoragePlanEntity retEntity : storagePlanList) {

                    // 根据入库计划单ID查询车辆信息
                    List<StorageVehicleEntity> vehiclelist = storageVehicleEntityMapper.selectByPlanId(retEntity.getId());
                    // 入库车辆数
                    retEntity.setCarNum(vehiclelist.size());

                    // 设置入库计划单状态名称
                    statusName = retValue.get(String.valueOf(retEntity.getStatus()));
                    retEntity.setStatusName(statusName);
                }
            }
            map.put("total", num);
            map.put("rows", storagePlanList);
        }

        return map;
    }

    /**
     * @param entity
     * @param pageNum
     * @param pageRows
     * @return
     * @see com.anji.allways.business.service.StorageService#queryStorageRecords(com.anji.allways.business.entity.StorageRecordEntity, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public Map<String, Object> queryStorageRecords(StorageRecordEntity entity, Integer pageNum, Integer pageRows) {
        Map<String, Object> map = new HashMap<String, Object>();

        // 查询总数
        int num = storagePlanEntityMapper.queryStorageRecordCount(entity);
        if (num > 0) {

            entity.setPageNumber(pageRows);
            entity.setTotalNumber(num);
            entity.setCurrentPage(pageNum);

            // 查询分页信息
            List<StorageRecordVO> storageRecordList = storagePlanEntityMapper.queryStorageRecords(entity);
            if (null != storageRecordList && storageRecordList.size() > 0) {
                map.put("total", num);
                map.put("rows", storageRecordList);
            }
        }

        return map;
    }

    /**
     * 查询导出入库记录
     * @param entity
     * @param pageNum
     * @param pageRows
     * @return
     */
    public List<Map<String, Object>> queryStorageRecordsForExportData(Map<String, Object> map) {

        return storagePlanEntityMapper.queryStorageRecordsForExportData(map);
    }

    /**
     * 取消入库计划单
     * @param id
     *            入库计划单ID
     * @param userId
     *            操作用户ID
     * @return 影响行数
     */
    public int cancelStoragePlan(Integer id, String userId) {

        Map<String, Object> map = new HashMap<String, Object>();
        // 状态, 0:未入库 1：已入库 2：已取消
        map.put("status", 2);
        // ID
        map.put("id", id);
        // 操作用户ID
        map.put("updateUser", userId);
        return storagePlanEntityMapper.cancelStoragePlan(map);
    }

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
    public int updateStoragePlan(Integer id, String userId, String customerName, Integer customerId, JSONArray cars) {

        // 更新入库计划单信息
        Map<String, Object> map = new HashMap<String, Object>();
        // ID
        map.put("id", id);
        // 操作用户ID
        map.put("updateUser", userId);
        // 客户Id
        map.put("customerId", customerId);
        // 客户名
        map.put("customer", customerName);
        int num = storagePlanEntityMapper.updateStoragePlanById(map);

        if (cars != null) {
            Iterator<Object> it = cars.iterator();
            while (it.hasNext()) {
                JSONObject car = (JSONObject) it.next();
                Map<String, Object> vehicleMap = new HashMap<String, Object>();
                // 车辆ID
                vehicleMap.put("id", car.getString("id"));
                // 备注
                vehicleMap.put("vehicleComment", car.getString("vehicleComment"));
                storageVehicleEntityMapper.updateStorageVehicleByPrimaryKey(vehicleMap);
            }
        }

        return num;
    }

    /**
     * 确定入库操作
     */
    public BaseResponseModel<Object> confirmStoragePlans(BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();

        // 入库计划单ID
        Integer id = request.getReqData().getInteger("id");
        if (id == null) {
            response.setRepCode(RespCode.CONFIRM_STORAGE_PLANS_FAILED);
            response.setRepMsg(RespMsg.CONFIRM_STORAGE_PLAN_ERROR_PARAS_MSG);
            return response;
        }

        // 根据ID获取当前入库计划单信息
        StoragePlanEntity storagePlan = this.queryStoragePlanDetailById(id);
        if (null == storagePlan || storagePlan.getStatus() != 0) {
            response.setRepCode(RespCode.CANCEL_STORAGE_PLANS_FAILED);
            response.setRepMsg(RespMsg.CANCEL_STORAGE_PLANS_FAILED_MSG);
            return response;
        }

        // 当前仓库是否已经停用
        WarehouseEntity warehouseEntity = warehouseEntityMapper.selectByPrimaryKey(storagePlan.getWarehouseId());
        if (warehouseEntity == null || warehouseEntity.getStatus() == 0) {
            response.setRepCode(RespCode.STORAGE_PLAN_WAREHOUSE_FAILED);
            response.setRepMsg(RespMsg.STORAGE_PLAN_WAREHOUSE_FAILED_MSG);
            return response;
        }

        List<StorageVehicleEntity> storageVehicleList = new ArrayList<StorageVehicleEntity>();
        JSONArray cars = request.getReqData().getJSONArray("carList");
        if (cars != null) {
            Iterator<Object> it = cars.iterator();

            // 校验出错时，返回数据集合
            List<Map<String, Object>> checkErrorMapList = new ArrayList<Map<String, Object>>();

            boolean isSuccess = true;
            while (it.hasNext()) {
                JSONObject car = (JSONObject) it.next();

                // 页面显示编号
                String lineNumber = car.getString("lineNumber");
                // 车辆ID
                Integer carId = car.getInteger("id");
                // VIN码
                String vin = car.getString("vin");
                // 库位号
                String location = car.getString("location");
                // 车辆备注
                String vehicleComment = car.getString("vehicleComment");

                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("id", car.getInteger("id"));
                paramMap.put("location", car.getString("location"));
                // 确认库位号是否已被占用
                int num = storageVehicleEntityMapper.checkRepeatValue(paramMap);

                Map<String, Object> checkErrorMap = new HashMap<String, Object>();

                checkErrorMap.put("lineNumber", lineNumber);
                checkErrorMap.put("id", carId);
                checkErrorMap.put("vin", vin);
                checkErrorMap.put("location", location);
                checkErrorMap.put("vehicleComment", vehicleComment);
                if (num > 0) {
                    checkErrorMap.put("message", RespMsg.LOCATION_EXISTS_FAILD_MSG);
                    checkErrorMapList.add(checkErrorMap);
                    isSuccess = false;
                    continue;
                }

                // 重复的行号
                List<String> lineNum = new ArrayList<>();

                Iterator<Object> it1 = cars.iterator();
                // 校验是否输入的库位号有重复
                while (it1.hasNext()) {
                    JSONObject checkCar = (JSONObject) it1.next();

                    String checkLineNumber = checkCar.getString("lineNumber");

                    String checkLocation = checkCar.getString("location");

                    // 排除自身且库位号与其他相同时
                    if (!lineNumber.equals(checkLineNumber) && location.equals(checkLocation)) {
                        lineNum.add(checkLineNumber);
                    }
                }

                if (lineNum.size() > 0) {
                    checkErrorMap.put("message", "库位号与编号：" + lineNum.toString() + "重复");
                    checkErrorMapList.add(checkErrorMap);
                    isSuccess = false;
                    continue;
                }
                checkErrorMap.put("message", "");
                checkErrorMapList.add(checkErrorMap);

                StorageVehicleEntity storageVehicleEntity = new StorageVehicleEntity();
                storageVehicleEntity.setId(carId);
                storageVehicleEntity.setLocation(location);
                storageVehicleEntity.setVehicleComment(vehicleComment);
                storageVehicleList.add(storageVehicleEntity);
            }
            if (!isSuccess) {
                response.setRepData(checkErrorMapList);
                response.setRepCode("isCheckList");
                return response;
            }
            // 更新车辆信息
            this.confirmStorage(storageVehicleList);
        }

        // 更新入库计划单信息
        Map<String, Object> map = new HashMap<String, Object>();
        // ID
        map.put("id", id);
        // 获取用户ID
        String retUserId = StringUtil.getUserId(request.getToken());
        if (StringUtil.isEmpty(retUserId, true)) {
            throw new Exception(RespMsg.AUTH_ERROR_TOKEN_MSG);
        }
        // 操作用户ID
        map.put("updateUser", retUserId);
        // 状态, 0:未入库 1：已入库 2：已取消
        map.put("status", 1);
        // 入库时间
        map.put("storageTime", new Date());
        // 入库计划单状态更新为已入库 入库时间为当前时间
        storagePlanEntityMapper.updateStoragePlanById(map);

        // 根据入库计划ID获取入库车辆信息
        List<StorageVehicleEntity> storageVehicles = this.queryVehiclesByPlanId(storagePlan.getId());
        if (null == storageVehicles || 0 == storageVehicles.size()) {
            throw new Exception(RespMsg.CONFIRM_STORAGE_PLAN_CARS_FAILED_MSG);
        }
        for (StorageVehicleEntity storageVehicle : storageVehicles) {
            // 添加在入库计划中的车辆(是否已加入计划单 0：未知 1：是 2：否)
            if (storageVehicle.getIsStorage() == 1) {
                // 添加车辆信息
                this.insertVehicle(storagePlan, storageVehicle, retUserId);
            }
        }
        response.setRepCode(RespCode.SUCCESS);
        response.setRepMsg(RespMsg.SUCCESS_MSG);
        return response;
    }

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
    public BaseResponseModel<Object> addStoragePlan(Integer customerId, String customerName, String warehouseName, Integer warehouseId, Integer userId, JSONArray cars) {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();

        // WP1706011000
        String serialNo = serialNumberBuildService.getSerialNumberByType(SlnsConstants.SERIAL_NUMBER_TYPE_STORAGE);
        if (null == serialNo) {
            response.setRepCode(RespCode.CREATE_STORAGE_PLAN_FAILED);
            response.setRepMsg(RespMsg.CREATE_STORAGE_PLAN_GET_SERIALNO_ERROR_MSG);
            return response;
        }

        if (cars != null) {
            Iterator<Object> it = cars.iterator();
            while (it.hasNext()) {
                JSONObject car = (JSONObject) it.next();
                String name = storageVehicleEntityMapper.checkVehicleVIN(car.getString("vin"));
                // 校验VIN码是否已经在库
                if (!StringUtil.isEmpty(name, true)) {
                    response.setRepCode(RespCode.CREATE_STORAGE_PLAN_FAILED);
                    response.setRepMsg("VIN码:" + car.getString("vin") + "已在库；VIN码:" + car.getString("vin") + "与入库计划：" + name + "重复");
                    return response;
                }
            }
        } else {

            // 根据用户ID去查询当前导入的入库计划单信息
            List<TempVehicleEntity> carList = tempVehicleEntityMapper.selectByNo(String.valueOf(userId));
            // 验证否有错误
            boolean errorFlg = false;
            for (TempVehicleEntity tempVehicleEntity : carList) {
                String name = storageVehicleEntityMapper.checkVehicleVIN(tempVehicleEntity.getVin());
                // 校验VIN码是否已经在库
                if (!StringUtil.isEmpty(name, true)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", tempVehicleEntity.getId());
                    // 备注（错误信息）
                    map.put("comment", "VIN码:" + tempVehicleEntity.getVin() + "已在库；VIN码:" + tempVehicleEntity.getVin() + "与入库计划：" + name + "重复");

                    // 更新临时表中的临时信息
                    tempVehicleEntityMapper.updateConmmentByPrimaryKey(map);
                    errorFlg = true;
                    continue;
                }
            }

            if (errorFlg) {
                response.setRepCode("isVINExists");
                response.setRepMsg(RespMsg.VIN_EXISTS_FAILED_MSG);
                return response;
            }
        }

        // 写入入库计划表
        StoragePlanEntity storagePlanEntity = new StoragePlanEntity();
        storagePlanEntity.setNo(serialNo);
        // 客户名称
        storagePlanEntity.setCustomer(customerName);
        // 客户ID
        storagePlanEntity.setCustomerId(customerId);
        storagePlanEntity.setWarehouseName(warehouseName);
        storagePlanEntity.setWarehouseId(warehouseId);
        // 操作人
        UserEntity entity = userService.queryUserEntityByUserId(userId);
        storagePlanEntity.setOperator(entity.getName());
        storagePlanEntity.setCreateUser(userId);
        storagePlanEntity.setCreateTime(new Date());
        // 状态, 0:未入库
        storagePlanEntity.setStatus(0);
        Integer planId = this.addPlan(storagePlanEntity);

        if (cars != null) {
            // 写入计划入库车辆表
            Iterator<Object> it = cars.iterator();
            while (it.hasNext()) {
                JSONObject car = (JSONObject) it.next();
                StorageVehicleEntity storageVehicleEntity = new StorageVehicleEntity();
                storageVehicleEntity.setVin(car.getString("vin"));
                storageVehicleEntity.setBrand(car.getString("brand"));
                storageVehicleEntity.setSeries(car.getString("series"));
                storageVehicleEntity.setModel(car.getString("model"));
                storageVehicleEntity.setColor(car.getString("color"));
                storageVehicleEntity.setLicenseNumber(car.getString("licenseNumber"));
                storageVehicleEntity.setRegistrationNumber(car.getString("registrationNumber"));
                storageVehicleEntity.setStoragePlanId(planId);
                // 是否已加入计划单（默认为1） 0：未知 1：是 2：否
                storageVehicleEntity.setIsStorage(1);
                this.addPreVehicle(storageVehicleEntity);
            }
        } else {

            // 根据用户ID去查询当前导入的入库计划单信息
            List<TempVehicleEntity> carList = tempVehicleEntityMapper.selectByNo(String.valueOf(userId));

            for (TempVehicleEntity tempVehicleEntity : carList) {
                StorageVehicleEntity storageVehicleEntity = new StorageVehicleEntity();
                storageVehicleEntity.setVin(tempVehicleEntity.getVin());
                storageVehicleEntity.setBrand(tempVehicleEntity.getBrand());
                storageVehicleEntity.setSeries(tempVehicleEntity.getSeries());
                storageVehicleEntity.setModel(tempVehicleEntity.getModel());
                storageVehicleEntity.setColor(tempVehicleEntity.getColor());
                storageVehicleEntity.setLicenseNumber(tempVehicleEntity.getLicenseNumber());
                storageVehicleEntity.setRegistrationNumber(tempVehicleEntity.getRegistrationNumber());
                storageVehicleEntity.setStoragePlanId(planId);
                // 是否已加入计划单（默认为1） 0：未知 1：是 2：否
                storageVehicleEntity.setIsStorage(1);
                this.addPreVehicle(storageVehicleEntity);
                // 先删除同一个用户导入的数据
                tempVehicleEntityMapper.deleteByNo(String.valueOf(userId));
            }
        }

        Map<String, String> parasMap = new HashMap<String, String>();
        parasMap.put("storageId", planId.toString());
        response.setRepData(parasMap);
        response.setRepCode(RespCode.SUCCESS);
        response.setRepMsg(RespMsg.SUCCESS_MSG);
        return response;
    }

    /**
     * 查询导出入库计划单
     * @param entity
     * @param pageNum
     * @param pageRows
     * @return
     */
    public List<Map<String, Object>> exportStoragePlanByExcel(Map<String, Object> map) {
        return storagePlanEntityMapper.exportStoragePlanByExcel(map);
    }

    /**
     * 入库计划导入操作
     * @param list
     * @param userId
     *            用户ID
     */
    public void importOperate(List<Map<String, String>> list, String userId) {

        // 先删除同一个用户导入的数据
        tempVehicleEntityMapper.deleteByNo(userId);

        for (Map<String, String> map : list) {
            TempVehicleEntity record = new TempVehicleEntity();

            // 批次号
            record.setNo(userId);
            // 商品车VIN
            String vin = map.get("vin");
            record.setVin(vin);
            // 品牌
            record.setBrand(map.get("brand"));
            // 车系
            record.setSeries(map.get("series"));
            // 车型
            record.setModel(map.get("model"));
            // 颜色
            record.setColor(map.get("color"));
            // 车牌号
            record.setLicenseNumber(map.get("licenseNumber"));
            // 行驶证号
            record.setRegistrationNumber(map.get("registrationNumber"));
            // 行号
            record.setRownum(map.get("rownum"));
            // 库位号
            record.setLocation(map.get("location"));
            // 描述
            record.setComment(map.get("message"));

            // 无错误信息时，做VIN码校验
            if (StringUtil.isEmpty(record.getComment(), true)) {
                String name = storageVehicleEntityMapper.checkVehicleVIN(vin);
                // 校验VIN码是否已经在库
                if (!StringUtil.isEmpty(name, true)) {
                    // 描述
                    record.setComment("VIN码:" + vin + "已在库；");
                }
            }

            // 无错误信息时，做库位号校验
            if (StringUtil.isEmpty(record.getComment(), true)) {
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("location", record.getLocation());
                // 确认库位号是否已被占用
                int num = storageVehicleEntityMapper.checkRepeatValue(paramMap);
                if (num > 0) {
                    // 描述
                    record.setComment(RespMsg.LOCATION_EXISTS_FAILD_MSG);
                }
            }

            // 创建用户
            record.setCreateUser(Integer.valueOf(userId));
            // 创建时间
            record.setCreateTime(new Date());
            tempVehicleEntityMapper.insertSelective(record);
        }
    }

    /**
     * 创建入库计划及确定入库
     * @param request
     * @return
     */
    public BaseResponseModel<Object> addInnerStoragePlanAndConfirm(BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        // 获取用户ID
        String retUserId = StringUtil.getUserId(request.getToken());
        if (StringUtil.isEmpty(retUserId, true)) {
            response.setRepCode(RespCode.LOGIN_FAILED);
            response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
            return response;
        }
        Integer userId = Integer.valueOf(retUserId);
        // 客户ID
        Integer customerId = request.getReqData().getInteger("customerId");
        // 客户名称
        String customerName = request.getReqData().getString("customerName");
        if (customerId == null || StringUtil.isEmpty(customerName, true)) {
            response.setRepCode(RespCode.CREATE_STORAGE_PLAN_FAILED);
            response.setRepMsg(RespMsg.CREATE_STORAGE_PLAN_ID_NOTEXIST_MSG);
            return response;
        }
        // 通过用户id获取仓库名和仓库id
        Integer warehouseId = userService.queryOwnerIdByUserId(userId);
        if (null == warehouseId) {
            response.setRepCode(RespCode.CREATE_STORAGE_PLAN_FAILED);
            response.setRepMsg(RespMsg.CREATE_STORAGE_PLAN_NULL_WHID_MSG);
            return response;
        }
        // 获取仓库信息
        WarehouseEntity retValue = warehouseEntityMapper.selectByPrimaryKey(warehouseId);
        if (null == retValue) {
            response.setRepCode(RespCode.CREATE_STORAGE_PLAN_FAILED);
            response.setRepMsg(RespMsg.CREATE_STORAGE_PLAN_ERROR_WH_MSG);
            return response;
        }
        String warehouseName = retValue.getName();
        // 获取入库计划单号
        String serialNo = serialNumberBuildService.getSerialNumberByType(SlnsConstants.SERIAL_NUMBER_TYPE_STORAGE);
        if (null == serialNo) {
            response.setRepCode(RespCode.CREATE_STORAGE_PLAN_FAILED);
            response.setRepMsg(RespMsg.CREATE_STORAGE_PLAN_GET_SERIALNO_ERROR_MSG);
            return response;
        }
        // 根据用户ID去查询当前导入的入库计划单信息
        List<TempVehicleEntity> carList = tempVehicleEntityMapper.selectByNo(String.valueOf(userId));
        // 验证否有错误
        boolean errorFlg = false;
        for (TempVehicleEntity tempVehicleEntity : carList) {
            String name = storageVehicleEntityMapper.checkVehicleVIN(tempVehicleEntity.getVin());
            Map<String, Object> map = new HashMap<>();
            map.put("id", tempVehicleEntity.getId());
            // 校验VIN码是否已经在库
            if (!StringUtil.isEmpty(name, true)) {
                // 备注（错误信息）
                map.put("comment", "VIN码:" + tempVehicleEntity.getVin() + "已在库；");
                // 更新临时表中的临时信息
                tempVehicleEntityMapper.updateConmmentByPrimaryKey(map);
                errorFlg = true;
                continue;
            }
            if (!errorFlg) {
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("location", tempVehicleEntity.getLocation());
                // 确认库位号是否已被占用
                int num = storageVehicleEntityMapper.checkRepeatValue(paramMap);
                if (num > 0) {
                    // 备注（错误信息）
                    map.put("comment", RespMsg.LOCATION_EXISTS_FAILD_MSG);
                    // 更新临时表中的临时信息
                    tempVehicleEntityMapper.updateConmmentByPrimaryKey(map);
                    errorFlg = true;
                    continue;
                }
            }
        }
        if (errorFlg) {
            response.setRepCode("isReload");
            response.setRepMsg(RespMsg.VIN_EXISTS_FAILED_MSG);
            return response;
        }
        // 系统时间
        Date sysDate = new Date();
        // 写入入库计划表
        StoragePlanEntity storagePlanEntity = new StoragePlanEntity();
        // 入库计划单号
        storagePlanEntity.setNo(serialNo);
        // 客户名称
        storagePlanEntity.setCustomer(customerName);
        // 客户ID
        storagePlanEntity.setCustomerId(customerId);
        // 仓库名
        storagePlanEntity.setWarehouseName(warehouseName);
        // 仓库ID
        storagePlanEntity.setWarehouseId(warehouseId);
        // 操作人
        UserEntity entity = userService.queryUserEntityByUserId(userId);
        storagePlanEntity.setOperator(entity.getName());
        // 创建者
        storagePlanEntity.setCreateUser(userId);
        // 创建时间
        storagePlanEntity.setCreateTime(sysDate);
        // 状态, 1:已入库
        storagePlanEntity.setStatus(1);
        // 入库时间
        storagePlanEntity.setStorageTime(sysDate);
        // 创建入库计划单
        Integer planId = this.addPlan(storagePlanEntity);
        for (TempVehicleEntity tempVehicleEntity : carList) {
            // 添加入库计划车辆信息
            StorageVehicleEntity storageVehicleEntity = this.insertStorageVehicle(tempVehicleEntity, planId);
            // 添加车辆信息
            this.insertVehicle(storagePlanEntity, storageVehicleEntity, retUserId);
        }
        // 先删除同一个用户导入的数据
        tempVehicleEntityMapper.deleteByNo(String.valueOf(userId));
        // 查询某个客户是否和某个仓库有合作关系
        Map<String, Object> map = new HashMap<String, Object>();
        // 仓库ID
        map.put("warehouseId", warehouseId);
        // 客户ID
        map.put("customerId", customerId);
        int id = warehouseLinkCustomerEntityMapper.selectLinkByWarehouseAndCustomerId(map);
        // 如果不存在关联关系，则新增一条关系
        if (id == 0) {
            // 创建仓库与客户关系
            WarehouseLinkCustomerEntity warehouseLinkCustomerEntity = new WarehouseLinkCustomerEntity();
            // 客户ID
            warehouseLinkCustomerEntity.setCustomerId(customerId);
            // 仓库ID
            warehouseLinkCustomerEntity.setWarehouseId(warehouseId);
            // 创建时间
            warehouseLinkCustomerEntity.setCreateTime(sysDate);
            // 创建者
            warehouseLinkCustomerEntity.setCreateUser(userId);
            // 车位数
            warehouseLinkCustomerEntity.setSpaceAmount(carList.size());
            warehouseLinkCustomerEntityMapper.insert(warehouseLinkCustomerEntity);
        } else {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("id", id);
            // 车位数
            paramMap.put("spaceAmount", carList.size());
            // 更新客户车位数
            warehouseLinkCustomerEntityMapper.updateSpaceAmountForInOrOut(paramMap);
        }
        response.setRepCode(RespCode.SUCCESS);
        response.setRepMsg(RespMsg.SUCCESS_MSG);
        return response;
    }

    /**
     * 查询导入入库计划
     * @param list
     * @param userId
     *            用户ID
     */
    public List<TempVehicleEntity> searchTemplateVehicle(String userId) {

        return tempVehicleEntityMapper.selectByNo(userId);
    }

    /**
     * 新建页面初始化时删除用户导入临时表中的数据
     * @param list
     * @param userId
     *            用户ID
     */
    public void deleteTemplateVehicle(String userId) {
        // 先删除同一个用户导入的数据
        tempVehicleEntityMapper.deleteByNo(userId);
    }

    /**
     * 添加车辆信息
     * @param storagePlanEntity
     *            入库计划单
     * @param storageVehicleEntity
     *            入库计划单车辆
     * @param retUserId
     *            用户ID
     */
    private void insertVehicle(StoragePlanEntity storagePlanEntity, StorageVehicleEntity storageVehicleEntity, String retUserId) {
        // 车辆信息
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setWarehouseId(storagePlanEntity.getWarehouseId());
        vehicle.setDealerId(storagePlanEntity.getCustomerId());
        // 预入库商品车id
        vehicle.setStorageVehicleId(storageVehicleEntity.getId());
        vehicle.setVin(storageVehicleEntity.getVin());
        vehicle.setBrand(storageVehicleEntity.getBrand());
        vehicle.setSeries(storageVehicleEntity.getSeries());
        vehicle.setModel(storageVehicleEntity.getModel());
        vehicle.setAnnounceYear(null);
        vehicle.setManufacturerColor(storageVehicleEntity.getColor());
        vehicle.setStandardColorId(0);
        vehicle.setPictureNumber(0);
        vehicle.setLicenseNumber(storageVehicleEntity.getLicenseNumber());
        vehicle.setRegistrationNumber(storageVehicleEntity.getRegistrationNumber());
        vehicle.setVehicleType(storageVehicleEntity.getVehicleType());
        vehicle.setVehicleStatus(0);
        vehicle.setQualityStatus(0);
        vehicle.setLockStatus(0);
        vehicle.setCreateUser(Integer.valueOf(retUserId));
        vehicle.setCreateTime(new Date());
        vehicleEntityMapper.insert(vehicle);
    }

    /**
     * 添加入库计划车辆信息
     * @param tempVehicleEntity
     *            入库计划导入临时表
     * @param planId
     *            入库计划单ID
     */
    private StorageVehicleEntity insertStorageVehicle(TempVehicleEntity tempVehicleEntity, Integer planId) {
        // 入库计划单车辆信息
        StorageVehicleEntity storageVehicleEntity = new StorageVehicleEntity();
        storageVehicleEntity.setVin(tempVehicleEntity.getVin());
        storageVehicleEntity.setBrand(tempVehicleEntity.getBrand());
        storageVehicleEntity.setSeries(tempVehicleEntity.getSeries());
        storageVehicleEntity.setModel(tempVehicleEntity.getModel());
        storageVehicleEntity.setColor(tempVehicleEntity.getColor());
        storageVehicleEntity.setLicenseNumber(tempVehicleEntity.getLicenseNumber());
        storageVehicleEntity.setRegistrationNumber(tempVehicleEntity.getRegistrationNumber());
        storageVehicleEntity.setStoragePlanId(planId);
        storageVehicleEntity.setLocation(tempVehicleEntity.getLocation());
        // 是否已加入计划单（默认为1） 0：未知 1：是 2：否
        storageVehicleEntity.setIsStorage(1);
        this.addPreVehicle(storageVehicleEntity);
        return storageVehicleEntity;
    }
}
