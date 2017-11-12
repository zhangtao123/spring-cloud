/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anji.allways.business.entity.VehicleEntity;
import com.anji.allways.business.mapper.BrandEntityMapper;
import com.anji.allways.business.mapper.FileEntityMapper;
import com.anji.allways.business.mapper.VehicleEntityMapper;
import com.anji.allways.business.service.EditCarParamenterService;
import com.anji.allways.common.util.DateUtil;

/**
 * @author 李军
 * @version $Id: EditCarParamenterServiceImpl.java, v 0.1 2017年8月26日 下午3:58:11 李军 Exp $
 */
@Service
@Transactional
public class EditCarParamenterServiceImpl implements EditCarParamenterService {

    @Autowired
    private VehicleEntityMapper vehicleEntityMapper;

    @Autowired
    private FileEntityMapper    fileEntityMapper;

    @Autowired
    private BrandEntityMapper   brandEntityMapper;

    /**
     * 获取编辑数据
     * @return
     * @see com.anji.allways.business.service.EditCarParamenterService#getEditParamenter()
     */
    @Override
    public Map<String, Object> getEditParamenter(int vehicleId) {
        Map<String, Object> map = new HashMap<String, Object>();

        // 查询车辆信息
        VehicleEntity vehicleEntity = vehicleEntityMapper.selectAllVehicle(vehicleId);

        map.put("id", vehicleEntity.getId());
        map.put("warehouseName", vehicleEntity.getWarehouseName());
        map.put("vin", vehicleEntity.getVin());
        map.put("vehicleStatus", vehicleEntity.getVehicleStatus());
        map.put("customerName", vehicleEntity.getCustomer());
        map.put("location", vehicleEntity.getLocation());
        map.put("createTime", DateUtil.getInstance().formatAll(vehicleEntity.getPlanTime(), "yyyy-MM-dd HH:mm:ss"));
        map.put("createUser", vehicleEntity.getUserName());
        map.put("brandName", vehicleEntity.getBrand());
        map.put("seriesName", vehicleEntity.getSeries());
        map.put("modelsName", vehicleEntity.getModel());
        map.put("announceYear", vehicleEntity.getAnnounceYear());
        map.put("standardColorId", vehicleEntity.getStandardColorId());
        map.put("standardColor", vehicleEntity.getStandardColor());
        map.put("manufacturerColor", vehicleEntity.getManufacturerColor());
        map.put("licenseNumber", vehicleEntity.getLicenseNumber());
        map.put("registrationNumber", vehicleEntity.getRegistrationNumber());
        map.put("comment", vehicleEntity.getComment());
        Map<String, Object> mapModelPicturMap = new HashMap<String, Object>();
        mapModelPicturMap.put("brand", vehicleEntity.getBrand());
        mapModelPicturMap.put("series", vehicleEntity.getSeries());
        mapModelPicturMap.put("model", vehicleEntity.getModel());
        mapModelPicturMap.put("announceYear", vehicleEntity.getAnnounceYear());
        String modelPicturMap = vehicleEntityMapper.getmodelPicture(mapModelPicturMap);
        vehicleEntity.setLogoPicturePath(modelPicturMap);
        map.put("logoPicturePath", vehicleEntity.getLogoPicturePath());

        // 获取车辆图片
        List<Map<String, Object>> vehiclePic = fileEntityMapper.getVehiclePic(vehicleEntity.getPicturePath());
        for (Map<String, Object> mapVehicle : vehiclePic) {
            mapVehicle.put("fileName", mapVehicle.get("path"));
        }
        Map<String, Object> mapPic = new HashMap<String, Object>();
        mapPic.put("path", vehicleEntity.getLogoPicturePath());
        mapPic.put("fileName", vehicleEntity.getLogoPicturePath());
        mapPic.put("id", "");
        vehiclePic.add(0, mapPic);
        map.put("vehiclePic", vehiclePic);

        return map;
    }

    /**
     * 保存编辑数据
     * @param vehicleEntity
     * @return
     * @see com.anji.allways.business.service.EditCarParamenterService#keepEditParamenter(com.anji.allways.business.entity.VehicleEntity)
     */
    @Override
    public int keepEditParamenter(VehicleEntity vehicleEntity) {
        return vehicleEntityMapper.updateByPrimaryKeyById(vehicleEntity);
    }

    /**
     * @param mapVue
     * @return
     * @see com.anji.allways.business.service.EditCarParamenterService#updateLockStruts(java.util.Map)
     */
    @Override
    public int updateLockStruts(Map<String, Object> mapVue) {
        // 校验是否下订单
        int countOrder = vehicleEntityMapper.queryOrderVehicleCount(Integer.parseInt(mapVue.get("vehicleId").toString()));
        if (countOrder > 0) {
            return -1;
        }
        return vehicleEntityMapper.updateLockStruts(mapVue);
    }

    /**
     * @param mapVue
     * @return
     * @see com.anji.allways.business.service.EditCarParamenterService#checkYearExist(java.util.Map)
     */
    @Override
    public int checkYearExist(Map<String, Object> mapVue) {
        return brandEntityMapper.checkYearExist(mapVue);
    }

    /**
     * @param mapVue
     * @return
     * @see com.anji.allways.business.service.EditCarParamenterService#selectByBrandToYear(java.util.Map)
     */
    @Override
    public List<String> selectByBrandToYear(Map<String, Object> mapVue) {
        return brandEntityMapper.selectByBrandToYear(mapVue);
    }

    /**
     * @param vehicleId
     * @return
     * @see com.anji.allways.business.service.EditCarParamenterService#selectByPrimaryKey(int)
     */
    @Override
    public VehicleEntity selectByPrimaryKey(int vehicleId) {
        return vehicleEntityMapper.selectByPrimaryKey(vehicleId);
    }

}
