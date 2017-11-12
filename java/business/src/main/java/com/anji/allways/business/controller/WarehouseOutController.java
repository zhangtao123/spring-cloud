package com.anji.allways.business.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.anji.allways.business.dto.DeliveryPlanDTO;
import com.anji.allways.business.entity.DamageEntity;
import com.anji.allways.business.entity.OrderEntity;
import com.anji.allways.business.mapper.DeliveryPlanEntityMapper;
import com.anji.allways.business.service.UserService;
import com.anji.allways.business.service.WarehouseOutboundService;
import com.anji.allways.business.service.WarehouseService;
import com.anji.allways.business.vo.DeliveryPlanVO;
import com.anji.allways.business.vo.HomePageVO;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.constants.RespMsg;
import com.anji.allways.common.util.NsExcelReadXUtils;
import com.anji.allways.common.util.StringUtil;

/**
 * @author zhangtao
 * @version $Id: WarehouseOutController.java, v 0.1 2017年9月4日 下午14:21:44 zhangtao Exp $
 */
@Controller
@RequestMapping("/business/outBound")
public class WarehouseOutController {
    private Logger                   logger = LogManager.getLogger(DealerIndexController.class);

    @Autowired
    private WarehouseOutboundService warehouseOutboundService;

    @Autowired
    private DeliveryPlanEntityMapper deliveryPlanMapper;

    @Autowired
    private UserService              userService;

    @Autowired
    private WarehouseService         warehouseService;

    @RequestMapping("/selectDeliveryPlanByCondition")
    @ResponseBody
    @LoggerManage(description = "根据过滤条件获取出库计划单列表")
    public BaseResponseModel<Object> selectDeliveryPlanByCondition(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        Integer pageNum = request.getReqData().getInteger("pageNum");
        Integer pageRows = request.getReqData().getInteger("pageRows");
        try {
            // 请求数据
            str = request.getReqData().toString();
            DeliveryPlanDTO bean = JSON.parseObject(str, DeliveryPlanDTO.class);
            str = request.getReqData().toString();

            // 获取用户ID
            String userId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(userId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }

            // 根据用户ID和所属组织类型查询所属仓库ID
            Integer customerId = userService.queryCustomerId(Integer.valueOf(userId), 2);
            // 如果是管理员获取所管理的仓库的集合，如果是普通员工，保存当前id作为创建者赋值
            Map<String, Object> map = new HashMap<String, Object>();
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
                map = warehouseOutboundService.queryDeliveryPlan(bean, pageNum, pageRows);
            }
            // map.put("userId", userId);
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.CREATE_DELIVERY_PLAN_FAILED);
            response.setRepMsg(RespMsg.QUERY_DELIVERY_PLAN_ID_NULL_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/selectWarehouseNameByUserId")
    @ResponseBody
    @LoggerManage(description = "根据登录人id查询出仓库名称，填充调度信息中仓库名")
    public BaseResponseModel<Object> selectWarehouseNameByUserId(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String userId = null;
        try {
            // 获取用户ID
            userId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(userId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }
            Map<String, Object> deliveryPlan = warehouseOutboundService.selectWareHouseNameByUserId(userId);
            response.setRepData(deliveryPlan);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.WAREHOUSE_NAME_SELECT_USERID_ERROR);
            response.setRepMsg(RespMsg.WAREHOUSE_NAME_SELECT_USERID_ERROR_MSG);
        } finally {
            logger.info("传入参数：{}" + userId);
        }
        return response;
    }

    @RequestMapping("/selectDriverInfoByMobile")
    @ResponseBody
    @LoggerManage(description = "根据司机电话查询司机信息，填充调度信息中司机信息")
    public BaseResponseModel<Object> selectDriverInfoByDriverMobile(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String mobile = null;
        try {
            mobile = request.getReqData().getString("mobile");
            Map<String, Object> map = warehouseOutboundService.selectDeliveryInfoByDriverMobile(mobile);
            if (map == null) {
                response.setRepCode(RespCode.DRIVER_MOBILE_EMPTY_ERROR);
                response.setRepMsg(RespMsg.DRIVER_MOBILE_EMPTY_ERROR_MSG);
                return response;
            }
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.DRIVER_SELECTONE_FAILED);
            response.setRepMsg(RespMsg.DRIVER_SELECTONE_MOBILE_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + mobile);
        }
        return response;
    }

    @RequestMapping("/selectOrderFYListByCondition")
    @ResponseBody
    @LoggerManage(description = "根据过滤条件获取运输单列表（发运）")
    public BaseResponseModel<Object> selectOrderFYByCondition(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        Map<String, Object> mapVue = new HashMap<String, Object>();
        String str = null;
        try {
            // 获取用户ID
            String userId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(userId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }
            // 当前页
            Integer pageNum = request.getReqData().getInteger("pageNum");
            // 当前记录数
            Integer pageRows = request.getReqData().getInteger("pageRows");
            mapVue.put("pageNumber", pageRows);
            mapVue.put("currentPage", pageNum);
            // 运输单号模糊查询
            if (StringUtil.isEmpty(request.getReqData().getString("no"), true)) {
                mapVue.put("no", null);
            } else {
                mapVue.put("no", request.getReqData().getString("no"));
            }
            // VIN码模糊查询
            if (StringUtil.isEmpty(request.getReqData().getString("vin"), true)) {
                mapVue.put("vin", null);
            } else {
                mapVue.put("vin", request.getReqData().getString("vin"));
            }
            String checkIds = request.getReqData().getString("checkIds");
            if (!StringUtil.isEmpty(checkIds, true)) {
                // 更新所包含的运单状态
                String[] lists = checkIds.split(",");
                List<Long> list = new ArrayList<Long>();
                for (int i = 0; i < lists.length; i++) {
                    list.add(Long.valueOf(lists[i]));
                }
                mapVue.put("list", list);
            }

            mapVue.put("customer", request.getReqData().getString("customer"));
            mapVue.put("createTimeStart", request.getReqData().getString("createTimeStart"));
            mapVue.put("createTimeEnd", request.getReqData().getString("createTimeEnd"));
            // 在修改发运出库计划时，查询中隐藏的过滤信息
            mapVue.put("customerId", request.getReqData().getString("customerId"));
            mapVue.put("destination", request.getReqData().getString("destination"));
            mapVue.put("consigneeId", request.getReqData().getString("consigneeId"));

            // 获取仓库该操作人所涉及的id
            // 根据用户ID和所属组织类型查询所属客户ID
            Integer customerId = userService.queryCustomerId(Integer.valueOf(userId), 2);
            // 登录用户存在时
            if (customerId != null) {
                // 登录用户为管理员时
                if (customerId != -1 && customerId != 0) {
                    // 获取某个仓库ID及其所有的下属仓库ID
                    List<Integer> idList = warehouseService.selectAllLevelIdByParentId(customerId);
                    mapVue.put("idList", idList);
                } else {
                    // 登录用户为普通用户时只能查看自己创建的数据
                    if (customerId == -1) {
                        mapVue.put("createUser", Integer.valueOf(userId));
                    }
                }
            }
            mapVue.put("flag", "fy");
            // 请求数据
            str = request.getReqData().toString();
            // 获取仓库信息
            Map<String, Object> retValue = warehouseOutboundService.selectOrderListByCondition(mapVue);
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.CREATE_DELIVERY_PLAN_FAILED);
            response.setRepMsg(RespMsg.QUERY_DELIVERY_PLAN_ID_NULL_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/selectOrderZTListByCondition")
    @ResponseBody
    @LoggerManage(description = "根据过滤条件获取运输单列表（自提）")
    public BaseResponseModel<Object> selectOrderZTByCondition(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        Map<String, Object> mapVue = new HashMap<String, Object>();
        String str = null;
        try {
            // 获取用户ID
            String userId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(userId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }
            // 当前页
            Integer pageNum = request.getReqData().getInteger("pageNum");
            // 当前记录数
            Integer pageRows = request.getReqData().getInteger("pageRows");
            mapVue.put("pageNumber", pageRows);
            mapVue.put("currentPage", pageNum);
            // 客户名称
            String customer = request.getReqData().getString("customer");
            if (!StringUtil.isEmpty(customer, true)) {
                mapVue.put("customer", "%" + customer + "%");
            } else {
                mapVue.put("customer", null);
            }
            mapVue.put("pickupSelfTimeStart", request.getReqData().getString("pickupSelfTimeStart"));
            mapVue.put("pickupSelfTimeEnd", request.getReqData().getString("pickupSelfTimeEnd"));
            // 获取仓库该操作人所涉及的id
            // 根据用户ID和所属组织类型查询所属客户ID
            Integer customerId = userService.queryCustomerId(Integer.valueOf(userId), 2);
            // 登录用户存在时
            if (customerId != null) {
                // 登录用户为管理员时
                if (customerId != -1 && customerId != 0) {
                    // 获取某个仓库ID及其所有的下属仓库ID
                    List<Integer> idList = warehouseService.selectAllLevelIdByParentId(customerId);
                    mapVue.put("idList", idList);
                } else {
                    // 登录用户为普通用户时只能查看自己创建的数据
                    if (customerId == -1) {
                        mapVue.put("createUser", Integer.valueOf(userId));
                    }
                }
            }
            mapVue.put("flag", "zt");
            // 请求数据
            str = request.getReqData().toString();
            // 查询仓库信息
            Map<String, Object> retValue = warehouseOutboundService.selectOrderListByCondition(mapVue);
            response.setRepData(retValue);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.CREATE_DELIVERY_PLAN_FAILED);
            response.setRepMsg(RespMsg.QUERY_DELIVERY_PLAN_ID_NULL_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/addDeliveryPlan")
    @ResponseBody
    @LoggerManage(description = "新增出库计划单")
    public BaseResponseModel<Object> addDeliveryPlan(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = request.getReqData().toString();
        try {
            // int userId = Integer.parseInt(request.getUserId());

            // 获取用户ID
            String userId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(userId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }

            // 判断是发运还是自提
            int type = request.getReqData().getInteger("type");
            // 获取到订单id
            String orderIds = (String) request.getReqData().get("orderIds");
            if (StringUtils.isEmpty(orderIds)) {
                throw new RuntimeException();
            }
            str = request.getReqData().getJSONObject("deliveryPlanEntity").toString();
            DeliveryPlanDTO deliveryPlanEntity = JSON.parseObject(str, DeliveryPlanDTO.class);
            Integer re = warehouseOutboundService.addDeliveryPlan(Integer.parseInt(userId), deliveryPlanEntity, orderIds, type);
            if (re == 5015) {
                response.setRepData(re);
                response.setRepCode(RespCode.SELECT_ORDER_STATUS_MODIFICATION);
                response.setRepMsg(RespMsg.SELECT_ORDER_STATUS_MODIFICATION_MSG);
            } else {
                response.setRepData(re);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.CREATE_DELIVERY_PLAN_FAILED);
            response.setRepMsg(RespMsg.CREATE_DRIVER_PLAN_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }
        return response;
    }

    @RequestMapping("/selectDeliveryPlanDetailZT")
    @ResponseBody
    @LoggerManage(description = "查询出库计划单明细【自提】")
    public BaseResponseModel<Object> selectDeliveryPlanDetailZT(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = request.getReqData().toString();
        try {
            // 判出库计划单id
            int id = request.getReqData().getInteger("id");
            DeliveryPlanVO deliveryPlanVO = warehouseOutboundService.selectDeliveryDetailZT(id);
            response.setRepData(deliveryPlanVO);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.CREATE_DELIVERY_PLAN_FAILED);
            response.setRepMsg(RespMsg.CREATE_DRIVER_PLAN_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }
        return response;
    }

    @RequestMapping("/selectDeliveryPlanDetailFY")
    @ResponseBody
    @LoggerManage(description = "查询出库计划单明细【发运】")
    public BaseResponseModel<Object> selectDeliveryPlanDetailFY(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = request.getReqData().toString();
        try {
            // 判出库计划单id
            int id = request.getReqData().getInteger("id");
            Map<String, Object> reMap = warehouseOutboundService.selectDeliveryDetailFY(id);
            response.setRepData(reMap);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.DRIVER_DETAIL_FY_FAILED);
            response.setRepMsg(RespMsg.DRIVER_DETAIL_FY_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }
        return response;
    }

    @RequestMapping("/updateDeliveryPlan")
    @ResponseBody
    @LoggerManage(description = "更新出库计划订单（取消，审核,领取钥匙）")
    public BaseResponseModel<Object> updateDeliveryPlan(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = request.getReqData().toString();
        String doType = request.getReqData().getString("doType");

        try {

            // 获取用户ID
            String userId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(userId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }

            // 请求数据
            str = request.getReqData().toString();
            DeliveryPlanDTO bean = JSON.parseObject(str, DeliveryPlanDTO.class);
            bean.setUpdateTime(new Date());
            bean.setUpdateUser(Integer.parseInt(userId));
            DeliveryPlanDTO deliveryPlan = null;
            if ("FY".equals(doType)) {
                deliveryPlan = warehouseOutboundService.updateDeliveryPlanFy(bean);
            } else {
                // deliveryPlan =
                // warehouseOutboundService.updateDeliveryPlanZt(bean,doType);
                deliveryPlan = warehouseOutboundService.updateDeliveryPlanZtForTwo(bean, doType);
            }

            if (deliveryPlan != null) {
                response.setRepData(deliveryPlan);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else {
                response.setRepData(null);
                response.setRepCode(RespCode.UPDATE_DELIVERY_PLAN_ID_FAILED);
                response.setRepMsg(RespMsg.CANCEL_DELIVERY_PLAN_ID_FAILED_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.UPDATE_DELIVERY_PLAN_ID_FAILED);
            response.setRepMsg(RespMsg.CANCEL_DELIVERY_PLAN_ID_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }
        return response;
    }

    @RequestMapping("/selectDeliveryPlanOrderForEditZt")
    @ResponseBody
    @LoggerManage(description = "查询出库计划页面的查询(编辑)(自提)")
    public BaseResponseModel<Object> selectDeliveryPlanOrderForEditZt(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = request.getReqData().toString();
        try {
            int id = (int) request.getReqData().get("id");
            // 获取到出库计划单以及其下的包含的订单列表
            DeliveryPlanVO deliveryPlanDTO = deliveryPlanMapper.selectDeliveryPlanOrdersForEditZt(id);
            response.setRepData(deliveryPlanDTO);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.UPDATE_DELIVERY_PLAN_ID_FAILED);
            response.setRepMsg(RespMsg.UPDATE_DELIVERY_PLAN_ID_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }
        return response;
    }

    @RequestMapping("/selectDeliveryPlanOrderForEditFy")
    @ResponseBody
    @LoggerManage(description = "编辑出库计划(编辑)(发运)(查询)")
    public BaseResponseModel<Object> selectDeliveryPlanOrderForEditFy(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = request.getReqData().toString();
        try {
            int id = request.getReqData().getInteger("id");
            // 获取到出库计划单以及其下的包含的订单列表
            Map<String, Object> reMap = warehouseOutboundService.selectDeliveryPlanOrdersForEditFy(id);
            response.setRepData(reMap);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.UPDATE_SELECT_DELIVERY_PLAN_ID_FAILED);

            response.setRepMsg(RespMsg.UPDATE_SELECT_DELIVERY_PLAN_ID_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }
        return response;
    }

    @RequestMapping("/updateDeliveryPlanOrderForEdit")
    @ResponseBody
    @LoggerManage(description = "编辑出库计划(编辑)(自提)(更新)")
    public BaseResponseModel<Object> updateDeliveryPlanOrderForEditZT(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = request.getReqData().toString();
        try {
            // 获取用户ID
            String userId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(userId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }

            // 加入的运单id
            String orderIds = request.getReqData().getString("orderIds");
            // 编辑出库计划id
            int deliveryPlanId = request.getReqData().getInteger("deliveryPlanId");

            String type = request.getReqData().getString("type");

            int re = warehouseOutboundService.updateDeliveryPlanOrderForEditZt(orderIds, deliveryPlanId, Integer.parseInt(userId), type);
            if (re > 0) {
                response.setRepData(re);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else {
                response.setRepData(re);
                response.setRepCode(RespCode.UPDATE_DELIVERY_PLAN_ID_FAILED);
                response.setRepMsg(RespMsg.UPDATE_DELIVERY_PLAN_ID_FAILED_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.UPDATE_DELIVERY_PLAN_ID_FAILED);
            response.setRepMsg(RespMsg.UPDATE_DELIVERY_PLAN_ID_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }
        return response;
    }

    @RequestMapping("/cancelDeliveryPlan")
    @ResponseBody
    @LoggerManage(description = "取消出库计划单")
    public BaseResponseModel<Object> updateDeliveryPlanOrderForEditFy(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = request.getReqData().toString();
        try {
            // 加入的运单id
            str = request.getReqData().toString();
            DeliveryPlanDTO bean = JSON.parseObject(str, DeliveryPlanDTO.class);

            // 1.0取消业务方法
            // DeliveryPlanDTO deliveryPlan = warehouseOutboundService.cancelDeliveryPlan(bean);
            // 2.0取消业务方法
            DeliveryPlanDTO deliveryPlan = warehouseOutboundService.cancelDeliveryPlanForTwo(bean);

            if (deliveryPlan != null) {
                response.setRepData(deliveryPlan);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else {
                response.setRepCode(RespCode.UPDATE_DELIVERY_PLAN_ID_FAILED);
                response.setRepMsg(RespMsg.UPDATE_DELIVERY_PLAN_ID_FAILED_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.UPDATE_DELIVERY_PLAN_ID_FAILED);
            response.setRepMsg(RespMsg.UPDATE_DELIVERY_PLAN_ID_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }
        return response;
    }

    @RequestMapping("/carHandover")
    @ResponseBody
    @LoggerManage(description = "车辆交接【无质损】")
    public BaseResponseModel<Object> carHandover(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = request.getReqData().toString();
        try {
            // 返回交接类型
            str = request.getReqData().getJSONObject("damageEntity").toString();
            // 获取所添加质损的json对象
            DamageEntity damageEntity = JSON.parseObject(str, DamageEntity.class);
            // 获取到运单号的id
            int id = request.getReqData().getInteger("orderId");
            // 获取用户ID
            String userId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(userId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }
            // 交接时添加质损信息
            int damageId = warehouseOutboundService.saveDamageEntity(damageEntity, id, "flag", userId);
            if (damageId > 0) {
                response.setRepData(damageId);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else {
                response.setRepData(damageId);
                response.setRepCode(RespCode.ADD_DAMAGE_FAILED);
                response.setRepMsg(RespMsg.ADD_DAMAGE_FAILED_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepData(null);
            response.setRepCode(RespCode.ADD_SHIP_RECEIPT_FAILED);
            response.setRepMsg(RespMsg.ADD_SHIP_RECEIPT_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }
        return response;
    }

    @RequestMapping("/addDamageEntity")
    @ResponseBody
    @LoggerManage(description = "新增质损信息【有质损】")
    public BaseResponseModel<Object> addDamageEntity(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = request.getReqData().toString();
        try {
            int id = request.getReqData().getInteger("id");
            str = request.getReqData().getJSONObject("damageEntity").toString();
            // 获取所添加质损的json对象
            DamageEntity damageEntity = JSON.parseObject(str, DamageEntity.class);
            int re = 0;

            // 获取用户ID
            String userId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(userId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }

            // 有损
            re = warehouseOutboundService.saveDamageEntity(damageEntity, id, null, userId);
            if (re > 0) {
                response.setRepData(re);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else {
                response.setRepData(re);
                response.setRepCode(RespCode.ADD_DAMAGE_FAILED);
                response.setRepMsg(RespMsg.ADD_DAMAGE_FAILED_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.ADD_DAMAGE_FAILED);
            response.setRepMsg(RespMsg.ADD_DAMAGE_FAILED_MSG);
            logger.info("传入参数：{}" + str.toString());
        }
        return response;
    }

    @RequestMapping("/carHandoverBack")
    @ResponseBody
    @LoggerManage(description = "查询车辆交接后的质损信息")
    public BaseResponseModel<Object> carHandoverBack(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = request.getReqData().toString();
        try {
            int orderId = request.getReqData().getInteger("orderId");
            String type = request.getReqData().getString("type");

            Map<String, Object> map = warehouseOutboundService.carHandoverBack(orderId, type);
            if (map != null) {
                response.setRepData(map);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else {
                response.setRepData(null);
                response.setRepCode(RespCode.SELECT_DAMAGE_BACK_FAILED);
                response.setRepMsg(RespMsg.SELECT_DAMAGE_BACK_FAILED_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.UPDATE_DELIVERY_PLAN_ID_FAILED);
            response.setRepMsg(RespMsg.CANCEL_DELIVERY_PLAN_ID_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }
        return response;
    }

    @RequestMapping("/printDeliveryPlan")
    @ResponseBody
    @LoggerManage(description = "交接单打印接口")
    public BaseResponseModel<Object> printDeliveryPlan(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = request.getReqData().toString();
        try {
            int deliveryPlanId = request.getReqData().getInteger("deliveryPlanId");
            // DeliveryPlanVO deliveryPlanVO =
            // deliveryPlanMapper.printDeliveryPlanVOMapFy(deliveryPlanId);
            DeliveryPlanVO deliveryPlanVO = warehouseOutboundService.printDeliveryPlan(deliveryPlanId);
            if (deliveryPlanVO != null) {
                response.setRepData(deliveryPlanVO);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else {
                response.setRepData(null);
                response.setRepCode(RespCode.SELECT_DAMAGE_BACK_FAILED);
                response.setRepMsg(RespMsg.SELECT_DAMAGE_BACK_FAILED_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.UPDATE_DELIVERY_PLAN_ID_FAILED);
            response.setRepMsg(RespMsg.CANCEL_DELIVERY_PLAN_ID_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str.toString());
        }
        return response;
    }

    @RequestMapping("/exportInfoByExcel")
    @LoggerManage(description = "导出出库记录信息")
    public void exportInfoByExcel(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Exception {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            String userId = servletRequest.getParameter("userId");
            // 根据用户ID和所属组织类型查询所属客户ID
            Integer customerId = userService.queryCustomerId(Integer.valueOf(userId), 2);
            List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("出库计划单号", "");
            map1.put("创建时间", "");
            map1.put("驳运车牌", "");
            map1.put("仓库", "");
            map1.put("计划自提时间", "");
            map1.put("计划发运时间", "");
            map1.put("出库时间", "");
            map1.put("交接单号", "");
            map1.put("操作人", "");
            map1.put("出库类型", "");
            map1.put("车辆数", "");
            map1.put("状态", "");
            listMap.add(map1);
            String transportType = null;
            // 登录用户存在时
            if (customerId != null) {
                transportType = servletRequest.getParameter("transportType");
                // VIN码
                map.put("no", servletRequest.getParameter("no"));
                map.put("truckNumber", servletRequest.getParameter("truckNumber"));
                // 创建时间
                map.put("createStartTime", servletRequest.getParameter("createStartTime"));
                map.put("createEndTime", servletRequest.getParameter("createEndTime"));
                map.put("shipNo", servletRequest.getParameter("shipNo"));
                map.put("customer", servletRequest.getParameter("customer"));
                map.put("transportType", transportType);
                map.put("status", servletRequest.getParameter("status"));
                // 出库时间
                map.put("deliveryTimeStart", servletRequest.getParameter("deliveryTimeStart"));
                map.put("deliveryTimeEnd", servletRequest.getParameter("deliveryTimeEnd"));
                // 计划自提时间
                map.put("pickupPlanTimeStart", servletRequest.getParameter("pickupPlanTimeStart"));
                map.put("pickupPlanTimeEnd", servletRequest.getParameter("pickupPlanTimeEnd"));
                map.put("warehouseName", servletRequest.getParameter("warehouseName"));
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
                List<Map<String, Object>> listMap1 = deliveryPlanMapper.queryDeliveryPlanForExportForTwo(map);
                if (listMap1 != null && listMap1.size() > 0) {
                    listMap = listMap1;
                }
            }
            NsExcelReadXUtils.exportDatas("出库计划单", "出库计划单", listMap, servletRequest, servletResponse, "");

        } catch (RuntimeException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }

    // *************************************以下为改版后新增接口*****************************************************

    @RequestMapping("/selectHomePageData")
    @ResponseBody
    @LoggerManage(description = "首页数据查询")
    public BaseResponseModel<Object> selectHomePage(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        String userId = null;
        try {
            // 获取用户ID
            userId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(userId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }

            // 根据用户ID和所属组织类型查询所属仓库ID
            Integer customerId = userService.queryCustomerId(Integer.valueOf(userId), 2);
            // 如果是管理员获取所管理的仓库的集合，如果是普通员工，保存当前id作为创建者赋值
            // 登录用户存在时
            if (customerId != null) {
                // 登录用户为管理员时
                if (customerId != -1 && customerId != 0) {
                    // 获取某个仓库ID及其所有的下属仓库ID
                    List<Integer> idList = warehouseService.selectAllLevelIdByParentId(customerId);
                    map.put("warehouseIdList", idList);
                } else {
                    // 登录用户为普通用户时只能查看自己创建的数据
                    if (customerId == -1) {
                        map.put("warehouseIdList", Integer.valueOf(userId));
                    }
                }
            }
            HomePageVO homePage = warehouseOutboundService.selectHomepageData(map);
            if (homePage != null) {
                response.setRepData(homePage);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else {
                response.setRepData(null);
                response.setRepCode(RespCode.SELECT_HOMEPAGE_DATA_FAILED);
                response.setRepMsg(RespMsg.SELECT_HOMEPAGE_DATA_ERROR_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.SELECT_HOMEPAGE_DATA_FAILED);
            response.setRepMsg(RespMsg.SELECT_HOMEPAGE_DATA_ERROR_MSG);
        } finally {
            logger.info("传入参数：{}" + userId);
        }
        return response;
    }

    @RequestMapping("/selectOrderListForDispatch")
    @ResponseBody
    @LoggerManage(description = "查询待调度列表数据")
    public BaseResponseModel<Object> selectOrderListByConditionForDispatch(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        String str = request.getReqData().toString();
        String userId = null;
        try {
            // 获取用户ID
            userId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(userId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }
            // 根据用户ID和所属组织类型查询所属仓库ID
            Integer customerId = userService.queryCustomerId(Integer.valueOf(userId), 2);
            // 如果是管理员获取所管理的仓库的集合，如果是普通员工，保存当前id作为创建者赋值
            // 登录用户存在时
            if (customerId != null) {
                // 登录用户为管理员时
                if (customerId != -1 && customerId != 0) {
                    // 获取某个仓库ID及其所有的下属仓库ID
                    List<Integer> idList = warehouseService.selectAllLevelIdByParentId(customerId);
                    map.put("warehouseIdList", idList);
                } else {
                    // 登录用户为普通用户时只能查看自己创建的数据
                    if (customerId == -1) {
                        map.put("warehouseIdList", Integer.valueOf(userId));
                    }
                }
            }
            // 运输单号模糊查询
            if (StringUtil.isEmpty(request.getReqData().getString("transportType"), true)) {
                map.put("transportType", null);
            } else {
                map.put("transportType", request.getReqData().getString("transportType"));
            }
            // 运输单号模糊查询
            if (StringUtil.isEmpty(request.getReqData().getString("status"), true)) {
                map.put("status", null);
            } else {
                map.put("status", request.getReqData().getString("status"));
            }
            List<OrderEntity> orderList = warehouseOutboundService.selectOrderListByConditionForDispatch(map);
            response.setRepData(orderList);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            response.setRepCode(RespCode.SELECT_ORDERLIST_FOR_DISPATCH_FAILED);
            response.setRepMsg(RespMsg.SELECT_ORDERLIST_FOR_DISPATCH_ERROR_MSG);
        } finally {
            logger.info("传入参数：{}" + str + userId);
        }
        return response;
    }

    @RequestMapping("/selectDeliveryPlanForTwo")
    @ResponseBody
    @LoggerManage(description = "查询出库计划单列表:待自提,待出库")
    public BaseResponseModel<Object> selectDeliveryPlanForTwo(@RequestBody BaseRequestModel request) throws Exception {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            // 请求数据
            str = request.getReqData().toString();
            DeliveryPlanDTO bean = JSON.parseObject(str, DeliveryPlanDTO.class);
            str = request.getReqData().toString();
            // 获取用户ID
            String userId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(userId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }
            // 根据用户ID和所属组织类型查询所属仓库ID
            Integer customerId = userService.queryCustomerId(Integer.valueOf(userId), 2);
            // 如果是管理员获取所管理的仓库的集合，如果是普通员工，保存当前id作为创建者赋值
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
                List<DeliveryPlanDTO> list = warehouseOutboundService.queryDeliveryPlanPro(bean);
                if (list != null) {
                    response.setRepData(list);
                    response.setRepCode(RespCode.SUCCESS);
                    response.setRepMsg(RespMsg.SUCCESS_MSG);
                } else {
                    response.setRepCode(RespCode.CREATE_DELIVERY_PLAN_FAILED);
                    response.setRepMsg(RespMsg.QUERY_DELIVERY_PLAN_ID_NULL_MSG);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.CREATE_DELIVERY_PLAN_FAILED);
            response.setRepMsg(RespMsg.QUERY_DELIVERY_PLAN_ID_NULL_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/checkKeyIsTaken")
    @ResponseBody
    @LoggerManage(description = "车辆交接时，检查车辆是否已领取钥匙")
    public BaseResponseModel<Object> checkKeyIsTaken(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        // String doType = request.getReqData().getString("doType");
        String str = null;
        try {
            // 请求数据
            str = request.getReqData().toString();
            // DeliveryPlanDTO bean = JSON.parseObject(str, DeliveryPlanDTO.class);
            str = request.getReqData().toString();
            Map<String, Object> map = new HashMap<String, Object>();
            int id = request.getReqData().getInteger("id");
            /*int orderId = request.getReqData().getInteger("orderId");
            int vehicleId = request.getReqData().getInteger("vehicleId");
            if (!StringUtil.isEmpty(id, true) && !StringUtil.isEmpty(vehicleId, true) && !StringUtil.isEmpty(orderId, true)) {
                map.put("id", id);
                map.put("orderId", orderId);
                map.put("vehicleId", vehicleId);
            } else {
                throw new RuntimeException();
            }*/

            if (!StringUtil.isEmpty(id, true)) {
                map.put("id", id);
            } else {
                throw new RuntimeException();
            }

            // 获取用户ID
            String userId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(userId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }

            /* bean.setUpdateTime(new Date());
            bean.setUpdateUser(Integer.parseInt(userId));*/

            int rs = warehouseOutboundService.checkKeyIsTaken(map);
            if (rs > 0) {
                response.setRepData(1);
                response.setRepCode(RespCode.SUCCESS);
                response.setRepMsg(RespMsg.SUCCESS_MSG);
            } else {
                response.setRepData(-1);
                response.setRepCode(RespCode.CHECK_KEY_IS_TAKEN_FAILED);
                response.setRepMsg(RespMsg.CHECK_KEY_IS_TAKEN_FAILED_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.CHECK_KEY_IS_TAKEN_FAILED);
            response.setRepMsg(RespMsg.CHECK_KEY_IS_TAKEN_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }

    @RequestMapping("/selectDeliveryPlanManage")
    @ResponseBody
    @LoggerManage(description = "查询出库管理")
    public BaseResponseModel<Object> selectDeliveryPlanManage(@RequestBody BaseRequestModel request) {
        BaseResponseModel<Object> response = new BaseResponseModel<Object>();
        String str = null;
        try {
            Integer pageNum = request.getReqData().getInteger("pageNum");
            Integer pageRows = request.getReqData().getInteger("pageRows");
            str = request.getReqData().toString();
            DeliveryPlanDTO bean = JSON.parseObject(str, DeliveryPlanDTO.class);
            str = request.getReqData().toString();

            // 获取用户ID
            String userId = StringUtil.getUserId(request.getToken());
            if (StringUtil.isEmpty(userId, true)) {
                response.setRepCode(RespCode.LOGIN_FAILED);
                response.setRepMsg(RespMsg.AUTH_ERROR_TOKEN_MSG);
                return response;
            }

            // 根据用户ID和所属组织类型查询所属仓库ID
            Integer customerId = userService.queryCustomerId(Integer.valueOf(userId), 2);
            // 如果是管理员获取所管理的仓库的集合，如果是普通员工，保存当前id作为创建者赋值
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
            }
            Map<String, Object> map = warehouseOutboundService.selectDeliveryPlanManage(bean, pageNum, pageRows);
            response.setRepData(map);
            response.setRepCode(RespCode.SUCCESS);
            response.setRepMsg(RespMsg.SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            response.setRepCode(RespCode.CHECK_KEY_IS_TAKEN_FAILED);
            response.setRepMsg(RespMsg.CHECK_KEY_IS_TAKEN_FAILED_MSG);
        } finally {
            logger.info("传入参数：{}" + str);
        }
        return response;
    }
}
