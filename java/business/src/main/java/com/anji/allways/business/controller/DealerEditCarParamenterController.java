/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anji.allways.base.aop.LoggerManage;
import com.anji.allways.base.entity.BaseRequestModel;
import com.anji.allways.base.entity.BaseResponseModel;
import com.anji.allways.business.entity.VehicleEntity;
import com.anji.allways.business.service.EditCarParamenterService;
import com.anji.allways.business.service.StockScreenService;
import com.anji.allways.business.service.VehicleDetailService;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;

/**
 * 编辑车辆信息
 * @author 李军
 * @version $Id: EditCarParamenter.java, v 0.1 2017年8月26日 下午1:56:43 李军 Exp $
 */
@Controller
@RequestMapping("/dealer")
public class DealerEditCarParamenterController {
    private Logger                   logger = LogManager.getLogger(DealerIndexController.class);

    @Autowired
    private EditCarParamenterService editCarParamenterService;

    @Autowired
    private VehicleDetailService     vehicleDetailService;

    @Autowired
    private StockScreenService       stockScreenService;

    @RequestMapping("/my/getEditParamenter")
    @ResponseBody
    @LoggerManage(description = "经销商车辆编辑获取信息")
    public BaseResponseModel<Object> getEditParamenter(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        String vehicleId = request.getReqData().getString("vehicleId");
        Map<String, Object> map = null;

        try {
            map = editCarParamenterService.getEditParamenter(Integer.parseInt(vehicleId));
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.SELECT_ALL_ERROR);
            response.setRepMsg(RespMsg.SELECT_ALL_MSG);
        }

        response.setRepData(map);
        return response;
    }

    @RequestMapping("/my/keepEditParamenter")
    @ResponseBody
    @LoggerManage(description = "经销商车辆编辑保存信息")
    public BaseResponseModel<Object> keepEditParamenter(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            VehicleEntity vehicleEntity = editCarParamenterService.selectByPrimaryKey(request.getReqData().getInteger("id"));
            vehicleEntity.setBrand(request.getReqData().getString("brand"));
            vehicleEntity.setSeries(request.getReqData().getString("series"));
            vehicleEntity.setModel(request.getReqData().getString("model"));
            vehicleEntity.setAnnounceYear(request.getReqData().getString("Year"));
            if (request.getReqData().getString("standardColorId") != null && !request.getReqData().getString("standardColorId").equals("")) {
                vehicleEntity.setStandardColorId(Integer.valueOf(request.getReqData().getString("standardColorId")));
            }
            vehicleEntity.setManufacturerColor(request.getReqData().getString("manufacturerColor"));
            vehicleEntity.setComment(request.getReqData().getString("comment"));
            vehicleEntity.setLicenseNumber(request.getReqData().getString("licenseNumber"));
            vehicleEntity.setRegistrationNumber(request.getReqData().getString("registrationNumber"));
            vehicleEntity.setUpdateTime(new Date());
            vehicleEntity.setUpdateUser(Integer.valueOf(request.getUserId()));

            Map<String, Object> mapVue = new HashMap<String, Object>();
            mapVue.put("brand", request.getReqData().getString("brand"));
            mapVue.put("series", request.getReqData().getString("series"));
            mapVue.put("model", request.getReqData().getString("model"));
            mapVue.put("announceYear", request.getReqData().getString("Year"));
            int ischeck = 0;
            if (request.getReqData().getString("brand") != null && !request.getReqData().getString("brand").equals("")) {
                ischeck = editCarParamenterService.checkYearExist(mapVue);
            } else {
                ischeck = 1;
                vehicleEntity.setAnnounceYear(null);
                vehicleEntity.setBrand("");
                vehicleEntity.setSeries("");
                vehicleEntity.setModel("");
            }
            if (ischeck > 0) {
                try {
                    int num = editCarParamenterService.keepEditParamenter(vehicleEntity);
                    if (num > 0) {
                        response.setRepCode(RespCode.SUCCESS);
                        response.setRepMsg(RespMsg.SUCCESS_MSG);
                    } else {
                        response.setRepCode(RespCode.UPDATE_VEHICLE_INFO);
                        response.setRepMsg(RespMsg.UPDATE_VAHICLE_INFO);
                    }
                } catch (Exception e) {
                    logger.info(e);
                    response.setRepCode(RespCode.UPDATE_VEHICLE_INFO);
                    response.setRepMsg(RespMsg.UPDATE_VAHICLE_INFO);
                }

            } else {
                response.setRepCode(RespCode.CHECK_YEAR_EXIST);
                response.setRepMsg(RespMsg.CHECK_YEAR_EXIST);
            }
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.UPDATE_VEHICLE_INFO);
            response.setRepMsg(RespMsg.UPDATE_VAHICLE_INFO);
        }

        return response;
    }

    @RequestMapping("/my/updateLockStatus")
    @ResponseBody
    @LoggerManage(description = "经销商车辆编辑锁定状态信息")
    public BaseResponseModel<Object> updateLockStatus(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Map<String, Object> mapVue = new HashMap<String, Object>();
            mapVue.put("vehicleId", request.getReqData().getInteger("vehicleId"));
            mapVue.put("lockStatus", request.getReqData().getInteger("lockStatus"));

            int count = editCarParamenterService.updateLockStruts(mapVue);

            if (count > 0) {
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else if (count == -1) {
                response.setRepCode(RespCode.UPDATE_VEHICLE_INFO);
                response.setRepMsg(RespMsg.RE_INSERT_ORDER_TRANSPORT);
            } else {
                response.setRepCode(RespCode.UPDATE_VEHICLE_INFO);
                response.setRepMsg(RespMsg.LOCK_VEHICLE_ERROR_MSG);
            }
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.UPDATE_VEHICLE_INFO);
            response.setRepMsg(RespMsg.LOCK_VEHICLE_ERROR_MSG);
        }

        return response;
    }

    @RequestMapping("/my/getByBrandToYear")
    @ResponseBody
    @LoggerManage(description = "经销商获取年份信息")
    public BaseResponseModel<Object> getByBrandToYear(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Map<String, Object> mapVue = new HashMap<String, Object>();
            mapVue.put("brand", request.getReqData().getString("brand"));
            mapVue.put("series", request.getReqData().getString("series"));
            mapVue.put("model", request.getReqData().getString("model"));

            List<String> list = editCarParamenterService.selectByBrandToYear(mapVue);

            List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
            for (String str : list) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("year", str);
                listMap.add(map);
            }
            Map<String, Object> mapreq = new HashMap<String, Object>();
            mapreq.put("allYear", listMap);
            response.setRepData(mapreq);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.GET_BRAND_TO_YEAR);
            response.setRepMsg(RespMsg.GET_BRAND_TO_YEAR);
        }

        return response;
    }

    @RequestMapping("/my/getScreenBrand")
    @ResponseBody
    @LoggerManage(description = "经销商获取车辆编辑品牌信息")
    public BaseResponseModel<Object> getScreenBrand(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Integer userId = Integer.valueOf(request.getUserId());
            Map<String, Object> map = stockScreenService.getScreenBrand(userId);

            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.GET_DEALER_BRAND);
            response.setRepMsg(RespMsg.GET_DEALER_BRAND);
        }

        return response;
    }

    @RequestMapping("/my/getByVinVehicle")
    @ResponseBody
    @LoggerManage(description = "经销商获取车辆编辑品牌信息")
    public BaseResponseModel<Object> getByVinVehicle(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            String vin = request.getReqData().getString("vin");
            int userId = Integer.parseInt(request.getUserId());
            Map<String, Object> map = vehicleDetailService.getByVinVehicle(vin, userId);

            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.GET_VEHICLE_BY_VIN);
            response.setRepMsg(RespMsg.GET_VEHICLE_BY_VIN);
        }

        return response;
    }

    @RequestMapping("/my/updateVehiclePath")
    @ResponseBody
    @LoggerManage(description = "经销商获取车辆编辑品牌信息")
    public BaseResponseModel<Object> updateVehiclePath(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            String vehicleId = request.getReqData().getString("vehicleId");
            String path = request.getReqData().getString("path");
            Map<String, Object> map = vehicleDetailService.updateVehiclePath(vehicleId, path);
            if (map.get("falg") == null || "".equals(map.get("falg"))) {
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else {
                response.setRepCode(RespCode.UPDATE_VEHICLE_PATH);
                response.setRepMsg(RespMsg.UPDATE_VEHICLE_PATH);
            }
            response.setRepData(map);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.UPDATE_VEHICLE_PATH);
            response.setRepMsg(RespMsg.UPDATE_VEHICLE_PATH_ERROR);
        }

        return response;
    }

    @RequestMapping("/my/deleteVehiclePath")
    @ResponseBody
    @LoggerManage(description = "经销商获取车辆编辑品牌信息")
    public BaseResponseModel<Object> deleteVehiclePath(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            int pathId = request.getReqData().getInteger("pathId");
            String path = request.getReqData().getString("path");
            boolean falg = vehicleDetailService.deleteVehiclePath(pathId, path);
            if (falg) {
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else {
                response.setRepCode(RespCode.UPDATE_VEHICLE_PATH);
                response.setRepMsg(RespMsg.UPDATE_VEHICLE_PATH);
            }
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.UPDATE_VEHICLE_PATH);
            response.setRepMsg(RespMsg.UPDATE_VEHICLE_PATH_ERROR);
        }

        return response;
    }
}
