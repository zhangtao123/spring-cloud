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
import com.anji.allways.common.util.StringUtil;

/**
 * @author zhangt
 * @version $Id: WarehouseAddMassloss.java, v 0.1 2017年9月29日 上午10:47:42 zhangt Exp $
 */
@Controller
@RequestMapping("/business/massloss")
public class WarehouseAddMasslossController {
    private Logger             logger = LogManager.getLogger(DealerIndexController.class);

    @Autowired
    private AddMasslossService addMasslossService;

    @RequestMapping("/addMassloss")
    @ResponseBody
    @LoggerManage(description = "仓库登记质损")
    public BaseResponseModel<Object> addMassloss(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        String str = null;
        try {
            str = request.getReqData().toString();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("type", request.getReqData().getString("type"));
            map.put("orderId", request.getReqData().getInteger("orderId"));
            map.put("dutyOfficer", request.getReqData().getString("dutyOfficer"));
            map.put("damageDetail", request.getReqData().getString("damageDetail"));

            // 获取用户ID
            String userId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(userId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }
            map.put("userId", Integer.valueOf(userId));
            map.put("path", request.getReqData().getString("path"));
            // 请求返回参数容器
            Map<String, Object> mapVue = addMasslossService.addMasslossWarehouse(map);
            if (mapVue == null) {
                response.setRepCode(RespCode.ADD_DAMAGE_FAILED);
                response.setRepMsg(RespMsg.ADD_DAMAGE_FAILED_MSG);
            } else if ("7".equals(mapVue.get("reCode"))) {
                response.setRepCode(RespCode.ADD_DAMAGE_FAILED_STATUS_COMPLETE);
                response.setRepMsg(RespMsg.ADD_DAMAGE_FAILED_STATUS_COMPLETE_MSG);
            } else {
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
                response.setRepData(mapVue);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.ADD_DAMAGE_FAILED);
            response.setRepMsg(RespMsg.ADD_DAMAGE_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/toUpdateMassloss")
    @ResponseBody
    @LoggerManage(description = "仓库编辑时查询质损")
    public BaseResponseModel<Object> toUpdateMassloss(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        Integer damageId = request.getReqData().getInteger("damageId");

        // 请求返回参数容器
        Map<String, Object> mapVue = addMasslossService.toUpdateMassloss(damageId);

        response.setRepCode(RespCode.SUCCESS);
        response.setRepMsg(RespMsg.SUCCESS_MSG);
        response.setRepData(mapVue);

        return response;
    }

    @RequestMapping("/updateMassloss")
    @ResponseBody
    @LoggerManage(description = "仓库更新质损")
    public BaseResponseModel<Object> updateMassloss(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        Map<String, Object> map = new HashMap<String, Object>();
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
        return response;
    }

    @RequestMapping("/selectByVehicleIdMassloss")
    @ResponseBody
    @LoggerManage(description = "仓库查询车辆质损")
    public BaseResponseModel<Object> selectByVehicleIdMassloss(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        Integer vehicleId = request.getReqData().getInteger("vehicleId");

        // 请求返回参数容器
        Map<String, Object> map = addMasslossService.selectDamageByVehicleIdAndType(vehicleId);

        response.setRepData(map);
        response.setRepCode(RespCode.SUCCESS);
        response.setRepMsg(RespMsg.SUCCESS_MSG);
        return response;
    }

    @RequestMapping("/deleteMassloss")
    @ResponseBody
    @LoggerManage(description = "仓库删除质损")
    public BaseResponseModel<Object> deleteMassloss(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        Integer damageId = request.getReqData().getInteger("damageId");

        Integer vehicleId = request.getReqData().getInteger("vehicleId");

        // 请求返回参数容器
        Integer count = addMasslossService.deleteMassloss(damageId.intValue(), vehicleId.intValue(), 1);
        if (count > 0) {
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } else {
            response.setRepCode(RespCode.DELETE_DAMAGE_STATUS);
            response.setRepMsg(RespMsg.DELETE_DAMAGE_STATUS);
        }
        return response;
    }
}
