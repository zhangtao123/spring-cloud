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
import com.anji.allways.business.service.WarehouseService;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;

/**
 * @author 李军
 * @version $Id: DealerWarehouse.java, v 0.1 2017年8月29日 下午2:04:34 李军 Exp $
 */
@Controller
@RequestMapping("/dealer")
public class DealerWarehouseController {
    private Logger           logger = LogManager.getLogger(DealerIndexController.class);

    @Autowired
    private WarehouseService warehouseService;

    @RequestMapping("/my/warehouse")
    @ResponseBody
    @LoggerManage(description = "经销商我的仓库信息")
    public BaseResponseModel<Object> getMyWarehouse(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();

        try {
            Map<String, Object> mapVue = new HashMap<String, Object>();
            mapVue.put("userId", Integer.valueOf(request.getUserId()));
            mapVue.put("currentPage", request.getReqData().getInteger("currentPage"));
            mapVue.put("pageNumber", request.getReqData().getInteger("pageNumber"));

            Map<String, Object> map = warehouseService.getMyWarehouseService(mapVue);

            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            logger.info(e);
            response.setRepCode(RespCode.GET_MY_WAREHOUSE);
            response.setRepMsg(RespMsg.GET_MY_WAREHOUSE);
        }
        return response;
    }
}
