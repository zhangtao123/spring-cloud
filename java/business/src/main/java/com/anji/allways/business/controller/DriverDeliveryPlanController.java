/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.controller;

import java.util.Date;
import java.util.HashMap;
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

import com.anji.allways.base.aop.LoggerManage;
import com.anji.allways.base.entity.BaseRequestModel;
import com.anji.allways.base.entity.BaseResponseModel;
import com.anji.allways.business.dto.DeliveryPlanDTO;
import com.anji.allways.business.service.DeliveryPlanService;
import com.anji.allways.business.service.IncomeVehicleService;
import com.anji.allways.business.vo.DeliveryPlanChildVO;
import com.anji.allways.business.vo.DeliveryPlanVO;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.MD5Util;
import com.anji.allways.common.util.StringUtil;

/**
 * @author xuyuyang
 * @version $Id: BerthController.java, v 0.1 2017年8月24日 上午10:31:37 xuyuyang Exp $
 */
@Controller
@RequestMapping("/driver")
public class DriverDeliveryPlanController {
    private Logger               logger = LogManager.getLogger(DriverDeliveryPlanController.class);

    // 出库计划单Service
    @Autowired
    private DeliveryPlanService  deliveryPlanService;

    @Autowired
    private IncomeVehicleService incomeVehicleService;

    @RequestMapping("/deliveryPlan/selectInfoById")
    @ResponseBody
    @LoggerManage(description = "根据主出库计划单ID查询出库计划单明细信息")
    public BaseResponseModel<Object> selectInfoById(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            // 请求数据
            str = request.getReqData().toString();

            // 主出库计划单ID
            Integer id = request.getReqData().getInteger("id");

            Map<String, Object> retMap = new HashMap<String, Object>();

            DeliveryPlanVO vo = deliveryPlanService.selectInfoById(id);

            if (vo != null) {
                StringUtil.convertNullToEmptyForEntity(vo, retMap);
            }

            response.setRepData(retMap);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepData(false);
            response.setRepCode(RespCode.APP_DRIVER_DETAIL_FAILED);
            response.setRepMsg(RespMsg.APP_DRIVER_DETAIL_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/deliveryPlan/getConfirmTDC")
    @ResponseBody
    @LoggerManage(description = "获取身份确认二维码信息")
    public BaseResponseModel<Object> getConfirmTDC(@RequestBody BaseRequestModel request, HttpServletRequest req, HttpServletResponse res) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {

            // 请求数据
            str = request.getReqData().toString();

            String userId = request.getUserId();

            Integer consigneeUserId = request.getReqData().getInteger("consigneeUserId");

            // 加密用户ID
            String retId = MD5Util.encryptBySalt(userId + "@" + consigneeUserId.toString());

            Map<String, Object> retMap = new HashMap<>();
            retMap.put("currentId", retId);
            long ts = new Date().getTime();
            retMap.put("currentTime", String.valueOf(ts));
            response.setRepData(retMap);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepData(false);
            response.setRepCode(RespCode.APP_DRIVER_GETTDC_FAILED);
            response.setRepMsg(RespMsg.APP_DRIVER_GETTDC_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/deliveryPlan/selectInfoForConfirmArrive")
    @ResponseBody
    @LoggerManage(description = "获取确认车辆送达信息")
    public BaseResponseModel<Object> selectInfoForConfirmArrive(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            // 请求数据
            str = request.getReqData().toString();

            // 子出库计划单ID
            Integer id = request.getReqData().getInteger("deliveryPlanId");

            DeliveryPlanChildVO vo = deliveryPlanService.findChildDeliveryPlanInfo(id);

            Map<String, Object> retMap = new HashMap<String, Object>();

            if (vo != null) {
                StringUtil.convertNullToEmptyForEntity(vo, retMap);
            }

            response.setRepData(retMap);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepData(false);
            response.setRepCode(RespCode.APP_DRIVER_ARRIVEINFO_FAILED);
            response.setRepMsg(RespMsg.APP_DRIVER_ARRIVEINFO_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/deliveryPlan/confirmArrive")
    @ResponseBody
    @LoggerManage(description = "确认车辆送达")
    public BaseResponseModel<Object> confirmArrive(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            // 请求数据
            str = request.getReqData().toString();

            // 子出库计划单ID
            Integer id = request.getReqData().getInteger("deliveryPlanId");

            // 确认送达照片名称集合
            String fileNames = request.getReqData().getString("fileNames");

            // 确认送达备注
            String acceptComment = request.getReqData().getString("acceptComment");

            // 子出库计划单状态
            Integer status = request.getReqData().getInteger("status");

            // 行程状态
            Integer routingStatus = request.getReqData().getInteger("routingStatus");

            DeliveryPlanDTO entity = deliveryPlanService.selectByPrimaryKey(id);

            // 子出库计划单的状态被他人修改，则当前记录不能做更新操作
            if (entity == null || status == null || routingStatus == null || !status.equals(entity.getStatus()) || !routingStatus.equals(entity.getRoutingStatus())) {
                response.setRepData(false);
                response.setRepCode(RespCode.APP_DRIVER_STATUS_CHANGE_FAILED);
                response.setRepMsg(RespMsg.APP_DRIVER_STATUS_CHANGE_FAILED_MSG);
                return response;
            }

            String userId = request.getUserId();

            deliveryPlanService.updateForConfirmArrive(id, fileNames, userId, acceptComment);

            response.setRepData(true);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepData(false);
            response.setRepCode(RespCode.APP_DRIVER_ARRIVEED_FAILED);
            response.setRepMsg(RespMsg.APP_DRIVER_ARRIVEED_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/deliveryPlan/sureOutDeliveryPlan")
    @ResponseBody
    @LoggerManage(description = "获取确认交接 ")
    public BaseResponseModel<Object> sureOutDeliveryPlan(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();

        try {
            Integer vehicleId = request.getReqData().getInteger("vehicleId");
            Integer orderId = request.getReqData().getInteger("orderId");
            Integer deliveryId = request.getReqData().getInteger("deliveryId");
            Integer userId = Integer.valueOf(request.getUserId());
            int count = incomeVehicleService.sureOutDeliveryPlan(vehicleId, orderId, deliveryId, userId);

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

    @RequestMapping("/my/driverDeliveryPlanCount")
    @ResponseBody
    @LoggerManage(description = "司机我的任务数")
    public BaseResponseModel<Object> driverDeliveryPlanCount(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", Integer.valueOf(request.getUserId()));
            map.put("type", request.getReqData().getInteger("type"));
            int count = incomeVehicleService.driverDeliveryPlanCount(map);
            Map<String, Object> mapVue = new HashMap<String, Object>();
            mapVue.put("count", count);
            response.setRepData(mapVue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.DRIVER_DELIVERYPLAN_COUNT);
            response.setRepMsg(RespMsg.DRIVER_DELIVERYPLAN_COUNT);
        }

        return response;
    }

    @RequestMapping("/my/outWarehouseIncomeDetial")
    @ResponseBody
    @LoggerManage(description = "司机收车出库/交接/确认交接页面")
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
            response.setRepCode(RespCode.DRIVER_OUTWAREHOUSE_INCOME_DETIAL);
            response.setRepMsg(RespMsg.DRIVER_OUTWAREHOUSE_INCOME_DETIAL);
        }

        return response;
    }

    @RequestMapping("/my/sureIncomeVehicle")
    @ResponseBody
    @LoggerManage(description = "司机确认交接")
    public BaseResponseModel<Object> sureIncomeVehicle(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Integer vehicleId = request.getReqData().getInteger("vehicleId");
            Integer orderId = request.getReqData().getInteger("orderId");
            Integer deliveryId = request.getReqData().getInteger("deliveryId");
            Integer userId = Integer.valueOf(request.getUserId());
            int count = incomeVehicleService.sureDriverIncomeVehicle(vehicleId, orderId, deliveryId, userId);
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
            response.setRepCode(RespCode.DRIVER_OUTWAREHOUSE_INCOME_DETIAL);
            response.setRepMsg(RespMsg.DRIVER_OUTWAREHOUSE_INCOME_DETIAL);
        }

        return response;
    }

}
