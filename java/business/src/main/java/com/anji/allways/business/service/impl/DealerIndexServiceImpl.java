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

import com.anji.allways.business.entity.AdvertisementEntity;
import com.anji.allways.business.entity.AppReleaseEntity;
import com.anji.allways.business.entity.CustomerEntity;
import com.anji.allways.business.mapper.AdvertisementEntityMapper;
import com.anji.allways.business.mapper.AppReleaseEntityMapper;
import com.anji.allways.business.mapper.CustomerEntityMapper;
import com.anji.allways.business.mapper.VehicleEntityMapper;
import com.anji.allways.business.service.DealerIndexService;

/**
 * @author 李军
 * @version $Id: DealerIndexServiceImpl.java, v 0.1 2017年8月24日 下午3:12:03 李军 Exp $
 */
@Service
@Transactional
public class DealerIndexServiceImpl implements DealerIndexService {

    @Autowired
    private AdvertisementEntityMapper advertisementEntityMapper;

    @Autowired
    private CustomerEntityMapper      customerEntityMapper;

    @Autowired
    private AppReleaseEntityMapper    appReleaseEntityMapper;

    @Autowired
    private VehicleEntityMapper       vehicleEntityMapper;

    /**
     * @return
     * @see com.anji.allways.business.service.DealerIndexService#getCustomerAbbreviation()
     */
    @Override
    public Map<String, Object> getCustomerAbbreviation(String userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 获取轮播图片
        List<AdvertisementEntity> advertisementEntities = advertisementEntityMapper.allAdvertisement();

        map.put("advertisement", advertisementEntities);

        if (userId != null && userId != "" && !userId.equals("0")) {
            // 获取客户信息
            CustomerEntity customerEntity = customerEntityMapper.queryCustomerInfoByUserId(Integer.valueOf(userId));
            map.put("abbreviation", customerEntity.getShortName());
        } else {
            map.put("abbreviation", "诚以行道，以信载物");
        }

        return map;
    }

    /**
     * @param type
     * @return
     * @see com.anji.allways.business.service.DealerIndexService#checkEditionInformation(int)
     */
    @Override
    public Map<String, Object> checkEditionInformation(int type) {
        Map<String, Object> map = new HashMap<String, Object>();
        AppReleaseEntity appReleaseEntity = appReleaseEntityMapper.selectByTypeDesc(type);
        map.put("appRelease", appReleaseEntity);
        return map;
    }

    /**
     * @param userId
     * @return
     * @see com.anji.allways.business.service.DealerIndexService#getVehicleCount(java.lang.String)
     */
    @Override
    public Map<String, Object> getVehicleCount(String userId) {
        Map<String, Object> map = new HashMap<String, Object>();

        Map<String, Object> mapVue = new HashMap<String, Object>();
        mapVue.put("userId", userId);

        if (userId != null && userId != "" && !userId.equals("0")) {
            // 当前库存数
            List<Integer> list = new ArrayList<Integer>();
            list.add(0);
            list.add(1);
            list.add(2);
            list.add(3);

            mapVue.put("list", list);
            map.put("currentStock", vehicleEntityMapper.selectByIdToHomeCount(mapVue));

            // 代收车数
            list = new ArrayList<Integer>();
            list.add(4);
            list.add(7);
            list.add(9);

            mapVue.put("list", list);
            map.put("waitIncomeVehilce", vehicleEntityMapper.selectByIdToHomeCount(mapVue));

            // 代发运
            list = new ArrayList<Integer>();
            list.add(1);
            list.add(2);
            list.add(3);

            mapVue.put("list", list);
            map.put("waitOutWarehouse", vehicleEntityMapper.selectByIdToHomeCount(mapVue));

        } else {
            map.put("currentStock", 0);
            map.put("waitIncomeVehilce", 0);
            map.put("waitOutWarehouse", 0);
        }

        return map;
    }

}
