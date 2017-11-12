/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import java.util.Date;
import java.util.Map;

/**
 * @author wangyanjun
 * @version $Id: StatisticsService.java, v 0.1 2017年8月24日 下午10:52:59 wangyanjun Exp $
 */
public interface StatisticsService {

    Map<String, Object> selectByDateAndWarehouseIdAndCustomerId(Date date, Integer warehouseId, Integer customerId);

    void refreshStatisticsData() throws Exception;

    Map<String, Object> selectByDateAndAndCustomerId(Date date, Integer customerId);

    Map<String, Object> selectAllwarehouseByCustomer(Date date, Integer customerId);

    Map<String, Object> selectAllwarehouseByWeb(Integer userId);

    Map<String, Object> statisticalAllWarehouse();

    Map<String, Object> statisticalCustomerGroup(Integer userId);
}
