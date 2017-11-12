/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.anji.allways.business.service.StatisticsService;

/**
 * 定时任务调度
 * @author Administrator
 * @version $Id: TaskCenter.java, v 0.1 2017年9月29日 下午7:36:00 Administrator Exp $
 */
@Component
public class TaskCenter {

    private Logger            logger = LogManager.getLogger(TaskCenter.class);

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 统计定时任务
     */
    /*
    @Scheduled(cron = "0 0 1 * * ?")
    // ** 1:00 am every day*//*
                             public void corpMonitorPointDataRefresh() {
                             try {
                             statisticsService.refreshStatisticsData();
                             } catch (Exception e) {
                             logger.error("我的数据统计定时任务出错：" + e.getMessage());
                             }
                             }*/

    /**
     * 统计定时任务
     */
    @Scheduled(cron = "0 0 1 * * ?")
    // ** 1:00 am every day*//*
    public void statisticalAllWarehouse() {
        try {
            statisticsService.statisticalAllWarehouse();
        } catch (Exception e) {
            logger.error("我的数据统计定时任务出错：" + e.getMessage());
        }
    }

}
