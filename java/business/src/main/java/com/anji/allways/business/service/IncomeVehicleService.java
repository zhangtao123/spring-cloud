/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import java.util.Map;

/**
 * @author Administrator
 * @version $Id: IncomeVehicleService.java, v 0.1 2017年9月9日 下午4:17:18 Administrator Exp $
 */
public interface IncomeVehicleService {

    Map<String, Object> incomeVehicle(Map<String, Object> map);

    Map<String, Object> incomeDetialForward(int deliveryId, String userId);

    Map<String, Object> outWarehouseIncomeDetial(int vehicleId, int orderId);

    Map<String, Object> distributionDriver(int driverId);

    /*int sureSinceDetail(int driverId, int deliveryId);*/

    boolean checkDriver(String driverId, int deliveryId, String time, String userId);

    boolean upIdentityCard(String path, int deliveryId);

    int allIncomeVehicleCount(int userId);

    int sureIncomeVehicle(Integer vehicleId, Integer orderId, Integer deliveryId, Integer userId);

    int sureOutDeliveryPlan(Integer vehicleId, Integer orderId, Integer deliveryId, Integer userId);

    int sureDriverIncomeVehicle(Integer vehicleId, Integer orderId, Integer deliveryId, Integer userId);

    int driverDeliveryPlanCount(Map<String, Object> map);
}
