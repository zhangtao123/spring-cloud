/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.anji.allways.base.aop.LoggerManage;
import com.anji.allways.business.entity.CustomerEntity;
import com.anji.allways.business.entity.StatisticsEntity;
import com.anji.allways.business.entity.WarehouseEntity;
import com.anji.allways.business.mapper.CustomerEntityMapper;
import com.anji.allways.business.mapper.StatisticsEntityMapper;
import com.anji.allways.business.mapper.VehicleEntityMapper;
import com.anji.allways.business.mapper.WarehouseEntityMapper;
import com.anji.allways.business.service.StatisticsService;
import com.anji.allways.business.service.UserService;
import com.anji.allways.business.service.WarehouseService;
import com.anji.allways.business.vo.StatisticsEntityResponseVo;
import com.anji.allways.business.vo.StatisticsEntityResquestVo;
import com.anji.allways.common.util.DateUtil;

/**
 * @author wangyanjun
 * @version $Id: StatisticsService.java, v 0.1 2017年8月24日 下午11:11:18 wangyanjun Exp $
 */
@Service
@Transactional
public class StatisticsServiceImpl implements StatisticsService {

    private Logger                 logger = LogManager.getLogger(StatisticsServiceImpl.class);

    @Autowired
    private StatisticsEntityMapper statisticsEntityMapper;

    @Autowired
    private WarehouseEntityMapper  warehouseEntityMapper;

    @Autowired
    private VehicleEntityMapper    vehicleEntityMapper;

    @Autowired
    private CustomerEntityMapper   customerEntityMapper;

    @Autowired
    private UserService            userService;

    @Autowired
    private WarehouseService       warehouseService;

    /**
     * @throws Exception
     * @see com.anji.allways.business.service.StatisticsService#refreshStatisticsData()
     */
    @SuppressWarnings("static-access")
    @Override
    @LoggerManage(description = "统计数据")
    public void refreshStatisticsData() throws Exception {
        // 获取时间信息
        DateUtil util = DateUtil.getInstance();
        String requestData = util.formatAll(util.getPlusDay(-1), util.DATEANDMINUSPATTERN);
        logger.info("开始统计日期为：" + requestData + " 的信息");
        // 获取所有仓库以及子仓库信息
        List<WarehouseEntity> topWarehouseList = warehouseEntityMapper.getWarehouseByParentID(0);
        List<StatisticsEntity> topCustomerList = new ArrayList<StatisticsEntity>();
        for (WarehouseEntity warehouseEntity : topWarehouseList) {
            List<StatisticsEntity> todayStatistics = doWarehouseStatistics(warehouseEntity, requestData);
            topCustomerList.addAll(todayStatistics);
            // 计算仓库与子仓库的总和
            StatisticsEntity todayAllStatistic = new StatisticsEntity();
            // 时间
            todayAllStatistic.setDate(util.getDateByString(requestData, util.DATEANDMINUSPATTERN));
            // 仓库ID
            todayAllStatistic.setWarehouseId(warehouseEntity.getId());
            // 客户ID
            todayAllStatistic.setCustomerId(0);
            // 统计类型
            todayAllStatistic.setType(1);
            // 统计总和
            this.getAllCountByWareHouse(todayStatistics, todayAllStatistic);
            this.getAllCountWareHouse(warehouseEntity, todayAllStatistic);
            // 备注信息
            todayAllStatistic.setReserved("仓库为：" + warehouseEntity.getName() + " 仓库及其子仓库的信息总集");
            todayStatistics.add(todayAllStatistic);
            for (StatisticsEntity todayStatistic : todayStatistics) {
                statisticsEntityMapper.insertSelective(todayStatistic);
            }
        }
        // 统计经销商信息
        this.doCustomerStatistics(topCustomerList, requestData);
        // 统计出库库龄数据
        this.countCustomerStorage(requestData);

    }

    /**
     * 计算以客户为维度的出库库龄统计
     */
    @SuppressWarnings("static-access")
    public void countCustomerStorage(String requestData) throws Exception {
        // 获取所有有效客户信息
        List<CustomerEntity> customerList = customerEntityMapper.queryAllCustomer();
        // 获取所有库存信息
        StatisticsEntityResquestVo staRequest = new StatisticsEntityResquestVo();
        List<StatisticsEntityResponseVo> customerStatistic = statisticsEntityMapper.selectOutTime(staRequest);
        DateUtil util = DateUtil.getInstance();
        for (CustomerEntity customer : customerList) {
            StatisticsEntity outStatistic = new StatisticsEntity();
            // 时间
            outStatistic.setDate(util.getDateByString(requestData, util.DATEANDMINUSPATTERN));
            // 仓库ID
            outStatistic.setWarehouseId(0);
            // 客户ID
            outStatistic.setCustomerId(customer.getId());
            // 统计类型
            outStatistic.setType(2);
            Map<String, Integer> storageDay = this.queryStayTime(2, customer.getId(), customerStatistic);
            // 库龄1-9天
            outStatistic.setAge19Amount(storageDay.get("one"));
            // 库龄10-19天
            outStatistic.setAge1019Amount(storageDay.get("two"));
            // 库龄20-29天
            outStatistic.setAge2029Amount(storageDay.get("three"));
            // 库龄30-45天
            outStatistic.setAge3045Amount(storageDay.get("four"));
            // 库龄46-60天
            outStatistic.setAge4660Amount(storageDay.get("five"));
            // 库龄60天以上
            outStatistic.setAgeOver60Amount(storageDay.get("six"));
            // 备注信息
            outStatistic.setReserved("客户为：" + customer.getName() + " 为维度的出库库龄统计");
            statisticsEntityMapper.insertSelective(outStatistic);
        }
    }

    /**
     * 以经销商为维度统计信息
     * @param warehouseEntity
     * @param requestData
     * @return
     * @throws Exception
     */
    @SuppressWarnings("static-access")
    private void doCustomerStatistics(List<StatisticsEntity> topCustomerList, String requestData) throws Exception {
        // 获取所有有效客户信息
        List<CustomerEntity> customerList = customerEntityMapper.queryAllCustomer();
        DateUtil util = DateUtil.getInstance();
        for (CustomerEntity customer : customerList) {
            //
            StatisticsEntity todayAllStatistic = new StatisticsEntity();
            // 时间
            todayAllStatistic.setDate(util.getDateByString(requestData, util.DATEANDMINUSPATTERN));
            // 仓库ID
            todayAllStatistic.setWarehouseId(0);
            // 客户ID
            todayAllStatistic.setCustomerId(customer.getId());
            // 统计类型
            todayAllStatistic.setType(1);
            // 备注信息
            todayAllStatistic.setReserved("经销商为：" + customer.getName() + " 在所有仓库及其子仓库的信息总集");
            // 统计信息
            List<StatisticsEntity> statisticCount = new ArrayList<StatisticsEntity>();
            for (StatisticsEntity statistic : topCustomerList) {
                if (customer.getId().equals(statistic.getCustomerId())) {
                    statisticCount.add(statistic);
                }
            }
            this.getAllCountByWareHouse(statisticCount, todayAllStatistic);
            statisticsEntityMapper.insertSelective(todayAllStatistic);
        }
    }

    /**
     * 获取信息
     * @param warehouseEntity
     * @param requestData
     * @return
     * @throws Exception
     */
    @SuppressWarnings("static-access")
    private List<StatisticsEntity> doWarehouseStatistics(WarehouseEntity warehouseEntity, String requestData) throws Exception {
        // 返回信息
        List<StatisticsEntity> statis = new ArrayList<StatisticsEntity>();
        // 查询所有子仓库以及本仓库
        List<WarehouseEntity> warehouseList = warehouseEntityMapper.getWarehouseByParentID(warehouseEntity.getId());
        warehouseList.add(warehouseEntity);
        // 仓库ID列表
        StatisticsEntityResquestVo staRequest = new StatisticsEntityResquestVo();
        staRequest.setRequestData(requestData);
        List<Integer> wareHouseIds = new ArrayList<Integer>();
        for (WarehouseEntity warehouse : warehouseList) {
            wareHouseIds.add(warehouse.getId());
        }
        logger.info("仓库ID:" + JSONObject.toJSONString(wareHouseIds));
        staRequest.setWarehouseIds(wareHouseIds);
        // 查询库位数
        List<StatisticsEntityResponseVo> storgeList = statisticsEntityMapper.queryStorgeCount(staRequest);
        logger.info("查询库位数:" + JSONObject.toJSONString(storgeList));
        // 查询库存数
        Map<Integer, Integer> storageedCarCount = this.queryStorageedCarCount(staRequest);
        logger.info("查询库存数:" + JSONObject.toJSONString(storageedCarCount));
        // 获取入库数
        Map<Integer, Integer> storgingCount = this.queryStorgingCount(staRequest);
        logger.info("获取入库数:" + JSONObject.toJSONString(storgingCount));
        // 获取订单数
        Map<Integer, Integer> orderCount = this.queryOrderCount(staRequest);
        logger.info("获取订单数:" + JSONObject.toJSONString(orderCount));
        // 获取出库单数
        Map<Integer, Integer> outStorgeCount = this.queryOutStorgeCount(staRequest);
        logger.info("获取出库单数:" + JSONObject.toJSONString(outStorgeCount));
        // 查询客户收车数
        Map<Integer, Integer> customerGetCarCount = this.queryCustomerGetCarCount(staRequest);
        logger.info("获取出库单数:" + JSONObject.toJSONString(customerGetCarCount));
        // 查询客户待收车数
        Map<Integer, Integer> watingCarCount = this.selectWaitingCarCount(staRequest);
        logger.info("查询客户待收车数:" + JSONObject.toJSONString(watingCarCount));
        DateUtil util = DateUtil.getInstance();
        for (StatisticsEntityResponseVo statisResponse : storgeList) {
            Integer customerId = statisResponse.getCustomerId();
            logger.info("客户信息ID:" + customerId);
            StatisticsEntity todayStatistics = new StatisticsEntity();
            // 时间
            todayStatistics.setDate(util.getDateByString(requestData, util.DATEANDMINUSPATTERN));
            // 仓库ID
            todayStatistics.setWarehouseId(warehouseEntity.getId());
            // 客户ID
            todayStatistics.setCustomerId(customerId);
            // 库位数
            todayStatistics.setWarehouseAmount(null == statisResponse.getCount() ? Integer.valueOf(0) : statisResponse.getCount());
            // 库存数
            todayStatistics.setStorageAmount(null == storageedCarCount.get(customerId) ? Integer.valueOf(0) : storageedCarCount.get(customerId));
            // 入库数
            todayStatistics.setIncomingAmount(null == storgingCount.get(customerId) ? Integer.valueOf(0) : storgingCount.get(customerId));
            // 订单数
            todayStatistics.setOrderAmount(null == orderCount.get(customerId) ? Integer.valueOf(0) : orderCount.get(customerId));
            // 出库数
            todayStatistics.setDeliveryAmount(null == outStorgeCount.get(customerId) ? Integer.valueOf(0) : outStorgeCount.get(customerId));
            // 客户收车数
            todayStatistics.setReceivedAmount(null == customerGetCarCount.get(customerId) ? Integer.valueOf(0) : customerGetCarCount.get(customerId));
            // 待收车数
            todayStatistics.setReceivingAmount(null == watingCarCount.get(customerId) ? Integer.valueOf(0) : watingCarCount.get(customerId));
            // 统计类型
            todayStatistics.setType(1);
            // 获取在库时间件数
            staRequest.setDealerId(customerId);
            List<StatisticsEntityResponseVo> statisticList = statisticsEntityMapper.selectStayTime(staRequest);
            Map<String, Integer> storageDay = this.queryStayTime(1, customerId, statisticList);
            // 库龄1-9天
            todayStatistics.setAge19Amount(storageDay.get("one"));
            // 库龄10-19天
            todayStatistics.setAge1019Amount(storageDay.get("two"));
            // 库龄20-29天
            todayStatistics.setAge2029Amount(storageDay.get("three"));
            // 库龄30-45天
            todayStatistics.setAge3045Amount(storageDay.get("four"));
            // 库龄46-60天
            todayStatistics.setAge4660Amount(storageDay.get("five"));
            // 库龄60天以上
            todayStatistics.setAgeOver60Amount(storageDay.get("six"));
            // 备注信息
            todayStatistics.setReserved("仓库对应客户统计在库信息");
            statis.add(todayStatistics);
        }
        return statis;
    }

    /**
     * 获取在库时间
     * @return
     */
    @SuppressWarnings("static-access")
    public Map<String, Integer> queryStayTime(int type, Integer customerId, List<StatisticsEntityResponseVo> statisticList) {
        logger.info("在库信息列表:" + JSONObject.toJSONString(statisticList));
        Map<String, Integer> cusStorage = new HashMap<String, Integer>();
        // 库龄1-9天
        Integer one = 0;
        // 库龄10-19天
        Integer two = 0;
        // 库龄20-29天
        Integer three = 0;
        // 库龄30-45天
        Integer four = 0;
        // 库龄46-60天
        Integer five = 0;
        // 库龄60天以上
        Integer six = 0;
        DateUtil util = DateUtil.getInstance();
        for (StatisticsEntityResponseVo storge : statisticList) {
            if (customerId.equals(storge.getDealerId())) {
                int len = 0;
                if (type == 1) {
                    // 入库统计
                    if (null == storge.getStorageTime()) {
                        continue;
                    }
                    len = util.getPeriodSeconds(storge.getStorageTime(), new Date());
                }
                if (type == 2) {
                    // 出库统计
                    if (null == storge.getStorageTime() || null == storge.getDeliveryTime()) {
                        continue;
                    }
                    len = util.getPeriodSeconds(storge.getStorageTime(), storge.getDeliveryTime());
                }
                BigDecimal org = new BigDecimal(len);
                BigDecimal base = new BigDecimal(24 * 60 * 60);
                BigDecimal bigDay = org.divide(base, 0, BigDecimal.ROUND_HALF_UP);
                int day = bigDay.intValue();
                if (day >= 1 && day <= 9) {
                    one++;
                }
                if (day >= 10 && day <= 19) {
                    two++;
                }
                if (day >= 20 && day <= 29) {
                    three++;
                }
                if (day >= 30 && day <= 45) {
                    four++;
                }
                if (day >= 46 && day <= 60) {
                    five++;
                }
                if (day >= 60) {
                    six++;
                }
            }
        }
        cusStorage.put("one", one);
        cusStorage.put("two", two);
        cusStorage.put("three", three);
        cusStorage.put("four", four);
        cusStorage.put("five", five);
        cusStorage.put("six", six);
        logger.info("获取在库时间件数:" + JSONObject.toJSONString(cusStorage));
        return cusStorage;
    }

    /**
     * 累计所有仓库的库位数以及已占用库位数
     * @param statisticList
     * @return
     */
    public void getAllCountWareHouse(WarehouseEntity warehouseEntity, StatisticsEntity todayAllStatistic) {
        // 查询所有子仓库以及本仓库
        List<WarehouseEntity> warehouseList = warehouseEntityMapper.getWarehouseByParentID(warehouseEntity.getId());
        warehouseList.add(warehouseEntity);
        // 总库位数
        todayAllStatistic.setWarehouseAmount(0);
        // 库存数
        todayAllStatistic.setStorageAmount(0);
        for (WarehouseEntity warehouse : warehouseList) {
            todayAllStatistic.setWarehouseAmount(todayAllStatistic.getWarehouseAmount() + warehouse.getSpaceAmount());
            todayAllStatistic.setStorageAmount(todayAllStatistic.getStorageAmount() + warehouse.getOccupiedAmount());
        }
    }

    /**
     * 计算仓库与子仓库的总和
     * @param statisticList
     * @return
     */
    public void getAllCountByWareHouse(List<StatisticsEntity> statisticList, StatisticsEntity todayAllStatistic) {
        todayAllStatistic.setWarehouseAmount(0);
        todayAllStatistic.setStorageAmount(0);
        todayAllStatistic.setIncomingAmount(0);
        todayAllStatistic.setOrderAmount(0);
        todayAllStatistic.setDeliveryAmount(0);
        todayAllStatistic.setReceivedAmount(0);
        todayAllStatistic.setReceivingAmount(0);
        todayAllStatistic.setAge19Amount(0);
        todayAllStatistic.setAge1019Amount(0);
        todayAllStatistic.setAge2029Amount(0);
        todayAllStatistic.setAge3045Amount(0);
        todayAllStatistic.setAge4660Amount(0);
        todayAllStatistic.setAgeOver60Amount(0);
        for (StatisticsEntity todayStatistic : statisticList) {
            // 库位数
            todayAllStatistic.setWarehouseAmount(todayAllStatistic.getWarehouseAmount() + todayStatistic.getWarehouseAmount());
            // 库存数
            todayAllStatistic.setStorageAmount(todayAllStatistic.getStorageAmount() + todayStatistic.getStorageAmount());
            // 入库数
            todayAllStatistic.setIncomingAmount(todayAllStatistic.getIncomingAmount() + todayStatistic.getIncomingAmount());
            // 订单数
            todayAllStatistic.setOrderAmount(todayAllStatistic.getOrderAmount() + todayStatistic.getOrderAmount());
            // 出库数
            todayAllStatistic.setDeliveryAmount(todayAllStatistic.getDeliveryAmount() + todayStatistic.getDeliveryAmount());
            // 客户收车数
            todayAllStatistic.setReceivedAmount(todayAllStatistic.getReceivedAmount() + todayStatistic.getReceivedAmount());
            // 客户待收车数
            todayAllStatistic.setReceivingAmount(todayAllStatistic.getReceivingAmount() + todayStatistic.getReceivedAmount());
            // 库龄1-9天
            todayAllStatistic.setAge19Amount(todayAllStatistic.getAge19Amount() + todayStatistic.getAge19Amount());
            // 库龄10-19天
            todayAllStatistic.setAge1019Amount(todayAllStatistic.getAge1019Amount() + todayStatistic.getAge1019Amount());
            // 库龄20-29天
            todayAllStatistic.setAge2029Amount(todayAllStatistic.getAge2029Amount() + todayStatistic.getAge2029Amount());
            // 库龄30-45天
            todayAllStatistic.setAge3045Amount(todayAllStatistic.getAge3045Amount() + todayStatistic.getAge3045Amount());
            // 库龄46-60天
            todayAllStatistic.setAge4660Amount(todayAllStatistic.getAge4660Amount() + todayStatistic.getAge4660Amount());
            // 库龄60天以上
            todayAllStatistic.setAgeOver60Amount(todayAllStatistic.getAgeOver60Amount() + todayStatistic.getAgeOver60Amount());
        }
    }

    /**
     * 查询库存数
     * @return
     */
    public Map<Integer, Integer> queryStorageedCarCount(StatisticsEntityResquestVo staRequest) {
        Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
        List<StatisticsEntityResponseVo> storgeList = statisticsEntityMapper.queryStorageedCarCount(staRequest);
        for (StatisticsEntityResponseVo storge : storgeList) {
            resultMap.put(storge.getCustomerId(), storge.getCount());
        }
        return resultMap;
    }

    /**
     * 获取入库数
     * @return
     */
    public Map<Integer, Integer> queryStorgingCount(StatisticsEntityResquestVo staRequest) {
        Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
        List<StatisticsEntityResponseVo> storgeList = statisticsEntityMapper.queryStorgingCount(staRequest);
        for (StatisticsEntityResponseVo storge : storgeList) {
            resultMap.put(storge.getCustomerId(), storge.getCount());
        }
        return resultMap;
    }

    /**
     * 获取订单数
     * @return
     */
    public Map<Integer, Integer> queryOrderCount(StatisticsEntityResquestVo staRequest) {
        Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
        List<StatisticsEntityResponseVo> storgeList = statisticsEntityMapper.queryOrderCount(staRequest);
        for (StatisticsEntityResponseVo storge : storgeList) {
            resultMap.put(storge.getCustomerId(), storge.getCount());
        }
        return resultMap;
    }

    /**
     * 获取出库单数
     * @return
     */
    public Map<Integer, Integer> queryOutStorgeCount(StatisticsEntityResquestVo staRequest) {
        Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
        List<StatisticsEntityResponseVo> storgeList = statisticsEntityMapper.queryOutStorgeCount(staRequest);
        for (StatisticsEntityResponseVo storge : storgeList) {
            resultMap.put(storge.getCustomerId(), storge.getCount());
        }
        return resultMap;
    }

    /**
     * 查询客户收车数
     * @return
     */
    public Map<Integer, Integer> queryCustomerGetCarCount(StatisticsEntityResquestVo staRequest) {
        Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
        List<StatisticsEntityResponseVo> storgeList = statisticsEntityMapper.queryCustomerGetCarCount(staRequest);
        for (StatisticsEntityResponseVo storge : storgeList) {
            resultMap.put(storge.getCustomerId(), storge.getCount());
        }
        return resultMap;
    }

    /**
     * 查询待收车数
     * @return
     */
    public Map<Integer, Integer> selectWaitingCarCount(StatisticsEntityResquestVo staRequest) {
        Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
        List<StatisticsEntityResponseVo> storgeList = statisticsEntityMapper.selectWaitingCarCount(staRequest);
        for (StatisticsEntityResponseVo storge : storgeList) {
            resultMap.put(storge.getCustomerId(), storge.getCount());
        }
        return resultMap;
    }

    /**
     * @param date
     * @param warehouseId
     * @return
     * @see com.anji.allways.business.service.StatisticsService#queryWarehouseDataByDateAndId(java.util.Date, java.lang.Integer)
     */
    @Override
    public Map<String, Object> selectByDateAndWarehouseIdAndCustomerId(Date date, Integer warehouseId, Integer customerId) {
        Map<String, Object> mapOne = new HashMap<String, Object>();

        Map<String, Object> mapTwo = new HashMap<String, Object>();

        Map<String, Object> vue = new HashMap<String, Object>();
        vue.put("date", date);
        vue.put("customerId", customerId);
        vue.put("warehouseId", warehouseId);
        StatisticsEntity statisticsEntity = statisticsEntityMapper.selectByDateAndWarehouseIdAndCustomerIdOne(vue);
        if (null != statisticsEntity) {
            mapOne.put("warehouseId", statisticsEntity.getWarehouseId());
            mapOne.put("customerId", statisticsEntity.getCustomerId());
            mapOne.put("date", statisticsEntity.getDate());
            mapOne.put("warehouseAmount", statisticsEntity.getWarehouseAmount());
            mapOne.put("storageAmount", statisticsEntity.getStorageAmount());
            mapOne.put("incomingAmount", statisticsEntity.getIncomingAmount());
            mapOne.put("orderAmount", statisticsEntity.getOrderAmount());
            mapOne.put("deliveryAmount", statisticsEntity.getDeliveryAmount());
            mapOne.put("receivedAmount", statisticsEntity.getReceivedAmount());
            mapOne.put("age19Amount", statisticsEntity.getAge19Amount());
            mapOne.put("age1019Amount", statisticsEntity.getAge1019Amount());
            mapOne.put("age2029Amount", statisticsEntity.getAge2029Amount());
            mapOne.put("age3045Amount", statisticsEntity.getAge3045Amount());
            mapOne.put("age4660Amount", statisticsEntity.getAge4660Amount());
            mapOne.put("ageOver60Amount", statisticsEntity.getAgeOver60Amount());
            mapOne.put("age19AmountRate", getAgeAcount(statisticsEntity, statisticsEntity.getAge19Amount()));
            mapOne.put("age1019AmountRate", getAgeAcount(statisticsEntity, statisticsEntity.getAge1019Amount()));
            mapOne.put("age2029AmountRate", getAgeAcount(statisticsEntity, statisticsEntity.getAge2029Amount()));
            mapOne.put("age3045AmountRate", getAgeAcount(statisticsEntity, statisticsEntity.getAge3045Amount()));
            mapOne.put("age4660AmountRate", getAgeAcount(statisticsEntity, statisticsEntity.getAge4660Amount()));
            mapOne.put("ageOver60AmountRate", getAgeAcount(statisticsEntity, statisticsEntity.getAgeOver60Amount()));
            mapOne.put("allWarehouse", statisticsEntityMapper.selectAllWarehouse(vue));
            List<String> list = vehicleEntityMapper.seleByWareHouseIdToStorageTime(vue);
            long dayNumbers = 0;
            if (list != null && list.size() > 0) {
                for (String storageTime : list) {
                    DateUtil.getInstance();
                    Date begin = DateUtil.formateStringToDate(storageTime, "yyyy-MM-dd");
                    long dayNumber = DateUtil.getTwoDay(begin, new Date());
                    dayNumbers += dayNumber;
                }
                mapOne.put("avgAmount", dayNumbers / list.size());
            } else {
                mapOne.put("avgAmount", 0);
            }
        }

        StatisticsEntity statisticsEntityTwo = statisticsEntityMapper.selectByDateAndWarehouseIdAndCustomerIdTwo(vue);
        if (null != statisticsEntityTwo) {
            mapTwo.put("warehouseId", statisticsEntityTwo.getWarehouseId());
            mapTwo.put("customerId", statisticsEntityTwo.getCustomerId());
            mapTwo.put("date", statisticsEntityTwo.getDate());
            mapTwo.put("warehouseAmount", statisticsEntityTwo.getWarehouseAmount());
            mapTwo.put("storageAmount", statisticsEntityTwo.getStorageAmount());
            mapTwo.put("incomingAmount", statisticsEntityTwo.getIncomingAmount());
            mapTwo.put("orderAmount", statisticsEntityTwo.getOrderAmount());
            mapTwo.put("deliveryAmount", statisticsEntityTwo.getDeliveryAmount());
            mapTwo.put("receivedAmount", statisticsEntityTwo.getReceivedAmount());
            mapTwo.put("age19Amount", statisticsEntityTwo.getAge19Amount());
            mapTwo.put("age1019Amount", statisticsEntityTwo.getAge1019Amount());
            mapTwo.put("age2029Amount", statisticsEntityTwo.getAge2029Amount());
            mapTwo.put("age3045Amount", statisticsEntityTwo.getAge3045Amount());
            mapTwo.put("age4660Amount", statisticsEntityTwo.getAge4660Amount());
            mapTwo.put("ageOver60Amount", statisticsEntityTwo.getAgeOver60Amount());
            mapTwo.put("age19AmountRate", getAgeAcount(statisticsEntityTwo, statisticsEntityTwo.getAge19Amount()));
            mapTwo.put("age1019AmountRate", getAgeAcount(statisticsEntityTwo, statisticsEntityTwo.getAge1019Amount()));
            mapTwo.put("age2029AmountRate", getAgeAcount(statisticsEntityTwo, statisticsEntityTwo.getAge2029Amount()));
            mapTwo.put("age3045AmountRate", getAgeAcount(statisticsEntityTwo, statisticsEntityTwo.getAge3045Amount()));
            mapTwo.put("age4660AmountRate", getAgeAcount(statisticsEntityTwo, statisticsEntityTwo.getAge4660Amount()));
            mapTwo.put("ageOver60AmountRate", getAgeAcount(statisticsEntityTwo, statisticsEntityTwo.getAgeOver60Amount()));
            List<String> list = vehicleEntityMapper.seleByWareHouseIdToStorageTime(vue);
            long dayNumbers = 0;
            if (list != null && list.size() > 0) {
                for (String storageTime : list) {
                    DateUtil.getInstance();
                    Date begin = DateUtil.formateStringToDate(storageTime, "yyyy-MM-dd");
                    long dayNumber = DateUtil.getTwoDay(begin, new Date());
                    dayNumbers += dayNumber;
                }
                mapTwo.put("avgAmount", dayNumbers / list.size());
            } else {
                mapTwo.put("avgAmount", 0);
            }
        }
        Map<String, Object> out = new HashMap<String, Object>();
        out.put("inner", mapOne);
        out.put("out", mapTwo);
        out.put("storageAmount", vehicleEntityMapper.selectStorageAmountByCustomerAndWarehouse(vue));
        out.put("spaceAmount", vehicleEntityMapper.selectSpaceAmountByCustomerAndWarehouse(vue));
        WarehouseEntity entity = warehouseEntityMapper.selectByPrimaryKey(warehouseId);
        out.put("warehouseName", entity.getName());
        out.put("date", DateUtil.getInstance().formatAll(new Date(), "yyyy-MM-dd HH:mm"));
        return out;
    }

    /**
     * @param date
     * @param warehouseId
     * @param customerId
     * @return
     * @see com.anji.allways.business.service.StatisticsService#selectByDateAndWarehouseId(java.util.Date, java.lang.Integer, java.lang.Integer)
     */
    @Override
    public Map<String, Object> selectByDateAndAndCustomerId(Date date, Integer customerId) {
        Map<String, Object> mapOne = new HashMap<String, Object>();

        Map<String, Object> mapTwo = new HashMap<String, Object>();

        Map<String, Object> vue = new HashMap<String, Object>();
        vue.put("date", date);
        vue.put("customerId", customerId);
        StatisticsEntity statisticsEntity = statisticsEntityMapper.selectByDateAndAndCustomerIdOne(vue);
        if (null != statisticsEntity) {
            mapOne.put("warehouseId", statisticsEntity.getWarehouseId());
            mapOne.put("customerId", statisticsEntity.getCustomerId());
            mapOne.put("date", statisticsEntity.getDate());
            mapOne.put("warehouseAmount", statisticsEntity.getWarehouseAmount());
            mapOne.put("storageAmount", statisticsEntity.getStorageAmount());
            mapOne.put("incomingAmount", statisticsEntity.getIncomingAmount());
            mapOne.put("orderAmount", statisticsEntity.getOrderAmount());
            mapOne.put("deliveryAmount", statisticsEntity.getDeliveryAmount());
            mapOne.put("receivedAmount", statisticsEntity.getReceivedAmount());
            mapOne.put("age19Amount", statisticsEntity.getAge19Amount());
            mapOne.put("age1019Amount", statisticsEntity.getAge1019Amount());
            mapOne.put("age2029Amount", statisticsEntity.getAge2029Amount());
            mapOne.put("age3045Amount", statisticsEntity.getAge3045Amount());
            mapOne.put("age4660Amount", statisticsEntity.getAge4660Amount());
            mapOne.put("ageOver60Amount", statisticsEntity.getAgeOver60Amount());
            mapOne.put("age19AmountRate", getAgeAcount(statisticsEntity, statisticsEntity.getAge19Amount()));
            mapOne.put("age1019AmountRate", getAgeAcount(statisticsEntity, statisticsEntity.getAge1019Amount()));
            mapOne.put("age2029AmountRate", getAgeAcount(statisticsEntity, statisticsEntity.getAge2029Amount()));
            mapOne.put("age3045AmountRate", getAgeAcount(statisticsEntity, statisticsEntity.getAge3045Amount()));
            mapOne.put("age4660AmountRate", getAgeAcount(statisticsEntity, statisticsEntity.getAge4660Amount()));
            mapOne.put("ageOver60AmountRate", getAgeAcount(statisticsEntity, statisticsEntity.getAgeOver60Amount()));
            mapOne.put("allWarehouse", statisticsEntityMapper.selectAllWarehouse(vue));
            List<String> list = vehicleEntityMapper.seleByWareHouseIdToStorageTime(vue);
            long dayNumbers = 0;
            if (list != null && list.size() > 0) {
                for (String storageTime : list) {
                    DateUtil.getInstance();
                    Date begin = DateUtil.formateStringToDate(storageTime, "yyyy-MM-dd");
                    long dayNumber = DateUtil.getTwoDay(begin, new Date());
                    dayNumbers += dayNumber;
                }
                mapOne.put("avgAmount", dayNumbers / list.size());
            } else {
                mapOne.put("avgAmount", 0);
            }
        }

        StatisticsEntity statisticsEntityTwo = statisticsEntityMapper.selectByDateAndAndCustomerIdOne(vue);
        if (null != statisticsEntityTwo) {
            mapTwo.put("warehouseId", statisticsEntityTwo.getWarehouseId());
            mapTwo.put("customerId", statisticsEntityTwo.getCustomerId());
            mapTwo.put("date", statisticsEntityTwo.getDate());
            mapTwo.put("warehouseAmount", statisticsEntityTwo.getWarehouseAmount());
            mapTwo.put("storageAmount", statisticsEntityTwo.getStorageAmount());
            mapTwo.put("incomingAmount", statisticsEntityTwo.getIncomingAmount());
            mapTwo.put("orderAmount", statisticsEntityTwo.getOrderAmount());
            mapTwo.put("deliveryAmount", statisticsEntityTwo.getDeliveryAmount());
            mapTwo.put("receivedAmount", statisticsEntityTwo.getReceivedAmount());
            mapTwo.put("age19Amount", statisticsEntityTwo.getAge19Amount());
            mapTwo.put("age1019Amount", statisticsEntityTwo.getAge1019Amount());
            mapTwo.put("age2029Amount", statisticsEntityTwo.getAge2029Amount());
            mapTwo.put("age3045Amount", statisticsEntityTwo.getAge3045Amount());
            mapTwo.put("age4660Amount", statisticsEntityTwo.getAge4660Amount());
            mapTwo.put("ageOver60Amount", statisticsEntityTwo.getAgeOver60Amount());
            mapTwo.put("age19AmountRate", getAgeAcount(statisticsEntityTwo, statisticsEntityTwo.getAge19Amount()));
            mapTwo.put("age1019AmountRate", getAgeAcount(statisticsEntityTwo, statisticsEntityTwo.getAge1019Amount()));
            mapTwo.put("age2029AmountRate", getAgeAcount(statisticsEntityTwo, statisticsEntityTwo.getAge2029Amount()));
            mapTwo.put("age3045AmountRate", getAgeAcount(statisticsEntityTwo, statisticsEntityTwo.getAge3045Amount()));
            mapTwo.put("age4660AmountRate", getAgeAcount(statisticsEntityTwo, statisticsEntityTwo.getAge4660Amount()));
            mapTwo.put("ageOver60AmountRate", getAgeAcount(statisticsEntityTwo, statisticsEntityTwo.getAgeOver60Amount()));
            List<String> list = vehicleEntityMapper.seleByWareHouseIdToStorageTime(vue);
            long dayNumbers = 0;
            if (list != null && list.size() > 0) {
                for (String storageTime : list) {
                    DateUtil.getInstance();
                    Date begin = DateUtil.formateStringToDate(storageTime, "yyyy-MM-dd");
                    long dayNumber = DateUtil.getTwoDay(begin, new Date());
                    dayNumbers += dayNumber;
                }
                mapTwo.put("avgAmount", dayNumbers / list.size());
            } else {
                mapTwo.put("avgAmount", 0);
            }
        }
        Map<String, Object> out = new HashMap<String, Object>();
        out.put("inner", mapOne);
        out.put("out", mapTwo);
        vue.put("userId", customerId);
        out.put("incomingAmount", vehicleEntityMapper.selectStorageVehicleCountByCustomer(vue));
        out.put("orderAmount", vehicleEntityMapper.selectOrderCountByCustomer(vue));
        // 代收车数
        List<Integer> list = new ArrayList<Integer>();
        list = new ArrayList<Integer>();
        list.add(4);
        list.add(7);
        list.add(9);
        vue.put("list", list);
        out.put("storageAmount", vehicleEntityMapper.selectByIdToHomeCount(vue));
        // 库存数
        list = new ArrayList<Integer>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        vue.put("list", list);
        out.put("currentStock", vehicleEntityMapper.selectByIdToHomeCount(vue));
        out.put("spaceAmount", StringUtils.isEmpty(vehicleEntityMapper.selectSpaceAmountByCustomer(vue)) == true ? 0 : vehicleEntityMapper.selectSpaceAmountByCustomer(vue));
        int stockCount = vehicleEntityMapper.selectByIdToHomeCount(vue);
        String spaceAmount = vehicleEntityMapper.selectSpaceAmountByCustomer(vue);
        if (spaceAmount != null && Integer.parseInt(spaceAmount) > 0) {
            out.put("rate", String.format("%1$.2f", Double.valueOf(stockCount) / Double.valueOf(spaceAmount)));
        } else {
            out.put("rate", 0);
        }
        List<Map<String, Object>> allWarehouse = vehicleEntityMapper.selectWarehouseVehicleCountByCustomer(vue);
        for (Map<String, Object> map : allWarehouse) {
            vue.put("warehouseId", map.get("id"));
            map.put("allCount", vehicleEntityMapper.selectVehicleCountByWarehouseAndCustomer(vue));
        }
        out.put("customerName", customerEntityMapper.selectCustomerNameByUserId(customerId));
        out.put("allWarehouse", allWarehouse);
        out.put("date", DateUtil.getInstance().formatAll(new Date(), "yyyy-MM-dd HH:mm"));
        return out;
    }

    public String getAgeAcount(StatisticsEntity statisticsEntity, Integer getAgeNumberAmount) {
        int count = statisticsEntity.getAge19Amount() + statisticsEntity.getAge1019Amount() + statisticsEntity.getAge2029Amount() + statisticsEntity.getAge3045Amount()
                    + statisticsEntity.getAge4660Amount() + statisticsEntity.getAgeOver60Amount();
        if (count > 0) {
            double rate = Double.valueOf(getAgeNumberAmount) / Double.valueOf(count);
            return String.format("%1$.2f", rate);
        } else {
            return "0.00";
        }
    }

    /**
     * @param date
     * @param customerId
     * @return
     * @see com.anji.allways.business.service.StatisticsService#selectAllwarehouseByCustomer(java.util.Date, java.lang.Integer)
     */
    @Override
    public Map<String, Object> selectAllwarehouseByCustomer(Date date, Integer customerId) {
        Map<String, Object> map = new HashMap<String, Object>();

        Map<String, Object> vue = new HashMap<String, Object>();
        vue.put("date", date);
        vue.put("customerId", customerId);
        List<String> list = vehicleEntityMapper.seleByWareHouseIdToStorageTime(vue);
        map.put("allWarehouse", list);
        return map;
    }

    /**
     * @param warehouseId
     * @return
     * @see com.anji.allways.business.service.StatisticsService#selectAllwarehouseByWeb(java.lang.Integer)
     */
    @Override
    public Map<String, Object> selectAllwarehouseByWeb(Integer userId) {
        Integer customerId = userService.queryCustomerId(userId, 2);
        List<Integer> ids = warehouseService.selectAllLevelIdByParentId(customerId);
        Map<String, Object> map = new HashMap<String, Object>();
        List<CustomerEntity> customerEntities = customerEntityMapper.selectCustomerByWarehouse(userId);
        Map<String, Object> mapVue = new HashMap<String, Object>();
        if (customerEntities.size() > 0) {
            mapVue.put("warehouseId", customerEntities.get(0).getWarehouseId());
            mapVue.put("customerId", 0);
            List<StatisticsEntity> stocks = statisticsEntityMapper.selectAllStockByWarehouse(mapVue);
            if (stocks.size() > 0) {
                map.put("stock", stocks.get(0));
            } else {
                map.put("stock", "");
            }
            map.put("stocks", stocks);
            mapVue.put("userId", userId);
            mapVue.put("ids", ids);
            map.put("incomingAmount", vehicleEntityMapper.selectStorageVehicleCountByWarehouse(mapVue));
            map.put("orderAmount", vehicleEntityMapper.selectOrderCountByWarehouse(mapVue));
            // 代收车数
            List<Integer> lists = new ArrayList<Integer>();
            lists = new ArrayList<Integer>();
            lists.add(4);
            lists.add(7);
            lists.add(9);
            mapVue.put("list", lists);
            map.put("storageAmount", vehicleEntityMapper.selectByIdToHomeCountByWeb(mapVue));
            // 已完成数
            lists = new ArrayList<Integer>();
            lists.add(5);
            mapVue.put("list", lists);
            map.put("incomeAmount", vehicleEntityMapper.selectNotIncomeVehicleByWeb(mapVue));
            // 库存数
            lists = new ArrayList<Integer>();
            lists.add(0);
            lists.add(1);
            lists.add(2);
            lists.add(3);
            mapVue.put("list", lists);
            map.put("currentStock", vehicleEntityMapper.selectByIdToHomeCountByWeb(mapVue));
            map.put("spaceAmount", StringUtils.isEmpty(vehicleEntityMapper.selectSpaceAmountByWarehouse(mapVue)) == true ? 0 : vehicleEntityMapper.selectSpaceAmountByWarehouse(mapVue));
            int stockCount = vehicleEntityMapper.selectByIdToHomeCount(mapVue);
            String spaceAmount = vehicleEntityMapper.selectSpaceAmountByCustomer(mapVue);
            if (spaceAmount != null && Integer.parseInt(spaceAmount) > 0) {
                map.put("rate", String.format("%1$.2f", Double.valueOf(stockCount) / Double.valueOf(spaceAmount)));
            } else {
                map.put("rate", 0);
            }
            List<Map<String, Object>> list = vehicleEntityMapper.selectCustomerByWarehouseWeb(mapVue);
            for (Map<String, Object> map2 : list) {
                int spaceAmounts = 0;
                int storageAmount = 0;
                for (Integer id : ids) {
                    mapVue.put("customerId", map2.get("id"));
                    mapVue.put("warehouseId", id);
                    if (!StringUtils.isEmpty(vehicleEntityMapper.selectSpaceAmountByWarehouse(mapVue)) && !vehicleEntityMapper.selectSpaceAmountByWarehouse(mapVue).equals("")) {
                        spaceAmounts += Integer.parseInt(vehicleEntityMapper.selectSpaceAmountByWarehouse(mapVue));
                    }
                    storageAmount += vehicleEntityMapper.selectStorageAmountByCustomerAndWarehouseWeb(mapVue);
                }
                map2.put("spaceAmount", spaceAmounts);
                map2.put("storageAmount", storageAmount);
            }
            map.put("customerCount", list);
        } else {
            map.put("stock", "");
            map.put("stocks", "");
        }

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CustomerEntity customer : customerEntities) {
            mapVue.put("warehouseId", customer.getWarehouseId());
            mapVue.put("customerId", customer.getId());
            Map<String, Object> mapCustomer = new HashMap<String, Object>();
            List<StatisticsEntity> statistics = statisticsEntityMapper.selectAllStockByWarehouse(mapVue);
            mapCustomer.put("statistics", statistics);
            if (statistics.size() > 0) {
                mapCustomer.put("storageAmount", statistics.get(0).getStorageAmount());
            } else {
                mapCustomer.put("storageAmount", 0);
            }
            mapCustomer.put("customerName", customer.getName());
            list.add(mapCustomer);
        }
        map.put("customerList", list);
        map.put("date", DateUtil.getInstance().formatAll(new Date(), "yyyy-MM-dd HH:mm"));
        return map;
    }

    /**
     * @return
     * @see com.anji.allways.business.service.StatisticsService#statisticalAllWarehouse()
     */
    @SuppressWarnings("static-access")
    @Override
    public Map<String, Object> statisticalAllWarehouse() {
        // 统计最底层的仓库
        List<WarehouseEntity> childrenEntities = warehouseEntityMapper.getAllChildrenWarehouse();
        statisticsList(childrenEntities);

        // 统计所有父类仓库
        List<WarehouseEntity> parentEntities = warehouseEntityMapper.getAllParentWarehouse();
        DateUtil util = DateUtil.getInstance();
        String requestData = util.formatAll(util.getPlusDay(-1), util.DATEANDMINUSPATTERN);
        logger.info("开始统计日期为：" + requestData + " 的信息");
        List<StatisticsEntity> topCustomerList = new ArrayList<StatisticsEntity>();
        for (WarehouseEntity warehouseEntity : parentEntities) {
            List<StatisticsEntity> todayStatistics = new ArrayList<StatisticsEntity>();
            try {
                todayStatistics = doWarehouseStatistics(warehouseEntity, requestData);
            } catch (Exception e) {
                e.printStackTrace();
            }
            topCustomerList.addAll(todayStatistics);
            // 计算仓库与子仓库的总和
            StatisticsEntity todayAllStatistic = new StatisticsEntity();
            // 时间
            try {
                todayAllStatistic.setDate(util.getDateByString(requestData, util.DATEANDMINUSPATTERN));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 仓库ID
            todayAllStatistic.setWarehouseId(warehouseEntity.getId());
            // 客户ID
            todayAllStatistic.setCustomerId(0);
            // 统计类型
            todayAllStatistic.setType(1);
            // 统计总和
            this.getAllCountByWareHouse(todayStatistics, todayAllStatistic);
            this.getAllCountWareHouse(warehouseEntity, todayAllStatistic);
            // 备注信息
            todayAllStatistic.setReserved("仓库为：" + warehouseEntity.getName() + " 仓库及其子仓库的信息总集");
            todayStatistics.add(todayAllStatistic);
            for (StatisticsEntity todayStatistic : todayStatistics) {
                statisticsEntityMapper.insertSelective(todayStatistic);
            }
        }
        // 统计经销商信息
        try {
            this.doCustomerStatistics(topCustomerList, requestData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 统计出库库龄数据
        try {
            this.countCustomerStorage(requestData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("static-access")
    public void statisticsList(List<WarehouseEntity> warehouseEntities) {
        DateUtil util = DateUtil.getInstance();
        String requestData = util.formatAll(util.getPlusDay(-1), util.DATEANDMINUSPATTERN);
        for (WarehouseEntity warehouse : warehouseEntities) {
            logger.info("仓库ID:" + JSONObject.toJSONString(warehouse.getId()));
            StatisticsEntityResquestVo staRequest = new StatisticsEntityResquestVo();
            List<Integer> warehouseIds = new ArrayList<Integer>();
            warehouseIds.add(warehouse.getId());
            staRequest.setWarehouseIds(warehouseIds);
            staRequest.setRequestData(requestData);
            // 查询库位数
            List<StatisticsEntityResponseVo> storgeList = statisticsEntityMapper.queryStorgeCount(staRequest);
            if (storgeList.size() > 0) {
                logger.info("查询库位数:" + JSONObject.toJSONString(storgeList));
                // 查询库存数
                Map<Integer, Integer> storageedCarCount = this.queryStorageedCarCount(staRequest);
                logger.info("查询库存数:" + JSONObject.toJSONString(storageedCarCount));
                // 获取入库数
                Map<Integer, Integer> storgingCount = this.queryStorgingCount(staRequest);
                logger.info("获取入库数:" + JSONObject.toJSONString(storgingCount));
                // 获取订单数
                Map<Integer, Integer> orderCount = this.queryOrderCount(staRequest);
                logger.info("获取订单数:" + JSONObject.toJSONString(orderCount));
                // 获取出库单数
                Map<Integer, Integer> outStorgeCount = this.queryOutStorgeCount(staRequest);
                logger.info("获取出库单数:" + JSONObject.toJSONString(outStorgeCount));
                // 查询客户收车数
                Map<Integer, Integer> customerGetCarCount = this.queryCustomerGetCarCount(staRequest);
                logger.info("获取出库单数:" + JSONObject.toJSONString(customerGetCarCount));
                // 查询客户待收车数
                Map<Integer, Integer> watingCarCount = this.selectWaitingCarCount(staRequest);
                logger.info("查询客户待收车数:" + JSONObject.toJSONString(watingCarCount));

                for (StatisticsEntityResponseVo statisResponse : storgeList) {
                    Integer customerId = statisResponse.getCustomerId();
                    logger.info("客户信息ID:" + customerId);
                    StatisticsEntity todayStatistics = new StatisticsEntity();
                    // 时间
                    try {
                        todayStatistics.setDate(util.getDateByString(requestData, util.DATEANDMINUSPATTERN));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 仓库ID
                    todayStatistics.setWarehouseId(warehouse.getId());
                    // 客户ID
                    todayStatistics.setCustomerId(customerId);
                    // 库位数
                    todayStatistics.setWarehouseAmount(null == statisResponse.getCount() ? Integer.valueOf(0) : statisResponse.getCount());
                    // 库存数
                    todayStatistics.setStorageAmount(null == storageedCarCount.get(customerId) ? Integer.valueOf(0) : storageedCarCount.get(customerId));
                    // 入库数
                    todayStatistics.setIncomingAmount(null == storgingCount.get(customerId) ? Integer.valueOf(0) : storgingCount.get(customerId));
                    // 订单数
                    todayStatistics.setOrderAmount(null == orderCount.get(customerId) ? Integer.valueOf(0) : orderCount.get(customerId));
                    // 出库数
                    todayStatistics.setDeliveryAmount(null == outStorgeCount.get(customerId) ? Integer.valueOf(0) : outStorgeCount.get(customerId));
                    // 客户收车数
                    todayStatistics.setReceivedAmount(null == customerGetCarCount.get(customerId) ? Integer.valueOf(0) : customerGetCarCount.get(customerId));
                    // 待收车数
                    todayStatistics.setReceivingAmount(null == watingCarCount.get(customerId) ? Integer.valueOf(0) : watingCarCount.get(customerId));
                    // 统计类型
                    todayStatistics.setType(0);
                    // 获取在库时间件数
                    staRequest.setDealerId(customerId);
                    List<StatisticsEntityResponseVo> statisticList = statisticsEntityMapper.selectStayTime(staRequest);
                    Map<String, Integer> storageDay = this.queryStayTime(1, customerId, statisticList);
                    // 库龄1-9天
                    todayStatistics.setAge19Amount(storageDay.get("one"));
                    // 库龄10-19天
                    todayStatistics.setAge1019Amount(storageDay.get("two"));
                    // 库龄20-29天
                    todayStatistics.setAge2029Amount(storageDay.get("three"));
                    // 库龄30-45天
                    todayStatistics.setAge3045Amount(storageDay.get("four"));
                    // 库龄46-60天
                    todayStatistics.setAge4660Amount(storageDay.get("five"));
                    // 库龄60天以上
                    todayStatistics.setAgeOver60Amount(storageDay.get("six"));
                    // 备注信息
                    todayStatistics.setReserved("仓库对应客户统计在库信息");
                    statisticsEntityMapper.insertSelective(todayStatistics);
                    // 客户ID
                    todayStatistics.setCustomerId(0);
                    // 备注信息
                    todayStatistics.setReserved("仓库为：" + warehouse.getName() + " 仓库及其子仓库的信息总集");
                    statisticsEntityMapper.insertSelective(todayStatistics);
                }
            } else {
                StatisticsEntity statisticsEntity = new StatisticsEntity();
                // 时间
                try {
                    statisticsEntity.setDate(util.getDateByString(requestData, util.DATEANDMINUSPATTERN));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 仓库id
                statisticsEntity.setWarehouseId(warehouse.getId());
                // 客户id
                statisticsEntity.setCustomerId(0);
                // 库位数
                statisticsEntity.setWarehouseAmount(warehouse.getSpaceAmount());
                // 库存数
                statisticsEntity.setStorageAmount(0);
                // 入库数
                statisticsEntity.setIncomingAmount(0);
                // 订单数
                statisticsEntity.setOrderAmount(0);
                // 出库数
                statisticsEntity.setDeliveryAmount(0);
                // 客户收车数
                statisticsEntity.setReceivedAmount(0);
                // 待收车数
                statisticsEntity.setReceivingAmount(0);
                // 库龄1-9
                statisticsEntity.setAge19Amount(0);
                // 库存10-19
                statisticsEntity.setAge1019Amount(0);
                // 库存20-29
                statisticsEntity.setAge2029Amount(0);
                // 库存30-45
                statisticsEntity.setAge3045Amount(0);
                // 库存46-60
                statisticsEntity.setAge4660Amount(0);
                // 库存超过60
                statisticsEntity.setAgeOver60Amount(0);
                statisticsEntity.setType(0);
                statisticsEntity.setReserved("仓库没有对应客户统计在库信息");
                statisticsEntityMapper.insert(statisticsEntity);
            }
        }
    }

    /**
     * @return
     * @see com.anji.allways.business.service.StatisticsService#statisticalCustomerGroup()
     */
    @Override
    public Map<String, Object> statisticalCustomerGroup(Integer userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> vue = new HashMap<String, Object>();
        vue.put("userId", userId);
        vue.put("customerId", userId);
        List<Integer> list = new ArrayList<Integer>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        vue.put("list", list);
        map.put("storageAmount", vehicleEntityMapper.selectStockByGroupId(vue));
        map.put("warehouseAmount", StringUtils.isEmpty(vehicleEntityMapper.selectSpaceAmountByGroupId(vue)) == true ? 0 : vehicleEntityMapper.selectSpaceAmountByGroupId(vue));
        map.put("incomingAmount", vehicleEntityMapper.selectStorageVehicleCountByGroup(vue));
        map.put("orderAmount", vehicleEntityMapper.selectOrderCountByGroup(vue));
        List<Map<String, Object>> customer = customerEntityMapper.selectCustomerByGroupId(userId);
        List<Map<String, Object>> customerList = new ArrayList<Map<String, Object>>();
        Integer receivingAmount = 0;
        Integer currentStock = 0;
        for (Map<String, Object> map2 : customer) {
            Map<String, Object> map3 = new HashMap<String, Object>();
            // 代收车数
            list = new ArrayList<Integer>();
            list.add(0);
            list.add(1);
            list.add(2);
            list.add(3);
            vue.put("list", list);
            vue.put("userId", map2.get("id"));
            map3.put("currentStock", vehicleEntityMapper.selectByIdToHomeCount(vue));
            receivingAmount += vehicleEntityMapper.selectByIdToHomeCount(vue);
            map3.put("spaceAmount", StringUtils.isEmpty(vehicleEntityMapper.selectSpaceAmountByCustomer(vue)) == true ? 0 : vehicleEntityMapper.selectSpaceAmountByCustomer(vue));
            map3.put("userId", map2.get("id"));
            map3.put("userName", map2.get("name"));
            customerList.add(map3);
            list = new ArrayList<Integer>();
            list.add(4);
            list.add(7);
            list.add(9);
            vue.put("list", list);
            currentStock += vehicleEntityMapper.selectByIdToHomeCount(vue);
        }
        map.put("date", DateUtil.getInstance().formatAll(new Date(), "yyyy-MM-dd HH:mm"));
        map.put("customerList", customerList);
        map.put("receivingAmount", receivingAmount);
        map.put("currentStock", currentStock);
        map.put("groupName", customerEntityMapper.selectCustomerNameByUserId(userId));
        return map;
    }

}
