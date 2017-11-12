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
import com.anji.allways.business.service.StockScreenService;
import com.anji.allways.business.service.VehicleDetailService;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.StringUtil;

/**
 * 经销商搜索页
 * @author 李军
 * @version $Id: StockScreen.java, v 0.1 2017年8月28日 下午6:50:42 李军 Exp $
 */
@Controller
@RequestMapping("/dealer")
public class DealerStockScreenController {
    private Logger               logger = LogManager.getLogger(DealerIndexController.class);

    @Autowired
    private StockScreenService   stockScreenService;

    @Autowired
    private VehicleDetailService vehicleDetailService;

    @RequestMapping("/my/getScreen")
    @ResponseBody
    @LoggerManage(description = "经销商获取我的库存筛选条件")
    public BaseResponseModel<Object> getScreen(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        try {
            Map<String, Object> map = null;
            if (request.getUserId() != null) {
                Integer userId = Integer.valueOf(request.getUserId());
                map = stockScreenService.getScreen(userId);
            }
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.GET_SCREEN);
            response.setRepMsg(RespMsg.GET_SCREEN);
        }

        return response;
    }

    @RequestMapping("/my/stock")
    @ResponseBody
    @LoggerManage(description = "经销商获取我的库存条件")
    public BaseResponseModel<Object> getStockDetail(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Map<String, Object> mapVue = new HashMap<String, Object>();
            mapVue.put("userId", Integer.valueOf(request.getUserId()));
            mapVue.put("currentPage", request.getReqData().getInteger("currentPage"));
            mapVue.put("pageNumber", request.getReqData().getInteger("pageNumber"));
            if (request.getReqData().getString("vehicleVtatus").contains("4")) {
                mapVue.put("vehicleVtatus", StringUtil.isEmptyAndSplit(request.getReqData().getString("vehicleVtatus") + ",7,9"));
            } else {
                mapVue.put("vehicleVtatus", StringUtil.isEmptyAndSplit(request.getReqData().getString("vehicleVtatus")));
            }
            if (!StringUtil.isEmpty(request.getReqData().getString("vin"), true)) {
                mapVue.put("vin", "%" + request.getReqData().getString("vin") + "%");
            } else {
                mapVue.put("vin", null);
            }

            mapVue.put("brand", request.getReqData().getString("brand"));
            mapVue.put("series", request.getReqData().getString("series"));
            mapVue.put("model", request.getReqData().getString("model"));
            mapVue.put("announceYear", StringUtil.isEmptyAndSplit(request.getReqData().getString("announceYear")));
            mapVue.put("standardColorId", StringUtil.isEmptyAndSplit(request.getReqData().getString("standardColorId")));
            mapVue.put("warehouseId", StringUtil.isEmptyAndSplit(request.getReqData().getString("warehouseId")));
            mapVue.put("qualityStatus", StringUtil.isEmptyAndSplit(request.getReqData().getString("qualityStatus")));
            mapVue.put("lockStatus", StringUtil.isEmptyAndSplit(request.getReqData().getString("lockStatus")));
            if (!StringUtil.isEmpty(request.getReqData().getString("storageStart"), true)) {
                mapVue.put("storageStart", request.getReqData().getString("storageStart") + " 00:00:00");
            } else {
                mapVue.put("storageStart", null);
            }
            if (!StringUtil.isEmpty(request.getReqData().getString("storageEnd"), true)) {
                mapVue.put("storageEnd", request.getReqData().getString("storageEnd") + " 23:59:59");
            } else {
                mapVue.put("storageEnd", null);
            }
            if (!StringUtil.isEmpty(request.getReqData().getString("deliveryStart"), true)) {
                mapVue.put("deliveryStart", request.getReqData().getString("deliveryStart") + " 00:00:00");
            } else {
                mapVue.put("deliveryStart", null);
            }
            if (!StringUtil.isEmpty(request.getReqData().getString("deliveryEnd"), true)) {
                mapVue.put("deliveryEnd", request.getReqData().getString("deliveryEnd") + " 23:59:59");
            } else {
                mapVue.put("deliveryEnd", null);
            }

            Map<String, Object> map = vehicleDetailService.getStockDetail(mapVue);
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.GET_STOCK_DETAIL);
            response.setRepMsg(RespMsg.GET_SCREEN);
        }

        return response;
    }
}
