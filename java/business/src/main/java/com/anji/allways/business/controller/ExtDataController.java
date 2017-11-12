/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anji.allways.base.aop.LoggerManage;
import com.anji.allways.base.entity.BaseRequestModel;
import com.anji.allways.base.entity.BaseResponseModel;
import com.anji.allways.business.common.AuthenticationCenter;
import com.anji.allways.business.common.constant.SlnsConstants;
import com.anji.allways.business.entity.BrandEntity;
import com.anji.allways.business.entity.OriginalBrandEntity;
import com.anji.allways.business.service.BrandService;
import com.anji.allways.business.service.OrginalBrandDataService;
import com.anji.allways.business.service.TokenService;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.HttpTool;
import com.anji.allways.common.util.MD5Util;
import com.anji.allways.common.util.PinyinUtil;

/**
 * <pre>
 * </pre>
 * @author wangyanjun
 * @version $Id: ExtDataController.java, v 0.1 2017年9月18日 下午2:20:12 wangyanjun Exp $
 */
@Controller
@RequestMapping("/business/extdata")
public class ExtDataController {

    private Logger                  logger = LogManager.getLogger(ExtDataController.class);

    @Autowired
    private TokenService            tokenService;

    @Autowired
    private OrginalBrandDataService orginalBrandDataService;

    @Autowired
    private BrandService            brandService;

    @RequestMapping("/get")
    @ResponseBody
    @LoggerManage(description = "拉取车型外部数据")
    public BaseResponseModel<Object> extCarModels(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = AuthenticationCenter.getInstance().isLogin(request, tokenService);
        if (RespCode.AUTH_FAILED.equals(response.getRepCode())) {
            return response;
        }
        String str = request.getReqData().toString();
        try {

            final int totalNum = 22144 / 200;
            for (int i = 0; i <= totalNum; i++) {
                Long now = Calendar.getInstance().getTimeInMillis() / 1000;

                StringBuffer sb = new StringBuffer();
                sb.append(SlnsConstants.DAAS_GET_DATA_URL);
                sb.append("?appId=").append(SlnsConstants.DAAS_AUTO_APPID);
                sb.append("&restTime=").append(now);
                sb.append("&appKey=").append(SlnsConstants.DAAS_AUTO_APPKEY);
                String sign = MD5Util.encrypt(sb.toString());

                String url = sb.append("&signMess=").append(sign).append("&pageNum=").append(i + 1).append("&pageSize=200").toString();

                String result = HttpTool.getInstance().get(url, null, request.getUserId().toString());
                JSONObject object = JSONObject.parseObject(result);
                if (object.getIntValue("resultCode") == 200) {
                    JSONArray carModels = object.getJSONArray("result");
                    Iterator<Object> it = carModels.iterator();
                    while (it.hasNext()) {
                        JSONObject carModel = (JSONObject) it.next();

                        OriginalBrandEntity originalBrandEntity = new OriginalBrandEntity();
                        originalBrandEntity.setVersionname(carModel.getString("versionName"));
                        originalBrandEntity.setSubmodelname(carModel.getString("subModelName"));
                        originalBrandEntity.setBrandname(carModel.getString("brandName"));
                        originalBrandEntity.setManfname(carModel.getString("manfName"));
                        originalBrandEntity.setVehicletypename(carModel.getString("vehicleTypeName"));
                        originalBrandEntity.setManfpropname(carModel.getString("manfPropName"));
                        originalBrandEntity.setBodytypename(carModel.getString("bodyTypeNames"));
                        originalBrandEntity.setSegmentname(carModel.getString("segmentName"));
                        originalBrandEntity.setEnginetypename(carModel.getString("engineTypeName"));
                        originalBrandEntity.setTrantypename(carModel.getString("tranTypeName"));
                        originalBrandEntity.setFueltypename(carModel.getString("fuelTypeName"));
                        originalBrandEntity.setModelyear(carModel.getString("modelYear"));
                        originalBrandEntity.setLaunchdate(carModel.getString("launchDate"));
                        originalBrandEntity.setMsrp(carModel.getString("msrp"));
                        originalBrandEntity.setChangesname(carModel.getString("changesName"));
                        originalBrandEntity.setOnproductflag(carModel.getString("onProductFlag"));
                        originalBrandEntity.setOnsaleflag(carModel.getString("onSaleFlag"));
                        originalBrandEntity.setCarcolor(carModel.getString("carColor"));
                        originalBrandEntity.setCreateUser(Integer.valueOf(request.getUserId()));
                        originalBrandEntity.setCreateTime(new Date());
                        originalBrandEntity.setUpdateUser(Integer.valueOf(request.getUserId()));
                        originalBrandEntity.setUpdateTime(new Date());
                        try {
                            orginalBrandDataService.addBrand(originalBrandEntity);

                            BrandEntity brandEntity = new BrandEntity();
                            brandEntity.setBrandInitial(PinyinUtil.getFirstPinYinHeadChar(carModel.getString("brandName")));
                            brandEntity.setBrand(carModel.getString("brandName"));
                            brandEntity.setSeries(carModel.getString("subModelName"));
                            brandEntity.setModel(carModel.getString("versionName"));
                            brandEntity.setAnnounceYear(carModel.getString("modelYear"));
                            brandEntity.setCreateUser(Integer.valueOf(request.getUserId()));
                            brandEntity.setCreateTime(new Date());
                            brandEntity.setUpdateUser(Integer.valueOf(request.getUserId()));
                            brandEntity.setUpdateTime(new Date());
                            brandService.addBrand(brandEntity);
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.info(e.getMessage());
                            response.setRepCode(RespCode.GET_EXT_DATA_FAILED);
                            response.setRepMsg(RespMsg.GET_EXT_DATA_EXCEPTION_MSG);
                        }
                    }
                } else {
                    System.out.println(object.get("resultCode") + ":" + object.get("resultMsg"));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.GET_EXT_DATA_FAILED);
            response.setRepMsg(RespMsg.GET_EXT_DATA_EXCEPTION_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }

        return response;
    }

}
