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

import com.anji.allways.business.entity.FileEntity;
import com.anji.allways.business.entity.VehicleEntity;
import com.anji.allways.business.enums.RowDictCodeE;
import com.anji.allways.business.mapper.FileEntityMapper;
import com.anji.allways.business.mapper.OrderEntityMapper;
import com.anji.allways.business.mapper.VehicleEntityMapper;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.service.VehicleDetailService;
import com.anji.allways.common.util.DateUtil;
import com.anji.allways.common.util.FileUtil;
import com.anji.allways.common.util.PageUtil;
import com.anji.allways.common.util.StringUtil;

/**
 * @author 李军
 * @version $Id: VehicleDetailServiceImpl.java, v 0.1 2017年8月29日 上午9:24:34 李军 Exp $
 */
@Service
@Transactional
public class VehicleDetailServiceImpl implements VehicleDetailService {

    @Autowired
    private VehicleEntityMapper vehicleEntityMapper;

    @Autowired
    private FileEntityMapper    fileEntityMapper;

    @Autowired
    private OrderEntityMapper   orderEntityMapper;

    // 获取数据字典Service
    @Autowired
    private DictService         dictService;

    /**
     * @return
     * @see com.anji.allways.business.service.VehicleDetailService#carDetail()
     */
    @Override
    public Map<String, Object> carDetail(int vehicleId) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 查询车辆信息
        VehicleEntity vehicleEntity = vehicleEntityMapper.selectAllVehicle(vehicleId);
        map.put("id", vehicleEntity.getId());
        map.put("warehouseName", vehicleEntity.getWarehouseName());
        map.put("vin", vehicleEntity.getVin());
        map.put("vehicleStatus", vehicleEntity.getVehicleStatus());
        map.put("qualityStatus", vehicleEntity.getQualityStatus());
        map.put("lockStatus", vehicleEntity.getLockStatus());
        map.put("customerName", vehicleEntity.getCustomer());
        map.put("location", vehicleEntity.getLocation());
        map.put("createTime", DateUtil.getInstance().formatAll(vehicleEntity.getPlanTime(), "yyyy-MM-dd HH:mm:ss"));
        map.put("createUser", vehicleEntity.getUserName());
        map.put("brandName", vehicleEntity.getBrand());
        map.put("seriesName", vehicleEntity.getSeries());
        map.put("modelsName", vehicleEntity.getModel());
        if (!StringUtil.isEmpty(vehicleEntity.getAnnounceYear(), true)) {
            map.put("announceYear", vehicleEntity.getAnnounceYear());
        } else {
            map.put("announceYear", "");
        }
        Map<String, Object> orderMap = orderEntityMapper.selectOrderIdByVehicleId(vehicleId);
        if (orderMap != null) {
            map.put("orderId", orderMap.get("id"));
            map.put("transportType", orderMap.get("transportType"));
            map.put("deliveryPlanId", orderMap.get("deliveryPlanId"));
        }
        map.put("standardColor", vehicleEntity.getStandardColor());
        map.put("manufacturerColor", vehicleEntity.getManufacturerColor());
        map.put("licenseNumber", vehicleEntity.getLicenseNumber());
        map.put("registrationNumber", vehicleEntity.getRegistrationNumber());
        map.put("comment", vehicleEntity.getComment());
        map.put("contactorMobile", vehicleEntity.getContactorMobile());
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
            mapVehicle.put("fileName", mapVehicle.get("path").toString());
        }
        Map<String, Object> mapPic = new HashMap<String, Object>();
        mapPic.put("path", vehicleEntity.getLogoPicturePath());
        mapPic.put("fileName", vehicleEntity.getLogoPicturePath());
        mapPic.put("id", "");
        vehiclePic.add(0, mapPic);
        map.put("vehiclePic", vehiclePic);

        // 获取车辆状态数据字典
        Map<String, String> retValue = dictService.queryAllByCode(RowDictCodeE.CAR_STATUS.getValue());
        // 获取质量状态数据字典
        Map<String, String> retValue1 = dictService.queryAllByCode(RowDictCodeE.QUALITY_STATUS.getValue());
        // 获取锁定状态数据字典
        Map<String, String> retValue2 = dictService.queryAllByCode(RowDictCodeE.LOCK_STATUS.getValue());

        Integer vehicleStatus = vehicleEntity.getVehicleStatus();
        // 设置车辆状态名称
        if (vehicleEntity.getVehicleStatus() == 7) {
            vehicleStatus = 4;
        }
        String vehicleStatusName = retValue.get(String.valueOf(vehicleStatus));
        map.put("vehicleStatusName", vehicleStatusName);

        // 设置质量状态名称
        String qualityStatusName = retValue1.get(String.valueOf(vehicleEntity.getQualityStatus()));
        map.put("qualityStatusName", qualityStatusName);

        // 设置锁定状态名称
        String lockStatusName = retValue2.get(String.valueOf(vehicleEntity.getLockStatus()));
        map.put("lockStatusName", lockStatusName);
        return map;
    }

    /**
     * @param map
     * @return
     * @see com.anji.allways.business.service.VehicleDetailService#getStockDetail(java.util.Map)
     */
    @Override
    public Map<String, Object> getStockDetail(Map<String, Object> map) {
        Map<String, Object> mapVue = new HashMap<String, Object>();
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNumber(Integer.parseInt(map.get("pageNumber").toString()));
        pageUtil.setTotalNumber(vehicleEntityMapper.getCount(map));
        pageUtil.setDbNumber(pageUtil.getDbIndex() + pageUtil.getPageNumber());
        pageUtil.setCurrentPage(Integer.parseInt(map.get("currentPage").toString()));
        map.put("pageIndex", pageUtil.getDbIndex());
        map.put("pageNumber", pageUtil.getDbNumber());
        List<VehicleEntity> list = vehicleEntityMapper.getStockDetail(map);
        Map<String, Object> mapModelPicturMap = new HashMap<String, Object>();
        for (VehicleEntity vehicleEntity : list) {
            Map<String, Object> orderMap = orderEntityMapper.selectOrderIdByVehicleId(vehicleEntity.getId());
            if (orderMap != null) {
                vehicleEntity.setOrderId(Integer.valueOf(orderMap.get("id").toString()));
                vehicleEntity.setTransportType(orderMap.get("transportType").toString());
                vehicleEntity.setDeliveryPlanId(Integer.parseInt(orderMap.get("deliveryPlanId").toString()));
            }
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
     * @param vin
     * @return
     * @see com.anji.allways.business.service.VehicleDetailService#getByVinVehicle(java.lang.String)
     */
    @Override
    public Map<String, Object> getByVinVehicle(String vin, int userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 查询车辆信息
        VehicleEntity vehicleEntity = vehicleEntityMapper.getByVinVehicle(vin, userId);
        map.put("id", vehicleEntity.getId());
        map.put("warehouseName", vehicleEntity.getWarehouseName());
        map.put("vin", vehicleEntity.getVin());
        map.put("vehicleStatus", vehicleEntity.getVehicleStatus());
        map.put("qualityStatus", vehicleEntity.getQualityStatus());
        map.put("lockStatus", vehicleEntity.getLockStatus());
        map.put("customerName", vehicleEntity.getCustomer());
        map.put("location", vehicleEntity.getLocation());
        map.put("createTime", DateUtil.getInstance().formatAll(vehicleEntity.getPlanTime(), "yyyy-MM-dd HH:mm:ss"));
        map.put("createUser", vehicleEntity.getUserName());
        map.put("brandName", vehicleEntity.getBrand());
        map.put("seriesName", vehicleEntity.getSeries());
        map.put("modelsName", vehicleEntity.getModel());
        if (!StringUtil.isEmpty(vehicleEntity.getAnnounceYear(), true)) {
            map.put("announceYear", vehicleEntity.getAnnounceYear());
        } else {
            map.put("announceYear", "");
        }
        map.put("standardColor", vehicleEntity.getStandardColor());
        map.put("manufacturerColor", vehicleEntity.getManufacturerColor());
        map.put("licenseNumber", vehicleEntity.getLicenseNumber());
        map.put("registrationNumber", vehicleEntity.getRegistrationNumber());
        map.put("comment", vehicleEntity.getComment());
        map.put("contactorMobile", vehicleEntity.getContactorMobile());
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
            mapVehicle.put("fileName", (mapVehicle.get("path")));
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
     * @param fileEntity
     * @return
     * @see com.anji.allways.business.service.VehicleDetailService#updateVehiclePath(com.anji.allways.business.entity.FileEntity)
     */
    @Override
    public Map<String, Object> updateVehiclePath(String vehicleId, String path) {
        List<Map<String, Object>> vehiclePic = fileEntityMapper.getVehiclePic("vehiclePath" + vehicleId);
        Map<String, Object> mapPath = new HashMap<String, Object>();
        mapPath.put("vehicleId", Integer.valueOf(vehicleId));
        mapPath.put("picturePath", "vehiclePath" + vehicleId);
        vehicleEntityMapper.updatePathById(mapPath);
        Map<String, Object> map = new HashMap<String, Object>();
        if (vehiclePic.size() < 4) {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setName("vehiclePath" + vehicleId);
            fileEntity.setPath(path);
            fileEntityMapper.insertSelective(fileEntity);
            map.put("path", path);
            map.put("fileName", path);
            map.put("id", fileEntity.getId());
            map.put("falg", "");
        } else {
            map.put("falg", "上传超限");
        }
        return map;
    }

    /**
     * @param id
     * @return
     * @see com.anji.allways.business.service.VehicleDetailService#deleteVehiclePath(java.lang.Integer)
     */
    @Override
    public boolean deleteVehiclePath(Integer pathId, String path) {
        boolean falg = false;
        int count = fileEntityMapper.deleteByPrimaryKey(pathId);
        if (count > 0) {
            falg = true;
            try {
                FileUtil.getInstance().delFilePathFromFileName(path);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            falg = false;
        }
        return falg;
    }

    /**
     * @param vehicleList
     * @return
     * @see com.anji.allways.business.service.VehicleDetailService#addBatchVehicles(java.util.List)
     */
    @Override
    public Integer addBatchVehicles(List<VehicleEntity> vehicleList) {
        for (VehicleEntity entity : vehicleList) {
            vehicleEntityMapper.insert(entity);
        }
        return vehicleList.size();
    }

    /**
     * @param wareHouseId
     * @param customerId
     * @return
     * @see com.anji.allways.business.service.VehicleDetailService#countInVehicle(java.lang.Integer, java.lang.Integer)
     */
    @Override
    public Integer countInVehicle(Integer wareHouseId, Integer customerId) {
        // TODO Auto-generated method stub
        return null;
    }

}
