/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.controller;

import java.util.ArrayList;
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
import com.anji.allways.business.entity.TruckDriverEntity;
import com.anji.allways.business.enums.RowDictCodeE;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.service.TransportCompanyService;
import com.anji.allways.business.service.TruckDriverService;
import com.anji.allways.business.service.UserService;
import com.anji.allways.business.vo.DictVO;
import com.anji.allways.business.vo.TruckDriverVO;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.StringUtil;

/**
 * @author xuyuyang
 * @version $Id: BerthController.java, v 0.1 2017年8月24日 上午10:31:37 xuyuyang Exp $
 */
@Controller
@RequestMapping("/business/truckDriver")
public class TruckDriverController {
    private Logger                  logger = LogManager.getLogger(TruckDriverController.class);

    // 运输司机Service
    @Autowired
    private TruckDriverService      truckDriverService;

    // 运输公司Service
    @Autowired
    private TransportCompanyService transportCompanyService;

    // 获取数据字典Service
    @Autowired
    private DictService             dictService;

    // 用户信息Service
    @Autowired
    private UserService             userService;

    @RequestMapping("/queryTruckDriverInfos")
    @ResponseBody
    @LoggerManage(description = "查询运输司机信息列表")
    public BaseResponseModel<Object> queryTruckDriverInfos(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        Integer pageNum = request.getReqData().getInteger("pageNum");
        Integer pageRows = request.getReqData().getInteger("pageRows");
        try {
            str = request.getReqData().toString();

            // 获取用户ID
            String retUserId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(retUserId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }
            String userId = retUserId;

            // 根据用户ID和所属组织类型查询所属客户ID
            Integer customerId = userService.queryCustomerId(Integer.valueOf(userId), 2);

            Map<String, Object> map = new HashMap<String, Object>();

            // 账号存在且为超级管理员时
            if (customerId != null && customerId == 0) {
                // 获取查询条件
                TruckDriverEntity bean = JSON.parseObject(str, TruckDriverEntity.class);
                // 运输公司状态处理(JSON转换之后，如果为""会自动赋值为0)
                String status = request.getReqData().getString("status");
                bean.setSearchStatus(status);
                map = truckDriverService.queryTruckDriverInfos(bean, pageNum, pageRows);
            }
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            return response;
        } catch (Exception e) {
            response.setRepCode(RespCode.SEARCH_DRIVER_INFO_ERROR);
            response.setRepMsg(RespMsg.SEARCH_DRIVER_INFO_ERROR_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;

    }

    @RequestMapping("/queryCompanyForDict")
    @ResponseBody
    @LoggerManage(description = "获取运输公司名称下拉列表")
    public BaseResponseModel<Object> queryCompanyForDict(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            // 请求数据
            str = request.getReqData().toString();
            TruckDriverEntity bean = JSON.parseObject(str, TruckDriverEntity.class);

            Map<String, Object> map = new HashMap<String, Object>();

            // 运输公司名称
            map.put("name", bean.getTransportCompanyName());

            // 获取运输公司名称下拉列表信息
            List<DictVO> retValue = transportCompanyService.queryCompanyForDict(map);
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.COMPANY_NAME_ERROR);
            response.setRepMsg(RespMsg.COMPANY_NAME_ERROR_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/queryDriverStatusForDict")
    @ResponseBody
    @LoggerManage(description = "获取运输司机状态下拉列表")
    public BaseResponseModel<Object> queryDriverStatusForDict(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        try {

            // 获取运输司机状态下拉列表
            List<DictVO> retValue = dictService.queryAllByCodeForPage(RowDictCodeE.DRIVER_STATUS.getValue());
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.DRIVER_STATUS_ERROR);
            response.setRepMsg(RespMsg.DRIVER_STATUS_ERROR_MSG);
        }
        return response;
    }

    @RequestMapping("/setEnableDriverInfos")
    @ResponseBody
    @LoggerManage(description = "批量激活司机信息")
    public BaseResponseModel<Object> setEnableDriverInfos(@RequestBody BaseRequestModel request) throws Exception {
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
                    response.setRepCode(RespCode.DRIVER_ENABLE_IDS_ERROR);
                    response.setRepMsg(RespMsg.DRIVER_ENABLE_IDS_ERROR_MSG);
                    return response;
                }

                String[] split = ids.split(",");
                List<Integer> list = new ArrayList<Integer>();
                for (int i = 0; i < split.length; i++) {
                    list.add(Integer.valueOf(split[i]));
                }

                TruckDriverVO vo = truckDriverService.checkOhterCompanyByIdList(list);

                if (vo != null) {
                    response.setRepCode(RespCode.DRIVER_ENABLE_EXISTS_ERROR);
                    response.setRepMsg("该司机账号(" + vo.getAccountName() + ")在" + vo.getTransportCompanyName() + "为启用状态，请禁用" + vo.getTransportCompanyName() + "的账号，再启用当前账号");
                    return response;
                }

                // 批量激活或停用司机(支持单条记录操作)
                int num = truckDriverService.batchUpdateDriver(list, Long.parseLong(userId), 1);
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } catch (Exception e) {
                e.printStackTrace();
                response.setRepCode(RespCode.DRIVER_ENABLE_ERROR);
                response.setRepMsg(RespMsg.DRIVER_ENABLE_ERROR_MSG);
            } finally {
                logger.info("传入参数：{}" + JSON.toJSONString(ids));
            }
            return response;
        }
    }

    @RequestMapping("/setDisableDriverInfos")
    @ResponseBody
    @LoggerManage(description = "批量停用司机信息")
    public BaseResponseModel<Object> setDisableDriverInfos(@RequestBody BaseRequestModel request) throws Exception {
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
                    response.setRepCode(RespCode.DRIVER_DISABLE_IDS_ERROR);
                    response.setRepMsg(RespMsg.DRIVER_DISABLE_IDS_ERROR_MSG);
                    return response;
                }

                String[] split = ids.split(",");
                List<Integer> list = new ArrayList<Integer>();
                for (int i = 0; i < split.length; i++) {
                    list.add(Integer.valueOf(split[i]));
                }

                // 校验选中的司机是否有任务正在进行
                List<String> nameList = truckDriverService.checkAllDriver(list);
                if (nameList != null && nameList.size() > 0) {
                    response.setRepCode(RespCode.DRIVER_DISABLE_ERROR);
                    response.setRepMsg("运输司机" + nameList.toString() + "正在进行出库任务，请确认后再次操作！");
                    return response;
                }

                // 批量激活或停用司机(支持单条记录操作)
                int num = truckDriverService.batchUpdateDriver(list, Long.parseLong(userId), 0);
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } catch (Exception e) {
                e.printStackTrace();
                response.setRepCode(RespCode.DRIVER_DISABLE_ERROR);
                response.setRepMsg(RespMsg.DRIVER_DISABLE_ERROR_MSG);
            } finally {
                logger.info("传入参数：{}" + JSON.toJSONString(ids));
            }
            return response;
        }
    }

    @RequestMapping("/selectInfoById")
    @ResponseBody
    @LoggerManage(description = "根据ID获取运输司机信息")
    public BaseResponseModel<Object> selectInfoById(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            // 请求数据
            str = request.getReqData().toString();
            TruckDriverEntity bean = JSON.parseObject(str, TruckDriverEntity.class);

            // 获取运输司机信息
            TruckDriverVO retValue = truckDriverService.selectByPrimaryKey(bean.getId());
            if (retValue == null) {
                response.setRepCode(RespCode.DRIVER_ID_EMPTY_ERROR);
                response.setRepMsg(RespMsg.DRIVER_ID_EMPTY_ERROR_MSG);
                return response;
            } else {
                response.setRepData(retValue);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.DRIVER_SELECTONE_FAILED);
            response.setRepMsg(RespMsg.DRIVER_SELECTONE_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/saveTruckDriver")
    @ResponseBody
    @LoggerManage(description = "保存运输司机信息")
    public BaseResponseModel<Object> saveTruckDriver(@RequestBody BaseRequestModel request) throws Exception {
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

                TruckDriverEntity bean = JSON.parseObject(str, TruckDriverEntity.class);

                int retNum = 0;
                // 编辑操作
                if (bean.getId() != null) {
                    // 更新用户
                    bean.setUpdateUser(Integer.valueOf(userId));
                } else {
                    // 创建用戶
                    bean.setCreateUser(Integer.valueOf(userId));
                }

                // 所属运输公司是否存在
                retNum = transportCompanyService.checkRepeatValue(bean.getTransportCompanyName(), "name", null);

                if (retNum == 0) {
                    // 所属运输公司不存在，请重新输入运输公司名称
                    response.setRepCode(RespCode.DRIVER_COMPANY_NOTEXISTS_ERROR);
                    response.setRepMsg(RespMsg.DRIVER_COMPANY_NOTEXISTS_ERROR_MSG);
                    return response;
                }

                // 账号名是否存在
                retNum = truckDriverService.checkRepeatValue(bean.getAccountName(), "accountName", bean.getId());

                if (retNum > 0) {
                    // 输入账号名已存在，请重新输入
                    response.setRepCode(RespCode.ACCOUNT_NAME_EXISTS_ERROR);
                    response.setRepMsg(RespMsg.ACCOUNT_NAME_EXISTS_ERROR_MSG);
                    return response;
                }

                // 联系电话是否存在
                List<TruckDriverVO> checkList = truckDriverService.selectInfoByMoblie(bean.getMobile());

                if (checkList != null && checkList.size() > 0) {

                    for (TruckDriverVO vo : checkList) {

                        // 若A公司原本有创建司机B 则，不可创建该司机账号，提示：当前司机已存在，不可重复创建
                        if (vo.getTransportCompanyId().equals(bean.getTransportCompanyId())) {
                            // 当前司机已存在，不可重复创建
                            response.setRepCode(RespCode.DRIVER_EXISTS_ERROR);
                            response.setRepMsg(RespMsg.DRIVER_EXISTS_ERROR_MSG);
                            return response;
                        }

                        // 若A公司创建司机时，司机的手机号已在其他运输公司中，且状态为正常,则，无法创建该司机
                        if (vo.getStatus() == 1 && !vo.getTransportCompanyId().equals(bean.getTransportCompanyId())) {
                            // 当前司机已在其他运输公司中，停用账号后才能进行注册
                            response.setRepCode(RespCode.DRIVER_IN_OTHERCOMPANY_ERROR);
                            response.setRepMsg(RespMsg.DRIVER_IN_OTHERCOMPANY_ERROR_MSG);
                            return response;
                        }

                    }
                }

                int num = 0;
                if (bean.getId() != null) {
                    // 保存编辑后运输公司信息
                    num = truckDriverService.updateTruckDriverEntity(bean);
                } else {
                    // 新增运输公司信息
                    num = truckDriverService.addTruckDriverEntityEntity(bean);
                }
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } catch (Exception e) {
                e.printStackTrace();
                response.setRepCode(RespCode.DRIVER_INSERT_UPDATE_ERROR);
                response.setRepMsg(RespMsg.DRIVER_INSERT_UPDATE_ERROR_MSG);
            } finally {
                logger.info("传入参数：{}" + str);
            }
            return response;
        }
    }

}
