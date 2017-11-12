/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.controller;

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
import com.anji.allways.business.service.VehicleDetailService;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;

/**
 * 经销商车辆详情
 * @author 李军
 * @version $Id: VehicleDetailController.java, v 0.1 2017年8月29日 上午9:06:11 李军 Exp $
 */
@Controller
@RequestMapping("/dealer")
public class DealerVehicleDetailController {
    private Logger               logger = LogManager.getLogger(DealerIndexController.class);

    @Autowired
    private VehicleDetailService vehicleDetailService;

    @RequestMapping("/my/carDetail")
    @ResponseBody
    @LoggerManage(description = "经销商车辆详情信息")
    public BaseResponseModel<Object> carDetail(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Integer vehicleId = request.getReqData().getInteger("vehicleId");
            Map<String, Object> map = vehicleDetailService.carDetail(vehicleId);

            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.VEHICLE_DETAIL);
            response.setRepMsg(RespMsg.VEHICLE_DETAIL);
        }
        return response;
    }
}
