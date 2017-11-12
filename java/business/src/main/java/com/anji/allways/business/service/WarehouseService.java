/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import java.util.List;
import java.util.Map;

import com.anji.allways.business.entity.WarehouseEntity;
import com.anji.allways.business.entity.WarehouseLinkCustomerEntity;
import com.anji.allways.business.vo.DictVO;
import com.anji.allways.business.vo.WarehouseLevelVO;
import com.anji.allways.business.vo.WarehouseVO;

/**
 * @author 李军
 * @version $Id: WarehouseService.java, v 0.1 2017年8月29日 下午2:39:05 李军 Exp $
 */
public interface WarehouseService {

    // 查询我的仓库
    Map<String, Object> getMyWarehouseService(Map<String, Object> mapVue);

    /**
     * 分页查询
     * @param bean
     * @param pageNum
     *            当前页数
     * @param pageRows
     *            每页显示数量
     * @return
     */
    Map<String, Object> queryWarehouseInfos(WarehouseEntity bean, Integer pageNum, Integer pageRows);

    /**
     * 批量激活或停用仓库
     * @param ids
     *            选中记录的IDlist
     * @param userId
     *            操作用户
     * @param status
     *            要更新成的状态(0:停用 1:正常)
     */
    Integer batchUpdateWarehouse(String ids, Long userId, Integer status);

    /**
     * 根据ID获取仓库信息
     * @param ID
     * @return
     */
    WarehouseVO selectByPrimaryKey(Integer id) throws Exception;

    /**
     * 获取仓库等级信息
     * @param parentId
     *            上级仓库ID
     * @return
     */
    List<WarehouseLevelVO> selectWarehouseLevelInfor(Map<String, Object> map);

    /**
     * 新增仓库信息
     * @param warehouseEntity
     */
    int addWarehouseEntity(WarehouseEntity warehouseEntity);

    /**
     * 保存编辑后仓库信息
     * @param warehouseEntity
     */
    int updateWarehouseEntity(WarehouseEntity warehouseEntity);

    /**
     * 校验字段值是否重复
     * @param value
     *            字段值
     * @param cloumnsName
     *            字段名
     * @return
     */
    Integer checkRepeatValue(String value, String cloumnsName, Integer id);

    /**
     * 库容分页查询
     * @param bean
     * @param pageNum
     * @param pageRows
     * @return
     */
    Map<String, Object> queryStorageInfos(WarehouseEntity bean, Integer pageNum, Integer pageRows);

    /**
     * 根据仓库ID获取库容信息
     * @param ID
     * @return
     */
    WarehouseVO selectStorageInfoById(Integer id);

    /**
     * 保存库容信息
     * @param map
     *            要更新的仓库信息
     * @param list
     *            客户信息
     * @param userId
     *            操作用户ID
     * @return
     */
    int saveStorage(Map<String, Object> map, List<WarehouseLinkCustomerEntity> list, Integer userId);

    /**
     * 删除仓库客户关联信息
     * @param id
     */
    int deleteCustomerById(Integer id);

    /**
     * 获取某个仓库ID及其所有的下属仓库ID
     * @param warehouseEntity
     */
    List<Integer> selectAllLevelIdByParentId(Integer id);

    /**
     * 库容分页查询
     * @param berth
     * @return
     */
    Map<String, Object> queryWarehouseRelationInfos(WarehouseEntity bean, Integer pageNum, Integer pageRows);

    /**
     * @Title: insert @Description: 插入对象
     * @param t
     *            插入的对象
     * @return 返回插入记录后的ID
     */
    Integer insert(WarehouseEntity record);

    /**
     * 获取客户名称下拉列表名称（仓库对应的客户信息）
     */
    List<DictVO> queryCustomerForDict(Map<String, Object> map);

    /**
     * 校验仓库是否存在已入库的入库计划单或未完成的出库计划单
     */
    List<String> checkWarehouseStatus(String ids);
}
