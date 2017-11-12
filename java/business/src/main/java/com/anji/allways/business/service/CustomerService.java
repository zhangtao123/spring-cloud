/**
 * anji-allways.com Inc.
 * Copyright (c) 2016-2017 All Rights Reserved.
 */
package com.anji.allways.business.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.anji.allways.business.entity.BrandEntity;
import com.anji.allways.business.entity.CustomerEntity;
import com.anji.allways.business.entity.CustomerOntimeRuleEntity;
import com.anji.allways.business.entity.UserEntity;
import com.anji.allways.business.vo.CustomerLevelVO;
import com.anji.allways.business.vo.CustomerVO;
import com.anji.allways.business.vo.DictVO;

/**
 * @author wangyanjun
 * @version $Id: CustomerService.java, v 0.1 2017年8月29日 下午11:45:47 wangyanjun Exp $
 */
public interface CustomerService {

    CustomerEntity queryCustomerById(Integer id);

    /**
     * 分页查询(编辑库容页面使用)
     * @param bean
     * @param pageNum
     * @param pageRows
     * @return
     */
    Map<String, Object> queryCustomerInfosForStorage(Map<String, Object> map, Integer pageNum, Integer pageRows);

    /**
     * @Title: insert @Description: 插入对象
     * @param t
     *            插入的对象
     * @return 返回插入记录后的ID
     */
    Integer insert(CustomerEntity record);

    /**
     * 分页查询
     * @param bean
     * @param pageNum
     *            当前页数
     * @param pageRows
     *            每页显示数量
     * @return
     */
    Map<String, Object> queryCustomerInfos(CustomerEntity bean, Integer pageNum, Integer pageRows);

    /**
     * 获取某个客户ID及其所有的下属客户ID
     * @param id
     */
    List<Integer> selectAllLevelIdByParentId(Integer id);

    /**
     * 获取所属集团下拉列表
     * @param map
     */
    List<DictVO> queryGroupForDict(Map<String, Object> map);

    /**
     * 批量激活或停用客户
     * @param ids
     *            选中记录的IDlist
     * @param userId
     *            操作用户
     * @param status
     *            要更新成的状态(0:停用 1:正常)
     */
    Integer batchUpdateCustomer(List<Integer> list, Long userId, Integer status);

    /**
     * 校验客户是否存在已入库的入库计划单且未完成的运输订单
     */
    List<String> checkCustomerStatus(List<Integer> list);

    /**
     * 根据ID获取客户信息
     * @param id
     *            客户ID
     */
    CustomerVO selectInfoById(Integer id);

    /**
     * 获取客户等级信息
     * @param parentId
     *            上级客户ID
     * @return
     */
    List<CustomerLevelVO> selectCustomerLevelInfor(Map<String, Object> map);

    /**
     * 根据客户ID获取品牌信息
     * @param customerId
     *            客户ID
     * @return
     */
    List<BrandEntity> selectBrandByCustomerId(String customerId);

    /**
     * 根据客户ID查询客户logo照片
     * @param customerId
     *            客户ID
     * @return
     */
    String selectLogoPictureById(Integer customerId);

    /**
     * 根据客户ID查询及时率规则信息
     * @param customerId
     *            客户ID
     * @return
     */
    List<CustomerOntimeRuleEntity> selectRuleInfoByCustomerId(Integer customerId);

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
     * 新增客户信息
     * @param warehouseId
     *            仓库ID
     * @param entity
     * @param brandList
     *            经营品牌范围
     * @param ruleList
     *            及时率规则信息
     * @return
     */
    Integer addCustomerEntity(Integer warehouseId, CustomerEntity entity, JSONArray brandList, JSONArray ruleList);

    /**
     * 更新客户信息
     * @param entity
     * @param brandList
     *            经营品牌范围
     * @param ruleList
     *            及时率规则信息
     * @return
     */
    Integer updateCustomerEntity(CustomerEntity entity, JSONArray brandList, JSONArray ruleList);

    /**
     * 客户账号分页查询
     * @param bean
     * @param pageNum
     *            当前页数
     * @param pageRows
     *            每页显示数量
     * @return
     */
    Map<String, Object> queryCustomerAccountInfos(UserEntity bean, Integer pageNum, Integer pageRows);

    /**
     * 获取所属客户下拉列表
     * @param map
     */
    List<DictVO> queryCustomerForDict(Map<String, Object> map);
}
