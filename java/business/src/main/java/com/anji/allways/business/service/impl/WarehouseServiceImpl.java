/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anji.allways.business.entity.WarehouseEntity;
import com.anji.allways.business.entity.WarehouseLinkCustomerEntity;
import com.anji.allways.business.enums.RowDictCodeE;
import com.anji.allways.business.mapper.WarehouseEntityMapper;
import com.anji.allways.business.mapper.WarehouseLinkCustomerEntityMapper;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.service.WarehouseService;
import com.anji.allways.business.vo.DictVO;
import com.anji.allways.business.vo.StateVO;
import com.anji.allways.business.vo.WarehouseLevelVO;
import com.anji.allways.business.vo.WarehouseVO;
import com.anji.allways.common.util.PageUtil;

/**
 * @author 李军
 * @version $Id: WarehouseServiceImpl.java, v 0.1 2017年8月29日 下午2:39:19 李军 Exp $
 */
@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseEntityMapper             warehouseEntityMapper;

    // 获取数据字典Service
    @Autowired
    private DictService                       dictService;

    @Autowired
    private WarehouseLinkCustomerEntityMapper warehouseLinkCustomerEntityMapper;

    /**
     * @return
     * @see com.anji.allways.business.service.WarehouseService#getMyWarehouseService()
     */
    @Override
    public Map<String, Object> getMyWarehouseService(Map<String, Object> mapVue) {
        Map<String, Object> map = new HashMap<String, Object>();
        PageUtil pageUtil = new PageUtil();
        pageUtil.setPageNumber(Integer.parseInt(mapVue.get("pageNumber").toString()));
        pageUtil.setTotalNumber(warehouseEntityMapper.getCount(Integer.valueOf(mapVue.get("userId").toString())));
        pageUtil.setDbNumber(pageUtil.getDbIndex() + pageUtil.getPageNumber());
        pageUtil.setCurrentPage(Integer.parseInt(mapVue.get("currentPage").toString()));
        mapVue.put("pageIndex", pageUtil.getDbIndex());
        mapVue.put("pageNumber", pageUtil.getDbNumber());
        List<WarehouseEntity> list = warehouseEntityMapper.getMyWarehouseById(mapVue);

        List<Map<String, Object>> lisemap = new ArrayList<Map<String, Object>>();
        for (WarehouseEntity warehouseEntity : list) {
            Map<String, Object> warehouse = new HashMap<String, Object>();
            warehouse.put("id", warehouseEntity.getId());
            warehouse.put("name", warehouseEntity.getName());
            warehouse.put("contactorMobile", warehouseEntity.getContactorMobile());
            warehouse.put("address", warehouseEntity.getProvinceName() + warehouseEntity.getCityName() + warehouseEntity.getDistrictName() + warehouseEntity.getAddress());
            lisemap.add(warehouse);
        }
        map.put("rows", lisemap);
        map.put("page", pageUtil);
        map.put("total", pageUtil.getTotalNumber());
        return map;
    }

    /**
     * 分页查询
     * @param bean
     * @param pageNum
     *            当前页数
     * @param pageRows
     *            每页显示数量
     * @return
     */
    @Override
    public Map<String, Object> queryWarehouseInfos(WarehouseEntity bean, Integer pageNum, Integer pageRows) {
        Map<String, Object> map = new HashMap<String, Object>();

        // 获取总件数
        Integer num = warehouseEntityMapper.queryWarehouseInfosCount(bean);
        // 获取仓库列表信息
        List<WarehouseVO> retValueList = this.fingPageInfo(bean, pageNum, pageRows, num);

        // 获取仓库状态数据字典
        Map<String, String> retValue = dictService.queryAllByCode(RowDictCodeE.WAREHOUSE_STATUS.getValue());
        // 获取营业时间类型数据字典
        Map<String, String> timeType = dictService.queryAllByCode(RowDictCodeE.BUSSIONTIME_TYPE.getValue());

        if (retValueList.size() > 0) {

            for (int i = 0; i < retValueList.size(); i++) {
                WarehouseVO vo = retValueList.get(i);

                // 营业时间
                // 判断是否为全天营业
                if (this.isAllDate(vo.getBusinessStartTime(), vo.getBusinessEndTime())) {
                    // 0:全天 1:时间段
                    vo.setBusinessTimeName(timeType.get("0"));
                } else {
                    // 类型为时间段时（营业开始时间+"-"+营业结束时间）
                    vo.setBusinessTimeName(vo.getBusinessStartTime() + "-" + vo.getBusinessEndTime());
                }

                // 设置仓库状态名称
                String statusName = retValue.get(String.valueOf(vo.getStatus()));
                vo.setStatusName(statusName);
            }
        }
        map.put("total", num);
        map.put("rows", retValueList);
        return map;
    }

    /**
     * 批量激活或停用仓库
     * @param ids
     *            选中记录的IDlist
     * @param userId
     *            操作用户
     * @param status
     *            要更新成的状态(0:停用 1:正常)
     */
    @Override
    public Integer batchUpdateWarehouse(String ids, Long userId, Integer status) {

        Map<String, Object> map = new HashMap<String, Object>();

        String[] split = ids.split(",");
        List<Long> list = new ArrayList<Long>();
        for (int i = 0; i < split.length; i++) {
            list.add(Long.valueOf(split[i]));
        }

        // 选中记录的IDlist
        map.put("idList", list);
        // 操作用户
        map.put("userId", userId);
        // 要更新的状态
        map.put("status", status);
        return warehouseEntityMapper.updateByIdList(map);
    }

    /**
     * 根据ID获取仓库信息
     * @return
     * @throws Exception
     */
    @Override
    public WarehouseVO selectByPrimaryKey(Integer id) throws Exception {

        WarehouseEntity retValue = warehouseEntityMapper.selectByPrimaryKey(id);

        // 页面返回VO
        WarehouseVO vo = null;
        if (retValue != null) {
            // 页面返回VO
            vo = new WarehouseVO();
            BeanUtils.copyProperties(vo, retValue);
            // 营业时间
            // 判断是否为全天营业
            if (this.isAllDate(vo.getBusinessStartTime(), vo.getBusinessEndTime())) {
                // 0:全天 1:时间段
                vo.setBusinessTimeName("0");
            } else {
                // 类型为时间段时（营业开始时间+"-"+营业结束时间）
                vo.setBusinessTimeName("1");
            }

            // 获取上级仓库信息
            WarehouseEntity parentValue = warehouseEntityMapper.selectByPrimaryKey(vo.getParentId());

            if (parentValue != null) {
                // 上级仓库名称
                vo.setParentName(parentValue.getName());
            }
        }
        return vo;
    }

    /**
     * 获取仓库等级信息
     * @param parentId
     *            上级仓库ID
     * @return
     */
    @Override
    public List<WarehouseLevelVO> selectWarehouseLevelInfor(Map<String, Object> map) {

        List<WarehouseLevelVO> levelList = warehouseEntityMapper.selectWarehouseLevelInfor(map);

        processLevelStatus(levelList);
        return levelList;
    }

    /**
     * 处理仓库层级状态
     * @param levelList
     */
    private void processLevelStatus(List<WarehouseLevelVO> levelList) {
        for (WarehouseLevelVO vo : levelList) {

            Map<String, String> attributes = new HashMap<>();
            attributes.put("id", vo.getId());
            vo.setAttributes(attributes);
            if (vo.getChildren() != null && vo.getChildren().size() > 0) {
                StateVO state = new StateVO();
                // 是否展开
                state.setOpened(true);
                // 是否选中
                state.setSelected(false);
                vo.setState(state);
                processLevelStatus(vo.getChildren());
            } else {
                StateVO state = new StateVO();
                // 是否展开
                state.setOpened(false);
                // 是否选中
                state.setSelected(false);
                vo.setState(state);

                vo.setType("leaf");
            }

        }
    }

    /**
     * 新增仓库信息（一级仓库时，上级仓库ID为0）
     * @param WarehouseEntity
     * @param parentId
     *            上级仓库ID
     * @return 插入记录ID
     */
    @Override
    public int addWarehouseEntity(WarehouseEntity warehouseEntity) {

        // 状态（0：停用 1：正常）新增的时候默认状态为正常
        warehouseEntity.setStatus(1);
        // 创建时间
        warehouseEntity.setCreateTime(new Date());

        warehouseEntityMapper.insertSelective(warehouseEntity);

        return warehouseEntity.getId();
    }

    /**
     * 保存编辑后的仓库信息（一级仓库时，上级仓库ID为0）
     * @param transportCompanyEntityMapper
     */
    @Override
    public int updateWarehouseEntity(WarehouseEntity warehouseEntity) {

        // 更新时间
        warehouseEntity.setUpdateTime(new Date());

        return warehouseEntityMapper.updateWarehouseForManageById(warehouseEntity);
    }

    /**
     * 校验字段值是否重复
     * @param value
     *            字段值
     * @param cloumnsName
     *            参数名
     * @return
     */
    @Override
    public Integer checkRepeatValue(String value, String cloumnsName, Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 要查询的字段
        map.put(cloumnsName, value);
        // ID(排除自身)
        map.put("id", id);
        int selectCountByExample = warehouseEntityMapper.checkRepeatValue(map);
        return selectCountByExample;
    }

    /**
     * 仓库关系分页查询
     * @param berth
     * @return
     */
    @Override
    public Map<String, Object> queryWarehouseRelationInfos(WarehouseEntity bean, Integer pageNum, Integer pageRows) {
        Map<String, Object> map = new HashMap<String, Object>();

        // 获取总件数
        Integer num = warehouseEntityMapper.queryWarehouseRelationInfosCount(bean);

        // 获取库容列表信息
        List<WarehouseVO> retValueList = this.fingWarehouseRelationPageInfo(bean, pageNum, pageRows, num);

        // 获取仓库状态数据字典
        Map<String, String> retValue = dictService.queryAllByCode(RowDictCodeE.WAREHOUSE_STATUS.getValue());

        if (retValueList.size() > 0) {

            for (int i = 0; i < retValueList.size(); i++) {
                WarehouseVO vo = retValueList.get(i);

                // 设置仓库状态名称
                String statusName = retValue.get(String.valueOf(vo.getStatus()));
                vo.setStatusName(statusName);
            }
        }
        map.put("total", num);
        map.put("rows", retValueList);
        return map;
    }

    /**
     * 库容分页查询
     * @param berth
     * @return
     */
    @Override
    public Map<String, Object> queryStorageInfos(WarehouseEntity bean, Integer pageNum, Integer pageRows) {
        Map<String, Object> map = new HashMap<String, Object>();

        // 获取总件数
        Integer num = warehouseEntityMapper.queryStorageInfosCount(bean);

        // 获取库容列表信息
        List<WarehouseVO> retValueList = this.fingStoragePageInfo(bean, pageNum, pageRows, num);

        // 获取仓库状态数据字典
        Map<String, String> retValue = dictService.queryAllByCode(RowDictCodeE.WAREHOUSE_STATUS.getValue());

        if (retValueList.size() > 0) {

            for (int i = 0; i < retValueList.size(); i++) {
                WarehouseVO vo = retValueList.get(i);

                // 设置仓库状态名称
                String statusName = retValue.get(String.valueOf(vo.getStatus()));
                vo.setStatusName(statusName);
            }
        }
        map.put("total", num);
        map.put("rows", retValueList);
        return map;
    }

    /**
     * 根据仓库ID获取库容信息
     * @param ID
     * @return
     */
    public WarehouseVO selectStorageInfoById(Integer id) {

        // 根据仓库ID获取库容信息
        WarehouseVO vo = warehouseEntityMapper.selectStorageInfoById(id);

        if (vo != null) {

            // 处理客户数
            vo.setCustomerNum(String.valueOf(vo.getCustomerList().size()));
        }
        return vo;
    }

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
    public int saveStorage(Map<String, Object> map, List<WarehouseLinkCustomerEntity> list, Integer userId) {

        // 更新仓库信息
        int num = warehouseEntityMapper.updateByIdForStorage(map);

        // 更新或添加仓库客户关联表
        if (list != null) {
            for (WarehouseLinkCustomerEntity entity : list) {

                // 如果ID为空，则是新增客户
                if (entity.getId() == null) {

                    // 创建用户
                    entity.setCreateUser(userId);

                    // 创建时间
                    entity.setCreateTime(new Date());
                    warehouseLinkCustomerEntityMapper.insertSelective(entity);
                } else {

                    WarehouseLinkCustomerEntity checkVo = warehouseLinkCustomerEntityMapper.selectByPrimaryKey(entity.getId());

                    // 如果这条记录已被删除，当前添加的保存进去
                    if (checkVo == null) {
                        // 创建用户
                        entity.setCreateUser(userId);

                        // 创建时间
                        entity.setCreateTime(new Date());
                        warehouseLinkCustomerEntityMapper.insertSelective(entity);
                    }

                    // 更新客户关系
                    warehouseLinkCustomerEntityMapper.updateByIDForStorage(entity);
                }
            }
        }

        return num;
    }

    /**
     * 删除仓库客户关联信息
     * @param warehouseEntity
     */
    public int deleteCustomerById(Integer id) {

        return warehouseLinkCustomerEntityMapper.deleteByPrimaryKey(id);
    }

    /**
     * 获取某个仓库ID及其所有的下属仓库ID
     * @param warehouseEntity
     */
    public List<Integer> selectAllLevelIdByParentId(Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 上级仓库ID
        map.put("parentId", id);
        List<WarehouseLevelVO> list = warehouseEntityMapper.selectWarehouseLevelInfor(map);

        List<Integer> idList = new ArrayList<Integer>();
        // 递归处理所有层级ID
        processLevelId(list, idList);

        return idList;
    }

    /**
     * 获取客户名称下拉列表名称（仓库对应的客户信息）
     */
    @Override
    public List<DictVO> queryCustomerForDict(Map<String, Object> map) {
        return warehouseEntityMapper.queryCustomerForDict(map);
    }

    /**
     * 递归处理所有层级ID
     * @param list
     *            list集合
     * @param idList
     */
    private void processLevelId(List<WarehouseLevelVO> list, List<Integer> idList) {

        for (WarehouseLevelVO vo : list) {
            // 添加下属仓库ID
            idList.add(Integer.valueOf(vo.getId()));
            if (vo.getChildren() != null && vo.getChildren().size() > 0) {
                // 递归添加所有下属仓库ID
                processLevelId(vo.getChildren(), idList);
            }
        }
    }

    /**
     * 计算是否为全天营业 <br>
     * 计算到秒，从00:00 到 23:59为全天
     * @param startTime
     *            开始时间段
     * @param endTime
     *            结束时间段
     * @return
     */
    private boolean isAllDate(String startTime, String endTime) {
        SimpleDateFormat sim = new SimpleDateFormat("HH:mm");

        try {
            Date time1 = sim.parse(startTime);
            Date time2 = sim.parse(endTime);
            long v1 = time1.getTime();
            long v2 = time2.getTime();
            long diff = (v2 - v1) / 1000 / 60;

            // 计算到分钟
            if (diff == 1439) {
                return true;
            }

            System.out.println(diff);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 分页查询功能
     * @param pageNum
     *            当前页数
     * @param pageRows
     *            每页显示数量
     * @param num
     *            查询总件数
     * @return
     */
    private List<WarehouseVO> fingPageInfo(WarehouseEntity bean, Integer pageNum, Integer pageRows, Integer num) {

        if (num > 0) {
            // 每页显示数量
            bean.setPageNumber(pageRows);
            // 总数量
            bean.setTotalNumber(num);
            // 当前页数
            bean.setCurrentPage(pageNum);

            // 查询分页信息
            List<WarehouseVO> list = warehouseEntityMapper.queryWarehouseInfos(bean);
            return list;
        }
        return new ArrayList<WarehouseVO>();
    }

    /**
     * 库容分页查询功能
     * @param pageNum
     *            当前页数
     * @param pageRows
     *            每页显示数量
     * @param num
     *            查询总件数
     * @return
     */
    private List<WarehouseVO> fingStoragePageInfo(WarehouseEntity bean, Integer pageNum, Integer pageRows, Integer num) {

        if (num > 0) {
            // 每页显示数量
            bean.setPageNumber(pageRows);
            // 总数量
            bean.setTotalNumber(num);
            // 当前页数
            bean.setCurrentPage(pageNum);

            // 查询分页信息
            List<WarehouseVO> list = warehouseEntityMapper.queryStorageInfos(bean);
            return list;
        }
        return new ArrayList<WarehouseVO>();
    }

    /**
     * 仓库关系分页查询功能
     * @param pageNum
     *            当前页数
     * @param pageRows
     *            每页显示数量
     * @param num
     *            查询总件数
     * @return
     */
    private List<WarehouseVO> fingWarehouseRelationPageInfo(WarehouseEntity bean, Integer pageNum, Integer pageRows, Integer num) {

        if (num > 0) {
            // 每页显示数量
            bean.setPageNumber(pageRows);
            // 总数量
            bean.setTotalNumber(num);
            // 当前页数
            bean.setCurrentPage(pageNum);

            // 查询分页信息
            List<WarehouseVO> list = warehouseEntityMapper.queryWarehouseRelationInfos(bean);
            return list;
        }
        return new ArrayList<WarehouseVO>();
    }

    /**
     * @Title: insert @Description: 插入对象
     * @param t
     *            插入的对象
     * @return 返回插入记录后的ID
     */
    @Override
    public Integer insert(WarehouseEntity record) {
        warehouseEntityMapper.insert(record);
        return record.getId();
    }

    /**
     * 校验仓库是否存在已入库的入库计划单或未完成的出库计划单
     */
    public List<String> checkWarehouseStatus(String ids) {
        Map<String, Object> map = new HashMap<String, Object>();

        String[] split = ids.split(",");
        List<Long> list = new ArrayList<Long>();
        for (int i = 0; i < split.length; i++) {
            list.add(Long.valueOf(split[i]));
        }

        // 选中记录的IDlist
        map.put("idList", list);
        return warehouseEntityMapper.checkWarehouseStatus(map);
    }
}
