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
import com.alibaba.fastjson.JSONArray;
import com.anji.allways.base.aop.LoggerManage;
import com.anji.allways.base.entity.BaseRequestModel;
import com.anji.allways.base.entity.BaseResponseModel;
import com.anji.allways.business.entity.BrandEntity;
import com.anji.allways.business.entity.CustomerEntity;
import com.anji.allways.business.entity.CustomerOntimeRuleEntity;
import com.anji.allways.business.entity.UserEntity;
import com.anji.allways.business.enums.RowDictCodeE;
import com.anji.allways.business.service.CustomerService;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.service.UserService;
import com.anji.allways.business.service.WarehouseService;
import com.anji.allways.business.vo.CustomerLevelVO;
import com.anji.allways.business.vo.CustomerVO;
import com.anji.allways.business.vo.DictVO;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.StringUtil;

/**
 * 客户管理接口
 * @author wangyanjun
 * @version $Id: CustomerController.java, v 0.1 2017年8月30日 上午12:01:48 wangyanjun Exp $
 */
@Controller
@RequestMapping("/business/customer")
public class CustomerController {

    private Logger           logger = LogManager.getLogger(CustomerController.class);

    @Autowired
    private CustomerService  customerService;

    @Autowired
    private UserService      userService;

    // 获取数据字典Service
    @Autowired
    private DictService      dictService;

    @Autowired
    private WarehouseService warehouseService;

    @RequestMapping("/queryCustomerInfos")
    @ResponseBody
    @LoggerManage(description = "查询客户信息列表")
    public BaseResponseModel<Object> queryCustomerInfos(@RequestBody BaseRequestModel request) throws Exception {
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
            CustomerEntity bean = JSON.parseObject(str, CustomerEntity.class);

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
                map = customerService.queryCustomerInfos(bean, pageNum, pageRows);
            }

            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.SEARCH_CUSTOMER_INFO_ERROR);
            response.setRepMsg(RespMsg.SEARCH_CUSTOMER_INFO_ERROR_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/queryCustomerStatusForDict")
    @ResponseBody
    @LoggerManage(description = "获取客户状态下拉列表")
    public BaseResponseModel<Object> queryCustomerStatusForDict(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        try {

            // 获取客户状态下拉列表
            List<DictVO> retValue = dictService.queryAllByCodeForPage(RowDictCodeE.CUSTOMER_STATUS.getValue());
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.CUSTOMER_STATUS_ERROR);
            response.setRepMsg(RespMsg.CUSTOMER_STATUS_ERROR_MSG);
        }
        return response;
    }

    @RequestMapping("/queryGroupForDict")
    @ResponseBody
    @LoggerManage(description = "获取所属集团下拉列表")
    public BaseResponseModel<Object> queryGroupForDict(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {

            // 请求数据
            str = request.getReqData().toString();

            CustomerEntity bean = JSON.parseObject(str, CustomerEntity.class);

            Map<String, Object> map = new HashMap<String, Object>();

            // 所属集团名称
            map.put("name", bean.getGroup());

            // 获取所属集团名称下拉列表信息
            List<DictVO> retValue = customerService.queryGroupForDict(map);
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.CUSTOMER_GROUP_ERROR);
            response.setRepMsg(RespMsg.CUSTOMER_GROUP_ERROR_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/setEnableCustomerInfos")
    @ResponseBody
    @LoggerManage(description = "批量激活客户信息")
    public BaseResponseModel<Object> setEnableCustomerInfos(@RequestBody BaseRequestModel request) throws Exception {
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
                    response.setRepCode(RespCode.CUSTOMER_ENABLE_IDS_ERROR);
                    response.setRepMsg(RespMsg.CUSTOMER_ENABLE_IDS_ERROR_MSG);
                    return response;
                }

                String[] split = ids.split(",");
                List<Integer> list = new ArrayList<Integer>();
                for (int i = 0; i < split.length; i++) {
                    list.add(Integer.valueOf(split[i]));
                }

                // 批量激活或停用客户(支持单条记录操作)
                int num = customerService.batchUpdateCustomer(list, Long.parseLong(userId), 1);
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } catch (Exception e) {
                e.printStackTrace();
                response.setRepCode(RespCode.CUSTOMER_ENABLE_ERROR);
                response.setRepMsg(RespMsg.CUSTOMER_ENABLE_ERROR_MSG);
            } finally {
                logger.info("传入参数：{}" + JSON.toJSONString(ids));
            }
            return response;
        }
    }

    @RequestMapping("/setDisableCustomerInfos")
    @ResponseBody
    @LoggerManage(description = "批量停用客户信息")
    public BaseResponseModel<Object> setDisableCustomerInfos(@RequestBody BaseRequestModel request) throws Exception {
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
                    response.setRepCode(RespCode.CUSTOMER_ENABLE_IDS_ERROR);
                    response.setRepMsg(RespMsg.CUSTOMER_ENABLE_IDS_ERROR_MSG);
                    return response;
                }

                String[] split = ids.split(",");
                List<Integer> list = new ArrayList<Integer>();
                for (int i = 0; i < split.length; i++) {
                    list.add(Integer.valueOf(split[i]));
                }

                // 校验客户是否存在已入库的入库计划单且未完成的运输订单
                List<String> nameList = customerService.checkCustomerStatus(list);
                if (nameList != null && nameList.size() > 0) {
                    response.setRepCode(RespCode.CUSTOMER_DISABLE_ERROR);
                    response.setRepMsg("客户" + nameList.toString() + "有未完成的计划单，请确认后再次操作！");
                    return response;
                }

                // 批量激活或停用客户(支持单条记录操作)
                int num = customerService.batchUpdateCustomer(list, Long.parseLong(userId), 0);
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } catch (Exception e) {
                e.printStackTrace();
                response.setRepCode(RespCode.CUSTOMER_ENABLE_ERROR);
                response.setRepMsg(RespMsg.CUSTOMER_ENABLE_ERROR_MSG);
            } finally {
                logger.info("传入参数：{}" + JSON.toJSONString(ids));
            }
            return response;
        }
    }

    @RequestMapping("/selectInfoById")
    @ResponseBody
    @LoggerManage(description = "根据ID获取客户信息")
    public BaseResponseModel<Object> selectInfoById(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            // 请求数据
            str = request.getReqData().toString();

            // 获取ID
            Integer id = request.getReqData().getInteger("id");

            // 获取客户信息
            CustomerVO retValue = customerService.selectInfoById(id);
            if (retValue == null) {
                response.setRepCode(RespCode.CUSTOMER_ID_EMPTY_ERROR);
                response.setRepMsg(RespMsg.CUSTOMER_ID_EMPTY_ERROR_MSG);
                return response;
            } else {
                response.setRepData(retValue);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.CUSTOMER_SELECTONE_FAILED);
            response.setRepMsg(RespMsg.CUSTOMER_SELECTONE_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/selectCustomerLevelInfor")
    @ResponseBody
    @LoggerManage(description = "获取客户等级信息（层级展开）")
    public BaseResponseModel<Object> selectCustomerLevelInfor(@RequestBody BaseRequestModel request) throws Exception {

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

            // 根据用户ID和所属组织类型查询所属客户ID
            Integer customerId = userService.queryCustomerId(Integer.valueOf(retUserId), 2);

            // 登录用户存在时
            if (customerId != null) {
                // 登录用户为管理员时
                if (customerId != -1 && customerId != 0) {

                    // 获取某个仓库ID及其所有的下属仓库ID
                    List<Integer> idList = warehouseService.selectAllLevelIdByParentId(customerId);
                    map.put("idList", idList);
                } else {

                    // 登录用户为普通用户时只能查看自己创建的数据
                    if (customerId == -1) {
                        map.put("createUser", retUserId);
                    }
                }
            }

            // 上级客户ID
            Integer parentId = request.getReqData().getInteger("parentId");
            map.put("parentId", parentId);

            // 上级客户名称
            String name = request.getReqData().getString("name");
            map.put("name", name);

            // 获取客户等级信息（层级展开）
            List<CustomerLevelVO> retValue = customerService.selectCustomerLevelInfor(map);
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

    @RequestMapping("/selectBrandByCustomerId")
    @ResponseBody
    @LoggerManage(description = "获取经营品牌信息")
    public BaseResponseModel<Object> selectBrandByCustomerId(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            // 请求数据
            str = request.getReqData().toString();

            String customerId = request.getReqData().getString("id");

            // 一级客户是的ID为0,默认检索全部
            if ("0".equals(customerId)) {
                customerId = "";
            }
            // 获取经营品牌信息
            List<BrandEntity> retList = customerService.selectBrandByCustomerId(customerId);
            response.setRepData(retList);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.CUSTOMER_BRAND_FAILED);
            response.setRepMsg(RespMsg.CUSTOMER_BRAND_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/selectLogoPictureById")
    @ResponseBody
    @LoggerManage(description = "根据客户ID查询客户logo照片")
    public BaseResponseModel<Object> selectLogoPictureById(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            // 请求数据
            str = request.getReqData().toString();

            // 客户ID
            Integer customerId = request.getReqData().getInteger("id");

            // 获取客户logo照片
            String logoPath = customerService.selectLogoPictureById(customerId);
            response.setRepData(logoPath);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.LOGO_PICTURE_FAILED);
            response.setRepMsg(RespMsg.LOGO_PICTURE_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/selectRuleInfoByCustomerId")
    @ResponseBody
    @LoggerManage(description = "根据客户ID查询及时率规则信息")
    public BaseResponseModel<Object> selectRuleInfoByCustomerId(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            // 请求数据
            str = request.getReqData().toString();

            // 客户ID
            Integer customerId = request.getReqData().getInteger("id");

            // 查询及时率规则信息
            List<CustomerOntimeRuleEntity> ruleList = customerService.selectRuleInfoByCustomerId(customerId);
            response.setRepData(ruleList);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.CUSTOMER_RULE_FAILED);
            response.setRepMsg(RespMsg.CUSTOMER_RULE_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/saveCustomerInfo")
    @ResponseBody
    @LoggerManage(description = "保存客户信息")
    public BaseResponseModel<Object> saveCustomerInfo(@RequestBody BaseRequestModel request) throws Exception {
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
                CustomerEntity bean = JSON.parseObject(str, CustomerEntity.class);

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
                    // 校验客户名称是否重复
                    checkNum = customerService.checkRepeatValue(bean.getName(), "name", bean.getId());

                    if (checkNum > 0) {
                        // 输入的客户名已存在
                        response.setRepCode(RespCode.CUSTOMER_NAME_REPEAT_ERROR);
                        response.setRepMsg(RespMsg.CUSTOMER_NAME_REPEAT_ERROR_MSG);
                        return response;
                    }
                }

                if (!StringUtils.isEmpty(bean.getShortName())) {
                    // 校验客户名简称是否重复
                    checkNum = customerService.checkRepeatValue(bean.getShortName(), "shortName", bean.getId());

                    if (checkNum > 0) {
                        // 输入的客户名简称已存在
                        response.setRepCode(RespCode.CUSTOMER_SHORT_NAME_REPEAT_ERROR);
                        response.setRepMsg(RespMsg.CUSTOMER_SHORT_NAME_REPEAT_ERROR_MSG);
                        return response;
                    }
                }

                // 经营品牌范围
                JSONArray brandList = request.getReqData().getJSONArray("brandList");

                // 及时率规则信息
                JSONArray ruleList = request.getReqData().getJSONArray("ruleList");

                // 影响记录数或插入ID
                int num = 0;
                if (bean.getId() != null) {
                    // 保存编辑后客户信息
                    num = customerService.updateCustomerEntity(bean, brandList, ruleList);
                } else {

                    // 根据登录用户ID获取仓库ID
                    Integer warehouseId = userService.queryOwnerIdByUserId(Integer.valueOf(userId));
                    // 新增客户信息
                    num = customerService.addCustomerEntity(warehouseId, bean, brandList, ruleList);
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

    @RequestMapping("/queryCustomerAccountInfos")
    @ResponseBody
    @LoggerManage(description = "查询客户账号信息列表")
    public BaseResponseModel<Object> queryCustomerAccountInfos(@RequestBody BaseRequestModel request) throws Exception {
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
            UserEntity bean = JSON.parseObject(str, UserEntity.class);

            // 登录用户存在时
            if (customerId != null) {

                // 是否有效状态处理(JSON转换之后，如果为""会自动赋值为0)
                String lockedSearch = request.getReqData().getString("locked");
                bean.setLockedSearch(lockedSearch);

                // 获取检索条件的所属客户ID
                String customerIdSearch = request.getReqData().getString("customerId");

                // 如果检索条件客户ID为空
                if (StringUtil.isEmpty(customerIdSearch, true)) {

                    // 登录用户为管理员时
                    if (customerId != -1 && customerId != 0) {

                        // 获取某个仓库ID及其所有的下属仓库ID
                        List<Integer> idList = warehouseService.selectAllLevelIdByParentId(customerId);
                        bean.setIdList(idList);
                    } else {

                        // 登录用户为普通用户时只能查看自己创建的数据
                        if (customerId == -1) {
                            bean.setCreateName(userId);
                        }
                    }
                }
                map = customerService.queryCustomerAccountInfos(bean, pageNum, pageRows);
            }

            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            return response;
        } catch (Exception e) {
            response.setRepCode(RespCode.SEARCH_ACCOUNT_INFO_ERROR);
            response.setRepMsg(RespMsg.SEARCH_ACCOUNT_INFO_ERROR_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/queryCustomerForDict")
    @ResponseBody
    @LoggerManage(description = "获取所属客户下拉列表")
    public BaseResponseModel<Object> queryCustomerForDict(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            // 请求数据
            str = request.getReqData().toString();

            Map<String, Object> map = new HashMap<String, Object>();

            // 所属客户名称
            map.put("name", request.getReqData().getString("name"));

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

            List<DictVO> retValue = new ArrayList<DictVO>();
            // 登录用户存在时
            if (customerId != null) {

                // 登录用户为管理员时
                if (customerId != -1 && customerId != 0) {

                    // 获取某个仓库ID及其所有的下属仓库ID
                    List<Integer> idList = warehouseService.selectAllLevelIdByParentId(customerId);
                    map.put("idList", idList);
                } else {

                    // 登录用户为普通用户时只能查看自己创建的数据
                    if (customerId == -1) {
                        map.put("createUser", userId);
                    }
                }
                // 获取所属客户名称下拉列表信息
                retValue = customerService.queryCustomerForDict(map);
            }
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.CUSTOMER_DICT_ERROR);
            response.setRepMsg(RespMsg.CUSTOMER_DICT_ERROR_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/queryAccountStatusForDict")
    @ResponseBody
    @LoggerManage(description = "获取客户账号状态下拉列表")
    public BaseResponseModel<Object> queryAccountStatusForDict(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        try {

            // 获取客户账号状态下拉列表
            List<DictVO> retValue = dictService.queryAllByCodeForPage(RowDictCodeE.LOCK_STATUS.getValue());
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);

        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.CUSTOMER_STATUS_ERROR);
            response.setRepMsg(RespMsg.CUSTOMER_STATUS_ERROR_MSG);
        }
        return response;
    }

    @RequestMapping("/setEnableCustomerAccountInfos")
    @ResponseBody
    @LoggerManage(description = "批量激活客户账号信息")
    public BaseResponseModel<Object> setEnableCustomerAccountInfos(@RequestBody BaseRequestModel request) throws Exception {
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
                    response.setRepCode(RespCode.CUSTOMER_ENABLE_IDS_ERROR);
                    response.setRepMsg(RespMsg.CUSTOMER_ENABLE_IDS_ERROR_MSG);
                    return response;
                }

                String[] split = ids.split(",");
                List<Integer> list = new ArrayList<Integer>();
                for (int i = 0; i < split.length; i++) {
                    list.add(Integer.valueOf(split[i]));
                }

                // 批量激活或停用客户账号(支持单条记录操作)
                int num = customerService.batchUpdateCustomer(list, Long.parseLong(userId), 1);
                response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } catch (Exception e) {
                e.printStackTrace();
                response.setRepCode(RespCode.CUSTOMER_ENABLE_ERROR);
                response.setRepMsg(RespMsg.CUSTOMER_ENABLE_ERROR_MSG);
            } finally {
                logger.info("传入参数：{}" + JSON.toJSONString(ids));
            }
            return response;
        }
    }

    @RequestMapping("/setDisableCustomerAccountInfos")
    @ResponseBody
    @LoggerManage(description = "批量停用客户账号信息")
    public BaseResponseModel<Object> setDisableCustomerAccountInfos(@RequestBody BaseRequestModel request) throws Exception {
        synchronized (this) {
            BaseResponseModel<Object> response = new BaseResponseModel<Object>();
            String ids = request.getReqData().getString("ids");
            try {

                // // 获取用户ID
                // String retUserId = StringUtil.getUserId(request.getToken());
                // if (StringUtil.isEmpty(retUserId, true)) {
                // response.setRepCode(RespCode.LOGIN_FAILED);
                // response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                // return response;
                // }
                // String userId = retUserId;
                // // 根据主键删除时，主键不能为空
                // if (StringUtils.isBlank(ids)) {
                // response.setRepCode(RespCode.CUSTOMER_ENABLE_IDS_ERROR);
                // response.setRepMsg(RespMsg.CUSTOMER_ENABLE_IDS_ERROR_MSG);
                // return response;
                // }
                //
                // String[] split = ids.split(",");
                // List<Integer> list = new ArrayList<Integer>();
                // for (int i = 0; i < split.length; i++) {
                // list.add(Integer.valueOf(split[i]));
                // }
                //
                // // 校验客户是否存在已入库的入库计划单且未完成的运输订单
                // List<String> nameList = customerService.checkCustomerStatus(list);
                // if (nameList != null && nameList.size() > 0) {
                // response.setRepCode(RespCode.CUSTOMER_DISABLE_ERROR);
                // response.setRepMsg("客户" + nameList.toString() + "有未完成的计划单，请确认后再次操作！");
                // return response;
                // }
                //
                // // 批量激活或停用客户(支持单条记录操作)
                // int num = customerService.batchUpdateCustomer(list, Long.parseLong(userId), 1);
                // response.setRepData(num);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } catch (Exception e) {
                e.printStackTrace();
                response.setRepCode(RespCode.CUSTOMER_ENABLE_ERROR);
                response.setRepMsg(RespMsg.CUSTOMER_ENABLE_ERROR_MSG);
            } finally {
                logger.info("传入参数：{}" + JSON.toJSONString(ids));
            }
            return response;
        }
    }
}
