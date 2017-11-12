/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import java.util.List;
import java.util.Map;

import com.anji.allways.business.entity.DictEntity;
import com.anji.allways.business.vo.DictVO;

/**
 * @author xuyuyang
 * @version $Id: DictService.java, v 0.1 2017年8月22日 下午2:40:39 xuyuyang Exp $
 */
public interface DictService {

    /**
     * 获取数据字典列表(供获取字典对应名称使用)
     * @param typeCode
     * @return
     */
    Map<String, String> queryAllByCode(String typeCode);

    /**
     * 获取数据字典列表(供页面显示下拉框使用)
     * @param typeCode
     * @return
     */
    List<DictVO> queryAllByCodeForPage(String typeCode);

    /**
     * 获取数据字典列表-颜色
     * @param typeCode
     * @return
     */
    List<Map<String, Object>> selectByCategoryCode(String categoryCode);

    /**
     * @Title: insert @Description: 插入对象
     * @param t
     *            插入的对象
     * @return 返回插入记录后的ID
     */
    Integer insert(DictEntity entity);

    /**
     * 获取颜色数据字典列表
     * @param typeCode
     * @return
     */
    List<DictVO> queryAllByCodeForColor(String typeCode);

}
