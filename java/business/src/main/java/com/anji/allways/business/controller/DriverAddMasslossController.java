/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.controller;

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
import com.anji.allways.business.service.AddMasslossService;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;

/**
 * @author Administrator
 * @version $Id: DriverAddMassloss.java, v 0.1 2017年9月15日 上午10:47:42 Administrator Exp $
 */
@Controller
@RequestMapping("/driver")
public class DriverAddMasslossController {
    private Logger             logger = LogManager.getLogger(DealerIndexController.class);

    @Autowired
    private AddMasslossService addMasslossService;

    @RequestMapping("/index/addMassloss")
    @ResponseBody
    @LoggerManage(description = "司机登记质损")
    public BaseResponseModel<Object> addMassloss(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("deliveryId", request.getReqData().getInteger("deliveryId"));
            map.put("vehicleId", request.getReqData().getInteger("vehicleId"));
            map.put("orderId", request.getReqData().getInteger("orderId"));
            map.put("dutyOfficer", request.getReqData().getString("dutyOfficer"));
            map.put("damageDetail", request.getReqData().getString("damageDetail"));
            map.put("userId", Integer.valueOf(request.getUserId()));
            map.put("path", request.getReqData().getString("path"));

            // 请求返回参数容器
            Integer mapVue = addMasslossService.addMassloss(map);

            if (mapVue > 0) {
                response.setRepData(true);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else {
                response.setRepData(false);
                response.setRepCode(RespCode.ADD_DRIVER_DAMAGE);
                response.setRepMsg(RespMsg.ADD_DRIVER_DAMAGE);
            }
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.ADD_MASSLOSS);
            response.setRepMsg(RespMsg.ADD_MASSLOSS);
        }

        return response;
    }

    @RequestMapping("/index/toUpdateMassloss")
    @ResponseBody
    @LoggerManage(description = "司机查询质损")
    public BaseResponseModel<Object> toUpdateMassloss(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Integer damageId = request.getReqData().getInteger("damageId");

            // 请求返回参数容器
            Map<String, Object> mapVue = addMasslossService.toUpdateMassloss(damageId);

            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            response.setRepData(mapVue);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.ADD_MASSLOSS);
            response.setRepMsg(RespMsg.ADD_MASSLOSS);
        }

        return response;
    }

    @RequestMapping("/index/updateMassloss")
    @ResponseBody
    @LoggerManage(description = "司机更新质损")
    public BaseResponseModel<Object> updateMassloss(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("type", request.getReqData().getInteger("type"));
            map.put("vehicleId", request.getReqData().getInteger("vehicleId"));
            map.put("damageId", request.getReqData().getInteger("damageId"));
            map.put("pathId", request.getReqData().getString("pathId"));
            map.put("dutyOfficer", request.getReqData().getString("dutyOfficer"));
            map.put("damageDetail", request.getReqData().getString("damageDetail"));
            map.put("newPath", request.getReqData().getString("newPath"));
            map.put("oldPath", request.getReqData().getString("oldPath"));

            // 请求返回参数容器
            Integer count = addMasslossService.updateMassloss(map);
            if (count > 0) {
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else {
                response.setRepCode(RespCode.UPDATE_DAMAGE_RECORD_FAILED);
                response.setRepMsg(RespMsg.UPDATE_DAMAGE_STATUS);
            }
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.UPDATE_MASSLOSS);
            response.setRepMsg(RespMsg.UPDATE_MASSLOSS);
        }

        return response;
    }

    @RequestMapping("/index/selectByVehicleIdMassloss")
    @ResponseBody
    @LoggerManage(description = "司机查询车辆质损")
    public BaseResponseModel<Object> selectByVehicleIdMassloss(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        try {
            Integer vehicleId = request.getReqData().getInteger("vehicleId");

            // 请求返回参数容器
            Map<String, Object> map = addMasslossService.selectDamageByVehicleIdAndType(vehicleId);

            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.SELECT_VEHICLE_MASSLOSS);
            response.setRepMsg(RespMsg.SELECT_VEHICLE_MASSLOSS);
        }

        return response;
    }

    @RequestMapping("/index/deleteMassloss")
    @ResponseBody
    @LoggerManage(description = "司机删除质损")
    public BaseResponseModel<Object> deleteMassloss(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Integer damageId = request.getReqData().getInteger("damageId");
            Integer vehicleId = request.getReqData().getInteger("vehicleId");
            Integer type = request.getReqData().getInteger("type");

            // 请求返回参数容器
            Integer count = addMasslossService.deleteMassloss(damageId, vehicleId, type);
            if (count > 0) {
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else {
                response.setRepCode(RespCode.DELETE_DAMAGE_STATUS);
                response.setRepMsg(RespMsg.DELETE_DAMAGE_STATUS);
            }
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.DELETE_DAMAGE_STATUS);
            response.setRepMsg(RespMsg.DELETE_DAMAGE_STATUS);
        }

        return response;
    }
}
