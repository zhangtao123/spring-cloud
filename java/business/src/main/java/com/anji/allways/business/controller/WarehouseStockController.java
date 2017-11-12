/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.anji.allways.base.aop.LoggerManage;
import com.anji.allways.base.entity.BaseRequestModel;
import com.anji.allways.base.entity.BaseResponseModel;
import com.anji.allways.business.entity.VehicleEntity;
import com.anji.allways.business.enums.RowDictCodeE;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.service.UserService;
import com.anji.allways.business.service.VehicleDetailService;
import com.anji.allways.business.service.WarehouseService;
import com.anji.allways.business.service.WarehouseStockService;
import com.anji.allways.business.vo.DictVO;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.NsExcelReadXUtils;
import com.anji.allways.common.util.StringUtil;

/**
 * @author wangyanjun
 * @version $Id: WarehouseStockController.java, v 0.1 2017年9月13日 下午2:13:23 wangyanjun Exp $
 */
@Controller
@RequestMapping("/business/warehouse/stock")
public class WarehouseStockController {

    private Logger                logger = LogManager.getLogger(WarehouseStockController.class);

    @Autowired
    private VehicleDetailService  vehicleDetailService;

    @Autowired
    private WarehouseStockService warehouseStockService;

    @Autowired
    private WarehouseService      warehouseService;

    // 获取数据字典Service
    @Autowired
    private DictService           dictService;

    @Autowired
    private UserService           userService;

    @RequestMapping("/list")
    @ResponseBody
    @LoggerManage(description = "库存管理分页查询")
    public BaseResponseModel<Object> storageManagePageInfo(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        String str = request.getReqData().toString();
        try {
            Integer pageNum = request.getReqData().getInteger("pageNum");
            Integer pageRows = request.getReqData().getInteger("pageRows");
            VehicleEntity entity = JSON.parseObject(str, VehicleEntity.class);

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

                // 车辆状态
                String vehicleStatus = request.getReqData().getString("vehicleStatus");
                entity.setVehicleStatusSearch(vehicleStatus);

                // 设置质量状态名称
                String qualityStatus = request.getReqData().getString("qualityStatus");
                entity.setQualityStatusSearch(qualityStatus);

                // 设置锁定状态名称
                String lockStatus = request.getReqData().getString("lockStatus");
                entity.setLockStatusSearch(lockStatus);

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
                map = warehouseStockService.queryStockVehicles(entity, pageNum, pageRows);

                // 保存最后一次查询时的条件，用于excel导出
                // VIN码
                map.put("lastSearchVin", StringUtil.convertNullToEmpty(entity.getVin()));
                // 客户名
                map.put("lastSearchCustomer", StringUtil.convertNullToEmpty(entity.getCustomer()));
                // 客户id
                map.put("lastSearchCustomerId", StringUtil.convertNullToEmpty(entity.getCustomerId()));
                // 仓库名
                map.put("lastSearchWarehouseName", StringUtil.convertNullToEmpty(entity.getWarehouseName()));
                // 品牌
                map.put("lastSearchBrand", StringUtil.convertNullToEmpty(entity.getBrand()));
                // 车系
                map.put("lastSearchSeries", StringUtil.convertNullToEmpty(entity.getSeries()));
                // 车型
                map.put("lastSearchModel", StringUtil.convertNullToEmpty(entity.getModel()));
                // 年份
                map.put("lastSearchAnnounceYear", StringUtil.convertNullToEmpty(entity.getAnnounceYear()));
                // 颜色
                map.put("lastSearchStandardColorId", StringUtil.convertNullToEmpty(entity.getStandardColorId()));
                // 车辆状态
                map.put("lastSearchVehicleStatus", StringUtil.convertNullToEmpty(vehicleStatus));
                // 质量状态
                map.put("lastSearchQualityStatus", StringUtil.convertNullToEmpty(qualityStatus));
                // 锁定状态
                map.put("lastSearchLockStatus", StringUtil.convertNullToEmpty(lockStatus));
                // 入库开始时间
                map.put("lastSearchStorageStartTime", StringUtil.convertNullToEmpty(entity.getStorageStartTime()));
                // 入库结束时间
                map.put("lastSearchStorageEndTime", StringUtil.convertNullToEmpty(entity.getStorageEndTime()));
                // 出库开始时间
                map.put("lastSearchDeliveryStartTime", StringUtil.convertNullToEmpty(entity.getDeliveryStartTime()));
                // 出库结束时间
                map.put("lastSearchDeliveryEndTime", StringUtil.convertNullToEmpty(entity.getDeliveryEndTime()));
                // 收车开始时间
                map.put("lastSearchReceivedStartTime", StringUtil.convertNullToEmpty(entity.getReceivedStartTime()));
                // 收车结束时间
                map.put("lastSearchReceivedEndTime", StringUtil.convertNullToEmpty(entity.getReceivedEndTime()));
            }
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.STORAGE_MANAGE_FAILED);
            response.setRepMsg(RespMsg.STORAGE_MANAGE_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }

        return response;
    }

    @RequestMapping("/detail")
    @ResponseBody
    @LoggerManage(description = "库存车辆详情信息")
    public BaseResponseModel<Object> vehicleDetail(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        Integer vehicleId = request.getReqData().getInteger("vehicleId");
        Map<String, Object> map = vehicleDetailService.carDetail(vehicleId);

        response.setRepData(map);
        response.setRepCode(RespCode.SUCCESS);
        response.setRepMsg(RespMsg.SUCCESS_MSG);
        return response;
    }

    @RequestMapping("/queryColorForDict")
    @ResponseBody
    @LoggerManage(description = "获取颜色下拉列表")
    public BaseResponseModel<Object> queryColorForDict(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        try {

            // 获取颜色下拉列表
            List<DictVO> retValue = dictService.queryAllByCodeForColor(RowDictCodeE.STANDARD_COLOR.getValue());
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.COLOR_FOR_DICT_FAILED);
            response.setRepMsg(RespMsg.COLOR_FOR_DICT_FAILED_MSG);
        }
        return response;
    }

    @RequestMapping("/queryCarStatusForDict")
    @ResponseBody
    @LoggerManage(description = "获取车辆状态下拉列表")
    public BaseResponseModel<Object> queryCarStatusForDict(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        try {

            // 获取车辆状态下拉列表
            List<DictVO> retValue = dictService.queryAllByCodeForPage(RowDictCodeE.STORAGE_CAR_STATUS.getValue());
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.CAR_STATUS_FOR_DICT_FAILED);
            response.setRepMsg(RespMsg.CAR_STATUS_FOR_DICT_FAILED_MSG);
        }
        return response;
    }

    @RequestMapping("/queryQualityStatusForDict")
    @ResponseBody
    @LoggerManage(description = "获取质量状态下拉列表")
    public BaseResponseModel<Object> queryQualityStatusForDict(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        try {

            // 获取质量状态下拉列表
            List<DictVO> retValue = dictService.queryAllByCodeForPage(RowDictCodeE.QUALITY_STATUS.getValue());
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.CAR_STATUS_FOR_DICT_FAILED);
            response.setRepMsg(RespMsg.CAR_STATUS_FOR_DICT_FAILED_MSG);
        }
        return response;
    }

    @RequestMapping("/queryLockStatusForDict")
    @ResponseBody
    @LoggerManage(description = "获取锁定状态下拉列表")
    public BaseResponseModel<Object> queryLockStatusForDict(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        try {

            // 获取锁定状态下拉列表
            List<DictVO> retValue = dictService.queryAllByCodeForPage(RowDictCodeE.LOCK_STATUS.getValue());
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.LOCK_STATUS_FOR_DICT_FAILED);
            response.setRepMsg(RespMsg.LOCK_STATUS_FOR_DICT_FAILED_MSG);
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

    @RequestMapping("/exportInfoByExcel")
    @LoggerManage(description = "导出库存信息")
    public void exportInfoByExcel(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
        try {

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
            initMap.put("仓库", "");
            initMap.put("客户名称", "");
            initMap.put("库位号", "");
            initMap.put("入库时间", "");
            initMap.put("出库时间", "");
            initMap.put("收车时间", "");
            initMap.put("车辆状态", "");
            initMap.put("锁定状态", "");
            initMap.put("质量状态", "");
            listMap.add(initMap);

            Map<String, Object> map = new HashMap<String, Object>();
            // 登录用户存在时
            if (customerId != null) {

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

                // VIN码
                map.put("vin", servletRequest.getParameter("lastSearchVin"));
                // 客户名
                map.put("customer", servletRequest.getParameter("lastSearchCustomer"));
                // 客户id
                map.put("customerId", servletRequest.getParameter("lastSearchCustomerId"));
                // 仓库名
                map.put("warehouseName", servletRequest.getParameter("lastSearchWarehouseName"));
                // 品牌
                map.put("brand", servletRequest.getParameter("lastSearchBrand"));
                // 车系
                map.put("series", servletRequest.getParameter("lastSearchSeries"));
                // 车型
                map.put("model", servletRequest.getParameter("lastSearchModel"));
                // 年份
                map.put("announceYear", servletRequest.getParameter("lastSearchAnnounceYear"));
                // 颜色
                map.put("standardColorId", servletRequest.getParameter("lastSearchStandardColorId"));
                // 车辆状态
                map.put("vehicleStatusSearch", servletRequest.getParameter("lastSearchVehicleStatus"));
                // 质量状态
                map.put("qualityStatusSearch", servletRequest.getParameter("lastSearchQualityStatus"));
                // 锁定状态
                map.put("lockStatusSearch", servletRequest.getParameter("lastSearchLockStatus"));
                // 入库开始时间
                map.put("storageStartTime", servletRequest.getParameter("lastSearchStorageStartTime"));
                // 入库结束时间
                map.put("storageEndTime", servletRequest.getParameter("lastSearchStorageEndTime"));
                // 出库开始时间
                map.put("deliveryStartTime", servletRequest.getParameter("lastSearchDeliveryStartTime"));
                // 出库结束时间
                map.put("deliveryEndTime", servletRequest.getParameter("lastSearchDeliveryEndTime"));
                // 收车开始时间
                map.put("receivedStartTime", servletRequest.getParameter("lastSearchReceivedStartTime"));
                // 收车结束时间
                map.put("receivedEndTime", servletRequest.getParameter("lastSearchReceivedEndTime"));

                List<Map<String, Object>> listMap1 = warehouseStockService.queryStorageRecordsForExportData(map);
                // 如果查询到结果集则覆盖初始值
                if (listMap1 != null && listMap1.size() > 0) {
                    listMap = listMap1;
                }
            }
            NsExcelReadXUtils.exportDatas("库存信息", "库存信息", listMap, servletRequest, servletResponse, "");

        } catch (RuntimeException e1) {
            e1.printStackTrace();
            logger.info(e1.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }
}
