/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.controller;

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
import com.anji.allways.business.common.AuthenticationCenter;
import com.anji.allways.business.entity.DamageEntity;
import com.anji.allways.business.service.DamageService;
import com.anji.allways.business.service.TokenService;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;

/**
 * <pre>
 *
 * </pre>
 * @author wangyanjun
 * @version $Id: DamageController.java, v 0.1 2017年9月14日 下午11:47:32 wangyanjun Exp $
 */
@Controller
@RequestMapping("/business/damage")
public class DamageController {

    private Logger        logger = LogManager.getLogger(DamageController.class);

    @Autowired
    private TokenService  tokenService;

    @Autowired
    private DamageService damageService;

    @RequestMapping("/add")
    @ResponseBody
    @LoggerManage(description = "增加质损记录")
    public BaseResponseModel<Object> addDamageInfo(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = AuthenticationCenter.getInstance().isLogin(request, tokenService);
        if (RespCode.AUTH_FAILED.equals(response.getRepCode())) {
            return response;
        }
        String str = request.getReqData().toString();
        try {
            String dutyOfficer = request.getReqData().getString("dutyOfficer");
            String damageDetail = request.getReqData().getString("damageDetail");
            String picturePath = request.getReqData().getString("picturePath");
            Integer pictureNum = request.getReqData().getInteger("pictureNum");
            Integer vehicleId = request.getReqData().getInteger("vehicleId");

            DamageEntity damageEntity = new DamageEntity();
            damageEntity.setDutyOfficer(dutyOfficer);
            damageEntity.setDamageDetail(damageDetail);
            damageEntity.setDamagePicturePath(picturePath);
            damageEntity.setDamagePictureNumber(pictureNum);
            damageEntity.setVehicleId(vehicleId);

            damageService.addDamageInfo(damageEntity);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.ADD_DAMAGE_RECORD_FAILED);
            response.setRepMsg(RespMsg.ADD_DAMAGE_RECORDS_EXCEPTION_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }

        return response;
    }

    @RequestMapping("/update")
    @ResponseBody
    @LoggerManage(description = "修改质损记录")
    public BaseResponseModel<Object> updateDamageInfo(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = AuthenticationCenter.getInstance().isLogin(request, tokenService);
        if (RespCode.AUTH_FAILED.equals(response.getRepCode())) {
            return response;
        }
        String str = request.getReqData().toString();
        try {
            Integer id = request.getReqData().getInteger("id");
            String dutyOfficer = request.getReqData().getString("dutyOfficer");
            String damageDetail = request.getReqData().getString("damageDetail");
            String picturePath = request.getReqData().getString("picturePath");
            Integer pictureNum = request.getReqData().getInteger("pictureNum");
            Integer vehicleId = request.getReqData().getInteger("vehicleId");

            DamageEntity damageEntity = new DamageEntity();
            damageEntity.setId(id);
            damageEntity.setDutyOfficer(dutyOfficer);
            damageEntity.setDamageDetail(damageDetail);
            damageEntity.setDamagePicturePath(picturePath);
            damageEntity.setDamagePictureNumber(pictureNum);
            damageEntity.setVehicleId(vehicleId);

            damageService.updateDamageInfo(damageEntity);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.UPDATE_DAMAGE_RECORD_FAILED);
            response.setRepMsg(RespMsg.UPDATE_DAMAGE_RECORDS_EXCEPTION_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }

        return response;
    }
}
