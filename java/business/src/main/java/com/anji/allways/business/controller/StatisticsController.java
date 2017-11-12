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
import com.anji.allways.business.service.StatisticsService;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.DateUtil;

/**
 * @author wangyanjun
 * @version $Id: StatisticsController.java, v 0.1 2017年8月29日 下午9:56:31 wangyanjun Exp $
 */
@Controller
@RequestMapping("/business")
public class StatisticsController {

    private Logger            logger = LogManager.getLogger(StatisticsController.class);

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping("/statistics/warehousedata")
    @ResponseBody
    @LoggerManage(description = "获取某日某仓库统计信息")
    public BaseResponseModel<Object> selectByDateAndWarehouseIdAndCustomerId(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            str = request.getReqData().toString();
            String date = DateUtil.getUpOneDate();
            Integer warehouseId = request.getReqData().getInteger("warehouseId");
            Integer customerId = Integer.valueOf(request.getUserId());

            Map<String, Object> map = statisticsService.selectByDateAndWarehouseIdAndCustomerId(DateUtil.formateStringToDate(date, "yyyy-MM-dd"), warehouseId, customerId);
            if (0 == map.size()) {
                response.setRepCode(RespCode.QUERY_STATISTICS_DATA_SUCCEED);
                response.setRepMsg(RespMsg.QUERY_STATISTICS_DATA_SUCCEED_MSG);
            } else {
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            }
            response.setRepData(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.QUERY_STATISTICS_DATA_FAILED);
            response.setRepMsg(RespMsg.QUERY_STATISTICS_DATA_EXCEPTION_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }

        return response;
    }

    @RequestMapping("/statistics/selectByDateAndWarehouseId")
    @ResponseBody
    @LoggerManage(description = "获取某日某仓库统计信息")
    public BaseResponseModel<Object> selectByDateAndAndCustomerId(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            str = request.getReqData().toString();
            String date = DateUtil.getUpOneDate();
            Integer customerId = Integer.valueOf(request.getUserId());

            Map<String, Object> map = statisticsService.selectByDateAndAndCustomerId(DateUtil.formateStringToDate(date, "yyyy-MM-dd"), customerId);
            if (0 == map.size()) {
                response.setRepCode(RespCode.QUERY_STATISTICS_DATA_SUCCEED);
                response.setRepMsg(RespMsg.QUERY_STATISTICS_DATA_SUCCEED_MSG);
            } else {
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            }
            response.setRepData(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.QUERY_STATISTICS_DATA_FAILED);
            response.setRepMsg(RespMsg.QUERY_STATISTICS_DATA_EXCEPTION_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }

        return response;
    }

    @RequestMapping("/statistics/selectAllwarehouseByCustomer")
    @ResponseBody
    @LoggerManage(description = "获取某日某仓库统计信息")
    public BaseResponseModel<Object> selectAllwarehouseByCustomer(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            str = request.getReqData().toString();
            String date = DateUtil.getUpOneDate();
            Integer customerId = Integer.valueOf(request.getUserId());

            Map<String, Object> map = statisticsService.selectAllwarehouseByCustomer(DateUtil.formateStringToDate(date, "yyyy-MM-dd"), customerId);
            if (0 == map.size()) {
                response.setRepCode(RespCode.QUERY_STATISTICS_DATA_SUCCEED);
                response.setRepMsg(RespMsg.QUERY_STATISTICS_DATA_SUCCEED_MSG);
            } else {
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            }
            response.setRepData(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.QUERY_STATISTICS_DATA_FAILED);
            response.setRepMsg(RespMsg.QUERY_STATISTICS_DATA_EXCEPTION_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }

        return response;
    }

    @RequestMapping("/statistics/selectAllwarehouseByWeb")
    @ResponseBody
    @LoggerManage(description = "获取某日某仓库统计信息")
    public BaseResponseModel<Object> selectAllwarehouseByWeb(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            str = request.getReqData().toString();
            Integer userId = Integer.valueOf(request.getUserId());

            Map<String, Object> map = statisticsService.selectAllwarehouseByWeb(userId);
            if (0 == map.size()) {
                response.setRepCode(RespCode.QUERY_STATISTICS_DATA_SUCCEED);
                response.setRepMsg(RespMsg.QUERY_STATISTICS_DATA_SUCCEED_MSG);
            } else {
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            }
            response.setRepData(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.QUERY_STATISTICS_DATA_FAILED);
            response.setRepMsg(RespMsg.QUERY_STATISTICS_DATA_EXCEPTION_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }

        return response;
    }

    @RequestMapping("/statistics/selectAllwarehouseByGroup")
    @ResponseBody
    @LoggerManage(description = "获取经销商集团统计数据")
    public BaseResponseModel<Object> selectCustomerGroup(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            str = request.getReqData().toString();
            Integer userId = Integer.valueOf(request.getUserId());

            Map<String, Object> map = statisticsService.statisticalCustomerGroup(userId);
            if (0 == map.size()) {
                response.setRepCode(RespCode.QUERY_STATISTICS_DATA_SUCCEED);
                response.setRepMsg(RespMsg.QUERY_STATISTICS_DATA_SUCCEED_MSG);
            } else {
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            }
            response.setRepData(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.QUERY_STATISTICS_DATA_FAILED);
            response.setRepMsg(RespMsg.QUERY_STATISTICS_DATA_EXCEPTION_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }

        return response;
    }
}
