/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anji.allways.business.common.constant.SlnsConstants;
import com.anji.allways.business.entity.CustomerEntity;
import com.anji.allways.business.entity.OrderEntity;
import com.anji.allways.business.entity.UserEntity;
import com.anji.allways.business.entity.VehicleEntity;
import com.anji.allways.business.enums.RowDictCodeE;
import com.anji.allways.business.mapper.CustomerEntityMapper;
import com.anji.allways.business.mapper.OrderEntityMapper;
import com.anji.allways.business.mapper.UserEntityMapper;
import com.anji.allways.business.mapper.VehicleEntityMapper;
import com.anji.allways.business.mapper.WarehouseEntityMapper;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.service.SerialNumberBuildService;
import com.anji.allways.business.service.TransportOrderService;
import com.anji.allways.business.service.UserService;
import com.anji.allways.common.util.DateUtil;
import com.anji.allways.common.util.PageUtil;
import com.anji.allways.common.util.StringUtil;

/**
 * @author 李军
 * @version $Id: DealerTransportOrderServiceImpl.java, v 0.1 2017年9月4日 下午6:49:59 李军 Exp $
 */
@Service
@Transactional
public class TransportOrderServiceImpl implements TransportOrderService {

    @Autowired
    private OrderEntityMapper        orderEntityMapper;

    @Autowired
    private VehicleEntityMapper      vehicleEntityMapper;

    @Autowired
    private CustomerEntityMapper     customerEntityMapper;

    @Autowired
    private UserEntityMapper         userEntityMapper;

    @Autowired
    private SerialNumberBuildService serialNumberBuildService;

    @Autowired
    private WarehouseEntityMapper    warehouseEntityMapper;

    // 获取数据字典Service
    @Autowired
    private DictService              dictService;

    @Autowired
    private UserService              userService;

    /**
     * @param userId
     * @param vehicle
     * @return
     * @see com.anji.allways.business.service.OrderService#getOrderTransport(java.lang.Integer, java.lang.Integer)
     */
    @Override
    public Map<String, Object> getOrderTransport(Integer userId, Integer vehicleId) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 查询车辆信息
        VehicleEntity vehicleEntity = vehicleEntityMapper.getOrderTransport(vehicleId);
        map.put("id", vehicleEntity.getId());
        map.put("vin", vehicleEntity.getVin());
        map.put("warehouseName", vehicleEntity.getWarehouseName());
        map.put("location", vehicleEntity.getLocation());
        map.put("now", DateUtil.getNowDateAndMinus());
        map.put("nextDay", DateUtil.getTomorowDay());

        Map<String, Object> mapModelPicturMap = new HashMap<String, Object>();
        mapModelPicturMap.put("brand", vehicleEntity.getBrand());
        mapModelPicturMap.put("series", vehicleEntity.getSeries());
        mapModelPicturMap.put("model", vehicleEntity.getModel());
        mapModelPicturMap.put("announceYear", vehicleEntity.getAnnounceYear());
        String modelPicturMap = vehicleEntityMapper.getmodelPicture(mapModelPicturMap);
        vehicleEntity.setLogoPicturePath(modelPicturMap);
        map.put("logoPicturePath", vehicleEntity.getLogoPicturePath());

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            Date businessStartTime = format.parse(vehicleEntity.getBusinessStartTime());
            map.put("businessStartTime", vehicleEntity.getBusinessStartTime());
            Date businessendtime = format.parse("24:00");
            map.put("businessendtime", vehicleEntity.getBusinessendtime());
            Calendar begin = Calendar.getInstance();
            begin.setTime(new Date());
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            if (DateUtil.getHour(new Date()) < 21) {
                begin.add(Calendar.HOUR, 3);
                vehicleEntity.setBusinessStartTime(format.format(begin.getTime()));
                begin.setTime(businessStartTime);
                while (businessStartTime.before(businessendtime)) {
                    Map<String, Object> mapTime = new HashMap<String, Object>();
                    begin.add(Calendar.MINUTE, 30);
                    businessStartTime = begin.getTime();
                    if (businessStartTime.after(format.parse(vehicleEntity.getBusinessStartTime()))) {
                        mapTime.put("rowTimes", format.format(begin.getTime()));
                        list.add(mapTime);
                    }
                }
            } else {
                Map<String, Object> mapTime = new HashMap<String, Object>();
                mapTime.put("rowTimes", "00:00");
                list.add(mapTime);
            }

            List<Map<String, Object>> tomorrow = new ArrayList<Map<String, Object>>();
            begin.setTime(format.parse("00:00"));
            for (int i = 0; i < 49; i++) {
                Map<String, Object> mapTime = new HashMap<String, Object>();
                mapTime.put("rowTimes", format.format(begin.getTime()));
                tomorrow.add(mapTime);
                begin.add(Calendar.MINUTE, 30);
            }

            map.put("tomorrow", tomorrow);
            map.put("today", list);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        UserEntity userEntity = userEntityMapper.selectUserAndInfo(userId);
        map.put("incomeId", userEntity.getConsigneeId());
        map.put("incomeName", userEntity.getName());
        map.put("contactorMobile", userEntity.getTelephone());
        map.put("address", userEntity.getAddress());
        map.put("customer", userEntity.getCustomer());

        return map;
    }

    /**
     * @param mapVue
     * @return
     * @see com.anji.allways.business.service.TransportOrderService#createTransportOrder(java.util.Map)
     */
    @Override
    public int createTransportOrder(Map<String, Object> mapVue) {
        synchronized (this) {
            OrderEntity orderEntity = new OrderEntity();
            // 查询车辆信息
            int count = 0;

            // 校验重复下订单
            int countOrder = vehicleEntityMapper.queryOrderVehicleCount(Integer.parseInt(mapVue.get("vehicleId").toString()));
            if (countOrder > 0) {
                return -1;
            }

            VehicleEntity vehicleEntity = vehicleEntityMapper.selectAllVehicle(Integer.valueOf(mapVue.get("vehicleId").toString()));
            if (vehicleEntity.getLockStatus() == 1) {
                return -2;
            }

            if (vehicleEntity.getVehicleStatus() == 0) {
                orderEntity.setNo(serialNumberBuildService.getSerialNumberByType(SlnsConstants.SERIAL_NUMBER_TYPE_TRANSPORT));
                orderEntity.setVin(vehicleEntity.getVin());
                orderEntity.setVehicleId(vehicleEntity.getId());
                orderEntity.setWarehouseName(vehicleEntity.getWarehouseName());
                orderEntity.setWarehouseId(vehicleEntity.getWarehouseId());
                orderEntity.setCustomer(vehicleEntity.getCustomer());
                orderEntity.setLocation(vehicleEntity.getLocation());
                orderEntity.setStorageTime(vehicleEntity.getStorageTime());
                orderEntity.setCreateTime(new Date());
                orderEntity.setStoragePlanId(vehicleEntity.getStoragePlanId());
                orderEntity.setDeliveryPlanId(vehicleEntity.getDeliveryPlanId());
                orderEntity.setStorageTime(vehicleEntity.getStorageTime());
                orderEntity.setDeliveryTime(vehicleEntity.getDeliveryTime());
                orderEntity.setIsVoid(0);
                orderEntity.setCreateUser(Integer.valueOf(mapVue.get("userId").toString()));
                if (mapVue.get("pickupSelfTime") != null) {
                    orderEntity.setPickupSelfTime(DateUtil.formateStringToDate(mapVue.get("pickupSelfTime").toString(), "yyyy-MM-dd HH:mm:ss"));
                } else {
                    orderEntity.setPickupSelfTime(null);
                }

                // 获取客户信息
                CustomerEntity customerEntity = customerEntityMapper.queryCustomerInfoByUserId(Integer.valueOf(mapVue.get("userId").toString()));
                orderEntity.setCustomer(customerEntity.getName());
                orderEntity.setCustomerId(customerEntity.getId());
                orderEntity.setConsigneeId(Integer.valueOf(mapVue.get("incomeId").toString()));
                orderEntity.setDestination(customerEntity.getProvinceName() + customerEntity.getCityName() + customerEntity.getDistrictName() + customerEntity.getAddress());
                // 获取用户姓名
                UserEntity userEntity = userEntityMapper.selectByPrimaryKey(Integer.valueOf(mapVue.get("userId").toString()));
                orderEntity.setOperatorName(userEntity.getName());
                orderEntity.setTransportType(mapVue.get("transportType").toString());
                orderEntity.setStatus(1);

                vehicleEntity = vehicleEntityMapper.selectByPrimaryKey(Integer.valueOf(mapVue.get("vehicleId").toString()));
                vehicleEntity.setVehicleStatus(1);
                vehicleEntity.setUpdateTime(new Date());
                vehicleEntity.setUpdateUser(Integer.valueOf(mapVue.get("userId").toString()));

                vehicleEntityMapper.updateByPrimaryKeySelective(vehicleEntity);
                count = orderEntityMapper.insertSelective(orderEntity);
            }
            return count;
        }
    }

    /**
     * @param map
     * @return
     * @see com.anji.allways.business.service.VehicleDetailService#getStockDetail(java.util.Map)
     */
    @Override
    public Map<String, Object> getTransportOrder(Map<String, Object> map) {
        Map<String, Object> mapVue = new HashMap<String, Object>();
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNumber(Integer.parseInt(map.get("pageNumber").toString()));
        pageUtil.setTotalNumber(vehicleEntityMapper.getCountToTransport(map));
        pageUtil.setDbNumber(pageUtil.getDbIndex() + pageUtil.getPageNumber());
        pageUtil.setCurrentPage(Integer.parseInt(map.get("currentPage").toString()));
        map.put("pageIndex", pageUtil.getDbIndex());
        map.put("pageNumber", pageUtil.getDbNumber());
        List<VehicleEntity> list = vehicleEntityMapper.getTransportOrder(map);
        Map<String, Object> mapModelPicturMap = new HashMap<String, Object>();
        for (VehicleEntity vehicleEntity : list) {
            if (!StringUtil.isEmpty(vehicleEntity.getPickupSelfTime(), true)) {
                vehicleEntity.setPickupSelfTime(vehicleEntity.getPickupSelfTime().substring(0, 16));
            }
            mapModelPicturMap.put("deliveryId", vehicleEntity.getDeliveryId());
            mapModelPicturMap.put("brand", vehicleEntity.getBrand());
            mapModelPicturMap.put("series", vehicleEntity.getSeries());
            mapModelPicturMap.put("model", vehicleEntity.getModel());
            mapModelPicturMap.put("announceYear", vehicleEntity.getAnnounceYear());
            String modelPicturMap = vehicleEntityMapper.getmodelPicture(mapModelPicturMap);
            vehicleEntity.setLogoPicturePath(modelPicturMap);
        }

        mapVue.put("rows", list);
        mapVue.put("page", pageUtil);
        mapVue.put("total", pageUtil.getTotalNumber());
        return mapVue;
    }

    /**
     * @param userId
     * @return
     * @see com.anji.allways.business.service.TransportOrderService#screenTransportOrder(java.lang.Integer)
     */
    @Override
    public Map<String, Object> screenTransportOrder(Integer userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 查询所有仓库
        List<Map<String, Object>> allWarehouse = warehouseEntityMapper.selectByCustomerId(userId);
        map.put("allWarehouse", allWarehouse);
        return map;
    }

    /**
     * @param vehicleId
     * @param orderId
     * @return
     * @see com.anji.allways.business.service.TransportOrderService#cancelOrder(java.lang.Integer, java.lang.Integer)
     */
    @Override
    public int cancelOrder(Integer vehicleId, Integer orderId) {
        synchronized (this) {
            VehicleEntity vehicleEntity = vehicleEntityMapper.selectByPrimaryKey(vehicleId);
            if (vehicleEntity.getVehicleStatus() != 1) {
                return 0;
            }
            vehicleEntity = new VehicleEntity();
            vehicleEntity.setId(vehicleId);
            vehicleEntity.setVehicleStatus(0);
            int vehicleCount = vehicleEntityMapper.updateByPrimaryKeySelective(vehicleEntity);
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setId(orderId);
            orderEntity.setStatus(6);
            int orderCount = orderEntityMapper.updateByPrimaryKeySelective(orderEntity);

            return vehicleCount + orderCount;
        }
    }

    /**
     * @param entity
     * @param pageNum
     * @param pageRows
     * @return
     * @see com.anji.allways.business.service.TransportOrderService#queryOrderByFilers(com.anji.allways.business.entity.OrderEntity, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public Map<String, Object> queryOrderByFilers(OrderEntity entity, Integer pageNum, Integer pageRows) {
        Map<String, Object> map = new HashMap<String, Object>();

        // 查询总数(全部订单)
        int num = orderEntityMapper.queryOrderByFilersCount(entity);

        // 查询分页信息
        List<OrderEntity> orderList = new ArrayList<>();
        if (num > 0) {

            entity.setPageNumber(pageRows);
            entity.setTotalNumber(num);
            entity.setCurrentPage(pageNum);

            // 查询分页信息
            orderList = orderEntityMapper.queryOrderByFilers(entity);

            // 获取运输订单状态数据字典
            Map<String, String> retValue = dictService.queryAllByCode(RowDictCodeE.ORDER_STATUS.getValue());
            if (null != orderList && orderList.size() > 0) {
                for (int i = 0; i < orderList.size(); i++) {
                    OrderEntity vo = orderList.get(i);

                    // 设置运输订单状态 7为确认交接状态时，页面显示待收货
                    if (vo.getStatus() == 7 || vo.getStatus() == 9) {
                        vo.setStatus(4);
                    }

                    // 设置运输订单状态名称
                    String statusName = retValue.get(String.valueOf(vo.getStatus()));
                    vo.setStatusName(statusName);
                }
            }
        }
        map.put("total", num);
        map.put("rows", orderList);
        return map;
    }

    /**
     * @param userId
     * @return
     * @see com.anji.allways.business.service.TransportOrderService#choseIncomeForward(java.lang.String)
     */
    @Override
    public Map<String, Object> choseIncomeForward(String userId, Integer type, String name) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (type == 0) {
            List<CustomerEntity> company = customerEntityMapper.queryCompanyCustomerByUserId(Integer.valueOf(userId));
            for (int i = company.size() - 1; i >= 0; i--) {
                boolean falg = userService.validatePermissionByUserId(company.get(i).getUseId(), Long.valueOf(469));
                if (falg == false) {
                    company.remove(company.get(i));
                }
            }
            map.put("rows", company);
        } else if (type == 1) {
            Map<String, Object> co = new HashMap<String, Object>();
            co.put("userId", userId);
            co.put("name", name);
            List<CustomerEntity> group = customerEntityMapper.queryGroupCustomerByUserId(co);
            for (int i = group.size() - 1; i >= 0; i--) {
                boolean falg = userService.validatePermissionByUserId(group.get(i).getUseId(), Long.valueOf(469));
                if (falg == false) {
                    group.remove(group.get(i));
                }
            }
            map.put("rows", group);
        }

        return map;
    }

    /**
     * 查询导出订单记录
     * @param map
     * @return
     */
    public List<Map<String, Object>> queryOrderRecordsForExportData(Map<String, Object> map) {
        return orderEntityMapper.queryOrderRecordsForExportData(map);
    }

}
