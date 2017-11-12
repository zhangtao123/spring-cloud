/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anji.allways.business.dto.DeliveryPlanDTO;
import com.anji.allways.business.entity.AppReleaseEntity;
import com.anji.allways.business.entity.DeliveryPlanEntity;
import com.anji.allways.business.mapper.AppReleaseEntityMapper;
import com.anji.allways.business.mapper.DeliveryPlanEntityMapper;
import com.anji.allways.business.service.DriverIndexService;
import com.anji.allways.common.util.DateUtil;
import com.anji.allways.common.util.PageUtil;

/**
 * @author Administrator
 * @version $Id: DriverIndexServiceImpl.java, v 0.1 2017年9月7日 下午8:25:13 Administrator Exp $
 */
@Service
@Transactional
public class DriverIndexServiceImpl implements DriverIndexService {

    @Autowired
    private DeliveryPlanEntityMapper deliveryPlanEntityMapper;

    @Autowired
    private AppReleaseEntityMapper   appReleaseEntityMapper;

    /**
     * @param userId
     * @return
     * @see com.anji.allways.business.service.DriverIndexService#home(int)
     */
    @Override
    public Map<String, Object> home(Map<String, Object> map) {
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNumber(Integer.parseInt(map.get("pageNumber").toString()));
        pageUtil.setTotalNumber(deliveryPlanEntityMapper.driverDeliveryPlanCount(map));
        pageUtil.setDbNumber(pageUtil.getDbIndex() + pageUtil.getPageNumber());
        pageUtil.setCurrentPage(Integer.parseInt(map.get("currentPage").toString()));
        map.put("pageIndex", pageUtil.getDbIndex());
        map.put("pageNumber", pageUtil.getDbNumber());
        Map<String, Object> maps = new HashMap<String, Object>();
        List<DeliveryPlanDTO> list = deliveryPlanEntityMapper.driverDeliveryPlan(map);
        List<Map<String, Object>> mapVue = new ArrayList<Map<String, Object>>();
        for (DeliveryPlanEntity deliveryPlanDTO : list) {
            Map<String, Object> mapDTO = new HashMap<String, Object>();
            mapDTO.put("id", deliveryPlanDTO.getId());
            mapDTO.put("no", deliveryPlanDTO.getNo());
            mapDTO.put("deliveryPlanTime", DateUtil.getInstance().formatAll(deliveryPlanDTO.getDeliveryPlanTime(), "yyyy-MM-dd HH:mm:ss"));
            mapDTO.put("warehouseName", deliveryPlanDTO.getWarehouseName());
            mapDTO.put("vehicleCount", deliveryPlanDTO.getVehicleCount());
            mapDTO.put("status", deliveryPlanDTO.getStatus());
            List<Map<String, Object>> lastPoint = deliveryPlanEntityMapper.driverDeliveryPlanPoint(deliveryPlanDTO.getId());
            mapDTO.put("lastPoint", lastPoint);
            mapVue.add(mapDTO);
        }

        maps.put("rows", mapVue);
        maps.put("page", pageUtil);
        maps.put("total", pageUtil.getTotalNumber());
        return maps;
    }

    /**
     * @param type
     * @return
     * @see com.anji.allways.business.service.DriverIndexService#checkEditionInformation(int)
     */
    @Override
    public Map<String, Object> checkEditionInformation(int type) {
        Map<String, Object> map = new HashMap<String, Object>();
        AppReleaseEntity appReleaseEntity = appReleaseEntityMapper.selectByTypeDesc(type);
        map.put("appRelease", appReleaseEntity);
        return map;
    }

}
