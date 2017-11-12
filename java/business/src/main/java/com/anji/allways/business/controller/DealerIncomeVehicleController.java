/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.controller;

import java.util.Date;
import java.util.HashMap;
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
import com.anji.allways.business.service.IncomeVehicleService;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.DateUtil;

/**
 * @author Administrator
 * @version $Id: DealerIncomeVehicleController.java, v 0.1 2017年9月9日 下午4:15:43 Administrator Exp $
 */
@Controller
@RequestMapping("/dealer")
public class DealerIncomeVehicleController {
    private Logger               logger = LogManager.getLogger(DealerIndexController.class);

    @Autowired
    private IncomeVehicleService incomeVehicleService;

    @RequestMapping("/my/incomeVehicle")
    @ResponseBody
    @LoggerManage(description = "经销商收车查询")
    public BaseResponseModel<Object> incomeVehicle(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", Integer.valueOf(request.getUserId()));
            map.put("transportType", request.getReqData().getString("transportType"));
            map.put("currentPage", request.getReqData().getInteger("currentPage"));
            map.put("pageNumber", request.getReqData().getInteger("pageNumber"));
            map.put("type", request.getReqData().getInteger("type"));
            Map<String, Object> maps = incomeVehicleService.incomeVehicle(map);
            response.setRepData(maps);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.DEALER_INCOME_ERROR);
            response.setRepMsg(RespMsg.DEALER_INCOME_ERROR);
        }

        return response;
    }

    @RequestMapping("/my/incomeDetialForward")
    @ResponseBody
    @LoggerManage(description = "经销商收车查询详情")
    public BaseResponseModel<Object> incomeDetialForward(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Integer deliveryId = request.getReqData().getInteger("deliveryId");
            String userId = request.getUserId();
            Map<String, Object> map = incomeVehicleService.incomeDetialForward(deliveryId, userId);

            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.INCOME_DETIAL_FORWARD);
            response.setRepMsg(RespMsg.INCOME_DETIAL_FORWARD);
        }

        return response;
    }

    @RequestMapping("/my/outWarehouseIncomeDetial")
    @ResponseBody
    @LoggerManage(description = "经销商收车出库/交接/确认交接页面")
    public BaseResponseModel<Object> outWarehouseIncomeDetial(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Integer vehicleId = request.getReqData().getInteger("vehicleId");
            Integer orderId = request.getReqData().getInteger("orderId");
            Map<String, Object> map = incomeVehicleService.outWarehouseIncomeDetial(vehicleId, orderId);
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.OUTWAREHOUSE_INCOME_DETIAL);
            response.setRepMsg(RespMsg.OUTWAREHOUSE_INCOME_DETIAL);
        }

        return response;
    }

    @RequestMapping("/my/distributionDriver")
    @ResponseBody
    @LoggerManage(description = "经销商查询司机信息")
    public BaseResponseModel<Object> distributionDriver(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Integer driverId = request.getReqData().getInteger("driverId");
            Map<String, Object> map = incomeVehicleService.distributionDriver(driverId);
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.OUTWAREHOUSE_INCOME_DETIAL);
            response.setRepMsg(RespMsg.OUTWAREHOUSE_INCOME_DETIAL);
        }

        return response;
    }

    @RequestMapping("/my/checkDriver")
    @ResponseBody
    @LoggerManage(description = "经销商查询司机信息")
    public BaseResponseModel<Object> checkDriver(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        String id = request.getReqData().getString("driverId");
        String userId = request.getUserId();
        Integer deliveryId = request.getReqData().getInteger("deliveryId");
        Date time = request.getReqData().getDate("time");
        if (DateUtil.getMinute(time).after(new Date())) {
            boolean falg = incomeVehicleService.checkDriver(id, deliveryId, time.toString(), userId);
            if (falg) {
                response.setRepData(falg);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else {
                response.setRepData(falg);
                response.setRepCode(RespCode.CHECK_DEALER_ID);
                response.setRepMsg(RespMsg.CHECK_DEALER_ID);
            }
        } else {
            response.setRepCode(RespCode.CHECK_DRIVER_TIME);
            response.setRepMsg(RespMsg.CHECK_DRIVER_TIME);
        }
        return response;
    }

    @RequestMapping("/my/upIdentityCard")
    @ResponseBody
    @LoggerManage(description = "经销商查询司机信息")
    public BaseResponseModel<Object> upIdentityCard(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            String path = request.getReqData().getString("path");
            Integer deliveryId = request.getReqData().getInteger("deliveryId");
            boolean falg = incomeVehicleService.upIdentityCard(path, deliveryId);
            response.setRepData(falg);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.UP_IDENTITY_CARD);
            response.setRepMsg(RespMsg.UP_IDENTITY_CARD);
        }

        return response;
    }

    @RequestMapping("/my/allIncomeVehicleCount")
    @ResponseBody
    @LoggerManage(description = "经销商查询司机信息")
    public BaseResponseModel<Object> allIncomeVehicleCount(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Integer userId = Integer.valueOf(request.getUserId());
            int count = incomeVehicleService.allIncomeVehicleCount(userId);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("count", count);
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.UP_IDENTITY_CARD);
            response.setRepMsg(RespMsg.UP_IDENTITY_CARD);
        }

        return response;
    }

    @RequestMapping("/my/sureIncomeVehicle")
    @ResponseBody
    @LoggerManage(description = "经销商查询司机信息")
    public BaseResponseModel<Object> sureIncomeVehicle(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Integer vehicleId = request.getReqData().getInteger("vehicleId");
            Integer orderId = request.getReqData().getInteger("orderId");
            Integer deliveryId = request.getReqData().getInteger("deliveryId");
            Integer userId = Integer.valueOf(request.getUserId());
            int count = incomeVehicleService.sureIncomeVehicle(vehicleId, orderId, deliveryId, userId);
            if (count > 0) {
                response.setRepData(true);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else {
                response.setRepData(false);
                response.setRepCode(RespCode.UPDATE_INCOME_STATUS);
                response.setRepMsg(RespMsg.UPDATE_INCOME_STRUTS_MSG);
            }
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.UPDATE_INCOME_STATUS);
            response.setRepMsg(RespMsg.UPDATE_INCOME_STRUTS_MSG);
        }

        return response;
    }
}
