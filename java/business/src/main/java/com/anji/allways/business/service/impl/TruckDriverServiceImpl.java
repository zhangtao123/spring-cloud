/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.anji.allways.business.entity.TruckDriverEntity;
import com.anji.allways.business.enums.RowDictCodeE;
import com.anji.allways.business.mapper.TruckDriverEntityMapper;
import com.anji.allways.business.service.DictService;
import com.anji.allways.business.service.TruckDriverService;
import com.anji.allways.business.vo.TruckDriverVO;

/**
 * @author xuyuyang
 * @version $Id: TruckDriverServiceImpl.java, v 0.1 2017年8月24日 上午10:52:29 xuyuyang Exp $
 */
@Service
@Transactional
public class TruckDriverServiceImpl implements TruckDriverService {

    @Autowired
    private TruckDriverEntityMapper truckDriverEntityMapper;

    // 获取数据字典Service
    @Autowired
    private DictService             dictService;

    /**
     * 分页查询
     * @param berth
     * @param pageNum
     *            当前页码
     * @param pageRows
     *            显示行
     * @return
     */
    @Override
    public Map<String, Object> queryTruckDriverInfos(TruckDriverEntity bean, Integer pageNum, Integer pageRows) {
        Map<String, Object> map = new HashMap<String, Object>();

        // 查询总件数
        Integer num = truckDriverEntityMapper.queryTruckDriverInfosCount(bean);

        List<TruckDriverVO> driverList = this.fingPageInfo(bean, pageNum, pageRows, num);

        // 获取运输司机状态数据字典
        Map<String, String> retValue = dictService.queryAllByCode(RowDictCodeE.DRIVER_STATUS.getValue());
        if (driverList.size() > 0) {

            String statusName = "";
            for (int i = 0; i < driverList.size(); i++) {
                TruckDriverVO vo = driverList.get(i);
                // 设置司机状态名称
                statusName = retValue.get(String.valueOf(vo.getStatus()));
                vo.setStatusName(statusName);
            }
        }
        map.put("total", num);
        map.put("rows", driverList);
        return map;
    }

    /**
     * 批量激活或停用司机
     * @param list
     *            选中记录的IDlist
     * @param userId
     *            操作用户
     * @param status
     *            要更新成的状态(false:停用 true:正常)
     */
    @Override
    public Integer batchUpdateDriver(List<Integer> list, Long userId, Integer status) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 选中记录的IDlist
        map.put("idList", list);
        // 操作用户
        map.put("userId", userId);
        // 要更新的状态
        map.put("status", status);
        return truckDriverEntityMapper.updateByIdList(map);
    }

    /**
     * 根据ID获取运输司机信息
     * @return
     */
    @Override
    public TruckDriverVO selectByPrimaryKey(Integer id) {
        TruckDriverVO retValue = truckDriverEntityMapper.selectByPrimaryKey(id);

        if (retValue != null) {
            // 获取运输司机状态数据字典
            Map<String, String> retValue1 = dictService.queryAllByCode(RowDictCodeE.DRIVER_STATUS.getValue());
            if (retValue1 != null) {
                // 设置司机状态名称
                String statusName = retValue1.get(String.valueOf(retValue.getStatus()));
                retValue.setStatusName(statusName);
            }
        }
        return retValue;
    }

    /**
     * 校验字段值是否重复
     * @param value
     *            字段值
     * @param cloumnsName
     *            字段名
     * @return
     */
    @Override
    public Integer checkRepeatValue(String value, String cloumnsName, Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 要查询的字段
        map.put(cloumnsName, value);
        // ID(排除自身)
        map.put("id", id);
        int selectCountByExample = truckDriverEntityMapper.checkRepeatValue(map);
        return selectCountByExample;
    }

    /**
     * 新增运输司机信息
     * @param transportCompanyEntityMapper
     */
    @Override
    public int addTruckDriverEntityEntity(TruckDriverEntity truckDriverEntity) {
        // 创建时间
        truckDriverEntity.setCreateTime(new Date());
        // 新增司机的状态(0:停用 1:正常) 默认为正常
        truckDriverEntity.setStatus(1);
        return truckDriverEntityMapper.insertSelective(truckDriverEntity);
    }

    /**
     * 保存编辑后的运输司机信息
     * @param transportCompanyEntityMapper
     */
    @Override
    public int updateTruckDriverEntity(TruckDriverEntity truckDriverEntity) {
        // 更新时间
        truckDriverEntity.setUpdateTime(new Date());
        return truckDriverEntityMapper.updateByIDForEditDriver(truckDriverEntity);
    }

    /**
     * 根据电话号码查询运输司机（可能存在多条）
     */
    public List<TruckDriverVO> selectInfoByMoblie(String mobile) {
        return truckDriverEntityMapper.selectInfoByMoblie(mobile);
    }

    /**
     * 校验要启用的司机是否在其他公司为启用状态
     * @param list
     *            选中记录的IDlist
     */
    public TruckDriverVO checkOhterCompanyByIdList(List<Integer> list) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 选中记录的IDlist
        map.put("idList", list);
        return truckDriverEntityMapper.checkOhterCompanyByIdList(map);
    }

    /**
     * This method was generated by MyBatis Generator. This method corresponds to the database table tb_transport_company
     * @mbggenerated Wed Aug 23 17:12:20 CST 2017
     * @return 返回插入记录后的ID
     */
    @Override
    public Integer insert(TruckDriverEntity record) {
        truckDriverEntityMapper.insert(record);
        // 返回插入记录的ID
        return record.getId();
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
    private List<TruckDriverVO> fingPageInfo(TruckDriverEntity bean, Integer pageNum, Integer pageRows, Integer num) {

        if (num > 0) {

            // 每页显示数量
            bean.setPageNumber(pageRows);
            // 总数量
            bean.setTotalNumber(num);
            // 当前页数
            bean.setCurrentPage(pageNum);

            // 查询分页信息
            List<TruckDriverVO> list = truckDriverEntityMapper.queryTruckDriverInfos(bean);
            return list;
        }
        return new ArrayList<TruckDriverVO>();
    }

    /**
     * 校验选中的司机是否有出库任务正在进行
     * @param list
     *            选中记录的IDlist
     */
    public List<String> checkAllDriver(List<Integer> list) {
        Map<String, Object> map = new HashMap<>();
        map.put("idList", list);
        return truckDriverEntityMapper.checkAllDriver(map);
    }

}
