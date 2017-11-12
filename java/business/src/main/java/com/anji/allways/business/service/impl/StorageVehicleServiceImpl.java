/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anji.allways.business.entity.StorageVehicleEntity;
import com.anji.allways.business.mapper.StorageVehicleEntityMapper;
import com.anji.allways.business.service.StorageVehicleService;

/**
 * @author wangyanjun
 * @version $Id: StorageServiceImpl.java, v 0.1 2017年8月31日 上午11:16:49 wangyanjun Exp $
 */
@Service
@Transactional
public class StorageVehicleServiceImpl implements StorageVehicleService {

    @Autowired
    private StorageVehicleEntityMapper storageVehicleEntityMapper;

    /**
     * 根据车辆ID检索入库车辆信息
     * @param id
     * @return
     */
    public StorageVehicleEntity selectByPrimaryKey(Integer id) {
        return storageVehicleEntityMapper.selectByPrimaryKey(id);
    }

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
    public int cancelOrAddCarStorage(Integer id, Integer isStorage, String comment) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("isStorage", isStorage);
        map.put("vehicleComment", comment);
        return storageVehicleEntityMapper.updateStorageVehicleByPrimaryKey(map);
    }

    /**
     * 修改入库计划时备注修改操作
     * @param id
     *            车辆ID
     * @param comment
     *            备注
     * @return 影响数据行数
     */
    public int updateStorageByComment(Integer id, String comment) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("vehicleComment", comment);
        return storageVehicleEntityMapper.updateStorageVehicleByPrimaryKey(map);
    }

}
