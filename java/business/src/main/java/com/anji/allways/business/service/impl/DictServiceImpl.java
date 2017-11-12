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

import com.anji.allways.business.entity.DictEntity;
import com.anji.allways.business.mapper.DictEntityMapper;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.vo.DictVO;

/**
 * @author xuyuyang
 * @version $Id: DictService.java, v 0.1 2017年8月22日 下午2:40:39 xuyuyang Exp $
 */
@Service
@Transactional
public class DictServiceImpl implements DictService {

    @Autowired
    private DictEntityMapper dictEntityMapper;

    /**
     * 获取数据字典列表
     * @param typeCode
     * @return
     */
    @Override
    public Map<String, String> queryAllByCode(String typeCode) {

        List<DictVO> list = dictEntityMapper.queryAllByCode(typeCode);

        Map<String, String> retMap = new HashMap<String, String>();

        // 把list类型转换成Map存储 方便使用时取值
        for (DictVO vo : list) {
            // key:数值（如0） value：名称（如停用）
            retMap.put(vo.getCode(), vo.getName());
        }
        return retMap;
    }

    /**
     * 获取数据字典列表(供页面显示下拉框使用)
     * @param typeCode
     * @return
     */
    @Override
    public List<DictVO> queryAllByCodeForPage(String typeCode) {

        List<DictVO> list = dictEntityMapper.queryAllByCode(typeCode);

        return list;
    }

    /**
     * 获取颜色数据字典列表
     * @param typeCode
     * @return
     */
    @Override
    public List<DictVO> queryAllByCodeForColor(String typeCode) {

        List<DictVO> list = dictEntityMapper.queryAllByCodeForColor(typeCode);

        return list;
    }

    /**
     * @Title: insert @Description: 插入对象
     * @param t
     *            插入的对象
     * @return 返回插入记录后的ID
     */
    @Override
    public Integer insert(DictEntity record) {
        dictEntityMapper.insert(record);
        return record.getId();
    }

    /**
     * @param typeCode
     * @return
     * @see com.anji.allways.business.service.DictService#selectByCategoryCode(java.lang.String)
     */
    @Override
    public List<Map<String, Object>> selectByCategoryCode(String categoryCode) {
        return dictEntityMapper.selectByCategoryCode(categoryCode);
    }
}
