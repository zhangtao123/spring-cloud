/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anji.allways.business.enums.RowDictCodeE;
import com.anji.allways.business.mapper.BrandEntityMapper;
import com.anji.allways.business.mapper.WarehouseEntityMapper;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.service.StockScreenService;
import com.anji.allways.business.vo.BrandVO;
import com.anji.allways.common.util.DateUtil;

/**
 * @author 李军
 * @version $Id: StockScreenServiceImpl.java, v 0.1 2017年8月28日 下午6:56:11 李军 Exp $
 */
@Service
@Transactional
public class StockScreenServiceImpl implements StockScreenService {

    @Autowired
    private BrandEntityMapper     brandEntityMapper;

    @Autowired
    private WarehouseEntityMapper warehouseEntityMapper;

    @Autowired
    private DictService           dictService;

    /**
     * 获取搜索信息
     * @param userId
     * @return
     * @see com.anji.allways.business.service.StockScreenService#getScreen(java.lang.String)
     */
    @Override
    public Map<String, Object> getScreen(Integer userId) {
        Map<String, Object> map = new HashMap<String, Object>();

        // 查询字母信息
        List<BrandVO> initials = brandEntityMapper.selectAllLetter(userId);

        for (BrandVO initial : initials) {
            // 查询品牌信息
            List<BrandVO> brands = brandEntityMapper.selectAllBrand(initial.getName());
            for (BrandVO brand : brands) {
                // 获取车系信息
                List<BrandVO> series = brandEntityMapper.selectAllSeries(brand.getName());
                for (BrandVO serie : series) {
                    // 查询车型信息
                    Map<String, Object> model = new HashMap<String, Object>();
                    model.put("series", serie.getName());
                    model.put("brand", brand.getName());
                    List<BrandVO> models = brandEntityMapper.selectAllModel(model);
                    serie.setList(models);
                }
                brand.setList(series);
            }

            initial.setList(brands);
        }

        map.put("row", initials);

        // 查询所有仓库
        List<Map<String, Object>> allWarehouse = warehouseEntityMapper.selectByCustomerId(userId);
        map.put("allWarehouse", allWarehouse);

        // 获取标准色
        List<Map<String, Object>> allColor = dictService.selectByCategoryCode(RowDictCodeE.STANDARD_COLOR.getValue());
        for (Map<String, Object> mapurl : allColor) {
            mapurl.put("colorUrl", mapurl.get("colorUrl").toString());
        }
        map.put("allColor", allColor);

        // 设置年份
        Calendar a = Calendar.getInstance();
        map.put("nowDate", DateUtil.getNormTime(new Date()));
        map.put("MAXYear", a.get(Calendar.YEAR));
        map.put("MINYear", "2000");
        return map;
    }

    /**
     * @param userId
     * @return
     * @see com.anji.allways.business.service.StockScreenService#getScreenBrand(java.lang.Integer)
     */
    @Override
    public Map<String, Object> getScreenBrand(Integer userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 查询字母信息
        List<BrandVO> initials = brandEntityMapper.selectAllLetter(userId);

        for (BrandVO initial : initials) {
            // 查询品牌信息
            List<BrandVO> brands = brandEntityMapper.selectAllBrand(initial.getName());
            for (BrandVO brand : brands) {
                // 获取车系信息
                List<BrandVO> series = brandEntityMapper.selectAllSeries(brand.getName());
                for (BrandVO serie : series) {
                    Map<String, Object> model = new HashMap<String, Object>();
                    model.put("serie", serie.getName());
                    model.put("brand", brand.getName());
                    List<BrandVO> models = brandEntityMapper.selectAllModel(model);
                    serie.setList(models);
                }
                brand.setList(series);
            }

            initial.setList(brands);
        }

        // 获取标准色
        List<Map<String, Object>> allColor = dictService.selectByCategoryCode(RowDictCodeE.STANDARD_COLOR.getValue());
        map.put("allColor", allColor);
        map.put("row", initials);
        return map;
    }

}
