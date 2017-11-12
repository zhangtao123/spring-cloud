/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.anji.allways.base.aop.LoggerManage;
import com.anji.allways.base.entity.BaseRequestModel;
import com.anji.allways.base.entity.BaseResponseModel;
import com.anji.allways.business.entity.CustomerEntity;
import com.anji.allways.business.entity.CustomerPrivateKeyEntity;
import com.anji.allways.business.entity.ExtRequestModel;
import com.anji.allways.business.entity.StoragePlanEntity;
import com.anji.allways.business.entity.StorageRecordEntity;
import com.anji.allways.business.entity.StorageVehicleEntity;
import com.anji.allways.business.entity.TempVehicleEntity;
import com.anji.allways.business.enums.RowDictCodeE;
import com.anji.allways.business.service.CustomerPrivateKeyService;
import com.anji.allways.business.service.CustomerService;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.service.StorageService;
import com.anji.allways.business.service.StorageVehicleService;
import com.anji.allways.business.service.UserService;
import com.anji.allways.business.service.WarehouseService;
import com.anji.allways.business.vo.DictVO;
import com.anji.allways.business.vo.WarehouseVO;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.MD5Util;
import com.anji.allways.common.util.NsExcelReadXUtils;
import com.anji.allways.common.util.StringUtil;

/**
 * 入库计划管理Controller
 * @author wangyanjun
 * @version $Id: WarehouseController.java, v 0.1 2017年8月31日 上午11:23:45 wangyanjun Exp $
 */
@Controller
@RequestMapping("/business/storagePlan")
public class StoragePlanController {

    private Logger                    logger = LogManager.getLogger(StoragePlanController.class);

    @Autowired
    private StorageService            storageService;

    @Autowired
    private WarehouseService          warehouseService;

    @Autowired
    private UserService               userService;

    // 获取数据字典Service
    @Autowired
    private DictService               dictService;

    @Autowired
    private CustomerPrivateKeyService customerPrivateKeyService;

    @Autowired
    private CustomerService           customerService;

    @Autowired
    private StorageVehicleService     storageVehicleService;

    /**
     * 外接系统创建入库计划
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/extstorage")
    @ResponseBody
    public BaseResponseModel<Object> addExtStoragePlan(@RequestBody ExtRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            String uid = request.getUid();
            String warehouseId = request.getWarehouseId();
            Boolean isAuthenticated = false;
            if (!StringUtil.isEmpty(uid, true) && !StringUtil.isEmpty(warehouseId, true)) {
                CustomerPrivateKeyEntity customerPrivateKeyEntity = customerPrivateKeyService.queryKeyById(uid);
                if (null != customerPrivateKeyEntity) {
                    String sign = request.getSign();
                    String key = customerPrivateKeyEntity.getPrivateKey();
                    String originStr = "uid=" + uid + "&key=" + key;
                    if (sign.equals(MD5Util.encrypt(originStr))) {
                        isAuthenticated = true;
                    } else {
                        response.setRepCode(RespCode.CREATE_STORAGE_PLAN_FAILED);
                        response.setRepMsg(RespMsg.CREATE_STORAGE_PLAN_CERT_FAILED_MSG);
                    }
                } else {
                    response.setRepCode(RespCode.CREATE_STORAGE_PLAN_FAILED);
                    response.setRepMsg(RespMsg.CREATE_STORAGE_PLAN_ID_NOTEXIST_MSG);
                }
            } else {
                response.setRepCode(RespCode.CREATE_STORAGE_PLAN_FAILED);
                response.setRepMsg(RespMsg.CREATE_STORAGE_PLAN_ID_NULL_MSG);
            }

            if (!isAuthenticated) {
                response.setRepData(null);
                return response;
            }

            Integer customerId = null;
            Integer warehouseIdInt = null;
            try {
                customerId = Integer.valueOf(uid);
                warehouseIdInt = Integer.valueOf(warehouseId);
            } catch (NumberFormatException ne) {
                response.setRepCode(RespCode.CREATE_STORAGE_PLAN_FAILED);
                response.setRepMsg(RespMsg.CREATE_STORAGE_PLAN_NUMBER_ERROR_MSG);
                response.setRepData(null);
                return response;
            }

            CustomerEntity customerEntity = customerService.queryCustomerById(customerId);
            if (null == customerEntity) {
                response.setRepCode(RespCode.CREATE_STORAGE_PLAN_FAILED);
                response.setRepMsg(RespMsg.CREATE_STORAGE_PLAN_ID_NOTEXIST_MSG);
                response.setRepData(null);
                return response;
            }

            WarehouseVO warehouseVO = warehouseService.selectByPrimaryKey(warehouseIdInt);
            if (null == warehouseVO) {
                response.setRepCode(RespCode.CREATE_STORAGE_PLAN_FAILED);
                response.setRepMsg(RespMsg.CREATE_STORAGE_PLAN_WH_NOTEXIST_MSG);
                response.setRepData(null);
                return response;
            }

            str = request.getReqData().toString();
            // 从外部客户接口查用户id
            Map<String, Object> map = new HashMap<String, Object>();
            // 客户ID
            map.put("customerId", customerId);
            // 用户所属组织（6：第三方系统）
            map.put("type", 6);
            Integer userId = userService.selectUserIdByCustomerIdAndType(map);

            response = storageService.addStoragePlan(customerId, customerEntity.getName(), warehouseVO.getName(), warehouseIdInt, userId, request.getReqData().getJSONArray("cars"));
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.CREATE_STORAGE_PLAN_FAILED);
            response.setRepMsg(RespMsg.CREATE_STORAGE_PLAN_EXCEPTION_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }

        return response;
    }

    @RequestMapping("/innstorage")
    @ResponseBody
    @LoggerManage(description = "系统内创建入库计划")
    public BaseResponseModel<Object> addInnerStoragePlan(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        String str = request.getReqData().toString();
        try {
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
            WarehouseVO warehouseVO = warehouseService.selectByPrimaryKey(warehouseId);
            if (null == warehouseVO) {
                response.setRepCode(RespCode.CREATE_STORAGE_PLAN_FAILED);
                response.setRepMsg(RespMsg.CREATE_STORAGE_PLAN_ERROR_WH_MSG);
                return response;
            }
            String warehouseName = warehouseVO.getName();

            response = storageService.addStoragePlan(customerId, customerName, warehouseName, warehouseId, userId, null);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.CREATE_STORAGE_PLAN_FAILED);
            response.setRepMsg(RespMsg.CREATE_STORAGE_PLAN_EXCEPTION_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }
        return response;
    }

    @RequestMapping("/queryStoragePlans")
    @ResponseBody
    @LoggerManage(description = "入库计划分页查询")
    public BaseResponseModel<Object> queryStoragePlans(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        String str = request.getReqData().toString();
        try {
            Integer pageNum = request.getReqData().getInteger("pageNum");
            Integer pageRows = request.getReqData().getInteger("pageRows");

            // 获取用户ID
            String retUserId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(retUserId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }
            String userId = retUserId;

            // 根据用户ID和所属组织类型查询所属客户ID
            Integer customerId = userService.queryCustomerId(Integer.valueOf(userId), 2);

            Map<String, Object> map = new HashMap<String, Object>();

            // 获取查询条件
            StoragePlanEntity bean = JSON.parseObject(str, StoragePlanEntity.class);

            // 登录用户存在时
            if (customerId != null) {

                // 仓库状态处理(JSON转换之后，如果为""会自动赋值为0)
                String status = request.getReqData().getString("status");
                bean.setStatusSearch(status);

                // 登录用户为管理员时
                if (customerId != -1 && customerId != 0) {

                    // 获取某个仓库ID及其所有的下属仓库ID
                    List<Integer> idList = warehouseService.selectAllLevelIdByParentId(customerId);
                    bean.setIdList(idList);
                } else {

                    // 登录用户为普通用户时只能查看自己创建的数据
                    if (customerId == -1) {
                        bean.setCreateUser(Integer.valueOf(userId));
                    }
                }
                map = storageService.queryStoragePlans(bean, pageNum, pageRows);

                // 保存最后一次查询时的条件，用于excel导出
                // 入库计划单号
                map.put("lastSearchNo", StringUtil.convertNullToEmpty(bean.getNo()));

                // 创建计划开始时间
                map.put("lastSearchCreateStartTime", StringUtil.convertNullToEmpty(bean.getCreateStartTime()));

                // 创建计划结束时间
                map.put("lastSearchCreateEndTime", StringUtil.convertNullToEmpty(bean.getCreateEndTime()));

                // 客户名
                map.put("lastSearchCustomer", StringUtil.convertNullToEmpty(bean.getCustomer()));

                // 仓库名
                map.put("lastSearchWarehouseName", StringUtil.convertNullToEmpty(bean.getWarehouseName()));

                // 状态
                map.put("lastSearchStatusSearch", StringUtil.convertNullToEmpty(status));

            }
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.QUERY_STORAGE_PLANS_FAILED);
            response.setRepMsg(RespMsg.QUERY_STORAGE_PLANS_EXCEPTION_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }

        return response;
    }

    @RequestMapping("/queryStoragePlanStatusForDict")
    @ResponseBody
    @LoggerManage(description = "获取入库计划状态下拉列表")
    public BaseResponseModel<Object> queryStoragePlanStatusForDict(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        try {

            // 获取运输司机状态下拉列表
            List<DictVO> retValue = dictService.queryAllByCodeForPage(RowDictCodeE.STORAGE_PLAN_STATUS.getValue());
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.STORAGE_PLAN_STATUS_FAILED);
            response.setRepMsg(RespMsg.STORAGE_PLAN_STATUS_FAILED_MSG);
        }
        return response;
    }

    @RequestMapping("/cancelStoragePlan")
    @ResponseBody
    @LoggerManage(description = "取消入库计划单")
    public BaseResponseModel<Object> cancelStoragePlan(@RequestBody BaseRequestModel request) throws Exception {
        synchronized (this) {

            BaseResponseModel<Object> response = new BaseResponseModel<>();
            String str = request.getReqData().toString();
            try {

                // 入库计划单ID
                Integer id = request.getReqData().getInteger("id");
                StoragePlanEntity storagePlan = storageService.queryStoragePlanDetailById(id);
                // 没查询到入库计划单或状态不为未入库状态时
                if (storagePlan == null || storagePlan.getStatus() != 0) {
                    response.setRepCode(RespCode.CANCEL_STORAGE_PLANS_FAILED);
                    response.setRepMsg(RespMsg.CANCEL_STORAGE_PLANS_FAILED_MSG);
                    return response;
                }

                // 获取用户ID
                String retUserId = StringUtil.getUserId(request.getToken());
                if (StringUtil.isEmpty(retUserId, true)) {
                    response.setRepCode(RespCode.LOGIN_FAILED);
                    response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                    return response;
                }
                String userId = retUserId;
                int num = storageService.cancelStoragePlan(id, userId);
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
                return response;

            } catch (Exception e) {
                e.printStackTrace();
                logger.info(e.getMessage());
                response.setRepCode(RespCode.CANCLE_STORAGE_PLAN_FAILED);
                response.setRepMsg(RespMsg.CANCLE_STORAGE_PLAN_FAILED_MSG);
            } finally {
                logger.info("传入参数：{}" + str.toString());
            }

            return response;
        }
    }

    @RequestMapping("/detail")
    @ResponseBody
    @LoggerManage(description = "根据ID获取入库计划单信息")
    public BaseResponseModel<Object> storagePlanDetail(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        String str = request.getReqData().toString();

        try {
            Integer id = request.getReqData().getInteger("id");

            // 获取页面的状态值
            Integer status = request.getReqData().getInteger("status");

            StoragePlanEntity storagePlan = storageService.queryStoragePlanDetailById(id);
            if (null == storagePlan) {
                response.setRepCode(RespCode.QUERY_STORAGE_PLAN_FAILED);
                response.setRepMsg(RespMsg.QUERY_STORAGE_PLAN_NOEXIST_MSG);
                return response;
            }

            // 页面值与获取的数据的状态值不一致时
            if (storagePlan.getStatus() != status) {
                response.setRepCode(RespCode.CANCEL_STORAGE_CAR_FAILED);
                response.setRepMsg(RespMsg.CANCEL_STORAGE_CAR_FAILED_MSG);
                return response;
            }

            Map<String, Object> parasMap = new HashMap<String, Object>();
            // 入库计划单ID
            parasMap.put("id", storagePlan.getId().toString());
            // 入库计划单号
            parasMap.put("no", storagePlan.getNo());
            // 客户ID
            parasMap.put("customerId", storagePlan.getCustomerId());
            // 客户名称
            parasMap.put("customer", storagePlan.getCustomer());
            // 仓库ID
            parasMap.put("warehouseId", storagePlan.getWarehouseId());
            // 仓库名
            parasMap.put("warehouseName", storagePlan.getWarehouseName());
            // 创建时间
            parasMap.put("createTime", storagePlan.getCreateTime());

            List<Map<String, Object>> vehicleParas = new ArrayList<Map<String, Object>>();
            // 根据入库计划单Id查询对应的入库车辆信息
            List<StorageVehicleEntity> vehicles = storageService.queryVehiclesByPlanId(storagePlan.getId());
            if (null == vehicles) {
                response.setRepCode(RespCode.QUERY_STORAGE_PLAN_FAILED);
                response.setRepMsg(RespMsg.QUERY_STORAGE_PLAN_NOVEHICLES_MSG);
                return response;
            }
            for (StorageVehicleEntity vehicle : vehicles) {
                Map<String, Object> vehiclePara = new HashMap<String, Object>();
                // 车辆ID
                vehiclePara.put("id", vehicle.getId());
                // 车辆VIN码
                vehiclePara.put("vin", vehicle.getVin());
                // 车辆库位号
                vehiclePara.put("location", vehicle.getLocation());
                // 备注
                vehiclePara.put("vehicleComment", vehicle.getVehicleComment());
                // 是否已加入计划单 0：未知 1：是 2：否
                vehiclePara.put("isStorage", vehicle.getIsStorage());
                vehiclePara.put("message", "");
                vehicleParas.add(vehiclePara);
            }
            parasMap.put("carList", vehicleParas);
            parasMap.put("vehicleNumber", vehicles.size());

            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            response.setRepData(parasMap);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.QUERY_STORAGE_PLAN_FAILED);
            response.setRepMsg(RespMsg.QUERY_STORAGE_PLAN_DETAIL_EXCEPTION_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }

        return response;
    }

    @RequestMapping("/queryCustomerForDict")
    @ResponseBody
    @LoggerManage(description = "获取客户名称下拉列表")
    public BaseResponseModel<Object> queryCustomerForDict(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<>();
        String str = request.getReqData().toString();
        try {

            // 获取用户ID
            String retUserId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(retUserId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }

            Map<String, Object> map = new HashMap<String, Object>();

            map.put("name", request.getReqData().get("name"));
            List<DictVO> retVO = warehouseService.queryCustomerForDict(map);

            response.setRepData(retVO);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.QUERY_CUSTOMER_DICT_FAILED);
            response.setRepMsg(RespMsg.QUERY_CUSTOMER_DICT_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }

        return response;
    }

    @RequestMapping("/cancelCarStorage")
    @ResponseBody
    @LoggerManage(description = "取消计划")
    public BaseResponseModel<Object> cancelCarStorage(@RequestBody BaseRequestModel request) throws Exception {
        synchronized (this) {
            BaseResponseModel<Object> response = new BaseResponseModel<>();
            String str = request.getReqData().toString();
            try {

                Integer storagePlanId = request.getReqData().getInteger("id");
                StoragePlanEntity storagePlan = storageService.queryStoragePlanDetailById(storagePlanId);
                // 没查询到入库计划单或状态不为未入库状态时
                if (storagePlan == null || storagePlan.getStatus() != 0) {
                    response.setRepCode(RespCode.CANCEL_STORAGE_PLANS_FAILED);
                    response.setRepMsg(RespMsg.CANCEL_STORAGE_PLANS_FAILED_MSG);
                    return response;
                }
                Integer carId = request.getReqData().getInteger("carId");
                // 车辆备注
                String vehicleComment = request.getReqData().getString("vehicleComment");
                StorageVehicleEntity entity = storageVehicleService.selectByPrimaryKey(carId);
                // 只有加入入库计划单的车辆才能取消
                if (entity == null || entity.getIsStorage() != 1) {
                    response.setRepCode(RespCode.CANCEL_STORAGE_CAR_FAILED);
                    response.setRepMsg(RespMsg.CANCEL_STORAGE_CAR_FAILED_MSG);
                    return response;
                }
                int num = storageVehicleService.cancelOrAddCarStorage(carId, 2, vehicleComment);
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
                return response;

            } catch (Exception e) {
                e.printStackTrace();
                logger.info(e.getMessage());
                response.setRepCode(RespCode.CANCLE_CAR_IN_PLAN_FAILED);
                response.setRepMsg(RespMsg.CANCLE_CAR_IN_PLAN_FAILED_MSG);
            } finally {
                logger.info("传入参数：{}" + str.toString());
            }

            return response;
        }
    }

    @RequestMapping("/addCarStorage")
    @ResponseBody
    @LoggerManage(description = "加入计划")
    public BaseResponseModel<Object> addCarStorage(@RequestBody BaseRequestModel request) throws Exception {
        synchronized (this) {
            BaseResponseModel<Object> response = new BaseResponseModel<>();
            String str = request.getReqData().toString();
            try {

                Integer storagePlanId = request.getReqData().getInteger("id");
                StoragePlanEntity storagePlan = storageService.queryStoragePlanDetailById(storagePlanId);
                // 没查询到入库计划单或状态不为未入库状态时
                if (storagePlan == null || storagePlan.getStatus() != 0) {
                    response.setRepCode(RespCode.CANCEL_STORAGE_PLANS_FAILED);
                    response.setRepMsg(RespMsg.CANCEL_STORAGE_PLANS_FAILED_MSG);
                    return response;
                }
                Integer carId = request.getReqData().getInteger("carId");
                // 车辆备注
                String vehicleComment = request.getReqData().getString("vehicleComment");
                StorageVehicleEntity entity = storageVehicleService.selectByPrimaryKey(carId);
                // 只有不在入库计划单的车辆才能加入
                if (entity == null || entity.getIsStorage() != 2) {
                    response.setRepCode(RespCode.CANCEL_STORAGE_CAR_FAILED);
                    response.setRepMsg(RespMsg.CANCEL_STORAGE_CAR_FAILED_MSG);
                    return response;
                }
                int num = storageVehicleService.cancelOrAddCarStorage(carId, 1, vehicleComment);
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
                return response;

            } catch (Exception e) {
                e.printStackTrace();
                logger.info(e.getMessage());
                response.setRepCode(RespCode.ADD_CAR_IN_PLAN_FAILED);
                response.setRepMsg(RespMsg.ADD_CAR_IN_PLAN_FAILED_MSG);
            } finally {
                logger.info("传入参数：{}" + str.toString());
            }

            return response;
        }
    }

    @RequestMapping("/updateStorageByComment")
    @ResponseBody
    @LoggerManage(description = "修改入库计划时备注修改操作")
    public BaseResponseModel<Object> updateStorageByComment(@RequestBody BaseRequestModel request) throws Exception {
        synchronized (this) {
            BaseResponseModel<Object> response = new BaseResponseModel<>();
            String str = request.getReqData().toString();
            try {

                Integer storagePlanId = request.getReqData().getInteger("id");
                StoragePlanEntity storagePlan = storageService.queryStoragePlanDetailById(storagePlanId);
                // 没查询到入库计划单或状态不为未入库状态时
                if (storagePlan == null || storagePlan.getStatus() != 0) {
                    response.setRepCode(RespCode.CANCEL_STORAGE_PLANS_FAILED);
                    response.setRepMsg(RespMsg.CANCEL_STORAGE_PLANS_FAILED_MSG);
                    return response;
                }
                Integer carId = request.getReqData().getInteger("carId");
                // 车辆备注
                String vehicleComment = request.getReqData().getString("vehicleComment");

                // 如果备注为空则不做更新操作
                if (StringUtil.isEmpty(vehicleComment, true)) {
                    response.setRepData(0);
                    response.setRepCode(RespCode.SUCCESS);
                    response.setRepMsg(RespMsg.SUCCESS_MSG);
                    return response;
                }
                int num = storageVehicleService.updateStorageByComment(carId, vehicleComment);
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
                return response;

            } catch (Exception e) {
                e.printStackTrace();
                logger.info(e.getMessage());
                response.setRepCode(RespCode.UPDATE_STORAGE_CAR_FAILED);
                response.setRepMsg(RespMsg.UPDATE_STORAGE_CAR_FAILED_MSG);
            } finally {
                logger.info("传入参数：{}" + str.toString());
            }

            return response;
        }
    }

    @RequestMapping("/updateStoragePlan")
    @ResponseBody
    @LoggerManage(description = "修改入库计划")
    public BaseResponseModel<Object> updateStoragePlan(@RequestBody BaseRequestModel request) throws Exception {
        synchronized (this) {
            BaseResponseModel<Object> response = new BaseResponseModel<>();
            String str = request.getReqData().toString();
            try {

                // 入库计划单ID
                Integer id = request.getReqData().getInteger("id");
                // 客户Id
                Integer customerId = request.getReqData().getInteger("customerId");
                // 客户名称
                String customerName = request.getReqData().getString("customerName");
                // 车辆信息
                JSONArray cars = request.getReqData().getJSONArray("carList");

                // 获取用户ID
                String retUserId = StringUtil.getUserId(request.getToken());
                if (StringUtil.isEmpty(retUserId, true)) {
                    response.setRepCode(RespCode.LOGIN_FAILED);
                    response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                    return response;
                }
                // 当前用户ID
                String userId = retUserId;

                StoragePlanEntity storagePlan = storageService.queryStoragePlanDetailById(id);
                // 没查询到入库计划单或状态不为未入库状态时
                if (storagePlan == null || storagePlan.getStatus() != 0) {
                    response.setRepCode(RespCode.CANCEL_STORAGE_PLANS_FAILED);
                    response.setRepMsg(RespMsg.CANCEL_STORAGE_PLANS_FAILED_MSG);
                    return response;
                }
                // 更新入库计划单
                int num = storageService.updateStoragePlan(id, userId, customerName, customerId, cars);
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
                return response;

            } catch (Exception e) {
                e.printStackTrace();
                logger.info(e.getMessage());
                response.setRepCode(RespCode.UPDATE_STORAGE_CAR_FAILED);
                response.setRepMsg(RespMsg.UPDATE_STORAGE_CAR_FAILED_MSG);
            } finally {
                logger.info("传入参数：{}" + str.toString());
            }

            return response;
        }
    }

    @RequestMapping("/confirm")
    @ResponseBody
    @LoggerManage(description = "确定入库")
    public BaseResponseModel<Object> confirmStoragePlans(@RequestBody BaseRequestModel request) throws Exception {
        synchronized (this) {
            BaseResponseModel<Object> response = new BaseResponseModel<>();
            String str = request.getReqData().toString();
            try {
                return storageService.confirmStoragePlans(request);
            } catch (Exception e) {
                e.printStackTrace();
                logger.info(e.getMessage());
                response.setRepCode(RespCode.CONFIRM_STORAGE_PLANS_FAILED);
                response.setRepMsg(RespMsg.CONFIRM_STORAGE_PLAN_EXCEPTION_MSG);
            } finally {
                logger.info("传入参数：{}" + str.toString());
            }

            return response;
        }
    }

    @RequestMapping("/record")
    @ResponseBody
    @LoggerManage(description = "查询入库记录")
    public BaseResponseModel<Object> queryStoragePlanRecord(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        String str = request.getReqData().toString();
        try {
            Integer pageNum = request.getReqData().getInteger("pageNum");
            Integer pageRows = request.getReqData().getInteger("pageRows");

            StorageRecordEntity entity = JSON.parseObject(str, StorageRecordEntity.class);

            // 获取用户ID
            String retUserId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(retUserId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }
            String userId = retUserId;

            // 根据用户ID和所属组织类型查询所属客户ID
            Integer customerId = userService.queryCustomerId(Integer.valueOf(userId), 2);

            Map<String, Object> map = new HashMap<String, Object>();

            // 登录用户存在时
            if (customerId != null) {

                // 只查“已入库”
                entity.setStatus(1);

                // 登录用户为管理员时
                if (customerId != -1 && customerId != 0) {

                    // 获取某个仓库ID及其所有的下属仓库ID
                    List<Integer> idList = warehouseService.selectAllLevelIdByParentId(customerId);
                    entity.setIdList(idList);
                } else {

                    // 登录用户为普通用户时只能查看自己创建的数据
                    if (customerId == -1) {
                        entity.setCreateUser(Integer.valueOf(userId));
                    }
                }

                map = storageService.queryStorageRecords(entity, pageNum, pageRows);
                // 保存最后一次查询时的条件，用于excel导出
                // VIN码
                map.put("lastSearchVin", entity.getVin());
                // 入库开始时间
                map.put("lastSearchStorageStartTime", entity.getStorageStartTime());
                // 入库结束时间
                map.put("lastSearchStorageEndTime", entity.getStorageEndTime());
                // 客户名
                map.put("lastSearchCustomer", entity.getCustomer());
                // 仓库名
                map.put("lastSearchWarehouseName", entity.getWarehouseName());
            }
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.QUERY_STORAGE_RECORD_FAILED);
            response.setRepMsg(RespMsg.QUERY_STORAGE_RECORDS_EXCEPTION_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }

        return response;
    }

    @RequestMapping(value = "/addInnstorageByExcel")
    @LoggerManage(description = "批量导入入库计划")
    public void addInnstorageByExcel(MultipartHttpServletRequest multiReq) throws Exception {
        String str = null;
        try {
            String userId = multiReq.getParameter("userId");
            multiReq.setCharacterEncoding("UTF-8");

            // 拿到上传的文件名称
            String uploadFileName = multiReq.getFile("filePath").getOriginalFilename();

            // 拿到上传的文件流
            InputStream input = multiReq.getFile("filePath").getInputStream();
            String[] colName = { "vin", "brand", "series", "model", "location", "color", "licenseNumber", "registrationNumber", "rownum" };

            List<Map<String, String>> dataList = NsExcelReadXUtils.getLists(input, uploadFileName, colName);

            for (int i = 0; i < dataList.size(); i++) {
                Map<String, String> map = dataList.get(i);
                String vin = map.get("vin");

                // 当前行号
                String num = map.get("rownum");

                String message = "";
                if (StringUtil.isEmpty(vin, true)) {
                    message = "VIN码必填项";
                    map.put("message", message);
                    continue;
                }

                // 输入的VIN码不足17位或者输入的VIN码为英文/数字以外的字符
                if (vin.length() != 17 || !vin.matches("^[A-Za-z0-9]+$")) {
                    message = "VIN码信息错误";
                    map.put("message", message);
                    continue;
                }

                // 重复的行号VIN码
                List<String> lineNum = new ArrayList<>();

                // 判断是否有重复的VIN码
                for (Map<String, String> childMap : dataList) {

                    String childNum = childMap.get("rownum");
                    String childVin = childMap.get("vin");

                    // 排除自身且VIN码重复时
                    if (!num.equals(childNum) && vin.equals(childVin)) {
                        lineNum.add(childNum);
                    }
                }

                if (lineNum.size() > 0) {
                    message = "VIN码与当前计划编号：" + lineNum.toString() + "重复";
                    map.put("message", message);
                    continue;
                }

                // 库位号
                String location = map.get("location");

                if (StringUtil.isEmpty(location, true)) {
                    message = "库位号必填项";
                    map.put("message", message);
                    continue;
                }

                String reg = "[\\u4e00-\\u9fa5]";
                Pattern pat = Pattern.compile(reg);
                Matcher mat = pat.matcher(location);
                // 校验库位号格式
                if (mat.find()) {
                    message = "库位号格式错误，仅支持英文、数字、符号字符";
                    map.put("message", message);
                    continue;
                }

                // 重复的行号(库位号)
                List<String> lineNumLocation = new ArrayList<>();

                // 判断是否有重复的库位号
                for (Map<String, String> childMap : dataList) {

                    String childNum = childMap.get("rownum");
                    String childLocation = childMap.get("location");

                    // 排除自身且库位号重复时
                    if (!num.equals(childNum) && location.equals(childLocation)) {
                        lineNumLocation.add(childNum);
                    }
                }

                if (lineNumLocation.size() > 0) {
                    message = "库位号与当前计划编号：" + lineNumLocation.toString() + "重复";
                    map.put("message", message);
                    continue;
                }

                map.put("message", message);
            }
            storageService.importOperate(dataList, userId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logger.info("传入参数：{}" + str);
        }
    }

    @RequestMapping(value = "/searchTemplateVehicle")
    @ResponseBody
    @LoggerManage(description = "查询导入入库计划")
    public BaseResponseModel<Object> searchTemplateVehicle(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        try {

            // 获取用户ID
            String retUserId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(retUserId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }
            String userId = retUserId;
            Map<String, Object> map = new HashMap<String, Object>();
            List<TempVehicleEntity> list = storageService.searchTemplateVehicle(userId);
            // 是否有错误信息
            String isSuccess = "1";
            for (TempVehicleEntity entity : list) {

                if (!StringUtil.isEmpty(entity.getComment(), true)) {
                    isSuccess = "0";
                    break;
                }
            }
            map.put("isSuccess", isSuccess);
            map.put("rows", list);
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.QUERY_STORAGE_RECORD_FAILED);
            response.setRepMsg(RespMsg.QUERY_STORAGE_RECORDS_EXCEPTION_MSG);
        }

        return response;
    }

    @RequestMapping(value = "/deleteTemplateVehicle")
    @ResponseBody
    @LoggerManage(description = "删除当前用户以前导入的临时数据")
    public BaseResponseModel<Object> deleteTemplateVehicle(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        try {

            // 获取用户ID
            String retUserId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(retUserId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }
            storageService.deleteTemplateVehicle(retUserId);
            response.setRepData(true);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.QUERY_STORAGE_RECORD_FAILED);
            response.setRepMsg(RespMsg.QUERY_STORAGE_RECORDS_EXCEPTION_MSG);
        }
        return response;
    }

    @RequestMapping("/exportInfoByExcel")
    @LoggerManage(description = "导出入库记录信息")
    public void exportInfoByExcel(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
        try {

            Map<String, Object> map = new HashMap<String, Object>();

            String userId = servletRequest.getParameter("userId");

            // 根据用户ID和所属组织类型查询所属客户ID
            Integer customerId = userService.queryCustomerId(Integer.valueOf(userId), 2);

            List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
            // 初始化Excel标题，检索数据为空时显示
            Map<String, Object> initMap = new HashMap<String, Object>();
            initMap.put("VIN码", "");
            initMap.put("品牌", "");
            initMap.put("车系", "");
            initMap.put("车型", "");
            initMap.put("颜色", "");
            initMap.put("入库时间", "");
            initMap.put("客户", "");
            initMap.put("仓库", "");
            initMap.put("库位号", "");
            initMap.put("操作人", "");
            listMap.add(initMap);

            // 登录用户存在时
            if (customerId != null) {

                // VIN码
                map.put("vin", servletRequest.getParameter("lastSearchVin"));
                // 入库开始时间
                map.put("storageStartTime", servletRequest.getParameter("lastSearchStorageStartTime"));
                // 入库结束时间
                map.put("storageEndTime", servletRequest.getParameter("lastSearchStorageEndTime"));
                // 客户名
                map.put("customer", servletRequest.getParameter("lastSearchCustomer"));
                // 仓库名
                map.put("warehouseName", servletRequest.getParameter("lastSearchWarehouseName"));
                // 只查“已入库”
                map.put("status", 1);

                // 登录用户为管理员时
                if (customerId != -1 && customerId != 0) {

                    // 获取某个仓库ID及其所有的下属仓库ID
                    List<Integer> idList = warehouseService.selectAllLevelIdByParentId(customerId);
                    map.put("idList", idList);
                } else {

                    // 登录用户为普通用户时只能查看自己创建的数据
                    if (customerId == -1) {
                        map.put("createUser", Integer.valueOf(userId));
                    }

                }
                List<Map<String, Object>> listMap1 = storageService.queryStorageRecordsForExportData(map);
                // 如果查询到结果集则覆盖初始值
                if (listMap1 != null && listMap1.size() > 0) {
                    listMap = listMap1;
                }
            }
            NsExcelReadXUtils.exportDatas("入库记录", "入库记录", listMap, servletRequest, servletResponse, "");

        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }

    @RequestMapping("/importTemplateDownload")
    @LoggerManage(description = "导入入库模板下载")
    public void importTemplateDownload(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
        try {
            LinkedList<Map<String, Object>> listMap = new LinkedList<Map<String, Object>>();
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("VIN码", "");
            map.put("品牌", "");
            map.put("车系", "");
            map.put("车型", "");
            map.put("库位号", "");
            map.put("颜色", "");
            map.put("车牌", "");
            map.put("行驶证", "");
            listMap.add(map);
            NsExcelReadXUtils.exportDatas("计划入库", "计划入库", listMap, servletRequest, servletResponse, "");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }

    @RequestMapping("/exportStoragePlanByExcel")
    @LoggerManage(description = "导出入库计划单信息")
    public void exportStoragePlanByExcel(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
        try {

            Map<String, Object> map = new HashMap<String, Object>();

            String userId = servletRequest.getParameter("userId");

            // 根据用户ID和所属组织类型查询所属客户ID
            Integer customerId = userService.queryCustomerId(Integer.valueOf(userId), 2);

            List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
            // 初始化Excel标题，检索数据为空时显示
            Map<String, Object> initMap = new HashMap<String, Object>();
            initMap.put("入库计划单号", "");
            initMap.put("创建计划时间", "");
            initMap.put("客户名称", "");
            initMap.put("仓库名", "");
            initMap.put("操作人", "");
            initMap.put("车辆数", "");
            initMap.put("状态", "");
            listMap.add(initMap);

            // 登录用户存在时
            if (customerId != null) {

                // 保存最后一次查询时的条件，用于excel导出
                // 入库计划单号
                map.put("no", servletRequest.getParameter("lastSearchNo"));
                // 入库开始时间
                map.put("createStartTime", servletRequest.getParameter("lastSearchCreateStartTime"));
                // 入库结束时间
                map.put("createEndTime", servletRequest.getParameter("lastSearchCreateEndTime"));
                // 客户名
                map.put("customer", servletRequest.getParameter("lastSearchCustomer"));
                // 仓库名
                map.put("warehouseName", servletRequest.getParameter("lastSearchWarehouseName"));
                // 只查“已入库”
                map.put("statusSearch", servletRequest.getParameter("lastSearchStatusSearch"));

                // 登录用户为管理员时
                if (customerId != -1 && customerId != 0) {

                    // 获取某个仓库ID及其所有的下属仓库ID
                    List<Integer> idList = warehouseService.selectAllLevelIdByParentId(customerId);
                    map.put("idList", idList);
                } else {

                    // 登录用户为普通用户时只能查看自己创建的数据
                    if (customerId == -1) {
                        map.put("createUser", Integer.valueOf(userId));
                    }

                }
                List<Map<String, Object>> listMap1 = storageService.exportStoragePlanByExcel(map);
                // 如果查询到结果集则覆盖初始值
                if (listMap1 != null && listMap1.size() > 0) {
                    listMap = listMap1;
                }
            }
            NsExcelReadXUtils.exportDatas("入库计划单信息", "入库计划单", listMap, servletRequest, servletResponse, "");

        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }

    @RequestMapping("/addInnerStoragePlanAndConfirm")
    @ResponseBody
    @LoggerManage(description = "创建入库计划及确定入库")
    public BaseResponseModel<Object> addInnerStoragePlanAndConfirm(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        String str = request.getReqData().toString();
        try {
            response = storageService.addInnerStoragePlanAndConfirm(request);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.CREATE_STORAGE_PLAN_FAILED);
            response.setRepMsg(RespMsg.CREATE_STORAGE_PLAN_EXCEPTION_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }
        return response;
    }

}
