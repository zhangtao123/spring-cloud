/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.anji.allways.base.aop.LoggerManage;
import com.anji.allways.base.entity.BaseRequestModel;
import com.anji.allways.base.entity.BaseResponseModel;
import com.anji.allways.business.entity.TransportCompanyEntity;
import com.anji.allways.business.enums.RowDictCodeE;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.service.TransportCompanyService;
import com.anji.allways.business.service.UserService;
import com.anji.allways.business.vo.DictVO;
import com.anji.allways.business.vo.TransportCompanyVO;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.StringUtil;

/**
 * @author xuyuyang
 * @version $Id: BerthController.java, v 0.1 2017年8月24日 上午10:31:37 xuyuyang Exp $
 */
@Controller
@RequestMapping("/business/transportCompany")
public class TransportCompanyController {
    private Logger                  logger = LogManager.getLogger(TransportCompanyController.class);

    @Autowired
    private TransportCompanyService transportCompanyService;

    // 获取数据字典Service
    @Autowired
    private DictService             dictService;

    // 用户信息Service
    @Autowired
    private UserService             userService;

    @RequestMapping("/queryTransportCompanyInfos")
    @ResponseBody
    @LoggerManage(description = "查询运输公司信息列表")
    public BaseResponseModel<Object> queryTransportCompanyInfos(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {

            // 获取用户ID
            String retUserId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(retUserId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }
            String userId = retUserId;

            Integer customerId = userService.queryCustomerId(Integer.valueOf(userId), 2);

            Map<String, Object> map = new HashMap<String, Object>();

            // 账号存在且为超级管理员时
            if (customerId != null && customerId == 0) {

                Integer pageNum = request.getReqData().getInteger("pageNum");
                Integer pageRows = request.getReqData().getInteger("pageRows");
                str = request.getReqData().toString();
                // 获取查询条件
                TransportCompanyEntity bean = JSON.parseObject(str, TransportCompanyEntity.class);

                // 运输公司状态处理(JSON转换之后，如果为""会自动赋值为0)
                String status = request.getReqData().getString("status");
                bean.setSearchStatus(status);

                map = transportCompanyService.queryTransportCompanyInfos(bean, pageNum, pageRows);
            }
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.SEARCH_COMPANY_INFO_ERROR);
            response.setRepMsg(RespMsg.SEARCH_COMPANY_INFO_ERROR_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/queryCompanyStatusForDict")
    @ResponseBody
    @LoggerManage(description = "获取运输公司状态下拉列表")
    public BaseResponseModel<Object> queryCompanyStatusForDict(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        try {

            // 获取运输司机状态下拉列表
            List<DictVO> retValue = dictService.queryAllByCodeForPage(RowDictCodeE.COMPANY_STATUS.getValue());
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.COMPANY_STATUS_ERROR);
            response.setRepMsg(RespMsg.COMPANY_STATUS_ERROR_MSG);
        }
        return response;
    }

    @RequestMapping("/setEnableCompanyInfos")
    @ResponseBody
    @LoggerManage(description = "批量激活运输公司信息")
    public BaseResponseModel<Object> setEnableCompanyInfos(@RequestBody BaseRequestModel request) throws Exception {
        synchronized (this) {
            BaseResponseModel<Object> response = new BaseResponseModel<Object>();
            String ids = request.getReqData().getString("ids");
            try {

                // 获取用户ID
                String retUserId = StringUtil.getUserId(request.getToken());
                if (StringUtil.isEmpty(retUserId, true)) {
                    response.setRepCode(RespCode.LOGIN_FAILED);
                    response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                    return response;
                }
                String userId = retUserId;
                // 根据主键删除时，主键不能为空
                if (StringUtils.isBlank(ids)) {
                    response.setRepCode(RespCode.COMPANY_ENABLE_IDS_ERROR);
                    response.setRepMsg(RespMsg.COMPANY_ENABLE_IDS_ERROR_MSG);
                    return response;
                }

                // 批量激活或停用司机(支持单条记录操作)
                int num = transportCompanyService.batchUpdateCompany(ids, Long.parseLong(userId), 1);
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } catch (Exception e) {
                e.printStackTrace();
                response.setRepCode(RespCode.COMPANY_ENABLE_ERROR);
                response.setRepMsg(RespMsg.COMPANY_ENABLE_ERROR_MSG);
            } finally {
                logger.info("传入参数：{}" + JSON.toJSONString(ids));
            }
            return response;
        }
    }

    @RequestMapping("/setDisableCompanyInfos")
    @ResponseBody
    @LoggerManage(description = "批量停用运输公司信息")
    public BaseResponseModel<Object> setDisableCompanyInfos(@RequestBody BaseRequestModel request) throws Exception {
        synchronized (this) {
            BaseResponseModel<Object> response = new BaseResponseModel<Object>();
            String ids = request.getReqData().getString("ids");
            try {

                // 获取用户ID
                String retUserId = StringUtil.getUserId(request.getToken());
                if (StringUtil.isEmpty(retUserId, true)) {
                    response.setRepCode(RespCode.LOGIN_FAILED);
                    response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                    return response;
                }
                String userId = retUserId;
                // 根据主键删除时，主键不能为空
                if (StringUtils.isBlank(ids)) {
                    response.setRepCode(RespCode.COMPANY_DISABLE_IDS_ERROR);
                    response.setRepMsg(RespMsg.COMPANY_DISABLE_IDS_ERROR_MSG);
                    return response;
                }

                // check所选运输公司下司机是否已经全部停用
                List<String> nameList = transportCompanyService.checkCompanyAllDriver(ids);

                if (nameList != null && nameList.size() > 0) {
                    response.setRepCode(RespCode.COMPANY_DISABLE_ERROR);
                    response.setRepMsg("运输公司" + nameList.toString() + "中存在启用司机，请全部停用后再次操作！");
                    return response;
                }

                // 批量激活或停用司机(支持单条记录操作)
                int num = transportCompanyService.batchUpdateCompany(ids, Long.parseLong(userId), 0);
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } catch (Exception e) {
                e.printStackTrace();
                response.setRepCode(RespCode.COMPANY_DISABLE_ERROR);
                response.setRepMsg(RespMsg.COMPANY_DISABLE_ERROR_MSG);
            } finally {
                logger.info("传入参数：{}" + JSON.toJSONString(ids));
            }
            return response;
        }
    }

    @RequestMapping("/saveTransportCompany")
    @ResponseBody
    @LoggerManage(description = "保存运输公司信息")
    public BaseResponseModel<Object> saveTransportCompany(@RequestBody BaseRequestModel request) throws Exception {
        synchronized (this) {
            BaseResponseModel<Object> response = new BaseResponseModel<Object>();
            String str = null;
            try {

                // 获取用户ID
                String retUserId = StringUtil.getUserId(request.getToken());
                if (StringUtil.isEmpty(retUserId, true)) {
                    response.setRepCode(RespCode.LOGIN_FAILED);
                    response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                    return response;
                }
                String userId = retUserId;
                // 请求数据
                str = request.getReqData().toString();
                TransportCompanyEntity bean = JSON.parseObject(str, TransportCompanyEntity.class);

                int checkName = 0;
                // 编辑操作
                if (bean.getId() != null) {
                    // 更新用户
                    bean.setUpdateUser(Integer.valueOf(userId));
                } else {
                    // 创建用戶
                    bean.setCreateUser(Integer.valueOf(userId));
                }
                // 校验运输公司名称是否重复
                checkName = transportCompanyService.checkRepeatValue(bean.getName(), "name", bean.getId());

                if (checkName > 0) {
                    response.setRepCode(RespCode.COMPANY_NAME_REPEAT_ERROR);
                    response.setRepMsg(RespMsg.COMPANY_NAME_REPEAT_ERROR_MSG);
                    return response;
                }

                // 影响记录数
                int num = 0;
                if (bean.getId() != null) {
                    // 保存编辑后运输公司信息
                    num = transportCompanyService.updateTransportCompanyEntity(bean);
                } else {
                    // 新增运输公司信息
                    num = transportCompanyService.addTransportCompanyEntity(bean);
                }
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } catch (Exception e) {
                e.printStackTrace();
                response.setRepCode(RespCode.COMPANY_INSERT_UPDATE_ERROR);
                response.setRepMsg(RespMsg.COMPANY_INSERT_UPDATE_ERROR_MSG);
            } finally {
                logger.info("传入参数：{}" + str);
            }
            return response;
        }
    }

    @RequestMapping("/selectInfoById")
    @ResponseBody
    @LoggerManage(description = "根据ID获取运输公司信息")
    public BaseResponseModel<Object> selectInfoById(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            // 请求数据
            str = request.getReqData().toString();
            TransportCompanyEntity bean = JSON.parseObject(str, TransportCompanyEntity.class);

            // 获取运输公司信息
            TransportCompanyVO retValue = transportCompanyService.selectByPrimaryKey(bean.getId());
            if (retValue == null) {
                response.setRepCode(RespCode.COMPANY_ID_EMPTY_ERROR);
                response.setRepMsg(RespMsg.COMPANY_ID_EMPTY_ERROR_MSG);
                return response;
            } else {
                response.setRepData(retValue);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.COMPANY_SELECTONE_FAILED);
            response.setRepMsg(RespMsg.COMPANY_SELECTONE_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

}
