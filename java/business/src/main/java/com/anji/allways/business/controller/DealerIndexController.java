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
import com.anji.allways.business.service.DealerIndexService;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;

/**
 * 经销商首页接口
 * @author 李军
 * @version $Id: DealerIndexController.java, v 0.1 2017年8月24日 下午3:09:49 李军 Exp $
 */
@Controller
@RequestMapping("/dealer")
public class DealerIndexController {
    private Logger             logger = LogManager.getLogger(DealerIndexController.class);

    @Autowired
    private DealerIndexService dealerIndexService;

    @RequestMapping("/index/home")
    @ResponseBody
    @LoggerManage(description = "经销商首页轮播图")
    public BaseResponseModel<Object> home(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            String userId = request.getUserId();
            // 请求返回参数容器
            Map<String, Object> map = dealerIndexService.getCustomerAbbreviation(userId);

            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            response.setRepData(map);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.INDEX_HOME);
            response.setRepMsg(RespMsg.INDEX_HOME);
        }

        return response;
    }

    @RequestMapping("/index/checkEditionInformation")
    @ResponseBody
    @LoggerManage(description = "经销商检查版本信息")
    public BaseResponseModel<Object> checkEditionInformation(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Integer type = request.getReqData().getInteger("type");
            // 请求返回参数容器
            Map<String, Object> map = dealerIndexService.checkEditionInformation(type);

            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.CHECK_EDITION_INFORMATION);
            response.setRepMsg(RespMsg.CHECK_EDITION_INFORMATION);
        }

        return response;
    }

    @RequestMapping("/index/getVehicleCount")
    @ResponseBody
    @LoggerManage(description = "经销商首页数据获取")
    public BaseResponseModel<Object> getVehicleCount(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            String userId = request.getUserId();
            // 请求返回参数容器
            Map<String, Object> map = dealerIndexService.getVehicleCount(userId);

            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            response.setRepData(map);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.INDEX_HOME);
            response.setRepMsg(RespMsg.INDEX_HOME);
        }

        return response;
    }

}
