/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.anji.allways.business.entity.OrderEntity;
import com.anji.allways.business.enums.RowDictCodeE;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.service.TransportOrderService;
import com.anji.allways.business.service.UserService;
import com.anji.allways.business.service.WarehouseService;
import com.anji.allways.business.vo.DictVO;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.NsExcelReadXUtils;
import com.anji.allways.common.util.StringUtil;

/**
 * @author wangyanjun
 * @version $Id: WarehouseOrderController.java, v 0.1 2017年9月12日 下午11:28:35 wangyanjun Exp $
 */
@Controller
@RequestMapping("/business/warehouse/order")
public class WarehouseOrderController {

    private Logger                logger = LogManager.getLogger(WarehouseOrderController.class);

    @Autowired
    private TransportOrderService orderService;

    // 获取数据字典Service
    @Autowired
    private DictService           dictService;

    @Autowired
    private WarehouseService      warehouseService;

    @Autowired
    private UserService           userService;

    @RequestMapping("/listbyfilters")
    @ResponseBody
    @LoggerManage(description = "运输订单管理分页查询")
    public BaseResponseModel<Object> queryOrderByFilters(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        String str = request.getReqData().toString();
        try {
            Integer pageNum = request.getReqData().getInteger("pageNum");
            Integer pageRows = request.getReqData().getInteger("pageRows");

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
            OrderEntity entity = JSON.parseObject(str, OrderEntity.class);

            // 登录用户存在时
            if (customerId != null) {

                String status = request.getReqData().getString("status");
                if (null == status || "".equals(status)) {
                    entity.setStatus(0);
                }

                // 登录用户为管理员时
                if (customerId != -1 && customerId != 0) {

                    // 获取某个仓库ID及其所有的下属仓库ID
                    List<Integer> idList = warehouseService.selectAllLevelIdByParentId(customerId);
                    entity.setIdList(idList);
                } else {

                    // 登录用户为普通用户时只能查看自己创建的数据
                    if (customerId == -1) {
                        entity.setCreateUser(Integer.valueOf(userId));
                    }
                }
                map = orderService.queryOrderByFilers(entity, pageNum, pageRows);

            }

            // 保存最后一次查询时的条件，用于excel导出
            // 订单编号
            map.put("lastSearchNo", StringUtil.convertNullToEmpty(entity.getNo()));
            // 交接单号
            map.put("lastSearchStorageShipNo", StringUtil.convertNullToEmpty(entity.getShipNo()));
            // 商品车vin
            map.put("lastSearchStorageVin", StringUtil.convertNullToEmpty(entity.getVin()));
            // 客户名
            map.put("lastSearchCustomer", StringUtil.convertNullToEmpty(entity.getCustomer()));
            // 客户id
            map.put("lastSearchCustomerId", StringUtil.convertNullToEmpty(entity.getCustomerId()));
            // 下单开始时间
            map.put("lastSearchCreateTimeStart", StringUtil.convertNullToEmpty(entity.getCreateTimeStart()));
            // 下单结束时间
            map.put("lastSearchCreateTimeEnd", StringUtil.convertNullToEmpty(entity.getCreateTimeEnd()));
            // 自提开始时间
            map.put("lastSearchPickupSelfTimeStart", StringUtil.convertNullToEmpty(entity.getPickupSelfTimeStart()));
            // 自提结束时间
            map.put("lastSearchPickupSelfTimeEnd", StringUtil.convertNullToEmpty(entity.getPickupSelfTimeEnd()));
            // 出库开始时间
            map.put("lastSearchDeliveryTimeStart", StringUtil.convertNullToEmpty(entity.getDeliveryTimeStart()));
            // 出库结束时间
            map.put("lastSearchDeliveryTimeEnd", StringUtil.convertNullToEmpty(entity.getDeliveryTimeEnd()));
            // 状态
            map.put("lastSearchStatus", StringUtil.convertNullToEmpty(entity.getStatus()));

            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.QUERY_TRANSPORT_ORDER_FAILED);
            response.setRepMsg(RespMsg.QUERY_TRANSPORT_ORDER_EXCEPTION_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }

        return response;
    }

    @RequestMapping("/queryOrderStatusForDict")
    @ResponseBody
    @LoggerManage(description = "获取订单状态下拉列表")
    public BaseResponseModel<Object> queryOrderStatusForDict(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<>();
        try {

            // 获取订单状态下拉列表
            List<DictVO> retValue = dictService.queryAllByCodeForPage(RowDictCodeE.ORDER_STATUS.getValue());

            // 界面排除
            for (int i = 0; i < retValue.size(); i++) {
                if ("7".equals(retValue.get(i).getCode()) || "9".equals(retValue.get(i).getCode())) {
                    retValue.remove(i);
                }
            }
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.ORDER_STATUS_ERROR);
            response.setRepMsg(RespMsg.ORDER_STATUS_ERROR_MSG);
        }
        return response;
    }

    @RequestMapping("/queryCustomerForDict")
    @ResponseBody
    @LoggerManage(description = "获取客户名称下拉列表")
    public BaseResponseModel<Object> queryCustomerForDict(@RequestBody BaseRequestModel request) throws Exception {

        BaseResponseModel<Object> response = new BaseResponseModel<>();
        String str = request.getReqData().toString();
        try {

            Map<String, Object> map = new HashMap<String, Object>();

            map.put("name", request.getReqData().get("name"));
            List<DictVO> retVO = warehouseService.queryCustomerForDict(map);
            response.setRepData(retVO);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.QUERY_CUSTOMER_DICT_FAILED);
            response.setRepMsg(RespMsg.QUERY_CUSTOMER_DICT_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }

        return response;
    }

    @RequestMapping("/exportInfoByExcel")
    @LoggerManage(description = "导出运输订单记录信息")
    public void exportInfoByExcel(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
        try {

            Map<String, Object> map = new HashMap<String, Object>();

            String userId = servletRequest.getParameter("userId");

            // 根据用户ID和所属组织类型查询所属客户ID
            Integer customerId = userService.queryCustomerId(Integer.valueOf(userId), 2);

            List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
            // 初始化Excel标题，检索数据为空时显示
            Map<String, Object> initMap = new HashMap<String, Object>();
            initMap.put("运输订单", "");
            initMap.put("下单时间", "");
            initMap.put("VIN码", "");
            initMap.put("交接单号", "");
            initMap.put("客户名", "");
            initMap.put("自提时间", "");
            initMap.put("出库时间", "");
            initMap.put("库位号", "");
            initMap.put("配送方式", "");
            initMap.put("状态", "");
            listMap.add(initMap);
            // 登录用户存在时
            if (customerId != null) {
                // 订单编号
                map.put("no", servletRequest.getParameter("lastSearchNo"));
                // 交接单号
                map.put("shipNo", servletRequest.getParameter("lastSearchStorageShipNo"));
                // 商品车vin
                map.put("vin", servletRequest.getParameter("lastSearchStorageVin"));
                // 客户名
                map.put("customer", servletRequest.getParameter("lastSearchCustomer"));
                // 客户id
                map.put("customerId", servletRequest.getParameter("lastSearchCustomerId"));
                // 下单开始时间
                map.put("createTimeStart", servletRequest.getParameter("lastSearchCreateTimeStart"));
                // 下单结束时间
                map.put("createTimeEnd", servletRequest.getParameter("lastSearchCreateTimeEnd"));
                // 自提开始时间
                map.put("pickupSelfTimeStart", servletRequest.getParameter("lastSearchPickupSelfTimeStart"));
                // 自提结束时间
                map.put("pickupSelfTimeEnd", servletRequest.getParameter("lastSearchPickupSelfTimeEnd"));
                // 出库开始时间
                map.put("deliveryTimeStart", servletRequest.getParameter("lastSearchDeliveryTimeStart"));
                // 出库结束时间
                map.put("deliveryTimeEnd", servletRequest.getParameter("lastSearchDeliveryTimeEnd"));
                // 状态
                map.put("status", servletRequest.getParameter("lastSearchStatus"));

                // 登录用户为管理员时
                if (customerId != -1 && customerId != 0) {

                    // 获取某个仓库ID及其所有的下属仓库ID
                    List<Integer> idList = warehouseService.selectAllLevelIdByParentId(customerId);
                    map.put("idList", idList);
                } else {

                    // 登录用户为普通用户时只能查看自己创建的数据
                    if (customerId == -1) {
                        map.put("createUser", Integer.valueOf(userId));
                    }

                }
                List<Map<String, Object>> listMap1 = orderService.queryOrderRecordsForExportData(map);
                // 如果查询到结果集则覆盖初始值
                if (listMap1 != null && listMap1.size() > 0) {
                    listMap = listMap1;
                }
            }

            NsExcelReadXUtils.exportDatas("运输订单记录", "运输订单记录", listMap, servletRequest, servletResponse, "");

        } catch (RuntimeException e1) {
            e1.printStackTrace();
            logger.info(e1.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }

}
