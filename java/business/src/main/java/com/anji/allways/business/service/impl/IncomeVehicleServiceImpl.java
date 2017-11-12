/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anji.allways.business.common.constant.SlnsConstants;
import com.anji.allways.business.dto.DeliveryPlanDTO;
import com.anji.allways.business.entity.DamageEntity;
import com.anji.allways.business.entity.DeliveryPlanEntity;
import com.anji.allways.business.entity.FileEntity;
import com.anji.allways.business.entity.TruckDriverEntity;
import com.anji.allways.business.entity.UserEntity;
import com.anji.allways.business.entity.VehicleEntity;
import com.anji.allways.business.mapper.DamageEntityMapper;
import com.anji.allways.business.mapper.DeliveryPlanEntityMapper;
import com.anji.allways.business.mapper.FileEntityMapper;
import com.anji.allways.business.mapper.OrderEntityMapper;
import com.anji.allways.business.mapper.TruckDriverEntityMapper;
import com.anji.allways.business.mapper.UserEntityMapper;
import com.anji.allways.business.mapper.VehicleEntityMapper;
import com.anji.allways.business.service.IncomeVehicleService;
import com.anji.allways.business.service.SerialNumberBuildService;
import com.anji.allways.business.service.WarehouseOutboundService;
import com.anji.allways.common.constants.RespCode;
import com.anji.allways.common.util.DateUtil;
import com.anji.allways.common.util.MD5Util;
import com.anji.allways.common.util.PageUtil;
import com.anji.allways.common.util.StringUtil;

/**
 * @author Administrator
 * @version $Id: IncomeVehicleServiceImpl.java, v 0.1 2017年9月9日 下午4:17:37 Administrator Exp $
 */
@Service
@Transactional
public class IncomeVehicleServiceImpl implements IncomeVehicleService {

    @Autowired
    private DeliveryPlanEntityMapper deliveryPlanEntityMapper;

    @Autowired
    private VehicleEntityMapper      vehicleEntityMapper;

    @Autowired
    private FileEntityMapper         fileEntityMapper;

    @Autowired
    private DamageEntityMapper       damageEntityMapper;

    @Autowired
    private TruckDriverEntityMapper  truckDriverEntityMapper;

    @Autowired
    private OrderEntityMapper        orderEntityMapper;

    @Autowired
    private UserEntityMapper         userEntityMapper;

    @Autowired
    private SerialNumberBuildService serialNumberBuildService;

    @Autowired
    private WarehouseOutboundService warehouseOutboundService;

    /**
     * @return
     * @see com.anji.allways.business.service.IncomeVehicleService#incomeVehicle()
     */
    @Override
    public Map<String, Object> incomeVehicle(Map<String, Object> maps) {
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNumber(Integer.parseInt(maps.get("pageNumber").toString()));
        pageUtil.setTotalNumber(deliveryPlanEntityMapper.selectIncomeVehicleCount(maps));
        pageUtil.setDbNumber(pageUtil.getDbIndex() + pageUtil.getPageNumber());
        pageUtil.setCurrentPage(Integer.parseInt(maps.get("currentPage").toString()));
        maps.put("pageIndex", pageUtil.getDbIndex());
        maps.put("pageNumber", pageUtil.getDbNumber());
        Map<String, Object> map = new HashMap<String, Object>();
        List<DeliveryPlanDTO> list = deliveryPlanEntityMapper.selectIncomeVehicle(maps);
        List<Map<String, Object>> listVue = new ArrayList<Map<String, Object>>();
        for (DeliveryPlanEntity deliveryPlanDTO : list) {
            Map<String, Object> mapvue = new HashMap<String, Object>();
            mapvue.put("id", deliveryPlanDTO.getId());
            mapvue.put("warehouseName", deliveryPlanDTO.getWarehouseName());
            mapvue.put("no", deliveryPlanDTO.getNo());
            mapvue.put("deliveryPlanTime", DateUtil.getInstance().formatAll(deliveryPlanDTO.getDeliveryPlanTime(), "yyyy-MM-dd HH:mm:ss"));
            mapvue.put("pickupPlanTime", DateUtil.getInstance().formatAll(deliveryPlanDTO.getPickupPlanTime(), "yyyy-MM-dd HH:mm:ss"));
            mapvue.put("transportType", deliveryPlanDTO.getTransportType());
            mapvue.put("status", deliveryPlanDTO.getStatus());
            mapvue.put("vehicleCount", deliveryPlanDTO.getVehicleCount());
            mapvue.put("transportCompanyName", deliveryPlanDTO.getTransportCompanyName());
            Date date = deliveryPlanEntityMapper.selectOrderReceiveTime(deliveryPlanDTO.getId());
            if (date == null) {
                mapvue.put("receiveTime", "");
            } else {
                mapvue.put("receiveTime", DateUtil.getInstance().formatAll(date, "yyyy-MM-dd HH:mm:ss"));
            }
            listVue.add(mapvue);
        }
        map.put("rows", listVue);
        map.put("page", pageUtil);
        map.put("total", pageUtil.getTotalNumber());
        return map;
    }

    /**
     * @param deliveryId
     * @return
     * @see com.anji.allways.business.service.IncomeVehicleService#incomeDetialForward(int)
     */
    @Override
    public Map<String, Object> incomeDetialForward(int deliveryId, String userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<VehicleEntity> vehicleEntities = vehicleEntityMapper.getBydeliveryPlanId(deliveryId);
        List<Map<String, Object>> listVue = new ArrayList<Map<String, Object>>();
        for (VehicleEntity vehicleEntity : vehicleEntities) {
            Map<String, Object> mapvue = new HashMap<String, Object>();
            mapvue.put("id", vehicleEntity.getId());
            mapvue.put("orderId", vehicleEntity.getOrderId());
            mapvue.put("vin", vehicleEntity.getVin());
            mapvue.put("orderNo", vehicleEntity.getOrderNo());
            mapvue.put("brand", vehicleEntity.getBrand());
            mapvue.put("series", vehicleEntity.getSeries());
            mapvue.put("model", vehicleEntity.getModel());
            mapvue.put("manufacturerColor", vehicleEntity.getManufacturerColor());
            mapvue.put("standardColor", vehicleEntity.getStandardColor());
            mapvue.put("vehicleStatus", vehicleEntity.getVehicleStatus());
            Map<String, Object> mapModelPicturMap = new HashMap<String, Object>();
            mapModelPicturMap.put("brand", vehicleEntity.getBrand());
            mapModelPicturMap.put("series", vehicleEntity.getSeries());
            mapModelPicturMap.put("model", vehicleEntity.getModel());
            mapModelPicturMap.put("announceYear", vehicleEntity.getAnnounceYear());
            String modelPicturMap = vehicleEntityMapper.getmodelPicture(mapModelPicturMap);
            mapvue.put("logoPicturePath", modelPicturMap);
            listVue.add(mapvue);
        }
        DeliveryPlanEntity deliveryPlanEntity = deliveryPlanEntityMapper.incomeDetialForward(deliveryId);

        map.put("identityPath", fileEntityMapper.getVehiclePic(deliveryPlanEntity.getIdentityCardPicturePath()));
        map.put("identitystatus", deliveryPlanEntity.getIdentityCardStatus());
        map.put("selfpickerPath", deliveryPlanEntity.getSelfpickerPicturePath());

        Map<String, Object> distribution = new HashMap<String, Object>();
        distribution.put("driverId", deliveryPlanEntity.getDriverId());
        distribution.put("transportType", deliveryPlanEntity.getTransportType());
        distribution.put("transportCompanyName", deliveryPlanEntity.getTransportCompanyName());
        distribution.put("truckNumber", deliveryPlanEntity.getTruckNumber());
        distribution.put("driverName", deliveryPlanEntity.getDriverName());
        distribution.put("driverMobile", deliveryPlanEntity.getDriverMobile());
        distribution.put("driverId", deliveryPlanEntity.getDriverId());
        distribution.put("identityCardPicturePath", deliveryPlanEntity.getIdentityCardPicturePath());
        distribution.put("identityCardStatus", deliveryPlanEntity.getIdentityCardStatus());
        distribution.put("selfpickerPicturePath", deliveryPlanEntity.getSelfpickerPicturePath());
        distribution.put("contactorMobile", deliveryPlanEntity.getContactorMobile());
        distribution.put("deliveryType", deliveryPlanEntity.getDeliveryType());

        Map<String, Object> warehouse = new HashMap<String, Object>();
        warehouse.put("warehouseName", deliveryPlanEntity.getWarehouseName());
        warehouse.put("deliveryNo", deliveryPlanEntity.getNo());
        warehouse.put("deliveryPlanTime", DateUtil.getInstance().formatAll(deliveryPlanEntity.getDeliveryPlanTime(), "yyyy-MM-dd HH:mm:ss"));
        warehouse.put("pickupPlanTime", DateUtil.getInstance().formatAll(deliveryPlanEntity.getPickupPlanTime(), "yyyy-MM-dd HH:mm:ss"));
        try {
            warehouse.put("shipNo", StringUtil.convertNullToEmpty(deliveryPlanEntity.getShipNo()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        warehouse.put("warehouseMobile", deliveryPlanEntity.getTelephone());

        map.put("vehicleEntities", listVue);
        map.put("deliveryId", deliveryPlanEntity.getId());
        map.put("createTime", DateUtil.getInstance().formatAll(deliveryPlanEntity.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        map.put("status", deliveryPlanEntity.getStatus());
        map.put("routingStatus", deliveryPlanEntity.getRoutingStatus());
        Map<String, Object> incomeInfo = new HashMap<String, Object>();
        incomeInfo.put("customerName", deliveryPlanEntity.getCustomerName());
        incomeInfo.put("incomeName", deliveryPlanEntity.getIncomeName());
        incomeInfo.put("contactorMobile", deliveryPlanEntity.getContactorMobile());
        incomeInfo.put("address", deliveryPlanEntity.getAddress());
        map.put("warehouse", warehouse);
        map.put("incomeInfo", incomeInfo);
        map.put("distribution", distribution);
        if (deliveryPlanEntity.getConsigneeId().equals(userId)) {
            map.put("isIncome", true);
        } else {
            map.put("isIncome", false);
        }
        return map;
    }

    /**
     * @param vehicleId
     * @return
     * @see com.anji.allways.business.service.IncomeVehicleService#outWarehouseIncomeDetial(int)
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> outWarehouseIncomeDetial(int vehicleId, int orderId) {
        Map<String, Object> map = new HashMap<String, Object>();
        VehicleEntity vehicleEntity = vehicleEntityMapper.getBydeliveryVehicleId(vehicleId);
        Map<String, Object> vehicle = new HashMap<String, Object>();
        vehicle.put("vin", vehicleEntity.getVin());
        vehicle.put("brand", vehicleEntity.getBrand());
        vehicle.put("series", vehicleEntity.getSeries());
        vehicle.put("model", vehicleEntity.getModel());
        vehicle.put("standardColor", vehicleEntity.getStandardColor());
        Map<String, Object> mapModelPicturMap = new HashMap<String, Object>();
        mapModelPicturMap.put("brand", vehicleEntity.getBrand());
        mapModelPicturMap.put("series", vehicleEntity.getSeries());
        mapModelPicturMap.put("model", vehicleEntity.getModel());
        mapModelPicturMap.put("announceYear", vehicleEntity.getAnnounceYear());
        String modelPicturMap = vehicleEntityMapper.getmodelPicture(mapModelPicturMap);
        vehicleEntity.setLogoPicturePath(modelPicturMap);
        vehicle.put("logoPicturePath", vehicleEntity.getLogoPicturePath());
        vehicle.put("manufacturerColor", vehicleEntity.getManufacturerColor());
        vehicle.put("orderId", orderId);
        List<DamageEntity> list = damageEntityMapper.selectDamageByVehicleId(vehicleId);
        List<Map<String, Object>> listInner = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> listOut = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> listIncome = new ArrayList<Map<String, Object>>();
        for (DamageEntity damageEntity : list) {
            Map<String, Object> mapVue = new HashMap<String, Object>();
            mapVue.put("register", damageEntity.getRegister());
            mapVue.put("registerTime", DateUtil.getInstance().formatAll(damageEntity.getRegisterTime(), "yyyy-MM-dd HH:mm:ss"));
            mapVue.put("confirmedStatus", damageEntity.getConfirmedStatus());
            if (damageEntity.getDutyOfficer() != null) {
                mapVue.put("dutyOfficer", damageEntity.getDutyOfficer());
            } else {
                mapVue.put("dutyOfficer", "");
            }
            if (damageEntity.getConfirmer() != null) {
                mapVue.put("confirmer", damageEntity.getConfirmer());
            } else {
                mapVue.put("confirmer", "");
            }
            if (damageEntity.getDamageDetail() != null) {
                mapVue.put("damageDetail", damageEntity.getDamageDetail());
            } else {
                mapVue.put("damageDetail", "");
            }
            mapVue.put("damagePicturePath", fileEntityMapper.getVehiclePic(damageEntity.getDamagePicturePath()));

            if (damageEntity.getDamageType() == 0) {
                listInner.add(mapVue);
            } else if (damageEntity.getDamageType() == 1) {
                listOut.add(mapVue);
            } else if (damageEntity.getDamageType() == 2) {
                listIncome.add(mapVue);
            }
        }
        map.put("vehicleEntity", vehicle);
        map.put("listInner", listInner);
        map.put("listOut", listOut);
        map.put("listIncome", listIncome);
        return map;
    }

    /**
     * @param driverId
     * @return
     * @see com.anji.allways.business.service.IncomeVehicleService#distributionDriver(int)
     */
    @Override
    public Map<String, Object> distributionDriver(int driverId) {
        Map<String, Object> map = new HashMap<String, Object>();
        TruckDriverEntity truckDriverEntity = truckDriverEntityMapper.selectByPrimaryKey(driverId);
        Map<String, Object> mapVue = new HashMap<String, Object>();
        mapVue.put("name", truckDriverEntity.getName());
        mapVue.put("mobile", truckDriverEntity.getMobile());
        mapVue.put("truckNumber", truckDriverEntity.getTruckNumber());
        mapVue.put("idCardNo", truckDriverEntity.getIdCardNo());

        mapVue.put("idCardFrontPicture", truckDriverEntity.getIdCardFrontPicture());

        map.put("truckDriver", mapVue);
        return map;
    }

    /**
     * @param orderId
     * @param vehicleId
     * @return
     * @see com.anji.allways.business.service.IncomeVehicleService#sureSinceDetail(int, int)
     */
    /*
    @Override
    public int sureSinceDetail(int vehicleId, int deliveryId) {
     int count = orderEntityMapper.selectOrderIdByVehicleIdCount(vehicleId);
     int vehicle = 0;
     if (count > 0) {
         DeliveryPlanEntity deliveryPlanEntity = new DeliveryPlanEntity();
         deliveryPlanEntity.setId(vehicleId);
         deliveryPlanEntity.setUpdateTime(new Date());
         deliveryPlanEntity.setStatus(5);
         vehicle = deliveryPlanEntityMapper.updateByPrimaryKeySelective(deliveryPlanEntity);
     }
     OrderEntity orderEntity = new OrderEntity();
     orderEntity.setId(orderId);
     orderEntity.setUpdateTime(new Date());
     orderEntity.setStatus(5);
     int order = orderEntityMapper.updateByPrimaryKeySelective(orderEntity);
     return order + vehicle;
    }*/

    /**
     * @param driverId
     * @param vehicleId
     * @return
     * @see com.anji.allways.business.service.IncomeVehicleService#checkDriver(int, int)
     */
    @Override
    public boolean checkDriver(String driverId, int deliveryId, String time, String userId) {
        boolean falg = false;
        String driverIds = deliveryPlanEntityMapper.checkDriver(deliveryId) + "@" + userId;
        String timeCheck = MD5Util.encryptBySalt(driverIds.toString());
        if (timeCheck.equals(driverId)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", deliveryId);
            map.put("routingStatus", 3);
            deliveryPlanEntityMapper.updateDeliveryPlanRoutingStatus(map);
            falg = true;
        }
        return falg;
    }

    /**
     * @param paht
     * @param deliveryId
     * @return
     * @see com.anji.allways.business.service.IncomeVehicleService#upIdentityCard(java.lang.String, int)
     */
    @Override
    public boolean upIdentityCard(String path, int deliveryId) {
        Map<String, Object> pathMap = new HashMap<String, Object>();
        pathMap.put("id", deliveryId);
        pathMap.put("path", "identity" + deliveryId);
        deliveryPlanEntityMapper.updateDeliveryPlanIdentityCardPath(pathMap);

        fileEntityMapper.deleteByPrimaryName("identity" + deliveryId);

        String[] split = null;
        if (!StringUtil.isEmpty(path, true)) {
            split = path.split(",");
            for (String str : split) {
                FileEntity fileEntity = new FileEntity();
                fileEntity.setName("identity" + deliveryId);
                fileEntity.setPath(str);
                fileEntityMapper.insertSelective(fileEntity);
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", deliveryId);
        map.put("identityCardStatus", 1);
        deliveryPlanEntityMapper.updateDeliveryPlanIdentityCardStatus(map);
        return true;
    }

    /**
     * @param userId
     * @return
     * @see com.anji.allways.business.service.IncomeVehicleService#allIncomeVehicleCount(int)
     */
    @Override
    public int allIncomeVehicleCount(int userId) {
        return deliveryPlanEntityMapper.selectAllIncomeVehicleCount(userId);
    }

    /**
     * @param vehicleId
     * @param deliveryId
     * @return
     * @see com.anji.allways.business.service.IncomeVehicleService#sureIncomeVehicle(java.lang.Integer, java.lang.Integer)
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int sureIncomeVehicle(Integer vehicleId, Integer orderId, Integer deliveryId, Integer userId) {
        synchronized (this) {
            try {
                Map<String, Object> mapVehicle = new HashMap<String, Object>();
                List<Integer> list = new ArrayList<Integer>();
                list.add(vehicleId);
                mapVehicle.put("list", list);
                mapVehicle.put("vehicleStatus", 5);
                VehicleEntity vehicleEntity = vehicleEntityMapper.selectByPrimaryKey(vehicleId);
                if (vehicleEntity.getVehicleStatus() != 9 && vehicleEntity.getVehicleStatus() != 7) {
                    return 0;
                }
                vehicleEntityMapper.updateVehicleStatus(mapVehicle);
                Map<String, Object> order = new HashMap<String, Object>();
                list.remove(0);
                list.add(orderId);
                order.put("list", list);
                order.put("status", 5);
                orderEntityMapper.updateOrderStruts(order);
                order.put("id", orderId);
                order.put("receiveTime", new Date());
                orderEntityMapper.updateOrderIncomeTime(order);
                int struts = vehicleEntityMapper.queryOrderIncomeVehicleStatus(deliveryId);
                if (struts == 0) {
                    Map<String, Object> delivery = new HashMap<String, Object>();
                    delivery.put("status", 5);
                    delivery.put("id", deliveryId);
                    delivery.put("routingStatus", 4);
                    if (vehicleEntity.getVehicleStatus() == 7) {
                        DeliveryPlanDTO deliveryPlanDTO = new DeliveryPlanDTO();
                        deliveryPlanDTO.setId(deliveryId);
                        deliveryPlanDTO.setUpdateUser(userId);
                        Map<String, Object> shipMap = warehouseOutboundService.saveReceiptEntity(deliveryPlanDTO);
                        if (RespCode.SUCCESS.equals(shipMap.get("code"))) {
                            delivery.put("shipNo", shipMap.get("shipNo"));
                        }
                        delivery.put("deliveryTime", new Date());
                    }
                    deliveryPlanEntityMapper.updateDeliveryPlanStrutsTime(delivery);
                    deliveryPlanEntityMapper.updateDeliveryPlanRoutingStatus(delivery);

                    int destruts = deliveryPlanEntityMapper.selectAllPlanCount(deliveryId);
                    if (destruts == 0) {
                        delivery = new HashMap<String, Object>();
                        delivery.put("id", deliveryPlanEntityMapper.selectFatherDeliveryPlanId(deliveryId));
                        delivery.put("status", 5);
                        delivery.put("deliveryPlanId", deliveryId);
                        delivery.put("receiveTime", new Date());
                        delivery.put("deliveryTime", new Date());
                        if (vehicleEntity.getVehicleStatus() == 7) {
                            DeliveryPlanDTO deliveryPlanDTO = new DeliveryPlanDTO();
                            deliveryPlanDTO.setId(Integer.valueOf(delivery.get("id").toString()));
                            deliveryPlanDTO.setUpdateUser(userId);
                            deliveryPlanDTO.setType(1);
                            Map<String, Object> shipMap = warehouseOutboundService.saveReceiptEntity(deliveryPlanDTO);
                            if (RespCode.SUCCESS.equals(shipMap.get("code"))) {
                                delivery.put("shipNo", shipMap.get("shipNo"));
                            }
                            delivery.put("deliveryTime", new Date());
                            orderEntityMapper.updateOrderShipNoByPlanId(delivery);
                        }
                        orderEntityMapper.updateReceiveTimeByPlanId(delivery);
                        deliveryPlanEntityMapper.updateDeliveryPlanStrutsTime(delivery);
                    }

                }
                Map<String, Object> damage = new HashMap<String, Object>();
                UserEntity userEntity = userEntityMapper.selectByPrimaryKey(userId);
                damage.put("confirmedStatus", 1);
                damage.put("confirmer", userEntity.getName());
                damage.put("vehicleId", vehicleId);
                damage.put("damageType", 2);
                damageEntityMapper.updateByIdAndType(damage);
            } catch (RuntimeException e) {
                return 0;
            } catch (Exception e) {
                return 0;
            }
            return 1;
        }
    }

    /**
     * @param vehicleId
     * @param orderId
     * @param deliveryId
     * @return
     * @see com.anji.allways.business.service.IncomeVehicleService#sureOutDeliveryPlan(java.lang.Integer, java.lang.Integer, java.lang.Integer)
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int sureOutDeliveryPlan(Integer vehicleId, Integer orderId, Integer deliveryId, Integer userId) {
        synchronized (this) {
            try {
                Map<String, Object> mapVehicle = new HashMap<String, Object>();
                List<Integer> list = new ArrayList<Integer>();
                list.add(vehicleId);
                mapVehicle.put("list", list);
                mapVehicle.put("vehicleStatus", 4);
                VehicleEntity vehicleEntity = vehicleEntityMapper.selectByPrimaryKey(vehicleId);
                if (vehicleEntity.getVehicleStatus() != 7) {
                    return 0;
                }
                vehicleEntityMapper.updateVehicleStatus(mapVehicle);
                Map<String, Object> order = new HashMap<String, Object>();
                list.remove(0);
                list.add(orderId);
                order.put("list", list);
                order.put("status", 4);
                orderEntityMapper.updateOrderStruts(order);
                order.put("id", order);
                order.put("receiveTime", new Date());
                orderEntityMapper.updateOrderIncomeTime(order);
                int struts = vehicleEntityMapper.queryDriverIncomeVehicleStatus(deliveryId);
                if (struts == 0) {
                    DeliveryPlanDTO deliveryPlanDTO = new DeliveryPlanDTO();
                    deliveryPlanDTO.setId(deliveryId);
                    deliveryPlanDTO.setUpdateUser(userId);
                    Map<String, Object> shipMap = warehouseOutboundService.saveReceiptEntity(deliveryPlanDTO);
                    Map<String, Object> delivery = new HashMap<String, Object>();
                    if (RespCode.SUCCESS.equals(shipMap.get("code"))) {
                        delivery.put("shipNo", shipMap.get("shipNo"));
                    }
                    delivery.put("status", 4);
                    delivery.put("id", deliveryId);
                    delivery.put("deliveryTime", new Date());
                    deliveryPlanEntityMapper.updateDeliveryPlanStrutsTime(delivery);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", deliveryId);
                    map.put("routingStatus", 1);
                    if (RespCode.SUCCESS.equals(shipMap.get("code"))) {
                        map.put("shipNo", shipMap.get("shipNo"));
                    }
                    deliveryPlanEntityMapper.updateDeliveryPlanRoutingStatus(map);
                    map.put("deliveryPlanId", deliveryId);
                    orderEntityMapper.updateOrderShipNoByPlanId(map);
                    int destruts = deliveryPlanEntityMapper.selectAllDeliveryPlanCount(deliveryId);
                    if (destruts == 0) {
                        delivery.put("id", deliveryPlanEntityMapper.selectFatherDeliveryPlanId(deliveryId));
                        deliveryPlanDTO.setId(Integer.valueOf(delivery.get("id").toString()));
                        deliveryPlanDTO.setType(1);
                        deliveryPlanDTO.setUpdateUser(userId);
                        Map<String, Object> shipMaps = warehouseOutboundService.saveReceiptEntity(deliveryPlanDTO);
                        delivery = new HashMap<String, Object>();
                        delivery.put("status", 4);
                        delivery.put("deliveryTime", new Date());
                        if (RespCode.SUCCESS.equals(shipMaps.get("code"))) {
                            delivery.put("shipNo", shipMaps.get("shipNo"));
                        }
                        delivery.put("id", deliveryPlanEntityMapper.selectFatherDeliveryPlanId(deliveryId));
                        deliveryPlanEntityMapper.updateDeliveryPlanStrutsTime(delivery);
                    }

                }
                Map<String, Object> damage = new HashMap<String, Object>();
                UserEntity userEntity = userEntityMapper.selectByPrimaryKey(userId);
                damage.put("confirmer", userEntity.getName());
                damage.put("confirmedStatus", 1);
                damage.put("vehicleId", vehicleId);
                damage.put("damageType", 1);
                damageEntityMapper.updateByIdAndType(damage);
            } catch (RuntimeException e) {
                return 0;
            } catch (Exception e) {
                return 0;
            }
            return 1;
        }
    }

    /**
     * @param vehicleId
     * @param deliveryId
     * @return
     * @see com.anji.allways.business.service.IncomeVehicleService#sureIncomeVehicle(java.lang.Integer, java.lang.Integer)
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int sureDriverIncomeVehicle(Integer vehicleId, Integer orderId, Integer deliveryId, Integer userId) {
        synchronized (this) {
            try {
                Map<String, Object> mapVehicle = new HashMap<String, Object>();
                List<Integer> list = new ArrayList<Integer>();
                list.add(vehicleId);
                mapVehicle.put("list", list);
                mapVehicle.put("vehicleStatus", 9);
                VehicleEntity vehicleEntity = vehicleEntityMapper.selectByPrimaryKey(vehicleId);
                if (vehicleEntity.getVehicleStatus() != 4) {
                    return 0;
                }
                vehicleEntityMapper.updateVehicleStatus(mapVehicle);
                Map<String, Object> order = new HashMap<String, Object>();
                list.remove(0);
                list.add(orderId);
                order.put("list", list);
                order.put("status", 9);
                orderEntityMapper.updateOrderStruts(order);
                order.put("id", orderId);
                order.put("receiveTime", new Date());
                orderEntityMapper.updateOrderIncomeTime(order);

                List<DamageEntity> listDamage = damageEntityMapper.selectDamageByVehicleIdAndType(vehicleId);
                if (listDamage.size() == 0) {
                    DamageEntity damageEntity = new DamageEntity();
                    damageEntity.setNo(serialNumberBuildService.getSerialNumberByType(SlnsConstants.SERIAL_NUMBER_TYPE_DAMAGE));
                    damageEntity.setVehicleId(vehicleId);
                    damageEntity.setDamageType(2);
                    UserEntity userEntity = userEntityMapper.selectByPrimaryKey(userId);
                    damageEntity.setRegister(userEntity.getName());
                    damageEntity.setRegisterTime(new Date());
                    damageEntity.setConfirmedStatus(0);
                    damageEntity.setCreatorName(userEntity.getId().toString());
                    damageEntity.setCreateTime(new Date());
                    damageEntity.setDutyOfficer("");
                    damageEntity.setConfirmer("");
                    damageEntityMapper.insertSelective(damageEntity);
                }

            } catch (Exception e) {
                return 0;
            }
            return 1;
        }
    }

    /**
     * @param map
     * @return
     * @see com.anji.allways.business.service.IncomeVehicleService#driverDeliveryPlanCount(java.util.Map)
     */
    @Override
    public int driverDeliveryPlanCount(Map<String, Object> map) {
        return deliveryPlanEntityMapper.driverDeliveryPlanCount(map);
    }

}
