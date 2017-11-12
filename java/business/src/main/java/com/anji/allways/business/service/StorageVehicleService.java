/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import com.anji.allways.business.entity.StorageVehicleEntity;

/**
 * @author wangyanjun
 * @version $Id: StorageService.java, v 0.1 2017年8月31日 上午10:59:49 wangyanjun Exp $
 */
public interface StorageVehicleService {

    /**
     * 根据车辆ID检索入库车辆信息
     * @param id
     * @return
     */
    StorageVehicleEntity selectByPrimaryKey(Integer id);

    /**
     * 车辆取消计划或加入计划操作
     * @param id
     *            车辆ID
     * @param isStorage
     *            是否加入计划单(1:是 2:否)
     * @param comment
     *            备注
     * @return 影响数据行数
     */
    int cancelOrAddCarStorage(Integer id, Integer isStorage, String comment);

    /**
     * 修改入库计划时备注修改操作
     * @param id
     *            车辆ID
     * @param comment
     *            备注
     * @return 影响数据行数
     */
    int updateStorageByComment(Integer id, String comment);
}
