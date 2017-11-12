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
import com.anji.allways.business.service.DriverIndexService;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;

/**
 * @author Administrator
 * @version $Id: DriverIndexController.java, v 0.1 2017年9月7日 下午8:21:28 Administrator Exp $
 */
@Controller
@RequestMapping("/driver")
public class DriverIndexController {
    private Logger             logger = LogManager.getLogger(DealerIndexController.class);

    @Autowired
    private DriverIndexService driverIndexService;

    @RequestMapping("/index/home")
    @ResponseBody
    @LoggerManage(description = "司机首页今日任务信息")
    public BaseResponseModel<Object> home(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", Integer.valueOf(request.getUserId()));
            map.put("pageNumber", request.getReqData().getInteger("pageNumber"));
            map.put("currentPage", request.getReqData().getInteger("currentPage"));
            map.put("type", request.getReqData().getInteger("type"));

            // 请求返回参数容器
            Map<String, Object> mapVue = driverIndexService.home(map);

            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            response.setRepData(mapVue);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.DRIVER_INDEX_HOME);
            response.setRepMsg(RespMsg.DRIVER_INDEX_HOME);
        }
        return response;
    }

    @RequestMapping("/index/checkEditionInformation")
    @ResponseBody
    @LoggerManage(description = "经销商检查版本")
    public BaseResponseModel<Object> checkEditionInformation(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Integer type = request.getReqData().getInteger("type");
            // 请求返回参数容器
            Map<String, Object> map = driverIndexService.checkEditionInformation(type);

            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.DRIVER_INDEX_HOME);
            response.setRepMsg(RespMsg.DRIVER_INDEX_HOME);
        }
        return response;
    }
}