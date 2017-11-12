/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
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
import com.anji.allways.business.entity.WarehouseEntity;
import com.anji.allways.business.entity.WarehouseLinkCustomerEntity;
import com.anji.allways.business.enums.RowDictCodeE;
import com.anji.allways.business.service.CustomerService;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.service.UserService;
import com.anji.allways.business.service.WarehouseService;
import com.anji.allways.business.vo.DictVO;
import com.anji.allways.business.vo.WarehouseLevelVO;
import com.anji.allways.business.vo.WarehouseLinkCustomerVO;
import com.anji.allways.business.vo.WarehouseVO;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.StringUtil;

/**
 * 仓库管理Controller
 * @author wangyanjun
 * @version $Id: WarehouseController.java, v 0.1 2017年8月31日 上午11:23:45 wangyanjun Exp $
 */
@Controller
@RequestMapping("/business/warehouse")
public class WarehouseController {

    private Logger           logger = LogManager.getLogger(WarehouseController.class);

    @Autowired
    private CustomerService  customerService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private UserService      userService;

    // 获取数据字典Service
    @Autowired
    private DictService      dictService;

    @RequestMapping("/queryWarehouseInfos")
    @ResponseBody
    @LoggerManage(description = "查询仓库信息列表")
    public BaseResponseModel<Object> queryWarehouseInfos(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        // 当前页数
        Integer pageNum = request.getReqData().getInteger("pageNum");
        // 每页显示数量
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

            // 获取查询条件
            WarehouseEntity bean = JSON.parseObject(str, WarehouseEntity.class);

            // 登录用户存在时
            if (customerId != null) {

                // 仓库状态处理(JSON转换之后，如果为""会自动赋值为0)
                String status = request.getReqData().getString("status");
                bean.setSearchStatus(status);

                // 登录用户为管理员时
                if (customerId != -1 && customerId != 0) {

                    // 获取某个仓库ID及其所有的下属仓库ID
                    List<Integer> idList = warehouseService.selectAllLevelIdByParentId(customerId);
                    bean.setIdList(idList);
                } else {

                    // 登录用户为普通用户时只能查看自己创建的数据
                    if (customerId == -1) {
                        bean.setCreateUser(Integer.valueOf(userId));
                    }
                }
                map = warehouseService.queryWarehouseInfos(bean, pageNum, pageRows);

            }

            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            return response;
        } catch (Exception e) {
            response.setRepCode(RespCode.SEARCH_WAREHOUSE_INFO_ERROR);
            response.setRepMsg(RespMsg.SEARCH_WAREHOUSE_INFO_ERROR_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;

    }

    @RequestMapping("/queryWarehouseStatusForDict")
    @ResponseBody
    @LoggerManage(description = "获取仓库状态下拉列表")
    public BaseResponseModel<Object> queryWarehouseStatusForDict(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        try {

            // 获取仓库状态下拉列表
            List<DictVO> retValue = dictService.queryAllByCodeForPage(RowDictCodeE.WAREHOUSE_STATUS.getValue());
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.WAREHOUSE_STATUS_ERROR);
            response.setRepMsg(RespMsg.WAREHOUSE_STATUS_ERROR_MSG);
        }
        return response;
    }

    @RequestMapping("/queryBusinessTimeForDict")
    @ResponseBody
    @LoggerManage(description = "获取仓库营业时间类型下拉列表")
    public BaseResponseModel<Object> queryBusinessTimeForDict(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        try {

            // 获取仓库营业时间类型下拉列表
            List<DictVO> retValue = dictService.queryAllByCodeForPage(RowDictCodeE.BUSSIONTIME_TYPE.getValue());
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.WAREHOUSE_BUSINESS_TIME_ERROR);
            response.setRepMsg(RespMsg.WAREHOUSE_BUSINESS_TIME_ERROR_MSG);
        }
        return response;
    }

    @RequestMapping("/setEnableWarehouseInfos")
    @ResponseBody
    @LoggerManage(description = "批量激活仓库信息")
    public BaseResponseModel<Object> setEnableWarehouseInfos(@RequestBody BaseRequestModel request) throws Exception {
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
                    response.setRepCode(RespCode.WAREHOUSE_ENABLE_IDS_ERROR);
                    response.setRepMsg(RespMsg.WAREHOUSE_ENABLE_IDS_ERROR_MSG);
                    return response;
                }

                // 批量激活或停用司机(支持单条记录操作)
                int num = warehouseService.batchUpdateWarehouse(ids, Long.parseLong(userId), 1);
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } catch (Exception e) {
                e.printStackTrace();
                response.setRepCode(RespCode.WAREHOUSE_ENABLE_ERROR);
                response.setRepMsg(RespMsg.WAREHOUSE_ENABLE_ERROR_MSG);
            } finally {
                logger.info("传入参数：{}" + JSON.toJSONString(ids));
            }
            return response;
        }
    }

    @RequestMapping("/setDisableWarehouseInfos")
    @ResponseBody
    @LoggerManage(description = "批量停用仓库信息")
    public BaseResponseModel<Object> setDisableWarehouseInfos(@RequestBody BaseRequestModel request) throws Exception {
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
                    response.setRepCode(RespCode.WAREHOUSE_DISABLE_IDS_ERROR);
                    response.setRepMsg(RespMsg.WAREHOUSE_DISABLE_IDS_ERROR_MSG);
                    return response;
                }

                // 校验仓库是否存在已入库的入库计划单或未完成的出库计划单
                List<String> nameList = warehouseService.checkWarehouseStatus(ids);
                if (nameList != null && nameList.size() > 0) {
                    response.setRepCode(RespCode.WAREHOUSE_DISABLE_ERROR);
                    response.setRepMsg("仓库" + nameList.toString() + "正在使用，请确认后再次操作！");
                    return response;
                }

                // 批量激活或停用仓库(支持单条记录操作)
                int num = warehouseService.batchUpdateWarehouse(ids, Long.parseLong(userId), 0);
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } catch (Exception e) {
                e.printStackTrace();
                response.setRepCode(RespCode.WAREHOUSE_DISABLE_ERROR);
                response.setRepMsg(RespMsg.WAREHOUSE_DISABLE_ERROR_MSG);
            } finally {
                logger.info("传入参数：{}" + JSON.toJSONString(ids));
            }
            return response;
        }
    }

    @RequestMapping("/selectInfoById")
    @ResponseBody
    @LoggerManage(description = "根据ID获取仓库信息")
    public BaseResponseModel<Object> selectInfoById(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            // 请求数据
            str = request.getReqData().toString();
            TransportCompanyEntity bean = JSON.parseObject(str, TransportCompanyEntity.class);

            // 获取仓库信息
            WarehouseVO retValue = warehouseService.selectByPrimaryKey(bean.getId());
            if (retValue == null) {
                response.setRepCode(RespCode.WAREHOUSE_ID_EMPTY_ERROR);
                response.setRepMsg(RespMsg.WAREHOUSE_ID_EMPTY_ERROR_MSG);
                return response;
            } else {
                response.setRepData(retValue);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.WAREHOUSE_SELECTONE_FAILED);
            response.setRepMsg(RespMsg.WAREHOUSE_SELECTONE_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/selectWarehouseLevelInfor")
    @ResponseBody
    @LoggerManage(description = "获取仓库等级信息（层级展开）")
    public BaseResponseModel<Object> selectWarehouseLevelInfor(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            Map<String, Object> map = new HashMap<String, Object>();

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

            // 上级仓库ID
            Integer parentId = request.getReqData().getInteger("parentId");

            // 登录用户存在且传入上级仓库ID值为null时
            if (customerId != null && parentId == null) {

                // 登录用户为管理员时
                if (customerId != -1 && customerId != 0) {

                    // 仓库ID
                    parentId = customerId;
                } else {

                    // 登录用户为普通用户时只能查看自己创建的数据
                    if (customerId == -1) {
                        map.put("createUser", Integer.valueOf(userId));
                    }
                }

            }

            map.put("parentId", parentId);

            // 上级仓库名称
            String name = request.getReqData().getString("name");
            map.put("name", name);

            // 获取仓库等级信息（层级展开）
            List<WarehouseLevelVO> retValue = warehouseService.selectWarehouseLevelInfor(map);
            Map<String, Object> retMap = new HashMap<String, Object>();
            retMap.put("data", retValue);
            response.setRepData(retMap);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.WAREHOUSE_LEVEL_FAILED);
            response.setRepMsg(RespMsg.WAREHOUSE_LEVEL_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/saveWarehouseInfo")
    @ResponseBody
    @LoggerManage(description = "保存仓库信息")
    public BaseResponseModel<Object> saveWarehouseInfo(@RequestBody BaseRequestModel request) throws Exception {
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
                WarehouseEntity bean = JSON.parseObject(str, WarehouseEntity.class);

                int checkNum = 0;
                // 编辑操作
                if (bean.getId() != null) {
                    // 更新用户
                    bean.setUpdateUser(Integer.valueOf(userId));
                } else {
                    // 创建用戶
                    bean.setCreateUser(Integer.valueOf(userId));
                }

                if (!StringUtils.isEmpty(bean.getName())) {
                    // 校验仓库名称是否重复
                    checkNum = warehouseService.checkRepeatValue(bean.getName(), "name", bean.getId());

                    if (checkNum > 0) {
                        // 输入的仓库名已存在
                        response.setRepCode(RespCode.WAREHOUSE_NAME_REPEAT_ERROR);
                        response.setRepMsg(RespMsg.WAREHOUSE_NAME_REPEAT_ERROR_MSG);
                        return response;
                    }
                }

                if (!StringUtils.isEmpty(bean.getShortName())) {
                    // 校验仓库名简称是否重复
                    checkNum = warehouseService.checkRepeatValue(bean.getShortName(), "shortName", bean.getId());

                    if (checkNum > 0) {
                        // 输入的仓库名简称已存在
                        response.setRepCode(RespCode.WAREHOUSE_SHORT_NAME_REPEAT_ERROR);
                        response.setRepMsg(RespMsg.WAREHOUSE_SHORT_NAME_REPEAT_ERROR_MSG);
                        return response;
                    }
                }

                // 影响记录数或插入ID
                int num = 0;
                if (bean.getId() != null) {
                    // 保存编辑后仓库信息
                    num = warehouseService.updateWarehouseEntity(bean);
                } else {
                    // 新增仓库信息
                    num = warehouseService.addWarehouseEntity(bean);
                }
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } catch (Exception e) {
                e.printStackTrace();
                response.setRepCode(RespCode.WAREHOUSE_INSERT_UPDATE_ERROR);
                response.setRepMsg(RespMsg.WAREHOUSE_INSERT_UPDATE_ERROR_MSG);
            } finally {
                logger.info("传入参数：{}" + str);
            }
            return response;
        }
    }

    @RequestMapping("/queryWarehouseRelationInfos")
    @ResponseBody
    @LoggerManage(description = "查询仓库关系信息列表")
    public BaseResponseModel<Object> queryWarehouseRelationInfos(@RequestBody BaseRequestModel request) throws Exception {
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

            // 获取查询条件
            WarehouseEntity bean = JSON.parseObject(str, WarehouseEntity.class);

            // 登录用户存在时
            if (customerId != null) {

                // 登录用户为管理员时
                if (customerId != -1 && customerId != 0) {

                    // 获取某个仓库ID及其所有的下属仓库ID
                    List<Integer> idList = warehouseService.selectAllLevelIdByParentId(customerId);
                    bean.setIdList(idList);
                } else {

                    // 登录用户为普通用户时只能查看自己创建的数据
                    if (customerId == -1) {
                        bean.setCreateUser(Integer.valueOf(userId));
                    }
                }
                map = warehouseService.queryWarehouseRelationInfos(bean, pageNum, pageRows);

            }
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.SEARCH_STORAGE_INFO_ERROR);
            response.setRepMsg(RespMsg.SEARCH_STORAGE_INFO_ERROR_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/queryStorageInfos")
    @ResponseBody
    @LoggerManage(description = "查询库容信息列表")
    public BaseResponseModel<Object> queryStorageInfos(@RequestBody BaseRequestModel request) throws Exception {
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

            // 获取查询条件
            WarehouseEntity bean = JSON.parseObject(str, WarehouseEntity.class);

            // 登录用户存在时
            if (customerId != null) {

                // 仓库状态处理(JSON转换之后，如果为""会自动赋值为0)
                String status = request.getReqData().getString("status");
                bean.setSearchStatus(status);

                // 登录用户为管理员时
                if (customerId != -1 && customerId != 0) {

                    // 获取某个仓库ID及其所有的下属仓库ID
                    List<Integer> idList = warehouseService.selectAllLevelIdByParentId(customerId);
                    bean.setIdList(idList);
                } else {

                    // 登录用户为普通用户时只能查看自己创建的数据
                    if (customerId == -1) {
                        bean.setCreateUser(Integer.valueOf(userId));
                    }
                }
                map = warehouseService.queryStorageInfos(bean, pageNum, pageRows);

            }
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.SEARCH_STORAGE_INFO_ERROR);
            response.setRepMsg(RespMsg.SEARCH_STORAGE_INFO_ERROR_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/selectStorageInfoById")
    @ResponseBody
    @LoggerManage(description = "根据仓库ID获取库容信息")
    public BaseResponseModel<Object> selectStorageInfoById(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            // 请求数据
            str = request.getReqData().toString();
            WarehouseEntity bean = JSON.parseObject(str, WarehouseEntity.class);

            // 获取库容信息
            WarehouseVO retValue = warehouseService.selectStorageInfoById(bean.getId());
            if (retValue == null) {
                response.setRepCode(RespCode.STORAGE_ID_EMPTY_ERROR);
                response.setRepMsg(RespMsg.STORAGE_ID_EMPTY_ERROR_MSG);
                return response;
            } else {
                response.setRepData(retValue);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.STORAGE_SELECTONE_FAILED);
            response.setRepMsg(RespMsg.STORAGE_SELECTONE_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/queryWarningTypeForDict")
    @ResponseBody
    @LoggerManage(description = "获取使用告警类型下拉列表")
    public BaseResponseModel<Object> queryWarningTypeForDict(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        try {

            // 获取使用告警类型下拉列表
            List<DictVO> retValue = dictService.queryAllByCodeForPage(RowDictCodeE.WARNING_TYPE.getValue());
            // 排除未知的下拉选项
            for (DictVO vo : retValue) {
                if ("0".equals(vo.getCode())) {
                    retValue.remove(vo);
                    break;
                }
            }
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.WAREHOUSE_BUSINESS_TIME_ERROR);
            response.setRepMsg(RespMsg.WAREHOUSE_BUSINESS_TIME_ERROR_MSG);
        }
        return response;
    }

    @RequestMapping("/queryCustomerForAdd")
    @ResponseBody
    @LoggerManage(description = "添加客户信息分页查询")
    public BaseResponseModel<Object> queryCustomerForAdd(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        Integer pageNum = request.getReqData().getInteger("pageNum");
        Integer pageRows = request.getReqData().getInteger("pageRows");
        try {
            str = request.getReqData().toString();

            Map<String, Object> paramMap = new HashMap<String, Object>();

            // 当前仓库ID
            Integer warehouseId = request.getReqData().getInteger("warehouseId");
            paramMap.put("warehouseId", warehouseId);
            // 客户名
            String name = request.getReqData().getString("name");
            paramMap.put("name", name);
            // 联系方式
            String contactorMobile = request.getReqData().getString("contactorMobile");
            paramMap.put("contactorMobile", contactorMobile);

            Map<String, Object> map = customerService.queryCustomerInfosForStorage(paramMap, pageNum, pageRows);
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.CUSTOMER_INFO_FAILED);
            response.setRepMsg(RespMsg.CUSTOMER_INFO_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/saveStorage")
    @ResponseBody
    @LoggerManage(description = "保存库容信息")
    public BaseResponseModel<Object> saveStorage(@RequestBody BaseRequestModel request) throws Exception {
        synchronized (this) {
            BaseResponseModel<Object> response = new BaseResponseModel<Object>();
            String str = null;
            try {

                Map<String, Object> paramMap = new HashMap<String, Object>();

                // 获取用户ID
                String retUserId = StringUtil.getUserId(request.getToken());
                if (StringUtil.isEmpty(retUserId, true)) {
                    response.setRepCode(RespCode.LOGIN_FAILED);
                    response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                    return response;
                }
                // 更新用户
                String userId = retUserId;

                // 请求数据
                str = request.getReqData().toString();

                WarehouseVO vo = JSON.parseObject(str, WarehouseVO.class);

                // 获取仓库ID
                Integer id = request.getReqData().getInteger("id");
                paramMap.put("id", id);

                // 获取总车位数
                Integer spaceAmount = request.getReqData().getInteger("spaceAmount");
                paramMap.put("spaceAmount", spaceAmount);

                // 使用告警类型 0：未知 1：数量 2：百分比
                Integer warningType = request.getReqData().getInteger("type");

                if (warningType != null && warningType != 0) {
                    paramMap.put("warningType", warningType);
                } else {
                    // 使用告警类型更新为0
                    paramMap.put("warningType", 0);
                    // 告警库存占用警戒线（剩余库位数）更新为0
                    paramMap.put("warningEmptyAmount", 0);
                    // 告警库存占用警戒线（百分百）更新为1
                    paramMap.put("warningOccupiedPercent", 1);
                }

                // 告警库存占用警戒线（剩余库位数）
                Integer warningEmptyAmount = vo.getWarningEmptyAmount();

                // 告警类型为数量时更新
                if (warningType != null && warningType == 1) {
                    paramMap.put("warningEmptyAmount", warningEmptyAmount);
                }

                // 告警库存占用警戒线（百分百）
                String warningOccupiedPercent = request.getReqData().getString("warningOccupiedPercent");

                // 告警类型为百分比时更新
                if (warningType != null && warningType == 2) {
                    paramMap.put("warningOccupiedPercent", warningOccupiedPercent);
                }

                // 客户信息list
                List<WarehouseLinkCustomerEntity> list = new ArrayList<WarehouseLinkCustomerEntity>();

                // 当前设置的客户库位总数不能超过设置的仓库库位总数
                if (vo.getCustomerList() != null) {

                    // 当设置的客户库位总数
                    Integer totalNum = 0;
                    for (WarehouseLinkCustomerVO warehouseLinkCustomerVO : vo.getCustomerList()) {
                        totalNum = totalNum + warehouseLinkCustomerVO.getSpaceAmount();

                        // 添加更新信息
                        WarehouseLinkCustomerEntity entity = new WarehouseLinkCustomerEntity();
                        BeanUtils.copyProperties(entity, warehouseLinkCustomerVO);
                        list.add(entity);
                    }

                    // 设置的客户库位总数超过设置的仓库库位总数
                    if (totalNum > spaceAmount) {
                        response.setRepCode(RespCode.SPACE_AMOUNT_FAILED);
                        response.setRepMsg(RespMsg.SPACE_AMOUNT_FAILED_MSG);
                        return response;
                    }
                }

                int num = warehouseService.saveStorage(paramMap, list, Integer.valueOf(userId));
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } catch (RuntimeException e1) {
                e1.printStackTrace();
                response.setRepCode(RespCode.STORAGE_UPDATE_ERROR);
                response.setRepMsg(RespMsg.STORAGE_UPDATE_ERROR_MSG);
            } catch (Exception e) {
                e.printStackTrace();
                response.setRepCode(RespCode.STORAGE_UPDATE_ERROR);
                response.setRepMsg(RespMsg.STORAGE_UPDATE_ERROR_MSG);
            } finally {
                logger.info("传入参数：{}" + str);
            }
            return response;
        }
    }

    @RequestMapping("/deleteCustomerById")
    @ResponseBody
    @LoggerManage(description = "删除仓库客户关联信息")
    public BaseResponseModel<Object> deleteCustomerById(@RequestBody BaseRequestModel request) throws Exception {
        synchronized (this) {
            BaseResponseModel<Object> response = new BaseResponseModel<Object>();
            String str = null;
            try {
                // 请求数据
                str = request.getReqData().toString();

                Integer id = request.getReqData().getInteger("id");

                int num = 0;
                if (id != null) {
                    // 删除仓库客户关联信息
                    num = warehouseService.deleteCustomerById(id);
                }
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } catch (Exception e) {
                e.printStackTrace();
                response.setRepCode(RespCode.WAREHOUSE_LINK_CUSTOMER_DELETE_ERROR);
                response.setRepMsg(RespMsg.WAREHOUSE_LINK_CUSTOMER_DELETE_ERROR_MSG);
            } finally {
                logger.info("传入参数：{}" + str);
            }
            return response;
        }
    }
}
