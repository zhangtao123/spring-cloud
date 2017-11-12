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
import com.anji.allways.business.service.TransportOrderService;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.StringUtil;

/**
 * @author 李军
 * @version $Id: TransportOrderController.java, v 0.1 2017年9月4日 下午6:47:57 李军 Exp $
 */
@Controller
@RequestMapping("/dealer")
public class DealerTransportOrderController {
    private Logger                logger = LogManager.getLogger(DealerIndexController.class);

    @Autowired
    private TransportOrderService transportOrderService;

    @RequestMapping("/my/orderTransport")
    @ResponseBody
    @LoggerManage(description = "经销商获取确认订单信息")
    public BaseResponseModel<Object> getOrderTransport(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Integer userId = Integer.valueOf(request.getUserId());

            Integer vehicleId = request.getReqData().getInteger("vehicleId");

            Map<String, Object> map = transportOrderService.getOrderTransport(userId, vehicleId);

            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.GET_ORDER_TRANSPORT);
            response.setRepMsg(RespMsg.GET_ORDER_TRANSPORT);
        }

        return response;
    }

    @RequestMapping("/my/sureOrder")
    @ResponseBody
    @LoggerManage(description = "经销商下运输订单")
    public BaseResponseModel<Object> sureOrder(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Map<String, Object> mapVue = new HashMap<String, Object>();
            mapVue.put("userId", Integer.valueOf(request.getUserId()));
            mapVue.put("incomeId", request.getReqData().getInteger("incomeId"));
            mapVue.put("vehicleId", request.getReqData().getInteger("vehicleId"));
            mapVue.put("transportType", request.getReqData().getString("transportType"));
            mapVue.put("pickupSelfTime", request.getReqData().getString("pickupSelfTime"));

            int count = transportOrderService.createTransportOrder(mapVue);

            if (count > 0) {
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else if (count == -1) {
                response.setRepCode(RespCode.INSERT_ORDER_TRANSPORT);
                response.setRepMsg(RespMsg.RE_INSERT_ORDER_TRANSPORT);
            } else if (count == -2) {
                response.setRepCode(RespCode.INSERT_ORDER_TRANSPORT);
                response.setRepMsg(RespMsg.LOCK_VEHICLE_MEG);
            } else {
                response.setRepCode(RespCode.INSERT_ORDER_TRANSPORT);
                response.setRepMsg(RespMsg.INSERT_ORDER_TRANSPORT);
            }
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.INSERT_ORDER_TRANSPORT);
            response.setRepMsg(RespMsg.INSERT_ORDER_TRANSPORT);
        }

        return response;
    }

    @RequestMapping("/my/getTransportOrder")
    @ResponseBody
    @LoggerManage(description = "经销商获取运输订单")
    public BaseResponseModel<Object> getTransportOrder(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Map<String, Object> mapVue = new HashMap<String, Object>();
            mapVue.put("userId", Integer.valueOf(request.getUserId()));
            mapVue.put("currentPage", request.getReqData().getInteger("currentPage"));
            mapVue.put("pageNumber", request.getReqData().getInteger("pageNumber"));
            if (request.getReqData().getString("status").contains("4")) {
                mapVue.put("status", StringUtil.isEmptyAndSplit(request.getReqData().getString("status") + ",7,9"));
            } else {
                mapVue.put("status", StringUtil.isEmptyAndSplit(request.getReqData().getString("status")));
            }
            if (!StringUtil.isEmpty(request.getReqData().getString("vin"), true)) {
                mapVue.put("vin", "%" + request.getReqData().getString("vin") + "%");
            } else {
                mapVue.put("vin", request.getReqData().getString("vin"));
            }

            if (!StringUtil.isEmpty(request.getReqData().getString("orderNo"), true)) {
                mapVue.put("orderNo", "%" + request.getReqData().getString("orderNo") + "%");
            } else {
                mapVue.put("orderNo", request.getReqData().getString("orderNo"));
            }

            mapVue.put("warehouseId", StringUtil.isEmptyAndSplit(request.getReqData().getString("warehouseId")));

            if (!StringUtil.isEmpty(request.getReqData().getString("pickupSelfStart"), true)) {
                mapVue.put("pickupSelfStart", request.getReqData().getString("pickupSelfStart") + " 00:00:00");
            } else {
                mapVue.put("pickupSelfStart", request.getReqData().getString("pickupSelfStart"));
            }

            if (!StringUtil.isEmpty(request.getReqData().getString("pickupSelfEnd"), true)) {
                mapVue.put("pickupSelfEnd", request.getReqData().getString("pickupSelfEnd") + " 23:59:59");
            } else {
                mapVue.put("pickupSelfEnd", request.getReqData().getString("pickupSelfEnd"));
            }

            if (!StringUtil.isEmpty(request.getReqData().getString("orderStart"), true)) {
                mapVue.put("orderStart", request.getReqData().getString("orderStart") + " 00:00:00");
            } else {
                mapVue.put("orderStart", request.getReqData().getString("orderStart"));
            }

            if (!StringUtil.isEmpty(request.getReqData().getString("orderEnd"), true)) {
                mapVue.put("orderEnd", request.getReqData().getString("orderEnd") + " 23:59:59");
            } else {
                mapVue.put("orderEnd", request.getReqData().getString("orderEnd"));
            }
            String transportType = request.getReqData().getString("transportType");
            String[] split = null;
            if (!StringUtil.isEmpty(transportType, true)) {
                split = transportType.split(",");
            }
            mapVue.put("transportType", split);
            mapVue.put("type", "订单");

            Map<String, Object> map = transportOrderService.getTransportOrder(mapVue);
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.GET_TRANSPORT_ORDER);
            response.setRepMsg(RespMsg.GET_TRANSPORT_ORDER);
        }

        return response;
    }

    @RequestMapping("/my/screenTransportOrder")
    @ResponseBody
    @LoggerManage(description = "经销商获取我的库存筛选条件")
    public BaseResponseModel<Object> screenTransportOrder(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Integer userId = Integer.valueOf(request.getUserId());
            Map<String, Object> map = transportOrderService.screenTransportOrder(userId);

            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.SREEN_TRANSPORT_ORDER);
            response.setRepMsg(RespMsg.SREEN_TRANSPORT_ORDER);
        }

        return response;
    }

    @RequestMapping("/my/cancelOrder")
    @ResponseBody
    @LoggerManage(description = "经销商取消运输订单")
    public BaseResponseModel<Object> cancelOrder(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Integer vehicleId = request.getReqData().getInteger("vehicleId");
            Integer orderId = request.getReqData().getInteger("orderId");
            int count = transportOrderService.cancelOrder(vehicleId, orderId);

            if (count > 1) {
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else {
                response.setRepCode(RespCode.CANCEL_ORDER_STATUS);
                response.setRepMsg(RespMsg.CANCEL_ORDER_STATUS);
            }
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.CANCEL_ORDER);
            response.setRepMsg(RespMsg.CANCEL_ORDER);
        }

        return response;
    }

    @RequestMapping("/my/choseIncomeForward")
    @ResponseBody
    @LoggerManage(description = "经销商选择收货人")
    public BaseResponseModel<Object> choseIncomeForward(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            String userId = request.getUserId();
            Integer type = request.getReqData().getInteger("type");
            String name = request.getReqData().getString("name");
            Map<String, Object> map = transportOrderService.choseIncomeForward(userId, type, name);

            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.CANCEL_ORDER);
            response.setRepMsg(RespMsg.CANCEL_ORDER);
        }

        return response;
    }
}
