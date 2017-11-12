/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import java.util.List;
import java.util.Map;

import com.anji.allways.business.entity.OrderEntity;

/**
 * @author 李军
 * @version $Id: DealerTransportOrderService.java, v 0.1 2017年9月4日 下午6:49:43 李军 Exp $
 */
public interface TransportOrderService {
    // 查询确认订单的信息
    Map<String, Object> getOrderTransport(Integer userId, Integer vehicleId);

    // 新增运输订单
    int createTransportOrder(Map<String, Object> mapVue);

    // 查询确认订单的信息
    Map<String, Object> getTransportOrder(Map<String, Object> map);

    // 获取运输订单筛选条件
    Map<String, Object> screenTransportOrder(Integer userId);

    // 取消运输订单
    int cancelOrder(Integer vehicleId, Integer orderId);

    // 查询运输订单
    Map<String, Object> queryOrderByFilers(OrderEntity entity, Integer pageNum, Integer pageRows);

    Map<String, Object> choseIncomeForward(String userId, Integer type, String name);

    /**
     * 查询导出订单记录
     * @param map
     * @return
     */
    List<Map<String, Object>> queryOrderRecordsForExportData(Map<String, Object> map);
}
